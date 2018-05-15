package com.crealytics.java_challenge.reporting.data_load;

import com.crealytics.java_challenge.reporting.config.ConfigProperties;
import com.crealytics.java_challenge.reporting.data_model.MonthEnum;
import com.crealytics.java_challenge.reporting.data_model.Report;
import com.crealytics.java_challenge.reporting.data_model.ReportId;
import com.crealytics.java_challenge.reporting.metrics.MetricsCalculator;
import com.crealytics.java_challenge.reporting.store.InMemoryReportStore;
import com.crealytics.java_challenge.reporting.util.MonthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class CSVDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    Logger logger = LoggerFactory.getLogger(CSVDataLoader.class.getName());


    @Autowired
    private InMemoryReportStore inMemoryReportStore;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private MetricsCalculator metricsCalculator;

    @Autowired
    private MonthUtil monthUtil;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        if(logger.isInfoEnabled()){
            logger.info("LOADING CSVs... ");
        }

        String reportDirectoryName = null;
        File reportDirectory = null;
        if(configProperties.getReportFolder() == null){
            logger.info("Using default directory for reading report files...");
            try {
                reportDirectory = ResourceUtils.getFile("classpath:reports");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            reportDirectoryName = configProperties.getReportFolder();
            reportDirectory = new File(reportDirectoryName);
        }

        if(!reportDirectory.isDirectory()){
            logger.error("Provided path is not a directory: "+reportDirectoryName);
        }

        //TODO: Find a better way to build the path
        for (String fileName : reportDirectory.list()){
            final MonthEnum month = getMonthFromFileName(fileName);
            if(month == null){
                System.exit(-1);
            }
            try (Stream<String> stream = Files.lines(Paths.get(reportDirectory.getCanonicalPath()+File.separator+fileName))) {
                stream.skip(1).forEach(s->this.loadData(s, month));
            } catch (IOException e) {
                logger.error("Error occurred while reading file: "+fileName, e);
                System.exit(-1);
            }
        }
    }

    private MonthEnum getMonthFromFileName(String fileName) {
        String[] fileNameTokens = fileName.split("_");
        if(fileNameTokens.length <3){
            logger.error("File name format is not correct: "+fileName);
            return null;
        }

        MonthEnum monthEnum = monthUtil.getMonthEnum(fileNameTokens[1]);
        if(monthEnum == null ){
            logger.error("File name format is not correct: "+fileName);
        }
        return monthEnum;
    }

    //TODO: Handle edge cases and validation
    private void loadData(String record, MonthEnum month){
        String[] fields = record.split(",");
        String site = fields[0];
        String s_requests = fields[1];
        String s_impressions = fields[2];
        String s_clicks = fields[3];
        String s_conversions = fields[4];
        String s_revenue = fields[5];

        try{
            int requests = Integer.parseInt(s_requests.trim());
            int impressions = Integer.parseInt(s_impressions.trim());
            int clicks = Integer.parseInt(s_clicks.trim());
            int conversions = Integer.parseInt(s_conversions.trim());
            double revenue = Double.parseDouble(s_revenue.trim());

            double conversionRate = metricsCalculator.calculateCR(conversions,impressions);
            double clickThroughRate = metricsCalculator.calculateCTR(clicks, impressions);
            double effectiveCostPerThousand = metricsCalculator.calculateEffectiveCPM(revenue, impressions);
            double fillRate = metricsCalculator.calculateFillRate(impressions,requests);

            Report report = new Report();
            report.setReportId(new ReportId(month, site));
            report.setRequests(requests);
            report.setImpressions(impressions);
            report.setClicks(clicks);
            report.setConversions(conversions);
            report.setRevenue(revenue);
            report.setConversionRate(conversionRate);
            report.setClickThroughRate(clickThroughRate);
            report.setEffectiveCostPerThousand(effectiveCostPerThousand);
            report.setFillRate(fillRate);

            inMemoryReportStore.save(report);

        }catch (NumberFormatException e){
            logger.error("Cannot parse record: "+record,e);
        }
    }
}
