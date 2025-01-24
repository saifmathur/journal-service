package com.journal.journal_service.dto;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkTypeDto {
    private String workType;
    private Long userId;

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
