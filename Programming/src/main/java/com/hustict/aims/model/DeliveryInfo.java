package com.hustict.aims.model;

/**
 * Cohesion Level: Communicational Cohesion
 * - All attributes and methods revolve around storing or displaying delivery-related data.
 * - Methods act on the same set of data (delivery ID, address, name, phone, etc.).
 * SRP: Does NOT violate SRP
 * - Only handles eligibility checking logic, no unrelated concerns like payment or UI.
 * Suggested improvement: Consider delegating the address check to a separate class
 * (e.g., LocationValidator) if more regions are added later.
 */

public class DeliveryInfo {
    private int deliveryID;
    private char[] customerName;
    private String phoneNumber;
    private String detailAddress;
    private String province;
    private char[] email;
    private String shippingInstruction;

    public DeliveryInfo() {
    }

    public DeliveryInfo(int deliveryID, char[] customerName, String phoneNumber,
                        String province, String detailAddress, char[] email,
                        String shippingInstruction) {
        this.deliveryID = deliveryID;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.detailAddress = detailAddress;
        this.email = email;
        this.shippingInstruction = shippingInstruction;
    }

    public String getAddressInfo(String detailedAddress, char province) {
        return detailedAddress + ", " + province;
    }

    public void getter() {
        System.out.println("Delivery ID: " + this.deliveryID);
        System.out.println("Customer Name: " + new String(this.customerName));
        System.out.println("Phone Number: " + this.phoneNumber);
        System.out.println("Province: " + this.province);
        System.out.println("Detailed Address: " + this.detailAddress);
        System.out.println("Email: " + new String(this.email));
        System.out.println("Shipping Instruction: " + this.shippingInstruction);
    }

    public void setter(int deliveryID, char[] customerName, String phoneNumber,
                       String province, String detailedAddress, char[] email,
                       String shippingInstruction) {
        this.deliveryID = deliveryID;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.detailAddress = detailedAddress;
        this.email = email;
        this.shippingInstruction = shippingInstruction;
    }

    // Các phương thức getter và setter riêng lẻ
    public int getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public char[] getCustomerName() {
        return customerName;
    }

    public void setCustomerName(char[] customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public char[] getEmail() {
        return email;
    }

    public void setEmail(char[] email) {
        this.email = email;
    }

    public String getShippingInstruction() {
        return shippingInstruction;
    }

    public void setShippingInstruction(String shippingInstruction) {
        this.shippingInstruction = shippingInstruction;
    }
}
