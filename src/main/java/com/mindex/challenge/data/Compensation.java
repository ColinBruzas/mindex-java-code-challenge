package com.mindex.challenge.data;
import java.math.BigDecimal;
import java.time.LocalDate;


// Compensation represents a salary for a given employee. There is a one to many relationship from employee to compensation.
// Compensations are differentiated by effectiveDate, in order to use the most recent one without deleting compensation history.
public class Compensation {
    String employeeId;
    Employee employee;
    BigDecimal salary;
    LocalDate effectiveDate;

    public Compensation(){}

    // Employee id will be set during creation. This simplifies the db call
    public void setEmployeeId(String compensationId) {
        this.employeeId = compensationId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }
}
