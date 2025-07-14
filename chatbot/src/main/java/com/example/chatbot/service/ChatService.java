package com.example.chatbot.service;

import com.example.chatbot.DTOs.DiscrepancyRequest;
import com.example.chatbot.DTOs.DiscrepancyResult;
import com.example.chatbot.DTOs.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    @Autowired VertexClient vertexClient;
    @Autowired ReportService reportService;
    @Autowired DiscrepancyService discrepancyService;
    @Autowired EmployeeService employeeService;

    public String processMessage(String input) {
        String lowerInput = input.toLowerCase();
        
        // SCENARIO 1A: Predefined Buttons - Without Report
        if (lowerInput.contains("show discrepancies") || lowerInput.contains("attendance issues")) {
            return handleShowDiscrepancies(input);
        }
        
        if (lowerInput.contains("show employees") || lowerInput.contains("team members")) {
            return handleShowEmployees(input);
        }
        
        if (lowerInput.contains("discrepancy summary") || lowerInput.contains("summary")) {
            return handleDiscrepancySummary(input);
        }
        
        // SCENARIO 1B: Predefined Buttons - With Report
        if (lowerInput.contains("generate report") || lowerInput.contains("create report")) {
            return handleGenerateReport(input);
        }
        
        if (lowerInput.contains("chart") || lowerInput.contains("visualization")) {
            return handleGenerateChart(input);
        }
        
        // SCENARIO 2: Free Text - Let Gemini decide
        return handleFreeTextInput(input);
    }
    
    private String handleShowDiscrepancies(String input) {
        try {
            String managerId = extractManagerId(input, "MGR001");
            
            // Extract time frame from user input
            String timeFrame = extractTimeFrame(input);
            Integer customDays = extractCustomDays(input);
            
            DiscrepancyRequest request = new DiscrepancyRequest(managerId, timeFrame, customDays);
            List<DiscrepancyResult> discrepancies = discrepancyService.getAttendanceDiscrepancies(request);
            
            if (discrepancies.isEmpty()) {
                return "✅ No discrepancies found for the selected period.";
            }
            
            StringBuilder response = new StringBuilder("📋 **Attendance Discrepancies:**\n\n");
            
            for (DiscrepancyResult discrepancy : discrepancies) {
                response.append(String.format(
                    "👤 **%s** (%s)\n" +
                    "📅 Date: %s\n" +
                    "⚠️ Issue: %s\n\n",
                    discrepancy.getEmployeeName(),
                    discrepancy.getEmployeeId(),
                    discrepancy.getDiscrepancyDate(),
                    discrepancy.getIssue()
                ));
            }
            
            return response.toString();
        } catch (Exception e) {
            return "❌ Error retrieving discrepancies: " + e.getMessage();
        }
    }

    // Helper method to extract time frame from user input
    private String extractTimeFrame(String input) {
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.contains("week") || lowerInput.contains("7 days")) {
            return "1_WEEK";
        } else if (lowerInput.contains("2 weeks") || lowerInput.contains("14 days")) {
            return "2_WEEKS";
        } else if (lowerInput.contains("month") || lowerInput.contains("30 days")) {
            return "1_MONTH";
        } else if (lowerInput.contains("quarter") || lowerInput.contains("90 days")) {
            return "1_QUARTER";
        } else if (lowerInput.contains("year") || lowerInput.contains("365 days")) {
            return "1_YEAR";
        }
        
        return "1_MONTH"; // Default
    }

    // Helper method to extract custom days from user input
    private Integer extractCustomDays(String input) {
        String lowerInput = input.toLowerCase();
        
        // Look for patterns like "last 15 days", "past 45 days", etc.
        if (lowerInput.contains("last") || lowerInput.contains("past")) {
            // Extract number using regex
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?:last|past)\\s+(\\d+)\\s+days?");
            java.util.regex.Matcher matcher = pattern.matcher(lowerInput);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        
        return null; // Use timeFrame instead
    }
    
    private String handleShowEmployees(String input) {
        try {
            String managerId = extractManagerId(input, "MGR001");
            List<EmployeeDTO> employees = employeeService.getEmployeesByManagerDTO(managerId);
            
            if (employees.isEmpty()) {
                return "👥 No employees found under this manager.";
            }
            
            StringBuilder response = new StringBuilder("👥 **Team Members:**\n\n");
            
            for (EmployeeDTO employee : employees) {
                response.append(String.format(
                    "👤 **%s** (%s)\n" +
                    "📧 %s\n" +
                    "💼 %s\n\n",
                    employee.getName(),
                    employee.getEmployeeId(),
                    employee.getEmail(),
                    employee.getDepartment()
                ));
            }
            
            return response.toString();
        } catch (Exception e) {
            return "❌ Error retrieving employees: " + e.getMessage();
        }
    }
    
    private String handleDiscrepancySummary(String input) {
        try {
            String managerId = extractManagerId(input, "MGR001");
            
            // Extract time frame from user input
            String timeFrame = extractTimeFrame(input);
            Integer customDays = extractCustomDays(input);
            
            DiscrepancyRequest request = new DiscrepancyRequest(managerId, timeFrame, customDays);
            List<DiscrepancyResult> discrepancies = discrepancyService.getAttendanceDiscrepancies(request);
            
            long presentAndOnLeaveCount = discrepancies.stream()
                .filter(d -> "Present and on leave".equals(d.getIssue()))
                .count();
            
            long absentWithoutLeaveCount = discrepancies.stream()
                .filter(d -> "Absent without approved leave".equals(d.getIssue()))
                .count();
            
            return String.format(
                "📊 **Discrepancy Summary:**\n\n" +
                "🔸 Present and on leave: %d\n" +
                "🔸 Absent without leave: %d\n" +
                "🔸 Total discrepancies: %d\n",
                getTimeFrameDisplay(timeFrame, customDays),
                presentAndOnLeaveCount,
                absentWithoutLeaveCount,
                discrepancies.size()
            );
        } catch (Exception e) {
            return "❌ Error generating summary: " + e.getMessage();
        }
    }

    private String getTimeFrameDisplay(String timeFrame, Integer customDays) {
        if (customDays != null) {
            return "Last " + customDays + " days";
        }
        
        return switch (timeFrame) {
            case "1_WEEK" -> "Last week";
            case "2_WEEKS" -> "Last 2 weeks";
            case "1_MONTH" -> "Last month";
            case "1_QUARTER" -> "Last quarter";
            case "1_YEAR" -> "Last year";
            default -> "Last month";
        };
    }
    
    private String handleGenerateReport(String input) {
        try {
            String managerId = extractManagerId(input, "MGR001");
            DiscrepancyRequest request = new DiscrepancyRequest(managerId, "1_MONTH", null);
            List<DiscrepancyResult> discrepancies = discrepancyService.getAttendanceDiscrepancies(request);
            
            String report = reportService.generateDetailedReport(discrepancies);
            
            return "📄 **Detailed Report Generated:**\n\n" + report + 
                   "\n\n✅ Report ready for download: [Download PDF]";
        } catch (Exception e) {
            return "❌ Error generating report: " + e.getMessage();
        }
    }
    
    private String handleGenerateChart(String input) {
        try {
            String managerId = extractManagerId(input, "MGR001");
            DiscrepancyRequest request = new DiscrepancyRequest(managerId, "1_MONTH", null);
            List<DiscrepancyResult> discrepancies = discrepancyService.getAttendanceDiscrepancies(request);
            
            String chartAndSummary = reportService.generateChartAndSummary(discrepancies);
            
            return "📊 **Chart Generated:**\n\n" + chartAndSummary;
        } catch (Exception e) {
            return "❌ Error generating chart: " + e.getMessage();
        }
    }
    
    private String handleFreeTextInput(String input) {
        // Enhanced intent recognition that includes time frame
        String intentPrompt = String.format(
            "User asked: '%s'\n\n" +
            "Available actions:\n" +
            "1. show_discrepancies - Show attendance discrepancies\n" +
            "2. show_employees - Show team members\n" +
            "3. discrepancy_summary - Show summary statistics\n" +
            "4. generate_report - Generate detailed report\n" +
            "5. generate_chart - Create visualization\n" +
            "6. general_query - Answer general questions\n\n" +
            "Also extract time frame if mentioned (1_WEEK, 2_WEEKS, 1_MONTH, 1_QUARTER, 1_YEAR) or custom days.\n" +
            "Respond with: action_name|timeFrame|customDays (if applicable)",
            input
        );
        
        String geminiResponse = vertexClient.queryGemini(intentPrompt);
        
        // Parse Gemini's response for action and time parameters
        String[] parts = geminiResponse.split("\\|");
        String action = parts[0];
        
        // Route based on action
        if (action.contains("show_discrepancies")) {
            return handleShowDiscrepancies(input);
        } else if (action.contains("show_employees")) {
            return handleShowEmployees(input);
        } else if (action.contains("discrepancy_summary")) {
            return handleDiscrepancySummary(input);
        } else if (action.contains("generate_report")) {
            return handleGenerateReport(input);
        } else if (action.contains("generate_chart")) {
            return handleGenerateChart(input);
        } else {
            return vertexClient.queryGemini(input);
        }
    }
    
    private String extractManagerId(String input, String defaultId) {
        // Simple extraction - can be enhanced with regex
        if (input.contains("MGR")) {
            String[] parts = input.split("\\s+");
            for (String part : parts) {
                if (part.startsWith("MGR")) {
                    return part;
                }
            }
        }
        return defaultId;
    }
}