package com.hustict.aims.service.shippingFeeCalculator;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import java.util.List;

@Service("rushShippingFee")
public class RushShippingFee implements ShippingFeeCalculator {

    @Override
    public int calculateShippingFee(String province, List<CartItemRequestDTO> productList, int totalPrice) {
        double totalWeight = 0;
        int totalItem = 0;
        //List<CartItemRequestDTO> productList= rushCart.getProductList();

        // Tính tổng khối lượng đơn hàng
        for (CartItemRequestDTO item : productList) {
            totalWeight += item.getWeight() * item.getQuantity();
            totalItem += 1;
        }

        double shippingFee;
        // Tính phí ship theo tỉnh và khối lượng
        if (province.equalsIgnoreCase("Hanoi") 
         || province.equalsIgnoreCase("HoChiMinhCity")) {
            shippingFee = (totalWeight <= 3)
                ? 22000
                : 22000 + Math.ceil((totalWeight - 3) / 0.5) * 2500;
        } else {
            shippingFee = (totalWeight <= 0.5)
                ? 30000
                : 30000 + Math.ceil((totalWeight - 0.5) / 0.5) * 2500;
        }

        // Phí rush: 10.000 cho mỗi sản phẩm trong giỏ hàng
        int rushFee = 10000 * totalItem;

        // Tổng phí = phí ship + phí rush
        return (int) Math.round(shippingFee + rushFee);
    }
}