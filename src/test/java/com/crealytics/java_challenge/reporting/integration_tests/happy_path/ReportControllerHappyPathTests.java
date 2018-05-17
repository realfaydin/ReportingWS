package com.crealytics.java_challenge.reporting.integration_tests.happy_path;

import com.crealytics.java_challenge.reporting.data_model.Report;
import com.crealytics.java_challenge.reporting.integration_tests.ControllerITBase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ReportControllerHappyPathTests extends ControllerITBase {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private String month;
    private String site;

    public ReportControllerHappyPathTests(String month, String site) {
        this.month = month;
        this.site = site;
    }

    @Parameterized.Parameters
    public static Collection input() {
        return Arrays.asList(new Object[][]{
                {"1", "desktop_web"},
                {"2", "desktop_web"},
                {"1", "mobile_web"},
                {"2", "mobile_web"},
                {"1", "android"},
                {"2", "android"},
                {"1", "iOS"},
                {"2", "iOS"},

                {"JAN", "desktop_web"},
                {"FEB", "desktop_web"},
                {"JAN", "mobile_web"},
                {"FEB", "mobile_web"},
                {"JAN", "android"},
                {"FEB", "android"},
                {"JAN", "iOS"},
                {"FEB", "iOS"},

                {null, "desktop_web"},
                {null, "mobile_web"},
                {null, "android"},
                {null, "iOS"},

                {"JAN", null},
                {"FEB", null},

                {null, null},

    });
    }

    @Test
    public void verifyGeneratedReports() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports", month, site),
                HttpMethod.GET, entity, Report.class);

        double clicks = response.getBody().getClicks();
        double requests = response.getBody().getRequests();
        double impressions = response.getBody().getImpressions();
        double conversions = response.getBody().getConversions();
        double revenue = response.getBody().getRevenue();

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(calculateClickThroughRate(clicks, impressions), response.getBody().getClickThroughRate(), 0.0d);
        Assert.assertEquals(calculateConversionRate(impressions, conversions), response.getBody().getConversionRate(), 0.0d);
        Assert.assertEquals(calculateEffectiveCostPerThousand(revenue, impressions), response.getBody().getEffectiveCostPerThousand(), 0.0d);
        Assert.assertEquals(calculatedFillRate(impressions, requests), response.getBody().getFillRate(), 0.0d);
    }
}
