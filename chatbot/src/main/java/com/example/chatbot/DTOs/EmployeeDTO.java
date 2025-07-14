package com.example.chatbot.DTOs;

public class EmployeeDTO {
    private String employeeId;
    private String name;
    private String department;
    private String email;
    private String managerId;
    
    // Constructors
    public EmployeeDTO() {}
    
    public EmployeeDTO(String employeeId, String name, String department, String email, String managerId) {
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
        return "EmployeeDTO{" +
                "employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                ", managerId='" + managerId + '\'' +
                '}';
    }
}
