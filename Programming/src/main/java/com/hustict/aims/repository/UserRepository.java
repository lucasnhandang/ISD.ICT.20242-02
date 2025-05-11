package com.hustict.aims.repository;

import com.hustict.aims.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static List<User> users = new ArrayList<>();
    private static int currentId = 1000;

    public static void addUser(User user) {
        user.setUserID(generateUserID());
        users.add(user);
    }

    private static int generateUserID() {
        return currentId++;
    }

    public static boolean emailExists(String email) {
        return users.stream()
            .filter(u -> u.getUserRoleAsString().equalsIgnoreCase("admin") ||
                         u.getUserRoleAsString().equalsIgnoreCase("productManager"))
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public static boolean phoneExists(String phoneNumber) {
        return users.stream()
            .filter(u -> u.getUserRoleAsString().equalsIgnoreCase("admin") ||
                         u.getUserRoleAsString().equalsIgnoreCase("productManager"))
            .anyMatch(u -> u.getPhoneNumber().equals(phoneNumber));
    }

    public static List<User> getAllUsers() {
        return users;
    }
}
