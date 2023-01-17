package com.acme.mannschaftsmitglied.graphql;

import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.ErrorType;
import java.util.List;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

/**
 * Behandung des Errors "DateTimeParse".
 */
@RequiredArgsConstructor
public class DateTimeParseError implements GraphQLError {
    private final String parsedString;

    @Override
    public ErrorType getErrorType() {
        return BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Das Datum " + parsedString + " ist nicht korrekt.";
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }
}
