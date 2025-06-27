package com.hustict.aims.dto.payment;

public class VnPayIpnResponseDTO {
    private String RspCode;
    private String Message;

    public VnPayIpnResponseDTO() {}
    public VnPayIpnResponseDTO(String rspCode, String message) {
        this.RspCode = rspCode;
        this.Message = message;
    }
    public String getRspCode() { return RspCode; }
    public void setRspCode(String rspCode) { RspCode = rspCode; }
    public String getMessage() { return Message; }
    public void setMessage(String message) { Message = message; }
} 