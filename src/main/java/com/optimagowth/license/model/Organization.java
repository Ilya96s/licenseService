package com.optimagowth.license.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("Organization")
public class Organization implements Serializable {


    private String id;

    @Id
    private String organizationId;

    private String name;

    private String contactName;

    private String contactEmail;

    private String contactPhone;
}
