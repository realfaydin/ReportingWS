package com.crealytics.java_challenge.reporting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
public class ConfigProperties {

    private String reportFolder;

    public String getReportFolder() {
        return reportFolder;
    }

    public void setReportFolder(String reportFolder) {
        this.reportFolder = reportFolder;
    }
}
