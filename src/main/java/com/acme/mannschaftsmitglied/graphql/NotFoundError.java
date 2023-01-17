package com.acme.mannschaftsmitglied.graphql;

import graphql.GraphQLError;
import graphql.language.SourceLocation;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.ErrorType;

import static org.springframework.graphql.execution.ErrorType.NOT_FOUND;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Fehlerklasse für GraphQL, falls eine NotFoundException geworfen wird.
 *
 * @author Jakob Winkler
 */
@RequiredArgsConstructor
class NotFoundError implements GraphQLError {
    private final UUID id;
    private final Map<String, List<String>> allParams;

    /**
     * Error-Typ auf NOT_FOUND setzten.
     *
     * @return NOT_FOUND als Error-Type
     */
    @Override
    public ErrorType getErrorType() {
        return NOT_FOUND;
    }

    /**
     * Message innerhalb von Errors beim Response für einen GraphQL-Request.
     *
     * @return Message für errors.
     */
    @Override
    public String getMessage() {
        return id == null
            ? "Kein Mitglied gefunden: suchkriterien=" + allParams
            : id + "gefunden";
    }

    /**
     * Keine Angabe von Zeilen- und Spaltennummer der GraphQL-Query, falls kein Kunde gefunden wurde.
     *
     * @return null.
     */
    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }
}
