package com.journal.journal_service.dto;


import lombok.Data;

@Data
public class GoogleLoginRequestDto {
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
