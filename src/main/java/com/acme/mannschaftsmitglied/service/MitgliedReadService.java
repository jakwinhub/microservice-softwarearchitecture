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
package com.acme.mannschaftsmitglied.service;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import com.acme.mannschaftsmitglied.repository.Fussballverein;
import com.acme.mannschaftsmitglied.repository.FussballvereinRepository;
import com.acme.mannschaftsmitglied.repository.FussballvereinServiceException;
import com.acme.mannschaftsmitglied.repository.MitgliedRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("NestedIfDepth")
public class MitgliedReadService {
    private final MitgliedRepository repo;
    private final FussballvereinRepository fussballvereinRepo;

    public Collection<Mitglied> findAll() {
        final var mitglieds = repo.findAll();
        mitglieds.forEach(mitglied -> {
            final var fussballvereinId = mitglied.getFussballvereinId();
            final var fussballverein = fetchFussballvereinById(fussballvereinId);
            final var email = fetchEmailById(fussballvereinId);
            mitglied.setFussballvereinVereinsname(fussballverein.vereinsname());
            mitglied.setFussballvereinEmail(email);
        });
        return mitglieds;
    }

    /**
     * Ein Mitglied mithilfe von angegebener UUID suchen.
     *
     * @param id UUID mit der gesucht werden soll
     * @return Das gedfundene Mitglied zur UUID
     * @throws NotFoundException wird geworfern, falls es zur gegebenen UUID kein Mitglied geben sollte
     */
    public @NonNull Mitglied findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var mitglied = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", mitglied);
        return mitglied;
    }

    /**
     * Mitglied zurFussballverein-ID suchen.
     *
     * @param fussballvereinId Die gefundene Id des gegebenen Fussballvereins.
     * @return Das gefundene Mitglied.
     * @throws NotFoundException Falls kein Mitglied gefunden werden konnte
     */
    public @NonNull Collection<Mitglied> findByFussballvereinId(final UUID fussballvereinId) {
        log.debug("findByFussballvereinId: fussballvereinId={}", fussballvereinId);
        final var mitglieder = repo.findByFussballvereinId(fussballvereinId);
        if (mitglieder.isEmpty()) {
            throw new NotFoundException();
        }
        final var fussballverein = fetchFussballvereinById(fussballvereinId);
        final var vereinsname = fussballverein == null ? null : fussballverein.vereinsname();
        final var email = fetchEmailById(fussballvereinId);
        log.trace("findByFussballvereinId: vreinsname={}, email={}", vereinsname, email);
        mitglieder.forEach(mitglied -> {
            mitglied.setFussballvereinVereinsname(vereinsname);
            mitglied.setFussballvereinEmail(email);
        });
        log.trace("findByFussballvereinId: fussballvereine={}", mitglieder);
        return mitglieder;
    }

    /**
     * Suchen nach Mitglied mithilfe von Query-Parametern.
     *
     * @param queryParams Die Query-Parameter gespeichert in einer Map
     * @return Die gefundenen Mitglieder zu den Suchkriterien.
     * @throws NotFoundException Falls es kein Mitglied zu den Suchkriterien geben sollte.
     */
    public @NonNull Collection<Mitglied> find(@NonNull final Map<String, List<String>> queryParams) {
        log.debug("find: suchkriterien={}", queryParams);

        if (queryParams.size() == 1) {
            final var nachnamen = queryParams.get("nachname");
            if (nachnamen != null && nachnamen.size() == 1) {
                final var mitglied = repo.findByNachname(nachnamen.get(0));
                if (mitglied.isEmpty()) {
                    throw new NotFoundException(queryParams);
                }
                log.debug("find mit Nachname: {}", mitglied);
                return mitglied;
            }
            final var email = queryParams.get("email");
            if (email != null && email.size() == 1) {
                final var mitglied = repo.findByEmail(email.get(0));
                if (mitglied.isEmpty()) {
                    throw new NotFoundException(queryParams);
                }
                log.debug("find mit Email: {}", mitglied);
                final var mitgliederList = List.of(mitglied.get());
                return mitgliederList;
            }
        }
        final var mitglieder = repo.findAll();
        if (mitglieder.isEmpty()) {
            throw new NotFoundException(queryParams);
        }
        log.debug("find: {}", queryParams);
        return mitglieder;
    }

    /**
     * Suche nach Nachnamen passend zu dem gegebenen Prefix.
     *
     * @param prefix Präfix, welces das Suchkriterium ist.
     * @return Gefundene Nachnamen.
     * @throws NotFoundException Falls es zu dem Präfix keine Nachnamen gben sollte.
     */
    public @NonNull Collection<String> findNachnameByPrefix(final String prefix) {
        log.debug("findByNachnamePrefix: {}", prefix);
        final var nachnamen = repo.findNachnameByPrefix(prefix);
        if (nachnamen.isEmpty()) {
            throw new NotFoundException();
        }
        log.debug("findByNachnamenPrefix: {}", nachnamen);
        return nachnamen;
    }

    /**
     * Suche nach Fussallverein zur gegebenene Id.
     *
     * @param fussballvereinId ID des Fussballvereins
     * @return Fussballverein
     */
    private Fussballverein fetchFussballvereinById(final UUID fussballvereinId) {
        log.debug("findFussballvereinById: fussballvereinId={}", fussballvereinId);
        try {
            final var fussballverein = fussballvereinRepo
                .findById(fussballvereinId)
                .orElse(new Fussballverein("N/A", "n.a@acm.com"));
            return fussballverein;
        } catch (final FussballvereinServiceException ex) {
            log.debug("findFussballvereinById {}", ex.getRestException().getClass().getSimpleName());
            return new Fussballverein("Exception", "exception@acme.com");
        }
    }

    /**
     * Suche nach Fussballverein mit der Email.
     *
     * @param fussballvereinId Id des Fussballvereins
     * @return Fussballverein
     */
    private String fetchEmailById(final UUID fussballvereinId) {
        log.debug("findEmailById: fussballverein={}", fussballvereinId);
        final var emailOpt = fussballvereinRepo
            .findEmailById(fussballvereinId);
        String email;
        try {
            email = emailOpt.orElse("N/A");
        } catch (final FussballvereinServiceException ex) {
            log.debug("findEmailById: message={}", ex.getGraphQlException().getMessage());
            email = "N/A";
        }
        return email;
    }
}
