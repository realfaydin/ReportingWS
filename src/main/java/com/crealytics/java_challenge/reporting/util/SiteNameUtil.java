package com.crealytics.java_challenge.reporting.util;

import com.crealytics.java_challenge.reporting.controller.exception.BadRequestException;
import com.crealytics.java_challenge.reporting.data_model.SiteNameEnum;
import org.springframework.stereotype.Component;

@Component
public class SiteNameUtil {

    public SiteNameEnum getSiteNameEnum(String siteName){

        if(siteName == null){
            return null;
        }

        siteName = siteName.toLowerCase();

        switch (siteName){
            case "desktop_web": return SiteNameEnum.DESKTOP_WEB;
            case "mobile_web": return SiteNameEnum.MOBILE_WEB;
            case "android": return SiteNameEnum.ANDROID;
            case "ios": return SiteNameEnum.IOS;
        }
        throw new BadRequestException();
    }
}
