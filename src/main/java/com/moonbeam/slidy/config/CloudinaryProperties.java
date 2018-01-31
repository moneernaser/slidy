package com.moonbeam.slidy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties("cloudinary")
@Validated
public class CloudinaryProperties {


    @NotNull
    private String apiKey;

    @NotNull
    private String cloudName;

    @NotNull
    private String apiSecret;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    @Override
    public String toString() {
        return "CloudinaryProperties{" +
            "apiKey='" + apiKey + '\'' +
            ", cloudName='" + cloudName + '\'' +
            ", apiSecret='" + apiSecret + '\'' +
            '}';
    }
}
