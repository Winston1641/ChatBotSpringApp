package com.example.chatbot.controller;

import com.example.chatbot.DTOs.DiscrepancyRequest;
import com.example.chatbot.DTOs.DiscrepancyResult;
import com.example.chatbot.DTOs.EmployeeDTO;
import com.example.chatbot.service.DiscrepancyService;
import com.example.chatbot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discrepancies")
@CrossOrigin(origins = "*")
public class DiscrepancyController {

    @Autowired
    private DiscrepancyService discrepancyService;

    @Autowired
    private EmployeeService employeeService;


    /**
     * Get attendance discrepancies using GET method with parameters
     * 
     * @param managerId The manager's employee ID
     * @param timeFrame Time frame for analysis (1_YEAR, 6_MONTHS, 1_MONTH, 1_WEEK, 2_WEEKS, 3_WEEKS)
     * @param customDays Optional custom number of days instead of time frame
     * @return List of discrepancies found
     */
    @GetMapping("/attendance")
    public ResponseEntity<List<DiscrepancyResult>> getAttendanceDiscrepancies(
            @RequestParam String managerId,
            @RequestParam(defaultValue = "1_MONTH") String timeFrame,
            @RequestParam(required = false) Integer customDays) {
        try {
            DiscrepancyRequest request = new DiscrepancyRequest(managerId, timeFrame, customDays);
            List<DiscrepancyResult> discrepancies = discrepancyService.getAttendanceDiscrepancies(request);
            return ResponseEntity.ok(discrepancies);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get summary of discrepancies (count by type)
     * 
     * @param managerId The manager's employee ID
     * @param timeFrame Time frame for analysis
     * @param customDays Optional custom number of days instead of timeFrame
     * @return Summary statistics
     */
    @GetMapping("/attendance/summary")
    public ResponseEntity<DiscrepancySummary> getDiscrepancySummary(
            @RequestParam String managerId,
            @RequestParam(defaultValue = "1_MONTH") String timeFrame,
            @RequestParam(required = false) Integer customDays) {
        try {
            DiscrepancyRequest request = new DiscrepancyRequest(managerId, timeFrame, customDays);
            List<DiscrepancyResult> discrepancies = discrepancyService.getAttendanceDiscrepancies(request);
            
            long presentAndOnLeaveCount = discrepancies.stream()
                .filter(d -> "Present and on leave".equals(d.getIssue()))
                .count();
            
            long absentWithoutLeaveCount = discrepancies.stream()
                .filter(d -> "Absent without approved leave".equals(d.getIssue()))
                .count();
            
            DiscrepancySummary summary = new DiscrepancySummary(
                (int) presentAndOnLeaveCount,
                (int) absentWithoutLeaveCount,
                discrepancies.size()
            );
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all employees under a specific manager
     */
    @GetMapping("/manager/{managerId}/employees")
    public ResponseEntity<?> getEmployeesByManager(@PathVariable String managerId) {
        try {
            List<EmployeeDTO> employees = employeeService.getEmployeesByManagerDTO(managerId);
            return ResponseEntity.ok(employees);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Manager not found",
                "message", e.getMessage(),
                "managerId", managerId
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Internal server error",
                "message", "An unexpected error occurred"
            ));
        }
    }

    /**
     * Test endpoint to check all employees
     */
    @GetMapping("/test/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees().stream()
                .map(employeeService::convertToDTO)
                .toList();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Inner class for summary response
    public static class DiscrepancySummary {
        private int presentAndOnLeaveCount;
        private int absentWithoutLeaveCount;
        private int totalDiscrepancies;

        public DiscrepancySummary(int presentAndOnLeaveCount, int absentWithoutLeaveCount, int totalDiscrepancies) {
            this.presentAndOnLeaveCount = presentAndOnLeaveCount;
            this.absentWithoutLeaveCount = absentWithoutLeaveCount;
            this.totalDiscrepancies = totalDiscrepancies;
        }

        // Getters and setters
        public int getPresentAndOnLeaveCount() { return presentAndOnLeaveCount; }
        public void setPresentAndOnLeaveCount(int presentAndOnLeaveCount) { this.presentAndOnLeaveCount = presentAndOnLeaveCount; }
        
        public int getAbsentWithoutLeaveCount() { return absentWithoutLeaveCount; }
        public void setAbsentWithoutLeaveCount(int absentWithoutLeaveCount) { this.absentWithoutLeaveCount = absentWithoutLeaveCount; }
        
        public int getTotalDiscrepancies() { return totalDiscrepancies; }
        public void setTotalDiscrepancies(int totalDiscrepancies) { this.totalDiscrepancies = totalDiscrepancies; }
    }
}
