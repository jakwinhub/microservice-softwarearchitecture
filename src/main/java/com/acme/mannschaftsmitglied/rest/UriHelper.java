package com.acme.mannschaftsmitglied.rest;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.acme.mannschaftsmitglied.rest.MitgliedGetController.REST_PATH;

/**
 * Hilfsklasse um URIs für HATEOAS oder für URIs in ProblemDetail zu ermitteln, falls ein API-Gateway verwendet wird.
 */
@Component
@Slf4j
final class UriHelper {
    private static final String X_FORWARDED_PROTO = "x-forwarded-proto";
    private static final String X_FORWARDED_HOST = "x-forwarded-host";
    private static final String X_FORWARDED_PREFIX = "x-forwarded-prefix";
    private static final String MANNSCHAFTSMITGLIEDER_PREFIX = "/mannschaftsmitglieder";

    URI getBaseUri(final HttpServletRequest request) {
        final var forwardedHost = request.getHeader(X_FORWARDED_HOST);
        if (forwardedHost != null) {
            return getBaseUriForwarded(request, forwardedHost);
        }

        final var uriComponents = ServletUriComponentsBuilder.fromRequestUri(request).build();
        final var baseUriStr = uriComponents.getScheme() + "://" + uriComponents.getHost() + ':' +
            uriComponents.getPort() + REST_PATH;
        log.debug("getBaseUri (ohne Forwarding): baseUriStr={}", baseUriStr);
        final URI baseUri;
        try {
            baseUri = new URI(baseUriStr);
        } catch (final URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
        return baseUri;
    }

    private URI getBaseUriForwarded(final HttpServletRequest request, final String forwardedHost) {
        // x-forwarded-host = Hostname des API-Gateways

        // "https" oder "http"
        final var forwardedProto = request.getHeader(X_FORWARDED_PROTO);
        if (forwardedProto == null) {
            throw new IllegalStateException("Kein \"" + X_FORWARDED_PROTO + "\" im Header");
        }

        var forwardedPrefix = request.getHeader(X_FORWARDED_PREFIX);
        // x-forwarded-prefix: null bei Kubernetes Ingress Controller bzw. "/kunden" bei Spring Cloud Gateway
        if (forwardedPrefix == null) {
            log.trace("getBaseUriForwarded: Kein \"" + X_FORWARDED_PREFIX + "\" im Header");
            forwardedPrefix = MANNSCHAFTSMITGLIEDER_PREFIX;
        }
        final var baseUriStr = forwardedProto + "://" + forwardedHost + forwardedPrefix + REST_PATH;
        log.debug("getBaseUriForwarded: baseUriStr={}", baseUriStr);
        final URI baseUri;
        try {
            baseUri = new URI(baseUriStr);
        } catch (final URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
        return baseUri;
    }
}
