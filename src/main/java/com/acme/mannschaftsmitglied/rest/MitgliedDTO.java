package com.acme.mannschaftsmitglied.rest;

import com.acme.mannschaftsmitglied.entity.Adresse;
import com.acme.mannschaftsmitglied.entity.FamilienstandType;
import com.acme.mannschaftsmitglied.entity.GeschlechtType;
import com.acme.mannschaftsmitglied.entity.Mitglied;
import java.net.URL;
import java.time.LocalDate;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Mitglieds.
 *
 * @author Jakob Winkler
 * @param nachname Gültiger Nachweis eines Mitglieds.
 * @param email Email eines Kunden.
 * @param mannschaft Mannschaft eines Mitglieds mit eingeschränkten Werten.
 * @param geburtsdatum Das Geburtsdatum eines Mitglieds.
 * @param homepage Die Homepage eines Mitglieds.
 * @param geschlecht  Das Geschlecht eines Mitglieds.
 * @param familienstand Der Familienstand eines Mitglieds.
 * @param adresse Die Adresse eines Mitglieds.
 */

@SuppressWarnings("RecordComponentNumber")
record MitgliedDTO(
    String nachname,
    String email,
    int mannschaft,
    LocalDate geburtsdatum,
    URL homepage,
    GeschlechtType geschlecht,
    FamilienstandType familienstand,
    Adresse adresse
) {
    /**
     * Konvertierung in ein Objekt des Anwendungskerns.
     *
     * @return Mitgliedsobjekt für den Anwendungskern.
     */
    Mitglied toMitglied() {
        return Mitglied
            .builder()
            .id(null)
            .version(0)
            .nachname(nachname)
            .email(email)
            .mannschaft(mannschaft)
            .geburtsdatum(geburtsdatum)
            .homepage(homepage)
            .geschlecht(geschlecht)
            .familienstand(familienstand)
            .adresse(adresse)
            .erzeugt(null)
            .aktualisiert(null)
            .build();
    }

}
