package com.acme.mannschaftsmitglied.service;

import lombok.Getter;

/**
 * Exception, falls die Emailadresse bereits existiert.
 *
 * @author Jakob Winkler.
 */
@Getter
public class EmailExistsException extends RuntimeException {
    private final String email;

    EmailExistsException(final String email) {
        super("Die Emailadresse " + email + " existiert bereits");
        this.email = email;
    }
}
