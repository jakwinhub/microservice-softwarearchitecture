package com.acme.mannschaftsmitglied.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange
@SuppressWarnings("WriteTag")
interface MitgliedRepository {
    @GetExchange("/{id}")
    Mono<MitgliedDownload> getMitglied(@PathVariable String id);
}
