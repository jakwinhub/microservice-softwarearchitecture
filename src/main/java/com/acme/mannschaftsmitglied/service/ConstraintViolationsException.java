package com.acme.mannschaftsmitglied.service;

import com.acme.mannschaftsmitglied.entity.Mitglied;
import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import lombok.Getter;

/**
 * Exception falls Constrains verletzt wurden.
 */
@Getter
public class ConstraintViolationsException extends RuntimeException {
    private final Collection<ConstraintViolation<Mitglied>> violations;

    ConstraintViolationsException(
        @SuppressWarnings("ParameterHidesMemberVariable")
        final Collection<ConstraintViolation<Mitglied>> violations
    ) {
        super("Constrains sind verletzt");
        this.violations = violations;
    }
}
