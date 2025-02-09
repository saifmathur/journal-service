package com.journal.journal_service.dto;


import lombok.Data;

@Data
public class ReminderDto {

    private Long userId;
    private Long reminderId;
    private String title;
    private String notes;
    private String reminderDate;
    private String reminderTime;
    private boolean isActive;
    private String priority;
    private String frequency;
    private String lastModified;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getReminderId() {
        return reminderId;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPriority() {
        return priority;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getLastModified() {
        return lastModified;
    }
}
