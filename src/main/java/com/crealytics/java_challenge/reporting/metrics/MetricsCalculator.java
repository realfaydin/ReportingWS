package com.crealytics.java_challenge.reporting.metrics;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MetricsCalculator {

    public double calculateCTR(double clicks, double impressions){
        double ctr = clicks/impressions*100;
        return truncateDoubleValue(ctr);
    }

    public double calculateCR(double conversions, double impressions){
        double cr = conversions/impressions*100;
        return truncateDoubleValue(cr);
    }

    public double calculateFillRate(double impressions, double requests){
        double fillRate = (impressions/requests)*100;
        return truncateDoubleValue(fillRate);
    }

    public double calculateEffectiveCPM(double revenue, double impressions){
        double effectiveCPM = (revenue*1000)/impressions;
        return truncateDoubleValue(effectiveCPM);
    }

    private double truncateDoubleValue(double valueToBeTruncated){
        return BigDecimal.valueOf(valueToBeTruncated)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
