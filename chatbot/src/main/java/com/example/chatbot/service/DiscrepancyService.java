package com.example.chatbot.service;

import com.example.chatbot.DTOs.DiscrepancyRequest;
import com.example.chatbot.DTOs.DiscrepancyResult;
import com.example.chatbot.models.Employee;
import com.example.chatbot.models.ItasAttendance;
import com.example.chatbot.models.EleaveRecord;
import com.example.chatbot.repos.EmployeeRepository;
import com.example.chatbot.repos.ItasAttendanceRepository;
import com.example.chatbot.repos.EleaveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscrepancyService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ItasAttendanceRepository attendanceRepository;
    
    @Autowired
    private EleaveRecordRepository leaveRepository;

    public List<DiscrepancyResult> getAttendanceDiscrepancies(DiscrepancyRequest request) {
        // Calculate date range based on timeframe
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(request.getTimeFrame(), request.getCustomDays(), endDate);
        
        // Get employees under this manager (assuming department-based management)
        List<Employee> managedEmployees = getEmployeesUnderManager(request.getManagerId());
        
        List<DiscrepancyResult> allDiscrepancies = new ArrayList<>();
        
        for (Employee employee : managedEmployees) {
            // Get discrepancy type 1: Present and on approved leave
            allDiscrepancies.addAll(getPresentAndOnLeaveDiscrepancies(employee, startDate, endDate));
            
            // Get discrepancy type 2: Absent without approved leave
            allDiscrepancies.addAll(getAbsentWithoutLeaveDiscrepancies(employee, startDate, endDate));
        }
        
        return allDiscrepancies;
    }
    
    private LocalDate calculateStartDate(String timeFrame, Integer customDays, LocalDate endDate) {
        if (customDays != null && customDays > 0) {
            return endDate.minusDays(customDays);
        }
        
        switch (timeFrame.toUpperCase()) {
            case "1_YEAR":
                return endDate.minusYears(1);
            case "6_MONTHS":
                return endDate.minusMonths(6);
            case "1_MONTH":
                return endDate.minusMonths(1);
            case "1_WEEK":
                return endDate.minusWeeks(1);
            case "2_WEEKS":
                return endDate.minusWeeks(2);
            case "3_WEEKS":
                return endDate.minusWeeks(3);
            default:
                return endDate.minusMonths(1); // Default to 1 month
        }
    }
    
    private List<Employee> getEmployeesUnderManager(String managerId) {
        // Get all direct subordinates of the manager
        return employeeRepository.findEmployeesByManagerId(managerId);
    }
    
    private List<DiscrepancyResult> getPresentAndOnLeaveDiscrepancies(Employee employee, LocalDate startDate, LocalDate endDate) {
        List<DiscrepancyResult> discrepancies = new ArrayList<>();
        
        // Get attendance records for the employee in the date range
        List<ItasAttendance> attendanceRecords = attendanceRepository.findByEmployeeIdAndDateBetween(
            employee.getEmployeeId(), startDate, endDate);
        
        // Get approved leave records for the employee in the date range
        List<EleaveRecord> approvedLeaves = leaveRepository.findByEmployeeIdAndLeaveDateBetween(
            employee.getEmployeeId(), startDate, endDate)
            .stream()
            .filter(leave -> "Approved".equals(leave.getStatus()))
            .collect(Collectors.toList());
        
        // Find overlaps: present (hours_clocked > 0) and on approved leave on same date
        for (ItasAttendance attendance : attendanceRecords) {
            if (attendance.getHoursClocked() != null && attendance.getHoursClocked() > 0) {
                for (EleaveRecord leave : approvedLeaves) {
                    if (attendance.getDate().equals(leave.getLeaveDate())) {
                        discrepancies.add(new DiscrepancyResult(
                            employee.getEmployeeId(),
                            employee.getName(),
                            attendance.getDate(),
                            "Present and on leave",
                            employee.getDepartment()
                        ));
                        break; // Avoid duplicate entries for same date
                    }
                }
            }
        }
        
        return discrepancies;
    }
    
    private List<DiscrepancyResult> getAbsentWithoutLeaveDiscrepancies(Employee employee, LocalDate startDate, LocalDate endDate) {
        List<DiscrepancyResult> discrepancies = new ArrayList<>();
        
        // Get all dates where there's either attendance or leave record for this employee
        Set<LocalDate> allRelevantDates = new HashSet<>();
        
        // Add attendance dates
        List<ItasAttendance> attendanceRecords = attendanceRepository.findByEmployeeIdAndDateBetween(
            employee.getEmployeeId(), startDate, endDate);
        allRelevantDates.addAll(attendanceRecords.stream()
            .map(ItasAttendance::getDate)
            .collect(Collectors.toSet()));
        
        // Add leave dates
        List<EleaveRecord> leaveRecords = leaveRepository.findByEmployeeIdAndLeaveDateBetween(
            employee.getEmployeeId(), startDate, endDate);
        allRelevantDates.addAll(leaveRecords.stream()
            .map(EleaveRecord::getLeaveDate)
            .collect(Collectors.toSet()));
        
        // Create maps for quick lookup
        Map<LocalDate, ItasAttendance> attendanceMap = attendanceRecords.stream()
            .collect(Collectors.toMap(ItasAttendance::getDate, a -> a, (a1, a2) -> a1));
        
        Map<LocalDate, EleaveRecord> approvedLeaveMap = leaveRecords.stream()
            .filter(leave -> "Approved".equals(leave.getStatus()))
            .collect(Collectors.toMap(EleaveRecord::getLeaveDate, l -> l, (l1, l2) -> l1));
        
        // Check each relevant date for discrepancies
        for (LocalDate date : allRelevantDates) {
            ItasAttendance attendance = attendanceMap.get(date);
            EleaveRecord approvedLeave = approvedLeaveMap.get(date);
            
            boolean isPresent = attendance != null && attendance.getHoursClocked() != null && attendance.getHoursClocked() > 0;
            boolean hasApprovedLeave = approvedLeave != null;
            
            // If neither present nor has approved leave = discrepancy
            if (!isPresent && !hasApprovedLeave) {
                discrepancies.add(new DiscrepancyResult(
                    employee.getEmployeeId(),
                    employee.getName(),
                    date,
                    "Absent without approved leave",
                    employee.getDepartment()
                ));
            }
        }
        
        return discrepancies;
    }
}
