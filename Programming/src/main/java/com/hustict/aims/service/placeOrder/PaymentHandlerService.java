package com.hustict.aims.service.placeOrder;
import org.springframework.stereotype.Service;

@Service
public class PaymentHandlerService {

    public void handlePaymentSuccess() {
        // Xử lý khi thanh toán thành công, ví dụ như cập nhật trạng thái đơn hàng và thông báo cho người dùng.
        // xóa

        System.out.println("Payment was successful, order placed!");
    }
}