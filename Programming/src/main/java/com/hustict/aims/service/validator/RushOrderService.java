package com.hustict.aims.service.validator;

import com.hustict.aims.model.DeliveryInfo;
import com.hustict.aims.model.Product;

import java.util.List;

public class RushOrderService {

    private static final String HANOI_INNER_CITY = "HN";

    public boolean isRushOrderEligible(DeliveryInfo deliveryInfo, List<Product> products) {
        // Input validation - defensive programming
        if (deliveryInfo == null) {
            throw new IllegalArgumentException("DeliveryInfo cannot be null");
        }
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null");
        }

        // Check if address is in Hanoi inner city
        if (!isAddressInHanoiInnerCity(deliveryInfo)) {
            return false;
        }

        // Check if at least one product supports rush order
        return hasRushOrderEligibleProduct(products);
    }

    private boolean isAddressInHanoiInnerCity(DeliveryInfo deliveryInfo) {
        String province = deliveryInfo.getProvince();
        // Convert province to string and compare
        return String.valueOf(province).equals(HANOI_INNER_CITY);
    }

    private boolean hasRushOrderEligibleProduct(List<Product> products) {
        // If product list is empty, no products are eligible
        if (products.isEmpty()) {
            return false;
        }

        // Check if at least one product supports rush order
        for (Product product : products) {
            if (product.getRushOrderSupported()) {
                return true;
            }
        }

        return false;
    }
}