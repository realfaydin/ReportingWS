package crealytics.java_challenge.reporting_ws;

public enum MonthEnum {

    JAN("January"), FEB("February"), MAR("March"), APR("April"), MAY("May"), JUN("June"),
    JUL("July"), AUG("August"), SEP("September"), OCT("October"), NOV("November"), DEC("December");

    private String monthStr;

    MonthEnum(String monthStr) {
        this.monthStr = monthStr;
    }
}
