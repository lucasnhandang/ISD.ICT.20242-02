package com.hustict.aims;

import com.hustict.aims.controller.CreateUserController;
import com.hustict.aims.model.User;
import com.hustict.aims.repository.UserRepository;
import com.hustict.aims.service.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserControllerTest {

    private UserValidator validator;
    private MockUserEmailService emailService;
    private MockAuditLogService auditLogService;
    private CreateUserController controller;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
        emailService = new MockUserEmailService();
        auditLogService = new MockAuditLogService();
        controller = new CreateUserController(validator, emailService, auditLogService);

        // Xóa user giữa các test
        UserRepository.getAllUsers().clear();
    }

    @Test
    void createUser_success() {
        controller.createUser("Alice", "alice@domain.com", "abc123", "0123456789", "admin");

        assertTrue(UserRepository.emailExists("alice@domain.com"));
        assertTrue(emailService.wasCalled);
        assertTrue(auditLogService.wasCalled);
    }

    @Test
    void createUser_duplicateEmail() {
        controller.createUser("Bob", "dup@domain.com", "abc123", "0123456789", "admin");
        controller.createUser("Eve", "dup@domain.com", "xyz456", "0987654321", "admin");

        assertEquals(1, UserRepository.getAllUsers().size());
    }

    @Test
    void createUser_invalidEmailFormat() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate("David", "invalid-email", "pass", "0123456789", "admin");
        });
        assertEquals("Invalid email format.", e.getMessage());
    }

    @Test
    void createUser_invalidPhoneNumber() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate("Eve", "eve@domain.com", "pass", "123abc", "admin");
        });
        assertEquals("Phone number must be 10 digits.", e.getMessage());
    }

    @Test
    void createUser_invalidRole() 
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            validator.validate("Frank", "frank@domain.com", "pass", "0123456789", "guest");
        });
        assertEquals("Role must be 'admin' or 'productManager'.", e.getMessage());
    }

    static class MockUserEmailService implements UserEmailService {
        boolean wasCalled = false;
        public void send
