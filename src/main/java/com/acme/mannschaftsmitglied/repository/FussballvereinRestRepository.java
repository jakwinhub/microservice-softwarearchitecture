package com.acme.mannschaftsmitglied.repository;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

/**
 * "HTTP Interface" für den REST-Controller für Fussballvereinsdaten.
 */
@HttpExchange("/rest")
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface FussballvereinRestRepository {
    /**
     * Einen Fußallvereinsdatnsatz vom Microservece "fussballverein" anfordern.
     *
     * @param id ID des angeforderten Fussballvereins
     * @return Mono-Objekt mit dem gefundenen Fussballverein
     */
    @GetExchange("/{id}")
    Mono<Fussballverein> getFussballverein(@PathVariable String id);
}
