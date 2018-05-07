package crealytics.java_challenge.reporting_ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @RequestMapping("/reports")
    public Report generateReport(@RequestParam(value="month") String month, @RequestParam(value="site") String site){

        if(MonthUtil.getMonth(month) == null){
            throw new BadRequestException();
        }else{
            return new Report(month,site);
        }
    }
}
