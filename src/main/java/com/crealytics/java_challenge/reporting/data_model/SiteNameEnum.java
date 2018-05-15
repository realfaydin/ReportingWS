package com.crealytics.java_challenge.reporting.data_model;

public enum SiteNameEnum {

    DESKTOP_WEB("desktop web"), IOS("iOS"), MOBILE_WEB("mobile web"), ANDROID("android");

    private String siteName;

    SiteNameEnum(String siteName){
        this.siteName = siteName;
    }

    public String getSiteName(){
        return this.siteName;
    }
}
