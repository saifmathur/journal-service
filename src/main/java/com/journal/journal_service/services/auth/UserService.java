package com.journal.journal_service.services.auth;

import com.journal.journal_service.dto.RegisterDto;

import java.util.Map;

public interface UserService {

    Map<String, String> registerUser(RegisterDto registerForm) throws Exception;

    Map<String, String> login(RegisterDto registerForm);

    Boolean checkDuplicateUserName(String username) throws Exception;
}
