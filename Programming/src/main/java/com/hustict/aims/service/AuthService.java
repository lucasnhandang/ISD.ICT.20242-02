package com.hustict.aims.service;

import com.hustict.aims.dto.auth.LoginRequestDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO;
import com.hustict.aims.dto.auth.UserInfoDTO;
import com.hustict.aims.model.user.Role;
import com.hustict.aims.model.user.User;
import com.hustict.aims.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import com.hustict.aims.utils.JwtUtils;
import com.hustict.aims.utils.AuthUtils;
import com.hustict.aims.service.MessageService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final MessageService messageService;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, MessageService messageService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.messageService = messageService;
    }

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        validateLoginInput(loginRequest);
        
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException(messageService.getAuthFailed());
        }
        
        User user = userOptional.get();
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new SecurityException(messageService.getAuthFailed());
        }
        
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        
        String token = jwtUtils.generateToken(
                String.valueOf(user.getId()),
                Map.of(
                        "email", user.getEmail(),
                        "roles", roleNames
                )
        );
        
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames
        );
        return new LoginResponseDTO(true, "Login successful", token, userInfo);
    }

    public LoginResponseDTO validateTokenAndGetUserInfo(String token) {
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException(messageService.getAuthFailed());
        }
        User user = userOptional.get();
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames
        );
        return new LoginResponseDTO(true, "Token is valid.", token, userInfo);
    }

    public LoginResponseDTO validateTokenAndGetUserInfo(String token, String requiredRole) {
        Claims claims = jwtUtils.getClaimsFromToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException(messageService.getAuthFailed());
        }
        User user = userOptional.get();
        if (requiredRole != null && !requiredRole.isEmpty() && !AuthUtils.hasRole(user, requiredRole)) {
            throw new SecurityException(messageService.getAuthFailed());
        }
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames
        );
        return new LoginResponseDTO(true, "Token is valid.", token, userInfo);
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