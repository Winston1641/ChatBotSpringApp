package com.example.chatbot.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chatbot.DTOs.DiscrepancyResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class ReportService {
    @Autowired VertexClient vertexClient;

    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
            new com.google.gson.JsonPrimitive(src.toString()))
        .create();

    public String generateChartAndSummary(List<DiscrepancyResult> discrepancies) {
        String json = gson.toJson(discrepancies);
        
        // Count by issue type for chart
        Map<String, Long> issueCount = discrepancies.stream()
            .collect(Collectors.groupingBy(
                DiscrepancyResult::getIssue,
                Collectors.counting()
            ));
        
        String prompt = String.format(
            "Create a Chart.js bar chart HTML snippet for this discrepancy data: %s\n\n" +
            "Issue counts: %s\n\n" +
            "Also provide a 2-line executive summary of the findings.",
            json, issueCount.toString()
        );
        
        return vertexClient.queryGemini(prompt);
    }

    public String generateDetailedReport(List<DiscrepancyResult> discrepancies) {
        String json = gson.toJson(discrepancies);
        
        String prompt = String.format(
            "Generate a professional management report for these attendance discrepancies: %s\n\n" +
            "Include:\n" +
            "1. Executive Summary\n" +
            "2. Key Findings\n" +
            "3. Impact Analysis\n" +
            "4. Recommendations\n" +
            "5. Next Steps\n\n" +
            "Format as a formal business report.",
            json
        );
        
        return vertexClient.queryGemini(prompt);
    }

    public String generateInsights(List<DiscrepancyResult> discrepancies) {
        String json = new Gson().toJson(discrepancies);
        
        String prompt = String.format(
            "Analyze these attendance discrepancies and provide management insights: %s\n\n" +
            "Focus on:\n" +
            "- Patterns and trends\n" +
            "- Risk assessment\n" +
            "- Actionable recommendations",
            json
        );
        
        return vertexClient.queryGemini(prompt);
    }
}