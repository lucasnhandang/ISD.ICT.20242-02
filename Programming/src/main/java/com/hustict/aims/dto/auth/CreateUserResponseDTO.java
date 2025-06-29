package com.hustict.aims.dto.auth;

public class CreateUserResponseDTO {
    private boolean success;
    private String message;
    private UserInfoDTO userInfo;

    public CreateUserResponseDTO() {}

    public CreateUserResponseDTO(boolean success, String message, UserInfoDTO userInfo) {
        this.success = success;
        this.message = message;
        this.userInfo = userInfo;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserInfoDTO getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfoDTO userInfo) { this.userInfo = userInfo; }

    @Override
    public String toString() {
        return "CreateUserResponseDTO{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
} 