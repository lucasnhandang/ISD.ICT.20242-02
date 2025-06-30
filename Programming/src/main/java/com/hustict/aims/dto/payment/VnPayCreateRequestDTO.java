package com.hustict.aims.dto.payment;

public class VnPayCreateRequestDTO {
    private int amount;
    private String orderInfo;
    private String bankCode;
    private String orderType;
 
    private String language;
    private String billingMobile;
    private String billingEmail;
    
    private String billingFullName;
    
    private String billingAddress;
    private String billingCity;
    private String billingCountry;
    private String billingState;
    private String invMobile;
    private String invEmail;
    private String invCustomer;
    private String invAddress;
    private String invCompany;
    private String invTaxcode;
    private String invType;

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getOrderInfo() { return orderInfo; }
    public void setOrderInfo(String orderInfo) { this.orderInfo = orderInfo; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getBillingFullName() { return billingFullName; }
    public void setBillingFullName(String billingFullName) { this.billingFullName = billingFullName; }
    public String getBillingMobile() { return billingMobile; }
    public void setBillingMobile(String billingMobile) { this.billingMobile = billingMobile; }
    public String getBillingEmail() { return billingEmail; }
    public void setBillingEmail(String billingEmail) { this.billingEmail = billingEmail; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getBillingCity() { return billingCity; }
    public void setBillingCity(String billingCity) { this.billingCity = billingCity; }
    public String getBillingCountry() { return billingCountry; }
    public void setBillingCountry(String billingCountry) { this.billingCountry = billingCountry; }
    public String getBillingState() { return billingState; }
    public void setBillingState(String billingState) { this.billingState = billingState; }
    public String getInvMobile() { return invMobile; }
    public void setInvMobile(String invMobile) { this.invMobile = invMobile; }
    public String getInvEmail() { return invEmail; }
    public void setInvEmail(String invEmail) { this.invEmail = invEmail; }
    public String getInvCustomer() { return invCustomer; }
    public void setInvCustomer(String invCustomer) { this.invCustomer = invCustomer; }
    public String getInvAddress() { return invAddress; }
    public void setInvAddress(String invAddress) { this.invAddress = invAddress; }
    public String getInvCompany() { return invCompany; }
    public void setInvCompany(String invCompany) { this.invCompany = invCompany; }
    public String getInvTaxcode() { return invTaxcode; }
    public void setInvTaxcode(String invTaxcode) { this.invTaxcode = invTaxcode; }
    public String getInvType() { return invType; }
    public void setInvType(String invType) { this.invType = invType; }
} 