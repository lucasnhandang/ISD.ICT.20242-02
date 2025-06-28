package com.hustict.aims.service.auth;

import com.hustict.aims.dto.auth.CreateUserRequestDTO;
import com.hustict.aims.dto.auth.CreateUserResponseDTO;
import com.hustict.aims.dto.auth.UserInfoDTO;
import com.hustict.aims.model.user.Role;
import com.hustict.aims.model.user.User;
import com.hustict.aims.repository.RoleRepository;
import com.hustict.aims.repository.UserRepository;
import com.hustict.aims.service.MessageService;
import com.hustict.aims.utils.AuthUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserCreationService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    public UserCreationService(UserRepository userRepository, 
                              RoleRepository roleRepository, 
                              PasswordEncoder passwordEncoder,
                              MessageService messageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    @Transactional
    public CreateUserResponseDTO createUser(CreateUserRequestDTO request, User adminUser) {
        try {
            // Validate admin permissions
            validateAdminPermissions(adminUser);
            
            // Validate input data
            validateCreateUserRequest(request);
            
            // Check for existing user
            validateUserDoesNotExist(request.getEmail(), request.getPhoneNumber());
            
            // Create and save user
            User newUser = createUserEntity(request);
            User savedUser = userRepository.save(newUser);
            
            // Create response
            UserInfoDTO userInfo = createUserInfo(savedUser);
            
            return new CreateUserResponseDTO(true, "User created successfully", userInfo);
            
        } catch (Exception e) {
            return new CreateUserResponseDTO(false, e.getMessage(), null);
        }
    }

    private void validateAdminPermissions(User adminUser) {
        if (!AuthUtils.isAdmin(adminUser)) {
            throw new SecurityException("Only administrators can create users");
        }
    }

    private void validateCreateUserRequest(CreateUserRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        
        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            throw new IllegalArgumentException("At least one role must be assigned");
        }
    }

    private void validateUserDoesNotExist(String email, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("User with this phone number already exists");
        }
    }

    private User createUserEntity(CreateUserRequestDTO request) {
        Set<Role> roles = getRolesFromNames(request.getRoles());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        
        return new User(
            request.getEmail(),
            request.getName(),
            hashedPassword,
            request.getPhoneNumber(),
            roles
        );
    }

    private Set<Role> getRolesFromNames(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role '" + roleName + "' does not exist"));
            roles.add(role);
        }
        
        return roles;
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

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{10}$");
    }
} 