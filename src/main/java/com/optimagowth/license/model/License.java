package com.optimagowth.license.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "License")
public class License extends RepresentationModel<License> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "license_id", nullable = false)
    private String licenseId;

    private String description;

    @Column(name ="organization_id", nullable = false)
    private String organizationId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    private String comment;

    public License withComment(String comment) {
        this.setComment(comment);
        return this;
    }
}
