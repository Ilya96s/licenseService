package com.optimagowth.license.service;

import com.optimagowth.license.config.ServiceConfig;
import com.optimagowth.license.model.License;
import com.optimagowth.license.model.Organization;
import com.optimagowth.license.repository.LicenseRepository;
import com.optimagowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagowth.license.service.client.OrganizationFeignClient;
import com.optimagowth.license.service.client.OrganizationRestTemplateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final ServiceConfig config;

    private final OrganizationFeignClient organizationFeignClient;

    private final OrganizationDiscoveryClient organizationDiscoveryClient;

    private final OrganizationRestTemplateClient organizationRestTemplateClient;

    public License getLicense(String licenseId, String organizationId, String clientType){
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null),licenseId, organizationId));
        }

        Organization organization = retrieveOrganization(organizationId, clientType);
        if (organization != null) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganization(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I'm using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I'm using the rest client");
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I'm using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
        }

        return organization;
    }

    public License createLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license){
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId){
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource.getMessage("license.delete.message", null, null),licenseId);
        return responseMessage;

    }
}
