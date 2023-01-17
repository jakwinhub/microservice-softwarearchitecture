package com.acme.mannschaftsmitglied.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Version;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Daten eines Mitglieds.
 */
@Entity
@Table(name = "mitglied")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({"ClassFanOutComplexity", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"})
public class Mitglied {
    /**
     * Muster für eine gültiges Mitglied.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";
    /**
     * Kleinster Wert für eine Mannschaft.
     */
    public static final int MIN_MANNSCHAFT = 1;
    /**
     * Maximaler Wert für eine Mannschaft.
     */
    public static final int MAX_MANNSCHAFT = 9;
    /**
     * Die ID des Mitglieds.
     */
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue
    private UUID id;
    /**
     * Versionsnummer.
     */
    @Version
    private int version;
    /**
     * Der Nachname des Kunden.
     */
    @NotEmpty
    @Pattern(regexp = NACHNAME_PATTERN)
    private String nachname;
    /**
     * Die Emailadresse des Kunden.
     */
    @Email
    @NotNull
    private String email;
    /**
     * Die Mannschaft des Mitglieds.
     */
    @Min(MIN_MANNSCHAFT)
    @Max(MAX_MANNSCHAFT)
    private int mannschaft;
    /**
     * Hat der Kunde den Newsletter abonniert.
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY)
    @JoinColumn(updatable = false)
    @Valid
    @ToString.Exclude
    private Adresse adresse;
    /**
     * Das Geburtsdatum des Kunden.
     */
    @Past
    private LocalDate geburtsdatum;
    /**
     * Die URL zur Homepage des Kunden.
     */
    private URL homepage;
    /**
     * Das Geschlecht des Kunden.
     */
    @Convert(converter = GeschlechtTypeConverter.class)
    private GeschlechtType geschlecht;
    /**
     * Der Familienstand des Kunden.
     */
    @Convert(converter = FamilienstandTypeConverter.class)
    private FamilienstandType familienstand;
    @CreationTimestamp
    private LocalDateTime erzeugt;
    @UpdateTimestamp
    private LocalDateTime aktualisiert;
    /**
     * Entity der Datenbank Fussballverein.
     */
    @Column(name = "Fussballverein_ID")
    private UUID fussballvereinId;
    /**
     * Name des Fussbalvereins.
     */
    @Transient
    private String fussballvereinVereinsname;
    /**
     * Email des Fusballvereins.
     */
    @Transient
    private String fussballvereinEmail;

    /**
     * setzen der Attribute.
     *
     * @param mitglied Mitglied
     */
    public void set(final Mitglied mitglied) {
        nachname = mitglied.nachname;
        email = mitglied.email;
        mannschaft = mitglied.mannschaft;
        geburtsdatum = mitglied.geburtsdatum;
        homepage = mitglied.homepage;
        geschlecht = mitglied.geschlecht;
        familienstand = mitglied.familienstand;
    }
}




