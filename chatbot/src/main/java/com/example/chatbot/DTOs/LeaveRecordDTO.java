package com.example.chatbot.DTOs;

import java.time.LocalDate;

public class LeaveRecordDTO {
    private Integer id;
    private String employeeId;
    private String employeeName;
    private LocalDate leaveDate;
    private String leaveType;
    private String status;

    // Constructors
    public LeaveRecordDTO() {}

    public LeaveRecordDTO(Integer id, String employeeId, String employeeName, LocalDate leaveDate, String leaveType, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.leaveDate = leaveDate;
        this.leaveType = leaveType;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
