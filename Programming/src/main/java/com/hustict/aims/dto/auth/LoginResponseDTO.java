package com.hustict.aims.dto.auth;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private String token;
    private UserInfoDTO userInfo;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message, String token, UserInfoDTO userInfo) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.userInfo = userInfo;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserInfoDTO getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfoDTO userInfo) { this.userInfo = userInfo; }
}