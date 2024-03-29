/*
 * Copyright (C) 2018 - present Juergen Zimmermann, Hochschule Karlsruhe
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
package com.acme.mannschaftsmitglied;

import com.acme.mannschaftsmitglied.repository.FussballvereinRestRepository;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

/**
 * Beans für die REST-Schnittstelle zu "kunde" (WebClient) und für die GraphQL-Schnittstelle zu "kunde"
 * (HttpGraphQlClient).
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
public interface ClientConfig {
    /**
     * Kommentar.
     */
    String GRAPHQL_PATH = "/graphql";
    /**
     * Kommentar.
     */
    int KUNDE_DEFAULT_PORT = 8081;

    /**
     * Markieren einer Klasse als Bean.
     *
     * @return Webclient.builder als Builder für die Beanklasse
     */
    @Bean
    default WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Builder um die URI für die Ermitteleten Daten zu bauen.
     *
     * @return URI bestehend aus Schema, Host und dem zugehörigen Port
     */
    @Bean
    @SuppressWarnings("CallToSystemGetenv")
    default UriComponentsBuilder uriComponentsBuilder() {
        final var kundeHostEnv = System.getenv("FUSSBALLVEREIN_SERVICE_HOST");
        final var kundeHost = kundeHostEnv == null ? "localhost" : kundeHostEnv;
        final var log = LoggerFactory.getLogger(ClientConfig.class);
        log.info("kundeHost: {}", kundeHost);
        final var kundePortEnv = System.getenv("FUSSBALLVEREIN_SERVICE_PORT");
        final int kundePort;
        if (kundePortEnv == null) {
            kundePort = KUNDE_DEFAULT_PORT;
        } else {
            kundePort = Integer.parseInt(kundePortEnv);
        }
        log.info("kundePort: {}", kundePort);
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host(kundeHost)
            .port(kundePort);
    }

    /**
     * Als Bean markierte Methode welche einen Webclient erstellen soll.
     *
     * @param webClientBuilder Builder für den Webclient
     * @param uriComponentsBuilder Builder für die Komponenten der URI
     * @return webClientBuilder enthält die BaseURL und einen Filter
     */
    @Bean
    default WebClient webClient(
        final WebClient.Builder webClientBuilder,
        final UriComponentsBuilder uriComponentsBuilder
    ) {
        final var uriComponents = uriComponentsBuilder.build();
        final var baseUrl = uriComponents.toUriString();
        return webClientBuilder
            .baseUrl(baseUrl)
            .filter(basicAuthentication("admin", "p"))
            .build();
    }

    /**
     * Markiert das FussballvereinRestRepository als Bean Methode.
     *
     * @param builder ein Builder
     * @return FussballvereinRestRepository als Bean Klasse
     */
    @Bean
    default FussballvereinRestRepository fussballvereinRestRepository(final WebClient builder) {
        final var clientAdapter = WebClientAdapter.forClient(builder);
        final var proxyFactory = HttpServiceProxyFactory.builder(clientAdapter).build();
        return proxyFactory.createClient(FussballvereinRestRepository.class);
    }

    /**
     * HttpGraphQL Client.
     *
     * @param webClientBuilder builder um der WebCient zu bauen
     * @param uriComponentsBuilder builder um die Komponenten der Uri zu laden
     * @return einen vollständigen graphQLClient
     */
    @Bean
    default HttpGraphQlClient graphQlClient(
        final WebClient.Builder webClientBuilder,
        final UriComponentsBuilder uriComponentsBuilder
    ) {
        final var uriComponents = uriComponentsBuilder
            .path(GRAPHQL_PATH)
            .build();
        final var baseUrl = uriComponents.toUriString();
        final var webclient = webClientBuilder
            .baseUrl(baseUrl)
            .filter(basicAuthentication("admin", "p"))
            .build();
        return HttpGraphQlClient.builder(webclient).build();
    }
}
