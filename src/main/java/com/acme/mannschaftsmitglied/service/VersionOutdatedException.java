package com.acme.mannschaftsmitglied.service;

import lombok.Getter;

/**
 * Exception falls die Version des Objektes nciht mehr auf dem Neuesten Stand ist.
 */
@Getter
public class VersionOutdatedException extends RuntimeException {
    private final int version;

    VersionOutdatedException(final int version) {
        super("Die Versionsnummer " + version + " ist veraltet.");
        this.version = version;
    }

}
