package com.journal.journal_service.services.auth;

import com.journal.journal_service.dto.GoogleLoginRequestDto;
import com.journal.journal_service.dto.RegisterDto;

import java.util.Map;

public interface UserService {

    Map<String, String> registerUser(RegisterDto registerForm) throws Exception;

    Map<String, String> login(RegisterDto registerForm);

    Boolean checkDuplicateUserName(String username);

    Map<String, String> googleLogin(GoogleLoginRequestDto googleLoginRequestDto) throws Exception;

    Map<String, Object> getUserDetails() throws Exception;

    Map<String, String> updateUser(RegisterDto registerForm) throws Exception;;
}
