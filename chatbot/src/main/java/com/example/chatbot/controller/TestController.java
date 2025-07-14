package com.example.chatbot.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/discrepancy-endpoints")
    public ResponseEntity<Map<String, Object>> getDiscrepancyEndpoints() {
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/discrepancies/attendance", "Get discrepancies with query parameters");
        endpoints.put("GET /api/discrepancies/attendance/summary", "Get discrepancy summary");
        endpoints.put("GET /api/discrepancies/manager/{managerId}/employees", "Get employees under manager");
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("managerId", "Manager's employee ID (required)");
        parameters.put("timeFrame", "1_YEAR, 6_MONTHS, 1_MONTH, 1_WEEK, 2_WEEKS, 3_WEEKS (default: 1_MONTH)");
        parameters.put("customDays", "Custom number of days (optional)");
        
        Map<String, Object> response = new HashMap<>();
        response.put("endpoints", endpoints);
        response.put("parameters", parameters);
        response.put("example_urls", Map.of(
            "basic", "/api/discrepancies/attendance?managerId=MGR001&timeFrame=1_MONTH",
            "custom_days", "/api/discrepancies/attendance?managerId=MGR001&customDays=15",
            "summary", "/api/discrepancies/attendance/summary?managerId=MGR001&timeFrame=6_MONTHS",
            "manager_employees", "/api/discrepancies/manager/MGR001/employees"
        ));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leave-endpoints")
    public ResponseEntity<Map<String, Object>> getLeaveEndpoints() {
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/leave/history/{employeeId}", "Get leave history for specific employee");
        endpoints.put("GET /api/leave/all", "Get all leave records within date range");
        endpoints.put("GET /api/leave/approved", "Get approved leave records within date range");
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("employeeId", "Employee's ID (required for history endpoint)");
        parameters.put("timeFrame", "1_YEAR, 6_MONTHS, 1_MONTH, 1_WEEK, 2_WEEKS, 3_WEEKS (default varies by endpoint)");
        parameters.put("customDays", "Custom number of days (optional)");
        
        Map<String, Object> response = new HashMap<>();
        response.put("endpoints", endpoints);
        response.put("parameters", parameters);
        response.put("example_urls", Map.of(
            "employee_history", "/api/leave/history/EMP0043?timeFrame=6_MONTHS",
            "employee_history_custom", "/api/leave/history/EMP0043?customDays=30",
            "all_leaves", "/api/leave/all?timeFrame=1_MONTH",
            "approved_leaves", "/api/leave/approved?timeFrame=1_WEEK"
        ));
        
        return ResponseEntity.ok(response);
    }
}
