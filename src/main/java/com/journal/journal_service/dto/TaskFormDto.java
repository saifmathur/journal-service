package com.journal.journal_service.dto;

import com.journal.journal_service.models.WorkType;

public class TaskFormDto {

    private Long userId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    private Long taskId;

    private String taskName;
    private WorkType typeOfWork;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    private String description;
    private String date; // Store as String for simplicity (or use LocalDate for better type safety)

    // Getters and Setters


    public void setTypeOfWork(WorkType typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public WorkType getTypeOfWork() {
        return typeOfWork;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
