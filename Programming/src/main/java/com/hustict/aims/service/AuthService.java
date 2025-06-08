package com.hustict.aims.service;

import com.hustict.aims.dto.auth.LoginRequestDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO.UserInfoDTO;
import com.hustict.aims.model.user.Role;
import com.hustict.aims.model.user.User;
import com.hustict.aims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) throws Exception {
        // Validate input
        validateLoginInput(loginRequest);
        
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException("User not found.");
        }

        User user = userOptional.get();

        // Validate password (In production, use proper password hashing)
        if (!isPasswordValid(user.getPassword(), loginRequest.getPassword())) {
            throw new SecurityException("Invalid password.");
        }

        // Convert roles to string list
        List<String> roleNames = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

        // Create user info DTO
        UserInfoDTO userInfo = new UserInfoDTO(
            user.getid(),
            user.getName(),
            user.getEmail(),
            roleNames
        );

        // Generate token (simplified - in production use JWT)
        String token = generateToken(user);

        return new LoginResponseDTO(true, "Login successfully.", token, userInfo);
    }

    /**
     * Validate token and return user info
     */
    public LoginResponseDTO validateTokenAndGetUserInfo(String token) throws Exception {
        Long userId = getUserIdFromToken(token);
        
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException("User not found");
        }

        User user = userOptional.get();
        List<String> roleNames = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

        UserInfoDTO userInfo = new UserInfoDTO(
            user.getid(),
            user.getName(),
            user.getEmail(),
            roleNames
        );

        return new LoginResponseDTO(true, "Token is valid.", token, userInfo);
    }

    /**
     * Refresh user token
     */
    public LoginResponseDTO refreshUserToken(String oldToken) throws Exception {
        Long userId = getUserIdFromToken(oldToken);
        
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchElementException("User not found.");
        }

        User user = userOptional.get();
        
        // Generate new token
        String newToken = generateToken(user);
        
        List<String> roleNames = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

        UserInfoDTO userInfo = new UserInfoDTO(
            user.getid(),
            user.getName(),
            user.getEmail(),
            roleNames
        );

        return new LoginResponseDTO(true, "Token is refreshed.", newToken, userInfo);
    }

    // ==================== HELPER METHODS ====================

    private void validateLoginInput(LoginRequestDTO loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        
        // Basic email format validation
        if (!isValidEmail(loginRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        
        // Basic password validation
        if (loginRequest.getPassword().length() < 5) {
            throw new IllegalArgumentException("Password must be at least 5 characters.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isPasswordValid(String storedPassword, String providedPassword) {
        // Simple comparison for now
        // In production: return BCrypt.checkpw(providedPassword, storedPassword);
        return storedPassword.equals(providedPassword);
    }

    private String generateToken(User user) {
        // Simplified token generation
        // In production: use JWT with proper signing and expiration
        return "token_" + user.getid() + "_" + System.currentTimeMillis();
    }

    /**
     * Extract user ID from token
     */
    private Long getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("_");
            if (parts.length >= 2) {
                return Long.parseLong(parts[1]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid token format");
        }
        throw new IllegalArgumentException("Invalid token format");
    }

    /**
     * Validate if user has required role
     * Can be used for authorization checks
     */
    public boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * Check if user is administrator
     */
    public boolean isAdmin(User user) {
        return hasRole(user, "ADMIN");
    }

    /**
     * Check if user is product manager
     */
    public boolean isProductManager(User user) {
        return hasRole(user, "PRODUCT_MANAGER");
    }
} 