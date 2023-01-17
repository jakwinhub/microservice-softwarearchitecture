package com.acme.mannschaftsmitglied.rest;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static com.acme.mannschaftsmitglied.rest.MitgliedWriteController.PROBLEM_PATH;
import static com.acme.mannschaftsmitglied.rest.ProblemType.PRECONDITION;

/**
 * Exception falls die Version des Objektes nicht übereinstimmen sollte.
 */
public class VersionInvalidException extends ErrorResponseException {
    VersionInvalidException(final HttpStatusCode status, final String message, final URI uri) {
        this(status, message, uri, null);
    }

    VersionInvalidException(
        final HttpStatusCode status,
        final String message,
        final URI uri,
        final Throwable cause
    ) {
        super(status, asProblemDetails(status, message, uri), cause);
    }

    private static ProblemDetail asProblemDetails(final HttpStatusCode status, final String detail, final URI uri) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(PROBLEM_PATH + PRECONDITION.getValue()));
        problemDetail.setInstance(uri);
        return problemDetail;
    }

}
