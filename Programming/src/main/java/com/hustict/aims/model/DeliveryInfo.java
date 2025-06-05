package com.hustict.aims.model;

import jakarta.persistence.*;

@Entity
public class DeliveryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String province;
    private String shippingInstruction;

    public DeliveryInfo() {}

    public DeliveryInfo(Long id, String name, String address, String phoneNumber, String email, String province, String shippingInstruction) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.province = province;
        this.shippingInstruction = shippingInstruction;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getShippingInstruction() { return shippingInstruction; }
    public void setShippingInstruction(String shippingInstruction) { this.shippingInstruction = shippingInstruction; }

    public DeliveryInfo(String name, String address, String phoneNumber, String email, String province, String shippingInstruction) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.province = province;
        this.shippingInstruction = shippingInstruction;
    }
} 