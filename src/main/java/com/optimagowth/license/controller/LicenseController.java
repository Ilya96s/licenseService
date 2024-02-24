package com.optimagowth.license.controller;

import com.optimagowth.license.model.License;
import com.optimagowth.license.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    /**
     *
     * @param clientType определяет тип клиента, который бцдет использоваться.
     *                   Типы которые можно передать этому маршруту:
     *                   -Discovery - требует использовать для вызова службы организаций
     *                   клиента Discovery Client и стандартный класс Spring RestTemplate
     *                   -Rest - требует использовать для вызова службы Load Balancer
     *                   расширенный шаблон RestTemplate
     *                   -Feign - требует использовать для вызова службы через Load Balancer
     *                   клиентскую библиотеку Netflix Feign
     */
    @GetMapping("/{licenseId}/{clientType}")
    public License getLicenseWithClient(@PathVariable String organizationId,
                                        @PathVariable("licenseId") String licenseId,
                                        @PathVariable("clientType") String clientType) {
        return licenseService.getLicense(organizationId, licenseId, clientType);
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId, "");
        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
        );

        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.createLicense(request));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}
