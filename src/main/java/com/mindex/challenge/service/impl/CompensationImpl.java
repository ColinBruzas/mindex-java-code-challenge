package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompensationImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating new compensation [{}]", compensation);

        // set an employee on the compensation itself. This is used in the read db search.
        compensation.setEmployeeId(compensation.getEmployee().getEmployeeId());

        // ensure employee exists
        Employee employee = employeeService.read(compensation.getEmployeeId());
        if (employee == null) {
            throw new RuntimeException("Error during create compensation. No employee found: " + compensation.getEmployeeId());
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public List<Compensation> read(String id) {
        LOG.debug("Reading compensation with employee id [{}]", id);

        List<Compensation> compensation = compensationRepository.findByEmployeeIdOrderByEffectiveDateDesc(id);

        if (compensation == null) {
            throw new RuntimeException("No compensation found for employee id: " + id);
        }

        return compensation;
    }
}
