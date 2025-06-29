package com.hustict.aims;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHashTest {
    
    @Test
    public void testPasswordHashing() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test password that should work for admin accounts
        String testPassword = "password123";
        String hashedPassword = encoder.encode(testPassword);
        
        System.out.println("Original password: " + testPassword);
        System.out.println("Hashed password: " + hashedPassword);
        
        // Verify the hash works
        boolean matches = encoder.matches(testPassword, hashedPassword);
        System.out.println("Password matches: " + matches);
        assertTrue(matches);
        
        // Test with the existing hash from database
        String existingHash = "$2a$10$UNqb33Qv1vHyuW6qMUuxJuOZmA/zgh8oyc493HInbvuE65vLV2lPm";
        boolean existingMatches = encoder.matches(testPassword, existingHash);
        System.out.println("Existing hash matches: " + existingMatches);
        
        // Test with common passwords
        String[] commonPasswords = {"123456", "password", "admin", "123456789", "qwerty"};
        for (String password : commonPasswords) {
            boolean commonMatches = encoder.matches(password, existingHash);
            System.out.println("Password '" + password + "' matches: " + commonMatches);
        }
    }
} 