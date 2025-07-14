package com.example.chatbot.controller;

import com.example.chatbot.DTOs.LeaveRecordDTO;
import com.example.chatbot.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    /**
     * Get leave history for a specific employee
     * 
     * @param employeeId The employee's ID
     * @param timeFrame Optional time frame (1_YEAR, 6_MONTHS, 1_MONTH, 1_WEEK) - defaults to 1_YEAR
     * @param customDays Optional custom number of days instead of time frame
     * @return List of leave records for the employee
     */
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<List<LeaveRecordDTO>> getLeaveHistory(
            @PathVariable String employeeId,
            @RequestParam(defaultValue = "1_YEAR") String timeFrame,
            @RequestParam(required = false) Integer customDays) {
        try {
            List<LeaveRecordDTO> leaveHistory = leaveService.getLeaveHistory(employeeId, timeFrame, customDays);
            return ResponseEntity.ok(leaveHistory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all leave records within a date range
     * 
     * @param timeFrame Time frame for analysis (1_YEAR, 6_MONTHS, 1_MONTH, 1_WEEK)
     * @param customDays Optional custom number of days instead of time frame
     * @return List of all leave records in the specified period
     */
    @GetMapping("/all")
    public ResponseEntity<List<LeaveRecordDTO>> getAllLeaves(
            @RequestParam(defaultValue = "1_MONTH") String timeFrame,
            @RequestParam(required = false) Integer customDays) {
        try {
            List<LeaveRecordDTO> allLeaves = leaveService.getAllLeaves(timeFrame, customDays);
            return ResponseEntity.ok(allLeaves);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get approved leave records within a date range
     * 
     * @param timeFrame Time frame for analysis (1_YEAR, 6_MONTHS, 1_MONTH, 1_WEEK)
     * @param customDays Optional custom number of days instead of time frame
     * @return List of approved leave records in the specified period
     */
    @GetMapping("/approved")
    public ResponseEntity<List<LeaveRecordDTO>> getApprovedLeaves(
            @RequestParam(defaultValue = "1_MONTH") String timeFrame,
            @RequestParam(required = false) Integer customDays) {
        try {
            List<LeaveRecordDTO> approvedLeaves = leaveService.getApprovedLeaves(timeFrame, customDays);
            return ResponseEntity.ok(approvedLeaves);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
