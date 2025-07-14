package com.example.chatbot.service;

import com.example.chatbot.DTOs.EmployeeDTO;
import com.example.chatbot.models.Employee;
import com.example.chatbot.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(String employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByManager(String managerId) {
        return employeeRepository.findEmployeesByManagerId(managerId);
    }

    public void deleteEmployee(String employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Helper method to convert Employee to EmployeeDTO
    public EmployeeDTO convertToDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getEmail(),
                employee.getManagerId()
        );
    }

    // Helper method to convert list of Employees to EmployeeDTOs
    public List<EmployeeDTO> convertToDTOList(List<Employee> employees) {
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get employees by manager with safe handling
    public List<EmployeeDTO> getEmployeesByManagerDTO(String managerId) {
        
        List<Employee> employees = employeeRepository.findEmployeesByManagerId(managerId);
        return convertToDTOList(employees);
    }
}
