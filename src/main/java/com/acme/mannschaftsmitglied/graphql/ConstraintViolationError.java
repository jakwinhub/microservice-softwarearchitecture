package com.acme.mannschaftsmitglied.graphql;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.ErrorType;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

/**
 * Fehlerklasse für GraphQL, falls eine ConstraintViolation geworfen wird.
 *
 * @author Jakob Winkler
 */
@RequiredArgsConstructor
final class ConstraintViolationError implements GraphQLError {
    private final ConstraintViolation<Mitglied> violation;

    /**
     * ErrorType auf BAD_REQUEST setzten.
     *
     * @return BAD_REQUEST
     */
    @Override
    public ErrorType getErrorType() {
        return BAD_REQUEST;
    }

    /**
     * Message innerhalb von Errors beim Response für einen GraphQL-Request.
     *
     * @return Message Key zum verletzten Constraint
     */
    @Override
    public String getMessage() {
        return violation.getMessage();
    }

    /**
     * Pfadangabe von der Wurzel bis zum fehlerhaften Datenfeld.
     *
     * @return Liste der Datenfelder von der Wurzel bis zum Fehler
     */
    @Override
    public List<Object> getPath() {
        final List<Object> result = new ArrayList<>(5);
        result.add("input");
        for (final Node node : violation.getPropertyPath()) {
            result.add(node.toString());
        }
        return result;
    }

    /**
     * Keine Angabe von Zeilen- und Spaltennummer der GraphQL-Mutation, falls Constraints verletzt sind.
     *
     * @return null
     */
    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }
}
