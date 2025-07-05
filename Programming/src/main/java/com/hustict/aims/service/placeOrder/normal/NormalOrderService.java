package com.hustict.aims.service.placeOrder.normal;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.NormalOrderResult;
import com.hustict.aims.dto.cart.CartRequestDTO;

public interface NormalOrderService {
    NormalOrderResult processNormalOrder(CartRequestDTO cart, DeliveryFormDTO deliveryForm);
}
