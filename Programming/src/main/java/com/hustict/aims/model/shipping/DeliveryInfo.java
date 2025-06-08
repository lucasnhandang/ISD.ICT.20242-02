package com.hustict.aims.model.shipping;

import jakarta.persistence.*;

@Entity
@Table(name = "deliveryinfo")
public class DeliveryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryinfoid")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "phonenumber")
    private String phoneNumber;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "shippinginstruction")
    private String shippingInstruction;

    public DeliveryInfo() {}

    public DeliveryInfo(String name, String address, String phoneNumber, String email, String province, String shippingInstruction) {
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

} 