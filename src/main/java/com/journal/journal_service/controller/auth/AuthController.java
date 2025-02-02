package com.journal.journal_service.controller.auth;

import com.journal.journal_service.dto.RegisterDto;
import com.journal.journal_service.services.auth.UserService;
import com.journal.journal_service.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createUser(@ModelAttribute RegisterDto registerForm) throws Exception {
        return ResponseEntity.ok(userService.registerUser(registerForm));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterDto registerForm) throws Exception {
        try {
            Map<String,String> login = userService.login(registerForm);
            return ResponseEntity.ok(login);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        return ResponseEntity.ok().build();
    }



}

