package com.hustict.aims.service;

import com.hustict.aims.model.User;
import com.hustict.aims.model.User.UserRole;
import com.hustict.aims.repository.UserRepository;

import java.util.regex.Pattern;
/*
Functional. All methods are focused on validating different information of a user.
It only changes if the validation criteria or rules are modified.
*/
public class UserValidator {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^\\d{10}$");

    public void validate(String name, String email, String password,
                         String phoneNumber, String roleStr) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be empty.");
        }

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (UserRepository.emailExists(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (password == null || password.length() > 32) {
            throw new IllegalArgumentException("Password must be <= 32 characters.");
        }

        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }

        if (UserRepository.phoneExists(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        try {
            UserRole.fromString(roleStr); // Sẽ throw nếu không hợp lệ
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role. Only admin or productManager allowed.");
        }
    }
}
