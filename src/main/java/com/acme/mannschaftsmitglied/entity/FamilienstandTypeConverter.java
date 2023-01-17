package com.acme.mannschaftsmitglied.entity;

import jakarta.persistence.AttributeConverter;

/**
 * Converter für den Familienstands-Typ.
 */
public class FamilienstandTypeConverter implements AttributeConverter<GeschlechtType, String> {
    @Override
    public String convertToDatabaseColumn(final GeschlechtType attribute) {
        return attribute.value;

    }

    @Override
    public GeschlechtType convertToEntityAttribute(final String dbData) {
        return null;
    }
}
