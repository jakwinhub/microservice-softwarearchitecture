package com.acme.mannschaftsmitglied.service;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import com.acme.mannschaftsmitglied.repository.MitgliedRepository;

import java.util.Objects;
import java.util.UUID;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Anwendungslogik für Kunden auch mit Bean Violation.
 *
 * @author Jakob Winkler
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MitgliedWriteService {
    private final MitgliedRepository repo;
    private final Validator validator;

    /**
     * Neues Mitglied erstellen.
     *
     * @param mitglied Mitglied das angelegt werden soll.
     * @return neu angelegtes Mitglied mit generierter UUID
     * @throws ConstraintViolationsException falls constrains verletzt wurden
     * @throws EmailExistsException          falls die angegebene Email bereits existiert
     */
    @Transactional
    public Mitglied create(final Mitglied mitglied) {
        log.debug("create: {}", mitglied);
        final var violations = validator.validate(mitglied);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }
        if (repo.existsByEmail(mitglied.getEmail())) {
            log.debug("create: violations={}", violations);
            throw new EmailExistsException(mitglied.getEmail());
        }
        final var mitgleideDB = repo.save(mitglied);
        log.debug("create: {}", mitgleideDB);
        return mitgleideDB;
    }

    /**
     * Aktualisieren eines vorhandenen Mitglieds.
     *
     * @param mitglied Das zu aktualisierende Mitgliedsobjekt.
     * @param id       Die ID des zu aktualisierenden Mitglieds.
     * @param version  Die Version des zu aktualiserenden Mitglieds
     * @return Das aktualierte Mitglied.
     * @throws ConstraintViolationsException falls ein Constraint verletzt wurde.
     * @throws NotFoundException             Falls das mitglied nicht gefunden werden konnte.
     * @throws VersionOutdatedException      Falls die version nicht mehr mit der vorigen übereinstimmt
     * @throws EmailExistsException          Falls die Email bereits existiert
     */
    @Transactional
    public Mitglied update(final Mitglied mitglied, final UUID id, final int version) {
        log.debug("update: {}", mitglied);
        log.debug("update: id={}, version={}", id, version);

        final var violations = validator.validate(mitglied);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);

        }
        final var mitgliedOpt = repo.findById(id);
        if (mitgliedOpt.isEmpty()) {
            throw new NotFoundException();
        }
        var mitgliedDB = mitgliedOpt.get();
        if (version != mitgliedDB.getVersion()) {
            throw new VersionOutdatedException(version);
        }
        final var email = mitglied.getEmail();
        if (!Objects.equals(email, mitgliedDB.getEmail()) && repo.existsByEmail(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }
        mitgliedDB.set(mitglied);
        mitgliedDB = repo.save(mitgliedDB);
        log.debug("update: {}", mitgliedDB);
        return mitgliedDB;
    }

    /**
     * Löschn eines ausgewählten Objektes.
     *
     * @param id ID des zu löschenden Objektes
     */
    @Transactional
    public void deleteById(final UUID id) {
        log.debug("deleteById: id={}", id);
        repo.deleteById(id);
    }
}
