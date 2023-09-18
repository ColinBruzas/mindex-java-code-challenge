package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportingService reportingService;


    @GetMapping("/report/{id}")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received generate Reporting Structure for employee [{}]", id);

        return reportingService.getReportingStructure(id);
    }
}
