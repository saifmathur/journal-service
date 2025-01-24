package com.journal.journal_service.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "work_type")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String workType;

    @CreationTimestamp
    private LocalDateTime createdDate;

    public void setId(Long id) {
        this.id = id;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getWorkType() {
        return workType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    private Long userId;

    //    public WorkType(Long id, String workType, LocalDateTime createdDate, Long userId) {
//        this.id = id;
//        this.workType = workType;
//        this.createdDate = createdDate;
//        this.userId = userId;
//    }
}
