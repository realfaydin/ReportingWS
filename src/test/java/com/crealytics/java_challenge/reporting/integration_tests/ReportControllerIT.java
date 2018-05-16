package com.crealytics.java_challenge.reporting.integration_tests;

import com.crealytics.java_challenge.reporting.data_model.Report;
import com.crealytics.java_challenge.reporting.store.InMemoryReportStore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.crealytics.java_challenge.reporting.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //this adds a bit of overhead, by forcing a spring context reload after each time, but it is necessary to isolate each test
@AutoConfigureRestDocs(outputDir = "out/snippets")
public class ReportControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    InMemoryReportStore inMemoryReportStore;

    @After
    public void truncateTables(){
        inMemoryReportStore.deleteAll();
    }

    @Test
    public void testBadRequestNoParameters(){

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports",null, null),
                HttpMethod.GET, entity, Report.class);

        assert(response.getStatusCode() == HttpStatus.BAD_REQUEST);

//        String expected = "{id:Course1}";
//        try {
//            JSONAssert.assertEquals(expected, response.getBody(), false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testBadRequestUnacceptedStringMonthValue(){

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports", "banuary","desktop"),
                HttpMethod.GET, entity, Report.class);

        assert(response.getStatusCode() == HttpStatus.BAD_REQUEST);

    }

    @Test
    public void testBadRequestUnacceptedIntegerMonthValue(){

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports", "13","desktop"),
                HttpMethod.GET, entity, Report.class);

        assert(response.getStatusCode() == HttpStatus.BAD_REQUEST);

    }

    @Test
    public void testAcceptedMonthValue(){

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports", "1","desktop_web"),
                HttpMethod.GET, entity, Report.class);

        System.out.println("Response code: "+response.getStatusCode());
        assert(response.getStatusCode() == HttpStatus.OK);

    }

    @Test
    public void testNonExistingReport(){

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports", "12","desktop_web"),
                HttpMethod.GET, entity, Report.class);

        assert(response.getStatusCode() == HttpStatus.NOT_FOUND);

    }

    private String createURLWithPort(String uri, String month, String site) {
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
}
