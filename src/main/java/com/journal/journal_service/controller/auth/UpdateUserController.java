package com.journal.journal_service.controller.auth;


import com.journal.journal_service.dto.RegisterDto;
import com.journal.journal_service.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UpdateUserController {

    @Autowired
    UserService userService;

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody RegisterDto registerForm) throws Exception {
        try {
            Map<String,String> login = userService.updateUser(registerForm);
            return ResponseEntity.ok(login);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<Map<String, Object>> getUserDetails() throws Exception {
        return ResponseEntity.ok((Map<String, Object>) userService.getUserDetails());
    }

}
