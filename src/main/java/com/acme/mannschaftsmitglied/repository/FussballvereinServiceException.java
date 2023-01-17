package com.acme.mannschaftsmitglied.repository;

import lombok.Getter;
import org.springframework.graphql.client.GraphQlTransportException;
import org.springframework.web.reactive.function.client.WebClientException;

/**
 * Exception für der FussballvereinService.
 */
@Getter
public class FussballvereinServiceException extends RuntimeException {
    private final WebClientException restException;
    private final GraphQlTransportException graphQlException;

    FussballvereinServiceException(final WebClientException restException) {
        this.restException = restException;
        graphQlException = null;
    }

    FussballvereinServiceException(final GraphQlTransportException graphQlException) {
        restException = null;
        this.graphQlException = graphQlException;
    }

}
