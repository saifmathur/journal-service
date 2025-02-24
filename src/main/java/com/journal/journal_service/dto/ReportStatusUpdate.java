package com.journal.journal_service.dto;

public class ReportStatusUpdate {
    private Long reportId;
    private String status;
    private String generatedFilePath;


    public ReportStatusUpdate(Long reportId, String status, String generatedFilePath) {
        this.reportId = reportId;
        this.status = status;
        this.generatedFilePath = generatedFilePath;
    }

    public String getGeneratedFilePath() {
        return generatedFilePath;
    }

    public Long getReportId() {
        return reportId;
    }

    public String getStatus() {
        return status;
    }
}
