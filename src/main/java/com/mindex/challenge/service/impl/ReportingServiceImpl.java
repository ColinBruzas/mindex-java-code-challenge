package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class ReportingServiceImpl implements ReportingService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure getReportingStructure(String id){
        LOG.debug("Generating Reporting Structure for employee [{}]", id);
        return generateReportingStructure(id);
    }

    // Need to generate numberOfReports during each call. Do not persist.
    private ReportingStructure generateReportingStructure(String id){
        ReportingStructure report = new ReportingStructure();
        report.setEmployeeId(id);

        Employee employee = employeeService.read(id);
        report.setNumberOfReports(numberOfReports(employee));

        return report;
    }



    // Depth First Search. Need to pass Hashmap to ensure there are no cycles, redundancies, or infinite loop.
    // (Should catch during create / update as well. But not in scope of this challenge.)
    // Start of recursive call
    public int numberOfReports(Employee employee){
        int numberOfReports = 0;
        HashSet<String> visitedSet = new HashSet<>();
        return countDirectReports(employee, visitedSet, numberOfReports);
    }

    private int numberOfReports(Employee employee, HashSet<String> visitedSet){
        int numberOfReports = 1;
        return countDirectReports(employee, visitedSet, numberOfReports);
    }

    // Handle subsequent calls and base case.
    private int countDirectReports(Employee employee, HashSet<String> visitedSet, int numberOfReports ){

        visitedSet.add(employee.getEmployeeId());
        if(employee.getDirectReports() == null) return numberOfReports;

        for(Employee directReportEmployee: employee.getDirectReports()){
            // Check visited set. Don't add repeat Ids
            boolean idPreviouslyCounted = visitedSet.contains(directReportEmployee.getEmployeeId());
            if(!idPreviouslyCounted){
                Employee employeeReport = employeeService.read(directReportEmployee.getEmployeeId());
                numberOfReports = numberOfReports + numberOfReports(employeeReport, visitedSet);
            }
        }
        return numberOfReports;
    }
}
