package com.hustict.aims.dto.auth;

import java.util.List;

public class UserInfoDTO {
    private Long id;
    private String name;
    private String email;
    private List<String> roles;

    public UserInfoDTO() {}

    public UserInfoDTO(Long id, String name, String email, List<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
} 