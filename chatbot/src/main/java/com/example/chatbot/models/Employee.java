package com.example.chatbot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @Column(name = "employee_id", length = 10)
    private String employeeId;
    
    @Column(name = "name", length = 100)
    private String name;
    
    @Column(name = "department", length = 50)
    private String department;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "manager_id", length = 10)
    private String managerId;
    
    // Constructors
    public Employee() {}
    
    public Employee(String employeeId, String name, String department, String email) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.email = email;
    }
    
    public Employee(String employeeId, String name, String department, String email, String managerId) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.email = email;
        this.managerId = managerId;
    }
    
    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                ", managerId='" + managerId + '\'' +
                '}';
    }
}
