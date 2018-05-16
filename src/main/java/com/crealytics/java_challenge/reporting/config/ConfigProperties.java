package com.crealytics.java_challenge.reporting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties
@Component
public class ConfigProperties {

    private String reportDirectory;

    public String getReportDirectory() {
        return reportDirectory;
    }

    public void setReportDirectory(String reportDirectory) {
        this.reportDirectory = reportDirectory;
    }
}
