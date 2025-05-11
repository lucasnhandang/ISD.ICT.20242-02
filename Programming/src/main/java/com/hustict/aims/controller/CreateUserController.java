package com.hustict.aims.controller;

import com.hustict.aims.model.User;
import com.hustict.aims.model.User.UserRole;
import com.hustict.aims.service.UserValidator;
import com.hustict.aims.repository.UserRepository;
import com.hustict.aims.service.UserEmailService;
import com.hustict.aims.service.AuditLogService;
/*
Functional. All the functions in the class work together to support a single responsibility - coordinating the user creation process.
 It should only change if the flow changes.
*/
public class CreateUserController {

    private final UserValidator validator;
    private final UserEmailService emailService;
    private final AuditLogService auditService;

    public CreateUserController(UserValidator validator,
                                UserEmailService emailService,
                                AuditLogService auditService) {
        this.validator = validator;
        this.emailService = emailService;
        this.auditService = auditService;
    }

    public void createUser(String name, String email, String password,
                           String phoneNumber, String roleStr) {
        try {
            // 1. Validate input
            validator.validate(name, email, password, phoneNumber, roleStr);

            // 2. Parse role
            UserRole role = UserRole.fromString(roleStr);

            // 3. Create user (userID will be assigned in repository)
            User user = new User(0, name, email, password, phoneNumber, role);
            UserRepository.addUser(user);  // assign userID here

            // 4. Send welcome email
            emailService.sendWelcomeEmail(user);

            // 5. Log creation
            auditService.logUserCreation(user);

            System.out.println("User created: " + user.getName() + " (ID: " + user.getUserID() + ")");

        } catch (IllegalArgumentException e) {
            System.err.println("Failed to create user: " + e.getMessage());
        }
    }
}
