package com.crealytics.java_challenge.reporting.store;

import com.crealytics.java_challenge.reporting.data_model.Report;
import com.crealytics.java_challenge.reporting.data_model.ReportId;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;

public interface InMemoryReportStore extends CrudRepository<Report, ReportId> {

}
