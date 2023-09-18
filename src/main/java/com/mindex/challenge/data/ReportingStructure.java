package com.mindex.challenge.data;

// Reporting Structure represents an employee and their total number of reports.
// Number of reports is processed during each call.
public class ReportingStructure {
    String employeeId;
    int numberOfReports;

    public ReportingStructure(){}

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }
}
