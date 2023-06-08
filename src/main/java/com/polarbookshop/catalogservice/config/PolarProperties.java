package com.polarbookshop.catalogservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "polar")
@Setter
@Getter
public class PolarProperties {
    /**
     * A greeting message to welcome users
     */
    private String greeting;
}
