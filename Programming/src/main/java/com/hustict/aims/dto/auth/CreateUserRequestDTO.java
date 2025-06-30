package com.hustict.aims.dto.auth;

import java.util.List;

public class CreateUserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private List<String> roles;

    public CreateUserRequestDTO() {}

    public CreateUserRequestDTO(String name, String email, String password, String phoneNumber, List<String> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    @Override
    public String toString() {
        return "CreateUserRequestDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='[HIDDEN]'" +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roles=" + roles +
                '}';
    }
} 