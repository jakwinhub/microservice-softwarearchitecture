package com.acme.mannschaftsmitglied.entity;

import jakarta.persistence.AttributeConverter;

/**
 * Converter für die Entity Geschlecht.
 */
@SuppressWarnings("FinalParameters")
public class GeschlechtTypeConverter implements AttributeConverter<GeschlechtType, String> {
    @Override
    public String convertToDatabaseColumn(GeschlechtType attribute) {
        return attribute.value;
    }

    @Override
    public  GeschlechtType convertToEntityAttribute(String dbData) {
        return null;
    }
}
