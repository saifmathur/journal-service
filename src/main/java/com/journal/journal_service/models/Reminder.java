package com.journal.journal_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table(name = "reminders")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    private String title;

    @Column(length = 500)
    private String notes;

    private LocalDate reminderDate;

    private LocalTime reminderTime;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private String priority;

    private String frequency;

    private String userTimeZone;

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isDeleted(boolean b) {
        return isDeleted;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }



    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserTimeZone(String userTimeZone) {
        this.userTimeZone = userTimeZone;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getUserTimeZone() {
        return userTimeZone;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public LocalTime getReminderTime() {
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


}
