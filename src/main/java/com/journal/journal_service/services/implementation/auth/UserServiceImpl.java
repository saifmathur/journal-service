package com.journal.journal_service.services.implementation.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.journal.journal_service.dto.CustomUserDetails;
import com.journal.journal_service.dto.GoogleLoginRequestDto;
import com.journal.journal_service.dto.RegisterDto;
import com.journal.journal_service.models.auth.User;
import com.journal.journal_service.models.auth.UserDetails;
import com.journal.journal_service.repository.auth.UserRepo;
import com.journal.journal_service.services.auth.UserService;
import com.journal.journal_service.utility.GoogleTokenService;
import com.journal.journal_service.utility.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    GoogleTokenService googleTokenService;

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

            userDetails.setPhone(registerForm.getPhone());
            userDetails.setDOB(registerForm.getDOB());
            userDetails.setAge(getAge(registerForm.getDOB()));
            userDetails.setFirstName(registerForm.getFirstName());
            userDetails.setLastName(registerForm.getLastName());
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
            e.printStackTrace();
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
            User user = userRepo.findByUsernameAndIsActive(registerForm.getUsername(), true).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String fullName = user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName().toString();

            String jwtToken = JwtUtil.generateToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("message", "Login successful");
            response.put("fullName", fullName);
            response.put("initials", getInitials(fullName));
            response.put("username", user.getUsername());
            response.put("status", "200");
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @Override
    public Boolean checkDuplicateUserName(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    @Override
    public Map<String, String> googleLogin(GoogleLoginRequestDto googleLoginRequestDto) throws Exception {
        try {
            GoogleIdToken.Payload payload = googleTokenService.verifyToken(googleLoginRequestDto.getIdToken());

            // Step 2: Extract user info from the payload
            String email = payload.getEmail();
            Boolean name = payload.getEmailVerified();
            User user = userRepo.findByUsernameAndIsActive(email, true).orElse(null);
            UserDetails ud = user == null ? new UserDetails() : user.getUserDetails();
            if (user == null) {
                user = new User();
                user.setId(null);
                ud.setId(null);
                user.setUsername(email);
                user.setRoles("ROLE_USER");
                ud.setEmail(email);
                ud.setFirstName((String) payload.get("given_name"));
                ud.setLastName((String) payload.get("family_name"));
                user.setUserDetails(ud);
                user = userRepo.saveAndFlush(user);
            }
            String fullName = user.getUserDetails().getFirstName() + " " + user.getUserDetails().getLastName();
            String jwtToken = JwtUtil.generateToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("message", "Login successful");
            response.put("fullName", fullName);
            response.put("initials", getInitials(fullName));
            response.put("username", user.getUsername());
            response.put("email", email);
            response.put("status", "200");
            return response;
            // Step 4: Return JWT token to the frontend
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        log.info("Attempting to load user with username: {}", username);

        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        log.info("USER LOADED: {} with roles: {} with ID: {}", user.getUsername(), user.getRoles(), user.getId());
        CustomUserDetails cud = new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
        return cud;
    }

    public String getAge(String dob) throws Exception {
//        try{
//            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formattedDate = outputFormat.format(dob);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate formatDob = LocalDate.parse(dob, formatter);
//            return String.valueOf(Period.between(formatDob, LocalDate.now()).getYears());
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
        try {
            // Define the input date format that matches the given date string
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");

            // Parse the input string into a Date object
            Date parsedDate = inputFormat.parse(dob);

            // Convert the Date object into LocalDate
            LocalDate formatDob = new java.sql.Date(parsedDate.getTime()).toLocalDate();

            // Calculate the age by finding the difference in years
            Period period = Period.between(formatDob, LocalDate.now());
            return String.valueOf(period.getYears());
        } catch (Exception e) {
            throw new Exception("Error parsing date: " + e.getMessage(), e);
        }
    }

    public String getInitials(String fullName) {
        String[] parts = fullName.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return initials.toString();
    }

}
