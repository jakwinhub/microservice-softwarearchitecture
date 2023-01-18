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
package com.acme.mannschaftsmitglied.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import net.minidev.json.annotate.JsonIgnore;


import java.util.UUID;


/**
 * Adressdaten für die Anwendungslogik und zum Abspeichern in der DB.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Entity
@Table(name = "adresse")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({"JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup", "MemberName", "MissingSummary"})
public class Adresse {
    /**
     * Konstante für den regulären Ausdruck einer Postleitzahl als 5-stellige Zahl mit führender Null.
     */
    public static final String PLZ_PATTERN = "^\\d{5}$";

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID ID;

    /**
     * Die Postleitzahl für die Adresse.
     *
     * @param plz Die Postleitzahl als String
     * @return Die Postleitzahl als String
     */
    @NotNull
    @Pattern(regexp = PLZ_PATTERN)
    private String plz;

    /**
     * Der Ort für die Adresse.
     *
     * @param ort Der Ort als String
     * @return Der Ort als String
     */
    @NotBlank
    private String ort;




}
