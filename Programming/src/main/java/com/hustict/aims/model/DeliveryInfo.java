package com.hustict.aims.model;

public class DeliveryInfo {
    private int deliveryID;
    private char[] customerName;
    private String phoneNumber;
    private String detailAddress;
    private String province;
    private char[] email;
    private String shippingInstruction;

    /**
     * Constructor mặc định
     */
    public DeliveryInfo() {
    }

    /**
     * Constructor với đầy đủ tham số
     */
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

    /**
     * Phương thức lấy thông tin địa chỉ đầy đủ
     * @param detailedAddress địa chỉ chi tiết
     * @param province tỉnh/thành phố
     * @return chuỗi địa chỉ đầy đủ
     */
    public String getAddressInfo(String detailedAddress, char province) {
        return detailedAddress + ", " + province;
    }

    /**
     * Phương thức getter trả về tất cả thông tin
     * @return không có giá trị trả về
     */
    public void getter() {
        System.out.println("Delivery ID: " + this.deliveryID);
        System.out.println("Customer Name: " + new String(this.customerName));
        System.out.println("Phone Number: " + this.phoneNumber);
        System.out.println("Province: " + this.province);
        System.out.println("Detailed Address: " + this.detailAddress);
        System.out.println("Email: " + new String(this.email));
        System.out.println("Shipping Instruction: " + this.shippingInstruction);
    }

    /**
     * Phương thức setter để thiết lập tất cả thông tin
     * @param deliveryID ID giao hàng
     * @param customerName tên khách hàng
     * @param phoneNumber số điện thoại
     * @param province tỉnh/thành phố
     * @param detailedAddress địa chỉ chi tiết
     * @param email địa chỉ email
     * @param shippingInstruction hướng dẫn giao hàng
     */
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
