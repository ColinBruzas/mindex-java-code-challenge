package com.mindex.challenge;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataBootstrapTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Test
    public void testEmployee() {
        Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals("Development Manager", employee.getPosition());
        assertEquals("Engineering", employee.getDepartment());
    }

    @Test
    public void testCompensation() {
        List<Compensation> compensations = compensationRepository
                .findByEmployeeIdOrderByEffectiveDateDesc("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(compensations);
        assertEquals(LocalDate.parse("2023-09-18") ,compensations.get(0).getEffectiveDate());
        BigDecimal salary = new BigDecimal("100000.00");
        assertEquals(salary, compensations.get(0).getSalary());
        assertEquals(LocalDate.parse("2022-08-20") ,compensations.get(1).getEffectiveDate());
        BigDecimal oldSalary = new BigDecimal("90000.00");
        assertEquals(oldSalary, compensations.get(1).getSalary());
    }
}