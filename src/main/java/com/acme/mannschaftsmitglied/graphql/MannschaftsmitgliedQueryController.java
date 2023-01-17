package com.acme.mannschaftsmitglied.graphql;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import com.acme.mannschaftsmitglied.service.MitgliedReadService;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import static java.util.Collections.emptyMap;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 */
@Controller
@RequiredArgsConstructor
@Slf4j
final class MannschaftsmitgliedQueryController {
    private final MitgliedReadService service;

    /**
     * Suche anhand der Mitglieds ID.
     *
     * @param id ID
     * @return Das gefundene Mitglied
     */
    @QueryMapping
    Mitglied mannschaftsmitglied(@Argument final UUID id) {
        log.debug("findById: {}", id);
        final var mitglied = service.findById(id);
        log.debug("findById: {}", mitglied);
        return mitglied;
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param input Suchkriterien
     * @return Die gefundenen Mitglieder als Collection
     */
    @QueryMapping
    Collection<Mitglied> mannschaftsmitglieder(@Argument final Optional<Suchkriterien> input) {
        log.debug("mitglied: input={}", input);
        final var suchkriterien = input.map(Suchkriterien::toMap).orElse(emptyMap());
        final var mannschaftsmitglieder = service.find(suchkriterien);
        log.debug("mitglieder: {}", input);
        return mannschaftsmitglieder;
    }
}
