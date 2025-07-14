package com.example.chatbot.DTOs;

public class DiscrepancyRequest {
    private String managerId;
    private String timeFrame; // "1_YEAR", "6_MONTHS", "1_MONTH", "1_WEEK", "2_WEEKS", "3_WEEKS"
    private Integer customDays; // Optional: for custom day ranges
    
    // Constructors
    public DiscrepancyRequest() {}
    
    public DiscrepancyRequest(String managerId, String timeFrame) {
        this.managerId = managerId;
        this.timeFrame = timeFrame;
    }
    
    public DiscrepancyRequest(String managerId, String timeFrame, Integer customDays) {
        this.managerId = managerId;
        this.timeFrame = timeFrame;
        this.customDays = customDays;
    }
    
    // Getters and Setters
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getTimeFrame() {
        return timeFrame;
    }
    
    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }
    
    public Integer getCustomDays() {
        return customDays;
    }
    
    public void setCustomDays(Integer customDays) {
        this.customDays = customDays;
    }
    
    @Override
    public String toString() {
        return "DiscrepancyRequest{" +
                "managerId='" + managerId + '\'' +
                ", timeFrame='" + timeFrame + '\'' +
                ", customDays=" + customDays +
                '}';
    }
}
