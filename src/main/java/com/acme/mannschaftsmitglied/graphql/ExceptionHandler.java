package com.acme.mannschaftsmitglied.graphql;

import com.acme.mannschaftsmitglied.service.ConstraintViolationsException;
import com.acme.mannschaftsmitglied.service.EmailExistsException;
import com.acme.mannschaftsmitglied.service.NotFoundException;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abbildung von Exceptions auf GraphQL.
 *
 */
@SuppressWarnings({"NullableProblems", "ReturnCount"})
@Component
public class ExceptionHandler extends DataFetcherExceptionResolverAdapter {
    /**
     * Abbildung der Exception auf GraphQL.
     *
     * @param ex  the exception to resolve
     * @param env the environment for the invoked {@code DataFetcher}
     */
    @Override
    protected GraphQLError resolveToSingleError(
        final Throwable ex,

        final DataFetchingEnvironment env
    ) {
        if (ex instanceof final NotFoundException notFound) {
            return new NotFoundError(notFound.getId(), notFound.getSuchkriterium());
        } else if (ex instanceof EmailExistsException emailExists) {
            return new EmailExistsError(emailExists.getEmail());
        } else if (ex instanceof final DateTimeParseException dateTimeParseException) {
            return new DateTimeParseError(dateTimeParseException.getParsedString());
        }

        return super.resolveToSingleError(ex, env);
    }

    /**
     * Abbildung der Exception aus MitgliedGetController auf GraphQL.
     *
     * @param ex  the exception to resolve
     * @param env the environment for the invoked {@code DataFetcher}
     */
    @Override
    protected List<GraphQLError> resolveToMultipleErrors(
        final Throwable ex,
        @SuppressWarnings("NullableProblems") final DataFetchingEnvironment env
    ) {
        if (ex instanceof final ConstraintViolationsException cve) {
            return cve.getViolations()
                .stream()
                .map(ConstraintViolationError::new)
                .collect(Collectors.toList());
        }

        return super.resolveToMultipleErrors(ex, env);
    }
}
