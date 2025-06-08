package com.hustict.aims.service;

import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.model.user.ProductChecklog;
import com.hustict.aims.repository.ProductChecklogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ProductActionService {
    private final ProductChecklogRepository checklogRepo;

    public ProductActionService(ProductChecklogRepository checklogRepo) {
        this.checklogRepo = checklogRepo;
    }

    public void validateProductUpdate(Long userId, Long productId, Map<String, Object> data) {
        LocalDate today = LocalDate.now();
        
        // Price update limit (max 2 times per day per product)
        boolean isPriceUpdate = data.containsKey("currentPrice");
        if (isPriceUpdate) {
            int priceUpdateCount = checklogRepo.countProductActions(userId, productId, ActionType.UPDATE_PRICE, today);
            if (priceUpdateCount >= 2) {
                throw new IllegalArgumentException(
                    String.format("Cannot update product price more than twice per day. Current count: %d", priceUpdateCount)
                );
            }
        }

        // Rule 2: Total update limit (max 30 updates per day)
        List<ActionType> updateActions = List.of(ActionType.UPDATE, ActionType.UPDATE_PRICE);
        int totalUpdates = checklogRepo.countUserActions(userId, updateActions, today);
        if (totalUpdates >= 30) {
            throw new IllegalArgumentException(
                String.format("Cannot update more than 30 products per day for security reasons. Current count: %d", totalUpdates)
            );
        }
    }

    /**
     * Log product action after successful operation
     */
    public void logProductAction(Long userId, Long productId, ActionType actionType) {
        ProductChecklog log = new ProductChecklog(userId, productId, actionType, LocalDateTime.now());
        checklogRepo.save(log);
    }

    /**
     * Get number of price updates for a product today
     */
    public int getPriceUpdateCount(Long userId, Long productId) {
        return checklogRepo.countProductActions(userId, productId, ActionType.UPDATE_PRICE, LocalDate.now());
    }

    /**
     * Get total updates count for user today
     */
    public int getTotalUpdatesCount(Long userId) {
        List<ActionType> updateActions = List.of(ActionType.UPDATE, ActionType.UPDATE_PRICE);
        return checklogRepo.countUserActions(userId, updateActions, LocalDate.now());
    }

    /**
     * Get detailed limits info for user
     */
    public Map<String, Object> getUserLimitsInfo(Long userId) {
        int totalUpdates = getTotalUpdatesCount(userId);
        return Map.of(
            "totalUpdatesToday", totalUpdates,
            "maxUpdatesPerDay", 30,
            "remainingUpdates", Math.max(0, 30 - totalUpdates)
        );
    }

    /**
     * Get detailed limits info for specific product
     */
    public Map<String, Object> getProductLimitsInfo(Long userId, Long productId) {
        int priceUpdates = getPriceUpdateCount(userId, productId);
        return Map.of(
            "priceUpdatesToday", priceUpdates,
            "maxPriceUpdatesPerDay", 2,
            "remainingPriceUpdates", Math.max(0, 2 - priceUpdates)
        );
    }
}