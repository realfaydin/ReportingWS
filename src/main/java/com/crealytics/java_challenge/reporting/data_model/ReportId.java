package com.crealytics.java_challenge.reporting.data_model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ReportId implements Serializable{
    private MonthEnum month;
    private String site;

    public ReportId(){

    }

    public ReportId(MonthEnum month, String site) {
        this.month = month;
        this.site = site;
    }
}
