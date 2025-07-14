package com.example.chatbot.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "eleave_records")
public class EleaveRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @Column(name = "leave_date")
    private LocalDate leaveDate;
    
    @Column(name = "leave_type", length = 50)
    private String leaveType;
    
    @Column(name = "status", length = 20)
    private String status;
    
    // Constructors
    public EleaveRecord() {}
    
    public EleaveRecord(Employee employee, LocalDate leaveDate, String leaveType, String status) {
        this.employee = employee;
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
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
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
    
    @Override
    public String toString() {
        return "EleaveRecord{" +
                "id=" + id +
                ", employee=" + (employee != null ? employee.getEmployeeId() : null) +
                ", leaveDate=" + leaveDate +
                ", leaveType='" + leaveType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
