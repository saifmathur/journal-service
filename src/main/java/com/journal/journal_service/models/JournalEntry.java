package com.journal.journal_service.models;


import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "journal_entry")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JournalEntry {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull // Validates that this field cannot be null
    @Column(nullable = false) // Enforces non-null constraint at the database level
    private Long userId;

    private String entryTitle;


    @Column(nullable = false)
    private Boolean isActive = true;


    @ManyToOne
    @JoinColumn(name = "work_type_id", nullable = true)
    private WorkType workType;

    public void setActive(Boolean active) {
        isActive = active;
    }

    private String description;

    public Boolean getActive() {
        return isActive;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public Long getId() {
        return id;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
