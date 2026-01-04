package com.zxtx.hummer.common.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hummer")
public class HummerVueConfig {
    private String vueUrl;
}
