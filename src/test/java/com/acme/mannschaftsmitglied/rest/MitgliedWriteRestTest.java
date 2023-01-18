package com.acme.mannschaftsmitglied.rest;


import com.acme.mannschaftsmitglied.entity.Adresse;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.condition.JRE.JAVA_19;
import static org.junit.jupiter.api.condition.JRE.JAVA_20;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static com.acme.mannschaftsmitglied.config.dev.DevConfig.DEV;
import static com.acme.mannschaftsmitglied.rest.MitgliedGetController.REST_PATH;
import static com.acme.mannschaftsmitglied.rest.MitgliedGetRestTest.HOST;
import static com.acme.mannschaftsmitglied.rest.MitgliedGetRestTest.SCHEMA;

@Tag("integration")
@Tag("rest")
@Tag("rest_write")
@DisplayName("Rest-Schnittstelle fuer Schreiben testen")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(DEV)
@EnabledForJreRange(min = JAVA_19, max = JAVA_20)
@SuppressWarnings("StaticAssignmentInConstructor")
public class MitgliedWriteRestTest {
    private static final String FUSSBALLVEREIN_ID = "00000000-0000-0000-0000-00000000010";

    private final WebClient client;

    MitgliedWriteRestTest(@LocalServerPort final int port, final ApplicationContext ctx) {
        final var writeController = ctx.getBean(MitgliedWriteController.class);
        assertThat(writeController).isNotNull();

        final var uriComponents = UriComponentsBuilder.newInstance()
            .scheme(SCHEMA)
            .host(HOST)
            .port(port)
            .path(REST_PATH)
            .build();
        final var baseUrl = uriComponents.toUriString();
        client = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    @ParameterizedTest(name = "[{index}] Abspeichern eines neuen Mitglieds: mitgliedId={0}, adresseId={1}")
    @CsvSource(FUSSBALLVEREIN_ID)
    void create(final String fussballvereinId) {
        final var adresse = Adresse
            .builder()
            .ID(null)
            .plz("76351")
            .ort("Linkenheim-Hochstetten")
            .build();

        final var response = client
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(adresse)
            .exchangeToMono(Mono::just)
            .block();

        assertThat(response)
            .isNotNull();
    }
}
