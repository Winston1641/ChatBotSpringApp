package com.example.chatbot.service;

import com.example.chatbot.DTOs.LeaveRecordDTO;
import com.example.chatbot.models.EleaveRecord;
import com.example.chatbot.repos.EleaveRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private EleaveRecordRepository eleaveRecordRepository;

    /**
     * Get leave history for a specific employee
     */
    public List<LeaveRecordDTO> getLeaveHistory(String employeeId, String timeFrame, Integer customDays) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(timeFrame, customDays, endDate);
        
        List<EleaveRecord> leaveRecords = eleaveRecordRepository.findByEmployeeIdAndLeaveDateBetween(employeeId, startDate, endDate);
        return convertToDTO(leaveRecords);
    }

    /**
     * Get all leave records within a date range
     */
    public List<LeaveRecordDTO> getAllLeaves(String timeFrame, Integer customDays) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(timeFrame, customDays, endDate);
        
        List<EleaveRecord> leaveRecords = eleaveRecordRepository.findByLeaveDateBetween(startDate, endDate);
        return convertToDTO(leaveRecords);
    }

    /**
     * Get approved leave records within a date range
     */
    public List<LeaveRecordDTO> getApprovedLeaves(String timeFrame, Integer customDays) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(timeFrame, customDays, endDate);
        
        List<EleaveRecord> leaveRecords = eleaveRecordRepository.findApprovedLeavesByDateBetween(startDate, endDate);
        return convertToDTO(leaveRecords);
    }

    /**
     * Convert EleaveRecord entities to DTOs to avoid Hibernate proxy serialization issues
     */
    private List<LeaveRecordDTO> convertToDTO(List<EleaveRecord> leaveRecords) {
        return leaveRecords.stream().map(record -> {
            String employeeId = record.getEmployee() != null ? record.getEmployee().getEmployeeId() : null;
            String employeeName = record.getEmployee() != null ? record.getEmployee().getName() : null;
            
            return new LeaveRecordDTO(
                record.getId(),
                employeeId,
                employeeName,
                record.getLeaveDate(),
                record.getLeaveType(),
                record.getStatus()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Calculate start date based on time frame or custom days
     */
    private LocalDate calculateStartDate(String timeFrame, Integer customDays, LocalDate endDate) {
        if (customDays != null && customDays > 0) {
            return endDate.minusDays(customDays);
        }
        
        return switch (timeFrame.toUpperCase()) {
            case "1_YEAR" -> endDate.minusYears(1);
            case "6_MONTHS" -> endDate.minusMonths(6);
            case "1_MONTH" -> endDate.minusMonths(1);
            case "2_WEEKS" -> endDate.minusWeeks(2);
            case "3_WEEKS" -> endDate.minusWeeks(3);
            case "1_WEEK" -> endDate.minusWeeks(1);
            default -> endDate.minusMonths(1); // Default to 1 month
        };
    }
}
