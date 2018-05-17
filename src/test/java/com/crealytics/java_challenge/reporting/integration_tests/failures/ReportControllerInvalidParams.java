package com.crealytics.java_challenge.reporting.integration_tests.failures;

import com.crealytics.java_challenge.reporting.data_model.Report;
import com.crealytics.java_challenge.reporting.integration_tests.ControllerITBase;
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
public class ReportControllerInvalidParams extends ControllerITBase {

    @After
    public void truncateTables(){
        inMemoryReportStore.deleteAll();
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
    public void testNonExistingReport(){

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Report> response = restTemplate.exchange(
                createURLWithPort("/reports", "12","desktop_web"),
                HttpMethod.GET, entity, Report.class);

        assert(response.getStatusCode() == HttpStatus.NOT_FOUND);

    }

}
