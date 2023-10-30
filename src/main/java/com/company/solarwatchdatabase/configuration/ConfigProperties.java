package com.company.solarwatchdatabase.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties {

    @Value("${api.key}")
    private String apiKey;

    public String getConfigValue(String apiValue) {
        return apiKey;
    }
}
