package com.crealytics.java_challenge.reporting.data_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private int id;

    @ApiModelProperty
    private MonthEnum month;

    @ApiModelProperty
    private String site;

    @ApiModelProperty
    private int requests;
    @ApiModelProperty
    private int impressions;
    @ApiModelProperty
    private int clicks;
    @ApiModelProperty
    private int conversions;
    @ApiModelProperty
    private double revenue;

    @Transient
    @ApiModelProperty
    private double clickThroughRate;
    @Transient
    @ApiModelProperty
    private double conversionRate;
    @Transient
    @ApiModelProperty
    private double fillRate;
    @Transient
    @ApiModelProperty
    private double effectiveCostPerThousand;

    public Report() {
    }

    public Report(MonthEnum monthEnum, String site, int requests, int impressions, int clicks, int conversions, double revenue, double clickThroughRate, double conversionRate, double fillRate, double effectiveCostPerThousand) {
        this.month = monthEnum;
        this.site = site;
        this.requests = requests;
        this.impressions = impressions;
        this.clicks = clicks;
        this.conversions = conversions;
        this.revenue = revenue;
        this.clickThroughRate = clickThroughRate;
        this.conversionRate = conversionRate;
        this.fillRate = fillRate;
        this.effectiveCostPerThousand = effectiveCostPerThousand;
    }

    @JsonIgnore
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MonthEnum getMonth() {
        return month;
    }

    public void setMonth(MonthEnum month) {
        this.month = month;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getConversions() {
        return conversions;
    }

    public void setConversions(int conversions) {
        this.conversions = conversions;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getClickThroughRate() {
        return clickThroughRate;
    }

    public void setClickThroughRate(double clickThroughRate) {
        this.clickThroughRate = clickThroughRate;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public double getFillRate() {
        return fillRate;
    }

    public void setFillRate(double fillRate) {
        this.fillRate = fillRate;
    }

    public double getEffectiveCostPerThousand() {
        return effectiveCostPerThousand;
    }

    public void setEffectiveCostPerThousand(double effectiveCostPerThousand) {
        this.effectiveCostPerThousand = effectiveCostPerThousand;
    }
}
