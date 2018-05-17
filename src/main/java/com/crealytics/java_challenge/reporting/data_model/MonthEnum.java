package com.crealytics.java_challenge.reporting.data_model;

public enum MonthEnum {

    JAN("January"), FEB("February"), MAR("March"), APR("April"), MAY("May"), JUN("June"),
    JUL("July"), AUG("August"), SEP("September"), OCT("October"), NOV("November"), DEC("December");

    private String monthStr;

    MonthEnum(String monthStr) {
        this.monthStr = monthStr;
    }
}
