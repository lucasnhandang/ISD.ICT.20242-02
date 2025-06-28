package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.RushOrderEligibilityResponseDTO;
import com.hustict.aims.service.placeRushOrder.CartSplitter;
import com.hustict.aims.service.placeRushOrder.CartSplitter.Pair;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.exception.RushOrderException;
import com.hustict.aims.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.text.Normalizer;

@Service
public class RushOrderEligibilityService {
    
    private final CartSplitter cartSplitter;
    private final ProductRepository productRepository;
    private final MessageService messageService;

    @Autowired
    public RushOrderEligibilityService(CartSplitter cartSplitter, 
                                     ProductRepository productRepository,
                                     MessageService messageService) {
        this.cartSplitter = cartSplitter;
        this.productRepository = productRepository;
        this.messageService = messageService;
    }

    public RushOrderEligibilityResponseDTO checkEligibility(CartRequestDTO cart, DeliveryFormDTO deliveryInfo) {
        // Kiểm tra địa chỉ có hỗ trợ rush order không
        if (!isAddressEligibleForRushOrder(deliveryInfo)) {
            throw new RushOrderException(
                "Delivery address does not support rush order delivery. Rush order is only available for addresses within Hanoi inner city.",
                "ADDRESS_NOT_ELIGIBLE",
                null
            );
        }

        // Chia sản phẩm thành rush và normal
        Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> split = 
            cartSplitter.splitRushAndNormal(cart, productRepository);
        List<CartItemRequestDTO> rushItems = split.first;
        List<CartItemRequestDTO> normalItems = split.second;

        // Kiểm tra có sản phẩm nào hợp lệ rush order không
        if (rushItems == null || rushItems.isEmpty()) {
            throw new RushOrderException(
                messageService.getRushOrderNoEligibleProducts(),
                "NO_ELIGIBLE_PRODUCTS",
                null
            );
        }

        // Tạo response
        RushOrderEligibilityResponseDTO response = new RushOrderEligibilityResponseDTO();
        response.setEligible(true);
        response.setRushItems(rushItems);
        response.setNormalItems(normalItems);
        response.setMessage("Rush order is eligible for your cart and delivery address.");

        return response;
    }

    private boolean isAddressEligibleForRushOrder(DeliveryFormDTO deliveryInfo) {
        // So sánh mềm, không phân biệt dấu, hoa thường
        String province = deliveryInfo.getDeliveryProvince();
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