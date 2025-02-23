package com.journal.journal_service.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "report_analyzer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportAnalyzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull // Validates that this field cannot be null
    @Column(nullable = false) // Enforces non-null constraint at the database level
    private Long userId;

    private String reportName;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    private String bucketFilePath;

    private String generatedFilePath;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    //getters

    public String getGeneratedFilePath() {
        return generatedFilePath;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getReportName() {
        return reportName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getBucketFilePath() {
        return bucketFilePath;
    }

    //setters

    public void setGeneratedFilePath(String generatedFilePath) {
        this.generatedFilePath = generatedFilePath;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setBucketFilePath(String bucketFilePath) {
        this.bucketFilePath = bucketFilePath;
    }
}
