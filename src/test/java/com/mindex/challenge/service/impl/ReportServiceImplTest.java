package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportUrl = "http://localhost:" + port + "/report/{id}";
    }

    @Test
    public void testNumberOfReports() {
        // create initial employee
        Employee noReportEmployee = createAndPostEmployee("John", "Lennon", "Engineering", "Developer", null);
        ReportingStructure report = restTemplate.getForEntity(reportUrl, ReportingStructure.class, noReportEmployee.getEmployeeId()).getBody();

        // Test no reports
        assertEquals(noReportEmployee.getEmployeeId(), report.getEmployeeId());
        assertEquals(0, report.getNumberOfReports());

        Employee oneReportEmployee = createAndPostEmployee("John", "Lennon", "Engineering", "Developer", listOfEmployee(noReportEmployee));
        ReportingStructure oneReport = restTemplate.getForEntity(reportUrl, ReportingStructure.class, oneReportEmployee.getEmployeeId()).getBody();

        // Test single report
        assertEquals(1, oneReport.getNumberOfReports());

        // Test second level of reports
        Employee twoReportEmployee = createAndPostEmployee("John", "Lennon", "Engineering", "Developer", listOfEmployee(oneReportEmployee));
        ReportingStructure twoReport = restTemplate.getForEntity(reportUrl, ReportingStructure.class, twoReportEmployee.getEmployeeId()).getBody();
        assertEquals(2, twoReport.getNumberOfReports());

        // Create infinite Loop. Test that that direct report number stays at two
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        noReportEmployee.setDirectReports(listOfEmployee(twoReportEmployee));
        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(noReportEmployee, headers),
                        Employee.class,
                        noReportEmployee.getEmployeeId()).getBody();
        ReportingStructure updatedCycleReport = restTemplate
                .getForEntity(reportUrl, ReportingStructure.class, updatedEmployee.getEmployeeId()).getBody();

        assertEquals(2, updatedCycleReport.getNumberOfReports());

        ReportingStructure updatedTwoReport = restTemplate
                .getForEntity(reportUrl, ReportingStructure.class, twoReportEmployee.getEmployeeId()).getBody();

        assertEquals(2, updatedTwoReport.getNumberOfReports());



    }

    private List<Employee> listOfEmployee(Employee employee){
        List<Employee> list = new ArrayList<>();
        list.add(employee);
        return list;
    }


    private Employee createAndPostEmployee(
            String firstName,
            String lastName,
            String dept,
            String position,
            List<Employee> directReports
    ){
        Employee employee = createEmployee("John", "Lennon", "Engineering", "Developer", directReports);
        return restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();
    }

    private Employee createEmployee(
            String firstName,
            String lastName,
            String dept,
            String position,
            List<Employee> directReports){
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(dept);
        employee.setPosition(position);
        employee.setDirectReports(directReports);
        return employee;
    }
}