package com.hustict.aims.service.product;

import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.ReservationItemRepository;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class InventoryService {
    private final ProductRepository productRepo;
    private final ReservationItemRepository reservationItemRepository;
    private final MessageService messageService;

    public InventoryService(ProductRepository productRepo,
                            ReservationItemRepository reservationItemRepository,
                            MessageService messageService) {
        this.productRepo = productRepo;
        this.reservationItemRepository = reservationItemRepository;
        this.messageService = messageService;
    }

    public int getAvailableQuantity(Long id) {
        Product product = productRepo.findByIdNotDeleted(id)
                .orElseThrow(() -> new NoSuchElementException(
                        messageService.getProductNotFound() + " with ID: " + id));

        int reservedQty = reservationItemRepository.getReservedQuantityByProductId(id);
        return Math.max(0, product.getQuantity() - reservedQty);
    }

    public boolean isAvailable(Long id, int requiredQty) {
        if (requiredQty <= 0) {
            throw new IllegalArgumentException(messageService.getInvalidInput() + ": Required quantity must be greater than 0");
        }
        return getAvailableQuantity(id) >= requiredQty;
    }

    public void decreaseQuantity(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(messageService.getInvalidInput() + ": Decrease quantity must be greater than 0.");
        }

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + id + " not found."));

        int currentQuantity = product.getQuantity();
        if (currentQuantity < quantity) {
            throw new IllegalStateException(messageService.getInvalidInput() + ": Not enough quantity to decrease.");
        }

        product.setQuantity(currentQuantity - quantity);
        productRepo.save(product);
    }

}
