package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
        // Finds all salaries for a given employee and orders them by effective date. Most recent first.
        List<Compensation> findByEmployeeIdOrderByEffectiveDateDesc(String employeeId);
}
