package com.healthcare.patient_service.controller;

import com.healthcare.patient_service.model.Patient;
import com.healthcare.patient_service.service.PatientService;
import com.healthcare.patient_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patients/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PatientService patientService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Patient patient = patientService.findByEmail(userDetails.getUsername());
            
            String token = jwtUtil.generateToken(userDetails, patient.getRole().name());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", patient.getRole().name());
            response.put("email", patient.getEmail());
            response.put("name", patient.getName());
            response.put("id", patient.getId());

            return response;
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect email or password", e);
        }
    }

    @PostMapping("/validate")
    public Map<String, Object> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("username", username);
        response.put("role", role);
        
        return response;
    }

    public static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
