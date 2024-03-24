package com.optimagowth.license.service.client;

import com.optimagowth.license.model.Organization;
import com.optimagowth.license.repository.OrganizationRedisRepository;
import com.optimagowth.license.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrganizationRestTemplateClient {

    private final RestTemplate restTemplate;

    private final OrganizationRedisRepository redisRepository;

    public Organization getOrganization(String organizationId) {
        log.info("In Licensing Service.getOrganization: {}",
                UserContext.getCorrelationId());

        Organization organization = checkRedisCache(organizationId);
        if (organization !=  null) {
            log.info("I have successfully retrieved an organization {} from redis cache: {}",
                    organizationId, organization);
            return organization;
        }

        log.info("Unable to locate organization from the reid cache: {}", organizationId);
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://organization-service/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationObject(organization);
        }

        return restExchange.getBody();
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return redisRepository.findById(organizationId)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error encountered while trying to retrieve organization {} check Redis cache. Exception {}",
                    organizationId, e);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            redisRepository.save(organization);
        } catch (Exception e) {
            log.error("Unable to cache organization {} in Redis. Exception {}",
                    organization.getId(), e);
        }
    }
}
