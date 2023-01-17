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
package com.acme.mannschaftsmitglied.repository;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository für de DB-Zugriff bei Mitglied.
 *
 * @author Jakob Winkler
 */

@Repository
public interface MitgliedRepository extends JpaRepository<Mitglied, UUID> {
    @EntityGraph(attributePaths = "adresse")
    @Override
    List<Mitglied> findAll();

    @EntityGraph(attributePaths = "adresse")
    @Override
    Optional<Mitglied> findById(UUID id);

    /**
     * Mitglied zu gegebener Email aus der Datenbank ermitteln.
     *
     * @param email Email für die Suche
     * @return Optional mit dem gefundenen Mitglied.
     */
    @Query(value = """
        SELECT m
        FROM Mitglied m
        WHERE lower(m.email) LIKE concat('%', lower (:email), '%')""")
    @EntityGraph(attributePaths = "adresse")
    Optional<Mitglied> findByEmail(String email);

    /**
     * Abfrage, ob es das Mitglied mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return tru, falls es ein solches Mitglied geben sollte.
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean existsByEmail(String email);

    /**
     * Mitglied zu gegebenem Nachnamen aus der Datenbank ermitteln.
     *
     * @param nachname Nachname für die Suche
     * @return Optional mit dem gefundenen Mitglied
     */
    @Query(value = """
        SELECT m
        FROM Mitglied m
        WHERE lower(m.nachname) LIKE concat('%', lower(:nachname), '%')
        ORDER BY m.id""")
    @EntityGraph(attributePaths = "adresse")
    Collection<Mitglied> findByNachname(CharSequence nachname);

    /**
     * Mitglied zu gegebenem Nachnamenspräfix ermitteln.
     *
     * @param prefix Nachname-Präfix
     * @return Collection mit dem gegebenen Nachnamen.
     */
    @Query(value = """
        SELECT DISTINCT m.nachname
        FROM    MITGLIED m
        WHERE   lower(m.nachname) LIKE concat(lower(:prefix), '%')
        ORDER BY m.nachname
        """, nativeQuery = true)
    Collection<String> findNachnameByPrefix(String prefix);

    /**
     * Mitglied zu gegebenem Fussballverein ermitten.
     *
     * @param fussballvereinId ID deszu suchenden Vereins
     * @return Mitglied des gefundenen Vereins
     */

    @EntityGraph(attributePaths = "adresse")
    List<Mitglied> findByFussballvereinId(UUID fussballvereinId);
}

