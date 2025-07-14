package com.example.chatbot.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "itas_attendance")
public class ItasAttendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "hours_clocked")
    private Integer hoursClocked;
    
    // Constructors
    public ItasAttendance() {}
    
    public ItasAttendance(Employee employee, LocalDate date, Integer hoursClocked) {
        this.employee = employee;
        this.date = date;
        this.hoursClocked = hoursClocked;
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
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getHoursClocked() {
        return hoursClocked;
    }
    
    public void setHoursClocked(Integer hoursClocked) {
        this.hoursClocked = hoursClocked;
    }
    
    @Override
    public String toString() {
        return "ItasAttendance{" +
                "id=" + id +
                ", employee=" + (employee != null ? employee.getEmployeeId() : null) +
                ", date=" + date +
                ", hoursClocked=" + hoursClocked +
                '}';
    }
}
