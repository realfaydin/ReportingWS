package com.crealytics.java_challenge.reporting.controller;

import com.crealytics.java_challenge.reporting.controller.exception.InternalServerErrorException;
import com.crealytics.java_challenge.reporting.controller.exception.NotFoundException;
import com.crealytics.java_challenge.reporting.data_model.MonthEnum;
import com.crealytics.java_challenge.reporting.data_model.Report;
import com.crealytics.java_challenge.reporting.data_model.ReportId;
import com.crealytics.java_challenge.reporting.data_model.SiteNameEnum;
import com.crealytics.java_challenge.reporting.metrics.MetricsCalculator;
import com.crealytics.java_challenge.reporting.store.InMemoryReportStore;
import com.crealytics.java_challenge.reporting.util.MonthUtil;
import com.crealytics.java_challenge.reporting.util.SiteNameUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController

@Api(value="ReportService", description="Generates CRT, CR, eCPM and FR")
public class ReportController {

    Logger logger = LoggerFactory.getLogger(ReportController.class.getName());

    @Autowired
    InMemoryReportStore inMemoryReportStore;

    @Autowired
    MonthUtil monthUtil;

    @Autowired
    SiteNameUtil siteNameUtil;

    @Autowired
    MetricsCalculator metricsCalculator;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "No report found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/reports")
    public Report generateReport(@RequestParam(value = "month", required = false) String month, @RequestParam(value = "site", required = false) String site) {

        try {
            if (logger.isInfoEnabled()) {
                logger.info("Request received for report generation with params-> month=" + month + ", site=" + site);
            }

            MonthEnum monthEnum = monthUtil.getMonthEnum(month);
            SiteNameEnum siteNameEnum = siteNameUtil.getSiteNameEnum(site);
            String siteName = (siteNameEnum == null) ? null : siteNameEnum.getSiteName();

            Report report = new Report();

            String[] ignorePaths = prepareIgnorePaths(monthEnum, siteName);

            report.setReportId(new ReportId(monthEnum, siteName));
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnoreNullValues().withIgnorePaths(ignorePaths);
            Example<Report> example = Example.of(report, matcher);

//        Optional result = inMemoryReportStore.findById(new ReportId(monthEnum, siteName));
            List<Report> result = inMemoryReportStore.findAll(example);

            if (result == null || result.size() == 0) {
                throw new NotFoundException();
            } else {
                return prepareResult(result, monthEnum, siteName);
            }
        }catch (Exception e){
            throw new InternalServerErrorException();
        }
    }

    private Report prepareResult(List<Report> result, MonthEnum month, String siteName) {
        Report endReport = new Report();
        endReport.setReportId(new ReportId(month, siteName));

        for (Report report : result){
            endReport.setConversions(endReport.getConversions()+report.getConversions());
            endReport.setClicks(endReport.getClicks()+report.getClicks());
            endReport.setImpressions(endReport.getImpressions()+report.getImpressions());
            endReport.setRequests(endReport.getRequests()+report.getRequests());
            endReport.setRevenue(endReport.getRevenue()+report.getRevenue());
        }

        double conversionRate = metricsCalculator.calculateCR(endReport.getConversions(),endReport.getImpressions());
        double clickThroughRate = metricsCalculator.calculateCTR(endReport.getClicks(), endReport.getImpressions());
        double effectiveCostPerThousand = metricsCalculator.calculateEffectiveCPM(endReport.getRevenue(), endReport.getImpressions());
        double fillRate = metricsCalculator.calculateFillRate(endReport.getImpressions(),endReport.getRequests());

        endReport.setConversionRate(conversionRate);
        endReport.setClickThroughRate(clickThroughRate);
        endReport.setEffectiveCostPerThousand(effectiveCostPerThousand);
        endReport.setFillRate(fillRate);

        return endReport;
    }

    private String[] prepareIgnorePaths(MonthEnum monthEnum, String siteName) {
        List<String> ignorePathsList = new ArrayList<String>();

        ignorePathsList.add("impressions");
        ignorePathsList.add("clicks");
        ignorePathsList.add("conversions");
        ignorePathsList.add("revenue");
        ignorePathsList.add("clickThroughRate");
        ignorePathsList.add("conversionRate");
        ignorePathsList.add("fillRate");
        ignorePathsList.add("effectiveCostPerThousand");
        ignorePathsList.add("requests");
        ignorePathsList.add("impressions");

        if(monthEnum == null){
            ignorePathsList.add("reportId.month");
        }

        if(siteName == null){
            ignorePathsList.add("reportId.site");
        }

        return ignorePathsList.toArray(new String[ignorePathsList.size()]);
    }
}
