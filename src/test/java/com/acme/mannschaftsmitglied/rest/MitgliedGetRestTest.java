/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.acme.mannschaftsmitglied.rest;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import static com.acme.mannschaftsmitglied.config.dev.DevConfig.DEV;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.condition.JRE.JAVA_19;
import static org.junit.jupiter.api.condition.JRE.JAVA_20;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Tag("integration")
@Tag("rest")
@Tag("rest_get")
@DisplayName("REST-Schnittstelle fuer GET-Requests")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(DEV)
@EnabledForJreRange(min = JAVA_19, max = JAVA_20)
@SuppressWarnings({"WriteTag", "StaticAssignmentInConstructor"})
public class MitgliedGetRestTest {

    public static final String SCHEMA = "http";
    public static final String HOST = "localhost";

    private final String baseUrl;
    private static WebClient client;
    private final MitgliedRepository mitgliedRepo;
    private static final String ID_VORHANDEN = "00000000-0000-0000-0000-000000000000";

    MitgliedGetRestTest(@LocalServerPort final int port) {
        baseUrl = "http://localhost:" + port + "/rest";
        client = WebClient.builder().baseUrl(baseUrl).build();
        final var clientAdapter = WebClientAdapter.forClient(client);
        final var proxyFactory = HttpServiceProxyFactory
            .builder(clientAdapter)
            .build();
        mitgliedRepo = proxyFactory.createClient(MitgliedRepository.class);
    }

    @ParameterizedTest(name = "[{index}] Suche mit vorhandener ID: id={0}")
    @ValueSource(strings = ID_VORHANDEN)
    void findById(final String id) {
        final var mitglied = mitgliedRepo.getMitglied(id).block();
        assertThat(mitglied).isNotNull();
    }


}
