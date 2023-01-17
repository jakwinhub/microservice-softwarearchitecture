package com.acme.mannschaftsmitglied.graphql;

import java.util.UUID;

/**
 * Value-Klasse für das Resultat, wenn an der GraphQL-Schnittstelle ein neues Mitglied angelegt wurde.
 *
 * @param id ID des neu angelegten Mitglieds
 */
public record CreatePayload(UUID id) {
}
