package com.hustict.aims.model;

import java.util.Objects;

public class User {
    private int userID;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private UserRole userRole;

    public enum UserRole {
        ADMIN("admin"),
        PRODUCT_MANAGER("productManager");

        private final String role;

        UserRole(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        public static UserRole fromString(String value) {
            for (UserRole r : values()) {
                if (r.role.equalsIgnoreCase(value)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("Invalid role: " + value);
        }
    }

    public User(int userID, String name, String email, String password, String phoneNumber, UserRole userRole) {
        this.userID = userID;
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.phoneNumber = Objects.requireNonNull(phoneNumber);
        this.userRole = Objects.requireNonNull(userRole);
    }

    // Getters and setters

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getUserRoleAsString() {
        return userRole.getRole();
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
