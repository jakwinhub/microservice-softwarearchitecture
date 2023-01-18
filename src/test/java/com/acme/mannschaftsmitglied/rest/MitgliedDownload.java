package com.acme.mannschaftsmitglied.rest;

import java.time.LocalDate;
import java.util.UUID;

@SuppressWarnings({"RecordComponentNumber", "WriteTag"})
record MitgliedDownload (
    LocalDate datum,
    UUID fussballvereinId,
    String fussballvereinVereinsname,
    String fussballvereinEmail,
    @SuppressWarnings("RecordComponentName")
    Links _links
) {
}
