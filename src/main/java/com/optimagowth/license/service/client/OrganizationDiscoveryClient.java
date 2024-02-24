package com.optimagowth.license.service.client;

import com.optimagowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrganizationDiscoveryClient {

    // DiscoveryClient используется для взаимодействия с балансировщиком Spring Cloud Load Balancer
    private final DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();

        // Поулчаем список всех экземпляров службы "organization-service", которые зарегистрированы в Eureka
        //ServiceInstance - объект содержащий всю информацию о конкретном экземпляре службы
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.isEmpty()) {
            return null;
        }
        // Получаем конечную точку службы
        String serviceUri = String.format("%s/v1/organization/%s", instances.get(0).getUri().toString(), organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId
                );

        return restExchange.getBody();
    }
}
