package com.hustict.aims.service;

import com.hustict.aims.dto.auth.CreateUserRequestDTO;
import com.hustict.aims.dto.auth.CreateUserResponseDTO;
import com.hustict.aims.model.user.Role;
import com.hustict.aims.model.user.User;
import com.hustict.aims.repository.RoleRepository;
import com.hustict.aims.repository.UserRepository;
import com.hustict.aims.service.auth.UserCreationService;
import com.hustict.aims.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCreationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private UserCreationService userCreationService;

    private User adminUser;
    private Role adminRole;
    private Role productManagerRole;
    private CreateUserRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        // Setup admin role
        adminRole = new Role("ADMIN");
        adminRole.setRoleID(1L);

        // Setup product manager role
        productManagerRole = new Role("PRODUCT_MANAGER");
        productManagerRole.setRoleID(2L);

        // Setup admin user
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@test.com");
        adminUser.setRoles(new HashSet<>(Arrays.asList(adminRole)));

        // Setup valid request
        validRequest = new CreateUserRequestDTO();
        validRequest.setName("Test User");
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");
        validRequest.setPhoneNumber("0123456789");
        validRequest.setRoles(Arrays.asList("PRODUCT_MANAGER"));
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(roleRepository.findByName("PRODUCT_MANAGER")).thenReturn(Optional.of(productManagerRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(adminUser);

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User created successfully", response.getMessage());
        assertNotNull(response.getUserInfo());
        
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).existsByPhoneNumber("0123456789");
        verify(roleRepository).findByName("PRODUCT_MANAGER");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_NonAdminUser_ThrowsException() {
        // Arrange
        User nonAdminUser = new User();
        nonAdminUser.setRoles(new HashSet<>(Arrays.asList(productManagerRole)));

        // Act & Assert
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, nonAdminUser);
        
        assertFalse(response.isSuccess());
        assertEquals("Only administrators can create users", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_EmptyName_ReturnsError() {
        // Arrange
        validRequest.setName("");

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Name is required", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_InvalidEmail_ReturnsError() {
        // Arrange
        validRequest.setEmail("invalid-email");

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Invalid email format", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShortPassword_ReturnsError() {
        // Arrange
        validRequest.setPassword("123");

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Password must be at least 6 characters long", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_InvalidPhoneNumber_ReturnsError() {
        // Arrange
        validRequest.setPhoneNumber("123");

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Invalid phone number format", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_NoRoles_ReturnsError() {
        // Arrange
        validRequest.setRoles(new ArrayList<>());

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("At least one role must be assigned", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ExistingEmail_ReturnsError() {
        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("User with this email already exists", response.getMessage());
        
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ExistingPhoneNumber_ReturnsError() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber("0123456789")).thenReturn(true);

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("User with this phone number already exists", response.getMessage());
        
        verify(userRepository).existsByPhoneNumber("0123456789");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_InvalidRole_ReturnsError() {
        // Arrange
        validRequest.setRoles(Arrays.asList("INVALID_ROLE"));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(roleRepository.findByName("INVALID_ROLE")).thenReturn(Optional.empty());

        // Act
        CreateUserResponseDTO response = userCreationService.createUser(validRequest, adminUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Role 'INVALID_ROLE' does not exist", response.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }
} 