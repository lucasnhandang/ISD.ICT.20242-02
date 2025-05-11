package com.hustict.aims.service.validator;

import com.hustict.aims.model.DeliveryInfo;
import com.hustict.aims.model.Product;

import java.util.List;

/**
 * Cohesion Level: Functional Cohesion
 * - All methods contribute to a single, clearly defined task: determining if a rush order is allowed
 * - No unrelated logic exists
 * SRP: Does NOT violate SRP
 * - Only handles eligibility checking logic, no unrelated concerns like payment or UI.
 * Suggested improvement: Consider delegating the address check to a separate class
 * (e.g., LocationValidator) if more regions are added later.
 */

public class RushOrderService {

    private static final String HANOI_INNER_CITY = "HN";

    public boolean isRushOrderEligible(DeliveryInfo deliveryInfo, List<Product> products) {
        if (deliveryInfo == null) {
            throw new IllegalArgumentException("DeliveryInfo cannot be null");
        }
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null");
        }

        if (!isAddressInHanoiInnerCity(deliveryInfo)) {
            return false;
        }

        return hasRushOrderEligibleProduct(products);
    }

    private boolean isAddressInHanoiInnerCity(DeliveryInfo deliveryInfo) {
        String province = deliveryInfo.getProvince();
        return String.valueOf(province).equals(HANOI_INNER_CITY);
    }

    private boolean hasRushOrderEligibleProduct(List<Product> products) {
        if (products.isEmpty()) {
            return false;
        }

        for (Product product : products) {
            if (product.getRushOrderSupported()) {
                return true;
            }
        }

        return false;
    }
}