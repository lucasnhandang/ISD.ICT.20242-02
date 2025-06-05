package com.hustict.aims.model;

public class DeliveryInfo {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String deliveryProvince;
    private String shippingInstruction;

    public DeliveryInfo() {}

    public DeliveryInfo(String name, String email, String phoneNumber, String address, String deliveryProvince, String shippingInstruction) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.deliveryProvince = deliveryProvince;
        this.shippingInstruction = shippingInstruction;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDeliveryProvince() { return deliveryProvince; }
    public void setDeliveryProvince(String deliveryProvince) { this.deliveryProvince = deliveryProvince; }
    public String getShippingInstruction() { return shippingInstruction; }
    public void setShippingInstruction(String shippingInstruction) { this.shippingInstruction = shippingInstruction; }
} 