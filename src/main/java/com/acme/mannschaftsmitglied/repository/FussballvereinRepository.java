package com.acme.mannschaftsmitglied.repository;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.graphql.client.GraphQlTransportException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Repository des Fussballvereins.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class FussballvereinRepository {
    private final FussballvereinRestRepository fussballvereinRestRepository;
    private final HttpGraphQlClient graphQlClient;

    /**
     * Suche nach eine Fussballverein mit vorhandener ID.
     *
     * @param fussballvereinId ID des Fussballvereins
     * @return Fussballverein, falls einer gefunden wurde.
     * @throws FussballvereinServiceException falls es keinen Fussbalverein zu der angegebenen ID gibt
     */
    public Optional<Fussballverein> findById(final UUID fussballvereinId) {
        log.debug("finsById: fussallvereinId={}", fussballvereinId);

        final Fussballverein fussballverein;
        try {
            fussballverein = fussballvereinRestRepository.getFussballverein(fussballvereinId.toString()).block();
        } catch (final WebClientResponseException.NotFound ex) {
            log.error("findById: WebClientResponseException.NotFound");
            return Optional.empty();
        } catch (final WebClientException ex) {
            log.error("findById: {}", ex.getClass().getSimpleName());
            throw new FussballvereinServiceException(ex);
        }
        log.debug("finsById: {}", fussballverein);
        return Optional.ofNullable(fussballverein);
    }

    /**
     * Suche nach einem Fussballverein mit der Email.
     *
     * @param fussballvereinId ID des Fussballvereins
     * @return Fussballverein falls einer gefunden wurde
     * @throws FussballvereinServiceException falls es zu der angebenene Email keinen Verein gibt
     */
    public Optional<String> findEmailById(final UUID fussballvereinId) {
        log.debug("finsEmailById: fussballvereinID={}", fussballvereinId);
        final var query = """
            query {
                fussballverein(id: "%s") {
                    email
                }
            }""".formatted(fussballvereinId);

        final String email;
        try {
            email = graphQlClient.document(query)
                .retrieve("fussballverein")
                .toEntity(EmailEntity.class)
                .map(EmailEntity::email)
                .block();
        } catch (final FieldAccessException ex) {
            log.warn("findEmailById: {}", ex.getClass().getSimpleName());
            return Optional.empty();
        } catch (final GraphQlTransportException ex) {
            log.warn("findEmailById: {}", ex.getClass().getSimpleName());
            throw new FussballvereinServiceException(ex);
        }
        log.debug("findEmailById: {}", email);
        return Optional.ofNullable(email);
    }
}
