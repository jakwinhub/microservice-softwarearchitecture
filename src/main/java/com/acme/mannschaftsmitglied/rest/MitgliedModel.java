package com.acme.mannschaftsmitglied.rest;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import com.acme.mannschaftsmitglied.entity.FamilienstandType;
import com.acme.mannschaftsmitglied.entity.GeschlechtType;
import com.acme.mannschaftsmitglied.entity.Adresse;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.net.URL;
import java.time.LocalDate;

/**
 * Model Klasse fü Spring HATEOAS.@lombo.Data fasstdie Annotationen @ToString, @EqualsHashCode, @Getter, @Setter,
 * und @RequiredArgsConstructor zusammen.
 */
@Relation(collectionRelation = "mannschaftsmitglieder", itemRelation = "mannschaftsmitglied")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
final class MitgliedModel extends RepresentationModel<MitgliedModel> {

    private final String nachname;

    @EqualsAndHashCode.Include
    private final String email;
    private final int mannschaft;
    private final LocalDate geburtsdatum;
    private final URL homepage;
    private final GeschlechtType geschlecht;
    private final FamilienstandType familienstand;
    private final Adresse adresse;

    MitgliedModel(final Mitglied mitglied) {
        nachname = mitglied.getNachname();
        email = mitglied.getEmail();
        mannschaft = mitglied.getMannschaft();
        geburtsdatum = mitglied.getGeburtsdatum();
        homepage = mitglied.getHomepage();
        geschlecht = mitglied.getGeschlecht();
        familienstand = mitglied.getFamilienstand();
        adresse = mitglied.getAdresse();
    }
}
