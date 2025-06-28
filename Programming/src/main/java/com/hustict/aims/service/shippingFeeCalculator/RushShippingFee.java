package com.hustict.aims.service.shippingFeeCalculator;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import java.text.Normalizer;

@Service("rushShippingFee")
public class RushShippingFee implements ShippingFeeCalculator {

    @Override
    public int calculateShippingFee(DeliveryFormDTO deliveryInfo, CartRequestDTO rushCart) {
        double totalWeight = 0;

        // Tính tổng khối lượng đơn hàng
        for (CartItemRequestDTO item : rushCart.getProductList()) {
            totalWeight += item.getWeight() * item.getQuantity();
        }

        double shippingFee;
        // Sử dụng so sánh mềm cho tỉnh/thành
        if (isHanoi(deliveryInfo.getDeliveryProvince())) {
            shippingFee = (totalWeight <= 3)
                ? 22000
                : 22000 + Math.ceil((totalWeight - 3) / 0.5) * 2500;
        } else {
            shippingFee = (totalWeight <= 0.5)
                ? 30000
                : 30000 + Math.ceil((totalWeight - 0.5) / 0.5) * 2500;
        }

        // Phí rush: 10.000 cho mỗi sản phẩm trong giỏ hàng
        int rushFee = 10000 * rushCart.getProductList().size();

        // Tổng phí = phí ship + phí rush
        return (int) Math.round(shippingFee + rushFee);
    }

    private boolean isHanoi(String province) {
        if (province == null) return false;
        String normalized = Normalizer.normalize(province, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "") // bỏ dấu tiếng Việt
            .replaceAll("[^a-zA-Z ]", "") // bỏ ký tự đặc biệt
            .toLowerCase()
            .replace("tp", "")
            .replace("thanh pho", "")
            .replace("thanhphố", "")
            .replace(" ", "");
        return normalized.contains("hanoi");
    }
}
