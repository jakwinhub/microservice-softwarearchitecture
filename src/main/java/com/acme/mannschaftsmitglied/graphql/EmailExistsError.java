package com.acme.mannschaftsmitglied.graphql;

import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.ErrorType;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

import java.util.List;

/**
 * Fehlerklasse für GraphQL, falls eine EmailExistsException geworfen wird.
 */
@RequiredArgsConstructor
class EmailExistsError implements GraphQLError {
    private final String email;

    @Override
    public ErrorType getErrorType() {
        return BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Die Emailadresse " + email + " existiert bereits.";
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }
}
