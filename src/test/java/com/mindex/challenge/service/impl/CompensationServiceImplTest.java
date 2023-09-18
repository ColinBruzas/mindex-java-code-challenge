package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;
    private String employeeUrl;


    @Autowired
    private CompensationService compensationService;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCompensationCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();


        Compensation originalCompensation =
                createCompensation(createdEmployee,new BigDecimal("100000"), LocalDate.parse("2022-08-18"));

        // Create compensation
        Compensation originalCreatedCompensation = restTemplate
                .postForEntity(compensationUrl, originalCompensation, Compensation.class).getBody();

        assertNotNull(originalCreatedCompensation.getEmployeeId());
        assertCompensationEquivalence(originalCreatedCompensation, originalCreatedCompensation);


        // Read compensation. Test with only one result
        Compensation[] responseCompensationList =
                restTemplate.getForObject(compensationIdUrl, Compensation[].class, originalCreatedCompensation.getEmployeeId());
        List<Compensation> originalCompensationList = Arrays.asList(responseCompensationList);

        assertEquals(originalCompensationList.size(), 1);
        assertEquals(originalCompensationList.get(0).getEmployeeId(), originalCreatedCompensation.getEmployeeId());
        assertCompensationEquivalence(originalCompensationList.get(0), originalCompensation);

        // Read with multiple salaries
        // create second salary
        Compensation updatedCompensation =
                createCompensation(createdEmployee,new BigDecimal("200000"), LocalDate.parse("2023-09-18"));
        Compensation updatedCreatedCompensation = restTemplate.postForEntity(compensationUrl, updatedCompensation, Compensation.class).getBody();

        //get the multiple salaries
        Compensation[] updatedResponseCompensationList = restTemplate
                .getForObject(compensationIdUrl, Compensation[].class, updatedCreatedCompensation.getEmployeeId());

        List<Compensation> updatedCompensationList = Arrays.asList(updatedResponseCompensationList);
        assertEquals(updatedCompensationList.get(0).getEmployeeId(), updatedCreatedCompensation.getEmployeeId());
        assertCompensationEquivalence(updatedCreatedCompensation, updatedCompensationList.get(0));
        assertCompensationEquivalence(originalCreatedCompensation, updatedCompensationList.get(1));
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());

        Employee expectedEmployee = expected.getEmployee();
        Employee actualEmployee = actual.getEmployee();
        assertEmployeeEquivalence(expectedEmployee, actualEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static Compensation createCompensation(Employee employee, BigDecimal salary, LocalDate effectiveDate){
        Compensation compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(salary);
        compensation.setEffectiveDate(effectiveDate);
        return compensation;
    }
}
