package com.acme.mannschaftsmitglied.rest;

@SuppressWarnings("WriteTag")
public record Links(
    Link self,
    Link list,
    Link add,
    Link update,
    Link remove
) {
}
