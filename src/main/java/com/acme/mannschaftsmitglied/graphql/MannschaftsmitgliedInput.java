package com.acme.mannschaftsmitglied.graphql;

import com.acme.mannschaftsmitglied.entity.Adresse;
import com.acme.mannschaftsmitglied.entity.FamilienstandType;
import com.acme.mannschaftsmitglied.entity.GeschlechtType;
import com.acme.mannschaftsmitglied.entity.Mitglied;

import java.net.URL;
import java.time.LocalDate;

/**
 * Eine Value Klasse für die Eingbedaten passend zu MitgliedInput aus dem GraphQL-Schema.
 *
 * @param nachname      Nachname
 * @param email         Email
 * @param mannschaft    Mannschaft
 * @param geburtsdatum  Geburtsdatum
 * @param homepage      Homepage
 * @param geschlecht    Geschlecht
 * @param familienstand Familienstand
 * @param adresse       Adresse
 */
@SuppressWarnings("RecordComponentNumber")
public record MannschaftsmitgliedInput(
    String nachname,
    String email,
    int mannschaft,
    String geburtsdatum,
    URL homepage,
    GeschlechtType geschlecht,
    FamilienstandType familienstand,
    AdresseInput adresse
) {
    /**
     * Konvertieren eines Mitgliedsobjekts inder Entity-Klasse Mitglied.
     *
     * @return Das konvertierende Mitglied-Objekt
     */

    Mitglied toMitglied() {
        final LocalDate geburtsdatumTmp;
        geburtsdatumTmp = LocalDate.parse(geburtsdatum);
        final var adresseTmp = Adresse.builder().plz(adresse.plz()).ort(adresse.ort()).build();

        return Mitglied
            .builder()
            .id(null)
            .nachname(nachname)
            .email(email)
            .mannschaft(mannschaft)
            .geburtsdatum(geburtsdatumTmp)
            .geschlecht(geschlecht)
            .familienstand(familienstand)
            .adresse(adresseTmp)
            .build();
    }
}
