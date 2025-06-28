package com.hustict.aims.controller;

import com.hustict.aims.dto.auth.LoginRequestDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO;
import com.hustict.aims.service.auth.AuthService;
import com.hustict.aims.utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final Set<String> blacklistedTokens = new HashSet<>();

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Email or password is incorrect", null, null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO(false, e.getMessage(), null, null));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Email or password is incorrect", null, null));

        } catch (Exception e) {
            System.err.println("Login error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO(false, "System error, please try again.", null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponseDTO> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = AuthUtils.extractTokenFromHeader(authHeader);
            if (token != null) {
                blacklistedTokens.add(token);
            }
            return ResponseEntity.ok(new LoginResponseDTO(true, "Logged out successfully.", null, null));
            
        } catch (Exception e) {
            // Always return success for logout
            return ResponseEntity.ok(new LoginResponseDTO(true, "Logged out successfully.", null, null));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<LoginResponseDTO> validateSession(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = AuthUtils.extractTokenFromHeader(authHeader);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO(false, "Token is not provided.", null, null));
            }
            
            if (blacklistedTokens.contains(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO(false, "Token is invalidated.", null, null));
            }

            LoginResponseDTO response = authService.validateTokenAndGetUserInfo(token);
            return ResponseEntity.ok(response);
            
        } catch (ExpiredJwtException e) {
            String token = AuthUtils.extractTokenFromHeader(authHeader);
            if (token != null) {
                blacklistedTokens.add(token);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token expired.", null, null));
                    
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "User does not exist.", null, null));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Token is invalid.", null, null));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO(false, "System error when validating token.", null, null));
        }
    }

    /**
     * Admin-only endpoint - validate admin role
     */
    @GetMapping("/admin")
    public ResponseEntity<LoginResponseDTO> adminOnly(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = AuthUtils.extractTokenFromHeader(authHeader);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO(false, "Token is not provided.", null, null));
            }
            
            LoginResponseDTO response = authService.validateTokenAndGetUserInfo(token, "ADMIN");
            return ResponseEntity.ok(new LoginResponseDTO(true, "Welcome ADMIN!", null, response.getUserInfo()));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponseDTO(false, "Access denied: ADMIN only", null, null));
        }
    }
} 