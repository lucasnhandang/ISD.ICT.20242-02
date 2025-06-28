package com.hustict.aims.service.auth;

import com.hustict.aims.dto.auth.LoginRequestDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO;
import com.hustict.aims.dto.auth.UserInfoDTO;
import com.hustict.aims.model.user.Role;
import com.hustict.aims.model.user.User;
import com.hustict.aims.repository.UserRepository;
import com.hustict.aims.service.MessageService;
import com.hustict.aims.utils.JwtUtils;
import com.hustict.aims.utils.AuthUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, 
                        JwtUtils jwtUtils, 
                        MessageService messageService, 
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
    }

    // Authenticate user with email and password
    // Returns JWT token and user information on success
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        try {
            validateLoginInput(loginRequest);
            
            User user = findUserByEmail(loginRequest.getEmail());
            
            verifyPassword(loginRequest.getPassword(), user.getPassword());
            
            // Generate JWT token
            String token = generateUserToken(user);
            
            UserInfoDTO userInfo = createUserInfo(user);
            
            return new LoginResponseDTO(true, "Login successful!", token, userInfo);
            
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Validate JWT token and return user information
    public LoginResponseDTO validateTokenAndGetUserInfo(String token) {
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        
        User user = findUserById(userId);
        UserInfoDTO userInfo = createUserInfo(user);
        
        return new LoginResponseDTO(true, "Token is valid.", token, userInfo);
    }

    // Validate JWT token with role requirement
    public LoginResponseDTO validateTokenAndGetUserInfo(String token, String requiredRole) {
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        
        User user = findUserById(userId);
        
        // Check role if required
        if (requiredRole != null && !requiredRole.isEmpty() && !AuthUtils.hasRole(user, requiredRole)) {
            throw new SecurityException(messageService.getAuthFailed());
        }
        
        UserInfoDTO userInfo = createUserInfo(user);
        return new LoginResponseDTO(true, "Token is valid.", token, userInfo);
    }

    // Private helper methods
    private User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException(messageService.getAuthFailed());
        }
        return userOptional.get();
    }
    
    private User findUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException(messageService.getAuthFailed());
        }
        return userOptional.get();
    }
    
    private void verifyPassword(String rawPassword, String hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new SecurityException(messageService.getAuthFailed());
        }
    }
    
    private String generateUserToken(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        
        return jwtUtils.generateToken(
                String.valueOf(user.getId()),
                Map.of(
                        "email", user.getEmail(),
                        "roles", roleNames
                )
        );
    }
    
    private UserInfoDTO createUserInfo(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        
        return new UserInfoDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames
        );
    }

    private void validateLoginInput(LoginRequestDTO loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException(messageService.getInvalidInput() + ": Email cannot be empty.");
        }

        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException(messageService.getInvalidInput() + ": Password cannot be empty.");
        }

        if (!isValidEmail(loginRequest.getEmail())) {
            throw new IllegalArgumentException(messageService.getInvalidInput() + ": Invalid email format.");
        }

        if (loginRequest.getPassword().length() < 5) {
            throw new IllegalArgumentException(messageService.getInvalidInput() + ": Password must be at least 5 characters.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}