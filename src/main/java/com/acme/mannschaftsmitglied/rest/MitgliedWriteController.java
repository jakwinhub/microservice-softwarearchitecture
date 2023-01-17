package com.acme.mannschaftsmitglied.rest;

import com.acme.mannschaftsmitglied.service.MitgliedWriteService;
import com.acme.mannschaftsmitglied.service.EmailExistsException;
import com.acme.mannschaftsmitglied.service.ConstraintViolationsException;
import com.acme.mannschaftsmitglied.service.NotFoundException;
import com.acme.mannschaftsmitglied.service.VersionOutdatedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.acme.mannschaftsmitglied.rest.MitgliedGetController.REST_PATH;
import static com.acme.mannschaftsmitglied.rest.MitgliedGetController.ID_PATTERN;
import static org.springframework.http.HttpStatus.PRECONDITION_REQUIRED;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

/**
 * Der Controller bildet die Rest-Schnittstell, wobei die HTTP-Methoden auf die Funktionen der Klasse abgebildet werden.
 */
@RestController
@RequestMapping(REST_PATH)
@Tag(name = "Mitglied API")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "MagicNumber"})
public final class MitgliedWriteController {
    /**
     * Kommentar.
     */
    public static final String PROBLEM_PATH = "/problem/";
    private static final String VERSIONSNUMMER_FEHLT = "Versionsnummer fehlt";
    private final MitgliedWriteService service;
    private final UriHelper uriHelper;

    /**
     * Einen neunen Mitglieds-Datensatz anlegen.
     *
     * @param mitgliedDTO Das Kundenobjektaus dem eigenen requestbody.
     * @param request     Das Request Objekt, um die Location im Rquest-Header zu erstellen.
     * @return Response mit Statuscode 201, Statuscode 400, Statuscode 422.
     * @throws URISyntaxException falls die URI im Request-Body nicht korrekt wäre.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Ein neues Mitglied anlegen")
    @ApiResponse(responseCode = "201", description = "Mitglied neu angelegt")
    @ApiResponse(responseCode = "400", description = "Systaktischer Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    @SuppressWarnings("TrailingComment")
    ResponseEntity<Void> create(
        @RequestBody final MitgliedDTO mitgliedDTO, final HttpServletRequest request) throws URISyntaxException {
        log.debug("create: {}", mitgliedDTO);
        final var mitgliedDb = service.create(mitgliedDTO.toMitglied());
        final var baseUri = uriHelper.getBaseUri(request);
        final var location = new URI(baseUri.toString() + '/' + mitgliedDb.getId());
        return created(location).build();
    }

    /**
     * Das Überschreiben eines bereits vorhandenen Mitgliedsdatensatzes.
     *
     * @param id          ID deszu aktualisierenden Mitglieds.
     * @param version     Version des zu aktualisierenden Objektes.
     * @param request     Der HttpServletRequest.
     * @param mitgliedDTO Das Mitgliedsobjekt aus demeigegangenen Request-Body.
     * @return Response mit dem Statuscode 204, Statuscode 400, Statuscode 404.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Ein bereits vorhandenes Mitglied aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Systaktischer Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Mitglied nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Constrains verletzt der Email nichtvorhanden")
    ResponseEntity<Void> update(
        @PathVariable final UUID id,
        @RequestHeader("IF-Match") final Optional<String> version, final HttpServletRequest request,
        @RequestBody final MitgliedDTO mitgliedDTO
    ) {
        log.debug("update: id={}, {}", id, mitgliedDTO);
        final int versionInt = getVersion(version, request);
        final var mitglied = service.update(mitgliedDTO.toMitglied(), id, versionInt);
        log.debug("update: {}", mitglied);
        return noContent().eTag("\"" + mitglied.getVersion() + '"').build();
    }

    private int getVersion(final Optional<String> versionOpt, final HttpServletRequest request) {
        if (versionOpt.isEmpty()) {
            throw new VersionInvalidException(
                PRECONDITION_REQUIRED,
                VERSIONSNUMMER_FEHLT,
                URI.create(request.getRequestURL().toString()));
        }

        final var versionStr = versionOpt.get();
        if (versionStr.length() < 3 ||
            versionStr.charAt(0) != '"' ||
            versionStr.charAt(versionStr.length() - 1) != '"') {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                "Ungueltiges ETag" + versionStr,
                URI.create(request.getRequestURL().toString())
            );
        }

        final int version;
        try {
            version = Integer.parseInt(versionStr.substring(1, versionStr.length() - 1));
        } catch (final NumberFormatException ex) {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                "Ungueltiges ETag" + versionStr,
                URI.create(request.getRequestURL().toString()),
                ex
            );
        }
        log.trace("gtVersion: version={}", version);
        return version;
    }

    @ExceptionHandler(ConstraintViolationsException.class)
    ResponseEntity<ProblemDetail> handleConstraintViolations(final ConstraintViolationsException ex,
                                                             final HttpServletRequest request
    ) {
        log.debug("handleConstraintViolations: {}", ex.getMessage());

        final var mitgliedViolations = ex.getViolations()
            .stream()
            .map(constraintViolation -> ex.getMessage())
            .toList();
        log.trace("handleConstraintViolaations: {}", mitgliedViolations);
        final String detail = mitgliedViolations.toString();
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create("/problem/ConstraintViolationException"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(EmailExistsException.class)
    ResponseEntity<ProblemDetail> handleEmailExistsException(final EmailExistsException ex,
                                                             final HttpServletRequest request
    ) {
        log.debug("handleEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create("/problem/EmailExists"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(VersionOutdatedException.class)
    ResponseEntity<ProblemDetail> handleVersionOutdated(
        final VersionOutdatedException ex,
        final HttpServletRequest request
    ) {
        log.debug("handleVersionOutdated: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(PRECONDITION_FAILED, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.PRECONDITION.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> handleNotFound(final NotFoundException ex
    ) {
        log.debug("handleNotFound: {}", ex.getMessage());
        return notFound().build();
    }
}

