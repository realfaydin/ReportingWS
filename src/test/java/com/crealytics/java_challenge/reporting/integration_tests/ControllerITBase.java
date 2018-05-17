package com.crealytics.java_challenge.reporting.integration_tests;

import com.crealytics.java_challenge.reporting.store.InMemoryReportStore;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RunWith(Parameterized.class)
@SpringBootTest(classes = com.crealytics.java_challenge.reporting.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
//this adds a bit of overhead, by forcing a spring context reload after each time, but it is necessary to isolate each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureRestDocs(outputDir = "out/snippets")
public class ControllerITBase {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    protected TestRestTemplate restTemplate = new TestRestTemplate();

    protected HttpHeaders headers = new HttpHeaders();

    @Autowired
    protected InMemoryReportStore inMemoryReportStore;

    @LocalServerPort
    private int port;

    @After
    public void truncateTables() {
        inMemoryReportStore.deleteAll();
    }

    protected String createURLWithPort(String uri, String month, String site) {
        StringBuilder sb = new StringBuilder();

        sb.append("http://localhost:");
        sb.append(port);
        sb.append(uri);
        sb.append("?");
        if(month !=null){
            sb.append("month=");
            sb.append(month);
        }
        if(site != null){
            sb.append("&");
            sb.append("site=");
            sb.append(site);
        }

        return sb.toString();
    }

    protected double calculateConversionRate(double impressions, double conversions){
        return truncateDoubleValue(conversions/impressions*100);
    }

    protected double calculatedFillRate(double impressions, double requests){
        return truncateDoubleValue(impressions/requests*100);
    }

    protected double calculateEffectiveCostPerThousand(double revenue, double impressions){
        return truncateDoubleValue(revenue*1000/impressions);
    }

    protected double calculateClickThroughRate(double clicks, double impressions){
        return truncateDoubleValue(clicks/impressions*100);
    }

    private double truncateDoubleValue(double valueToBeTruncated){
        return BigDecimal.valueOf(valueToBeTruncated)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
