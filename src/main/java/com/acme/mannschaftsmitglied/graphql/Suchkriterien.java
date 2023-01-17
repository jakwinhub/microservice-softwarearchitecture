package com.acme.mannschaftsmitglied.graphql;


import org.springframework.util.LinkedMultiValueMap;
import java.util.List;
import java.util.Map;

/**
 * Eine Value-Klasse für Eingabedaten passend zu Suchkriterien aus dem GraphQL-Schema.
 *
 * @author Jakob Winkler
 * @param nachname Nachname
 * @param email    Emailadresse
 */
record Suchkriterien(
    String nachname,
    String email

) {
    /**
     * Konvertierung in eine Map.
     *
     * @return Das konvertierte Map-Objekt
     */
    Map<String, List<String>> toMap() {
        final Map<String, List<String>> map = new LinkedMultiValueMap<>();
        if (nachname != null) {
            map.put("nachname", List.of(nachname));
        }
        if (email != null) {
            map.put("email", List.of(email));
        }
        return map;
    }
}
