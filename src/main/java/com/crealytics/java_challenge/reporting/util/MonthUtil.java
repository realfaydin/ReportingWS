package com.crealytics.java_challenge.reporting.util;

import com.crealytics.java_challenge.reporting.controller.BadRequestException;
import com.crealytics.java_challenge.reporting.data_model.MonthEnum;
import org.springframework.stereotype.Component;

@Component
public class MonthUtil {

    public MonthEnum getMonthEnum(String month){

        if(month == null){
            return null;
        }
        month = month.toLowerCase();

        try {
            int monthInteger = Integer.parseInt(month);
            return getMonthFromInteger(monthInteger);
        }catch (NumberFormatException e){
           //No need to take any action here. We'll fall back to string format
        }
        return getMonthFromString(month);
    }

    private MonthEnum getMonthFromString(String month) {
        switch (month){
            case "jan": return MonthEnum.JAN;
            case "january": return MonthEnum.JAN;

            case "feb": return MonthEnum.FEB;
            case "february": return MonthEnum.FEB;

            case "mar": return MonthEnum.MAR;
            case "march": return MonthEnum.MAR;

            case "apr": return MonthEnum.APR;
            case "april": return MonthEnum.APR;

            case "may": return MonthEnum.MAY;


            case "jun": return MonthEnum.JUN;
            case "june": return MonthEnum.JUN;

            case "jul": return MonthEnum.JUL;
            case "july": return MonthEnum.JUL;

            case "aug": return MonthEnum.AUG;
            case "august": return MonthEnum.AUG;

            case "sep": return MonthEnum.SEP;
            case "september": return MonthEnum.SEP;

            case "oct": return MonthEnum.OCT;
            case "october": return MonthEnum.OCT;

            case "nov": return MonthEnum.NOV;
            case "november": return MonthEnum.NOV;

            case "dec": return MonthEnum.DEC;
            case "december": return MonthEnum.DEC;

        }

        throw new BadRequestException();
    }

    private MonthEnum getMonthFromInteger(int monthInteger) {

        switch (monthInteger){
            case 1: return MonthEnum.JAN;
            case 2: return MonthEnum.FEB;
            case 3: return MonthEnum.MAR;
            case 4: return MonthEnum.APR;
            case 5: return MonthEnum.MAY;
            case 6: return MonthEnum.JUN;
            case 7: return MonthEnum.JUL;
            case 8: return MonthEnum.AUG;
            case 9: return MonthEnum.SEP;
            case 10: return MonthEnum.OCT;
            case 11: return MonthEnum.NOV;
            case 12: return MonthEnum.DEC;
        }
        return null;
    }
}
