package com.hustict.aims.controller;

import com.hustict.aims.dto.auth.CreateUserRequestDTO;
import com.hustict.aims.dto.auth.CreateUserResponseDTO;
import com.hustict.aims.dto.auth.LoginResponseDTO;
import com.hustict.aims.model.user.User;
import com.hustict.aims.repository.UserRepository;
import com.hustict.aims.service.auth.UserCreationService;
import com.hustict.aims.utils.AuthUtils;
import com.hustict.aims.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/users")
@CrossOrigin(origins = "*")
public class CreateUserController {

    private final UserCreationService userCreationService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public CreateUserController(UserCreationService userCreationService, 
                               UserRepository userRepository,
                               JwtUtils jwtUtils) {
        this.userCreationService = userCreationService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @RequestBody CreateUserRequestDTO request,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            System.out.println("=== Create User Request ===");
            System.out.println("Request: " + request);
            System.out.println("Auth Header: " + authHeader);
            
            String token = AuthUtils.extractTokenFromHeader(authHeader);
            if (token == null) {
                System.out.println("ERROR: Authorization token required");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CreateUserResponseDTO(false, "Authorization token required", null));
            }

            User adminUser = getAdminUserFromToken(token);
            if (adminUser == null) {
                System.out.println("ERROR: Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CreateUserResponseDTO(false, "Invalid or expired token", null));
            }

            System.out.println("Admin User: " + adminUser.getEmail() + " with roles: " + adminUser.getRoles());

            CreateUserResponseDTO response = userCreationService.createUser(request, adminUser);
            
            System.out.println("Service Response: " + response);
            
            if (response.isSuccess()) {
                System.out.println("SUCCESS: User created successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                System.out.println("ERROR: " + response.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            System.err.println("EXCEPTION in createUser: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CreateUserResponseDTO(false, "Internal server error: " + e.getMessage(), null));
        }
    }

    private User getAdminUserFromToken(String token) {
        try {
            String userId = jwtUtils.getClaimsFromToken(token).getSubject();
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
            
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (AuthUtils.isAdmin(user)) {
                    return user;
                }
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Error getting admin user from token: " + e.getMessage());
            return null;
        }
    }
} 