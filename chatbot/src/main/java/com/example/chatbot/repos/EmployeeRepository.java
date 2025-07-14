package com.example.chatbot.repos;

import com.example.chatbot.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    @Query("SELECT e FROM Employee e WHERE e.employeeId = :managerId")
    Employee findByEmployeeId(@Param("managerId") String managerId);
    
    // Get all employees under a specific manager
    @Query("SELECT e FROM Employee e WHERE e.managerId = :managerId")
    List<Employee> findEmployeesByManagerId(@Param("managerId") String managerId);
}
