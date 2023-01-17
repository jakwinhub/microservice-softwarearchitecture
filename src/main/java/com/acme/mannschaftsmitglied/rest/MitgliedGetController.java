/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.mannschaftsmitglied.rest;

import com.acme.mannschaftsmitglied.service.MitgliedReadService;
import com.acme.mannschaftsmitglied.service.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.acme.mannschaftsmitglied.rest.MitgliedGetController.REST_PATH;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;

import java.util.UUID;
import java.util.Optional;
import java.util.Objects;


/**
 * Eine @RestController-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Funktionen der Klasse abgebildet werden.
 */
@RestController
@RequestMapping(REST_PATH)
@Tag(name = "Mitglied API")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
class MitgliedGetController {

    static final String REST_PATH = "/rest";
    /**
     * Muster für eine UUID. `$HEX_PATTERN{8}-($HEX_PATTERN{4}-){3}$HEX_PATTERN{12}` enthält eine _capturing group_
     * und ist nicht zulässig.
     */
    static final String ID_PATTERN = "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    private final MitgliedReadService service;

    private final UriHelper uriHelper;

    /**
     * Suche anhand Mitglieds-ID als Pfad-Parameter.
     *
     * @param id      ID des zu suchenden Mitglieds.
     * @param version Version des zu generierenden Objektes.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Statuscode 200.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit der Mitglied-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Mitglied gefunden")
    @ApiResponse(responseCode = "404", description = "Mitglied nicht gefunden")
    ResponseEntity<MitgliedModel> findById(@PathVariable final UUID id,
                                           @RequestHeader("If-None-Match") final Optional<String> version,
                                           final HttpServletRequest request) {
        log.debug("findById: id={}", id);
        log.trace("findById: version={}", version);
        final var mitglied = service.findById(id);

        final var currentVersion = "\"" + mitglied.getVersion() + '"';
        if (Objects.equals(version.orElse(null), currentVersion)) {
            return status(NOT_MODIFIED).build();
        }

        final var model = new MitgliedModel(mitglied);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + mitglied.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);
        return ok(model);
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param allParams Query-Parameter als Map.
     * @param request   Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Statuscode 200.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Mitglied gefunden")
    @ApiResponse(responseCode = "404", description = "Mitglied nicht gefunden")
    CollectionModel<MitgliedModel> find(@RequestParam final MultiValueMap<String, String> allParams,
                                        final HttpServletRequest request) {
        log.debug("find: {}", allParams);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var models = service.find(allParams)
            .stream()
            .map(mitglied -> {
                final var model = new MitgliedModel(mitglied);
                model.add(Link.of(baseUri + '/' + mitglied.getId()));
                return model;
            })
            .toList();

        log.debug("find: {}", models);
        return CollectionModel.of(models);
    }

    @GetMapping(path = "/nachname/{prefix}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit Prefix", tags = "Suchen")
    String findNachnameByPrefix(@PathVariable final String prefix) {
        log.debug("findeNachnamenByPrefix: {}", prefix);
        final var nachnamen = service.findNachnameByPrefix(prefix);
        log.debug("findNachnamenByPrefix: {}", nachnamen);
        return nachnamen.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @SuppressWarnings("unused")
    void handleNotFound(final NotFoundException ex) {
        log.debug("handleNotFound: {}", ex.getMessage());
    }
}
