package com.hustict.aims.controller;

import com.hustict.aims.dto.auth.LoginRequestDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO;
import com.hustict.aims.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    
    // Simple in-memory token blacklist
    private final Set<String> blacklistedTokens = new HashSet<>();

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User login endpoint
     * Improved error handling with specific exceptions
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
            
        } catch (NoSuchElementException e) {
            // User not found - return generic message for security
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Email or password is incorrect", null, null));
                
        } catch (IllegalArgumentException e) {
            // Invalid input (email format, password, etc.)
            return ResponseEntity.badRequest()
                .body(new LoginResponseDTO(false, e.getMessage(), null, null));
                
        } catch (SecurityException e) {
            // Password mismatch - return generic message for security
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Email or password is incorrect", null, null));
                
        } catch (Exception e) {
            // Unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponseDTO(false, "System error, please try again.", null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponseDTO> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            
            if (token != null) {
                // Add token to blacklist to invalidate it
                blacklistedTokens.add(token);
                
                // Optionally, you can also validate the token before blacklisting
                if (isValidTokenFormat(token)) {
                    return ResponseEntity.ok(
                        new LoginResponseDTO(true, "Logged out successfully.", null, null));
                }
            }
            
            // Even if token is invalid, still return success for logout
            return ResponseEntity.ok(
                new LoginResponseDTO(true, "Logged out successfully.", null, null));
                
        } catch (Exception e) {
            // Log error but still return success for logout
            return ResponseEntity.ok(
                new LoginResponseDTO(true, "Logged out successfully.", null, null));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<LoginResponseDTO> validateSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is not provided.", null, null));
            }
            
            // Check if token is blacklisted
            if (blacklistedTokens.contains(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is invalidated.", null, null));
            }
            
            // Validate token format and expiration
            if (!isValidTokenFormat(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is invalid.", null, null));
            }
            
            if (isTokenExpired(token)) {
                // Auto blacklist expired tokens
                blacklistedTokens.add(token);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token expired.", null, null));
            }
            
            // Token is valid, get user info from AuthService
            try {
                LoginResponseDTO response = authService.validateTokenAndGetUserInfo(token);
                return ResponseEntity.ok(response);
                
            } catch (NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "User does not exist.", null, null));
            }
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Token is invalid.", null, null));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponseDTO(false, "System error when validating token.", null, null));
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String oldToken = extractTokenFromHeader(authHeader);
            
            if (oldToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is not provided.", null, null));
            }
            
            // Check if token is blacklisted
            if (blacklistedTokens.contains(oldToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is invalidated.", null, null));
            }
            
            // Validate old token
            if (!isValidTokenFormat(oldToken) || isTokenExpired(oldToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is invalid.", null, null));
            }
            
            try {
                // Generate new token through AuthService
                LoginResponseDTO response = authService.refreshUserToken(oldToken);
                
                // Blacklist old token
                blacklistedTokens.add(oldToken);
                
                return ResponseEntity.ok(response);
                
            } catch (NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "User does not exist.", null, null));
            }
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Token is invalid.", null, null));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponseDTO(false, "System error when refreshing token.", null, null));
        }
    }


    @GetMapping("/check")
    public ResponseEntity<LoginResponseDTO> checkAuth(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = extractTokenFromHeader(authHeader);
            
            if (token == null || blacklistedTokens.contains(token) || 
                !isValidTokenFormat(token) || isTokenExpired(token)) {
                return ResponseEntity.ok(
                    new LoginResponseDTO(false, "Not logged in.", null, null));
            }
            
            return ResponseEntity.ok(
                new LoginResponseDTO(true, "Logged in.", null, null));
                
        } catch (Exception e) {
            return ResponseEntity.ok(
                new LoginResponseDTO(false, "Not logged in.", null, null));
        }
    }

    // ==================== HELPER METHODS ====================

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        return null;
    }

    private boolean isValidTokenFormat(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // Simple validation: token should start with "token_" and have reasonable length
        return token.startsWith("token_") && token.length() > 20;
    }

    private boolean isTokenExpired(String token) {
        try {
            // Extract timestamp from simple token format: token_userId_timestamp
            String[] parts = token.split("_");
            if (parts.length >= 3) {
                long tokenTime = Long.parseLong(parts[2]);
                long currentTime = System.currentTimeMillis();
                // Token expires after 24 hours (86400000 ms)
                return (currentTime - tokenTime) > 86400000;
            }
        } catch (NumberFormatException e) {
            // If can't parse, consider expired
            return true;
        }
        return true;
    }


    @GetMapping("/debug/blacklisted-count")
    public ResponseEntity<Integer> getBlacklistedTokensCount() {
        return ResponseEntity.ok(blacklistedTokens.size());
    }
} 