package com.journal.journal_service.dto;

import org.springframework.web.multipart.MultipartFile;

public class ReportAnalyzerDto {

    private String reportName;
    private String jobDescription;
    private MultipartFile resume;


    //get

    public String getReportName() {
        return reportName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public MultipartFile getResume() {
        return resume;
    }

    //set

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setResume(MultipartFile resume) {
        this.resume = resume;
    }
}
