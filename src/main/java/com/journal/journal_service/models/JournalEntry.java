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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull // Validates that this field cannot be null
    @Column(nullable = false) // Enforces non-null constraint at the database level
    private Long userId;

    private String entryTitle;

    @ManyToOne
    @JoinColumn(name = "work_type_id", nullable = true)
    private WorkType workType;

    private String description;

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
