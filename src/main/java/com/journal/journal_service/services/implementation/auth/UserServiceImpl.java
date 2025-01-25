package com.journal.journal_service.services.implementation.auth;

import com.journal.journal_service.dto.RegisterDto;
import com.journal.journal_service.models.auth.User;
import com.journal.journal_service.models.auth.UserDetails;
import com.journal.journal_service.repository.auth.UserRepo;
import com.journal.journal_service.services.auth.UserService;
import com.journal.journal_service.utility.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepo userRepo;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager; // Inject AuthenticationManager

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, String> registerUser(RegisterDto registerForm) throws Exception {
        User user = new User();
        UserDetails userDetails = new UserDetails();
        try {
            if (userRepo.findByUsername(registerForm.getUsername()).isPresent()) {
                throw new DataIntegrityViolationException("Data Integrity Violation");
            }
            user.setUsername(registerForm.getUsername());
            user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
            userDetails.setAge(registerForm.getAge());
            userDetails.setPhone(registerForm.getPhone());
            userDetails.setDOB(registerForm.getDOB());
            userDetails.setAddressLine1(registerForm.getAddressLine1());
            userDetails.setAddressLine2(registerForm.getAddressLine2());
            userDetails.setEmail(registerForm.getEmail());
            user.setUserDetails(userDetails);
            user.setRoles("ROLE_USER");
            userRepo.save(user);
            Map<String, String> resMap = new HashMap<>();
            resMap.put("message", "Registered user: " + user.getUsername());
            resMap.put("status", "200");
            return resMap;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Found duplicate username.");
        } catch (Exception e) {
            throw new Exception("Error while registering user.");
        }

    }

    @Override
    public Map<String, String> login(RegisterDto registerForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registerForm.getUsername(),
                            registerForm.getPassword()
                    )
            );

            String jwtToken = JwtUtil.generateToken(authentication.getName());
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("message", "Login successful");
            response.put("status", "200");
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        log.info("Attempting to load user with username: {}", username);

        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        log.info("USER LOADED: {} with roles: {}", user.getUsername(), user.getRoles());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
