package com.hustict.aims.utils;

import com.hustict.aims.model.user.User;

public class AuthUtils {
    public static boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }

    public static boolean isAdmin(User user) {
        return hasRole(user, "ADMIN");
    }

    public static boolean isProductManager(User user) {
        return hasRole(user, "PRODUCT_MANAGER");
    }

    public static String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        return null;
    }
} 