package com.acme.mannschaftsmitglied.graphql;

import com.acme.mannschaftsmitglied.service.MitgliedWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Eine Controller-Klasse für das Schreiben mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 */
@Controller
@RequiredArgsConstructor
@Slf4j
final class MannschaftsmitgliedMutationController {
    private final MitgliedWriteService service;

    /**
     * Ein neues Mitglied anlegen.
     *
     * @param input Eingabedaten für ein neues Mitglied.
     * @return Die generierte ID für das neue Mitglied als Payload.
     */
    @MutationMapping
    CreatePayload create(@Argument final MannschaftsmitgliedInput input) {
        log.debug("create:{}", input);
        final var id = service.create(input.toMitglied()).getId();
        log.debug("create: {}", id);
        return new CreatePayload(id);
    }
}
