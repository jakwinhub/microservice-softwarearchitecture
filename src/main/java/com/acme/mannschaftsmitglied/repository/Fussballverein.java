package com.acme.mannschaftsmitglied.repository;

/**
 * Fussballverein als Entity.
 *
 * @param vereinsname Name des Fussballvereins
 * @param email Email des Fussballvereins
 */
public record Fussballverein(String vereinsname, String email) {
}
