package com.example.chatbot.DTOs;

import java.time.LocalDate;

public class DiscrepancyResult {
    private String employeeId;
    private String employeeName;
    private LocalDate discrepancyDate;
    private String issue;
    private String department;
    
    // Constructors
    public DiscrepancyResult() {}
    
    public DiscrepancyResult(String employeeId, String employeeName, LocalDate discrepancyDate, String issue, String department) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.discrepancyDate = discrepancyDate;
        this.issue = issue;
        this.department = department;
    }
    
    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public LocalDate getDiscrepancyDate() {
        return discrepancyDate;
    }
    
    public void setDiscrepancyDate(LocalDate discrepancyDate) {
        this.discrepancyDate = discrepancyDate;
    }
    
    public String getIssue() {
        return issue;
    }
    
    public void setIssue(String issue) {
        this.issue = issue;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Override
    public String toString() {
        return "DiscrepancyResult{" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", discrepancyDate=" + discrepancyDate +
                ", issue='" + issue + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
