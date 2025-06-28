package com.hustict.aims.service.product;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ReservationItemRepository;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.MessageService;
import com.hustict.aims.service.handler.ProductHandler;
import com.hustict.aims.service.handler.ProductHandlerRegistry;
import com.hustict.aims.service.validation.ProductValidator;
import com.hustict.aims.service.validation.ProductValidatorRegistry;
import com.hustict.aims.service.storage.ImageUploadStorage;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductService {
        private final ImageUploadStorage uploadService;
        private final ProductHandlerRegistry handlerReg;
        private final ProductValidatorRegistry validatorReg;
        private final ProductRepository productRepo;
        private final ProductActionService actionService;
        private final ReservationItemRepository reservationItemRepository;
        private final MessageService messageService;

        @Autowired
        public ProductService(ProductHandlerRegistry handlerReg,
                                ProductValidatorRegistry validatorReg,
                                ProductRepository productRepo,
                                ProductActionService actionService,
                                ImageUploadStorage uploadService,
                                ReservationItemRepository reservationItemRepository,
                                MessageService messageService) {
                this.handlerReg = handlerReg;
                this.validatorReg = validatorReg;
                this.productRepo = productRepo;
                this.actionService = actionService;
                this.uploadService = uploadService;
                this.reservationItemRepository = reservationItemRepository;
                this.messageService = messageService;
        }

        public ProductDetailDTO createProduct(Map<String, Object> data, MultipartFile image) {
                String type = (String) data.get("category");
                if (type == null) throw new IllegalArgumentException(messageService.getInvalidInput() + ": Missing category field");

                // Upload image if provided
                if (image != null && !image.isEmpty()) {
                        try {
                                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                                String imageUrl = uploadService.upload(image.getBytes(), fileName, image.getContentType());
                                data.put("imageUrl", imageUrl);
                        } catch (Exception e) {
                                throw new RuntimeException("Failed to upload image: " + e.getMessage());
                        }
                }

                ProductHandler handler = handlerReg.getHandler(type)
                        .orElseThrow(() -> new IllegalArgumentException(messageService.getInvalidInput() + ": Unsupported category: " + type));

                Product product = handler.toEntity(data);

                ProductValidator<?> validator = validatorReg.getValidator(type)
                        .orElseThrow(() -> new IllegalArgumentException(messageService.getInvalidInput() + ": No validator for: " + type));

                List<String> errors = validator.validate(product);
                if (!errors.isEmpty()) throw new IllegalArgumentException(messageService.getValidationError() + ": " + errors.toString());

                return handler.saveAndReturnDTO(product);
        }

        public ProductDetailDTO updateProduct(Long id, Map<String, Object> data, MultipartFile image, Long userId) {
                // Validate action limits before processing
                actionService.validateUpdate(userId, id, data);

                Product existing = productRepo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException(messageService.getProductNotFound() + " with ID: " + id));

                // Upload new image if provided
                if (image != null && !image.isEmpty()) {
                        try {
                                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                                String imageUrl = uploadService.upload(image.getBytes(), fileName, image.getContentType());
                                data.put("imageUrl", imageUrl);
                        } catch (Exception e) {
                                throw new RuntimeException("Failed to upload image: " + e.getMessage());
                        }
                }

                String type = existing.getCategory();

                ProductHandler handler = handlerReg.getHandler(type)
                        .orElseThrow(() -> new IllegalArgumentException(messageService.getInvalidInput() + ": Unsupported category: " + type));

                Product updated = handler.updateEntity(existing, data);

                ProductValidator<?> validator = validatorReg.getValidator(type)
                        .orElseThrow(() -> new IllegalArgumentException(messageService.getInvalidInput() + ": No validator for: " + type));

                List<String> errors = validator.validate(updated);
                if (!errors.isEmpty()) throw new IllegalArgumentException(messageService.getValidationError() + ": " + errors.toString());

                ProductDetailDTO result = handler.saveAndReturnDTO(updated);

                // Log the action after successful update
                ActionType actionType = data.containsKey("currentPrice") ? ActionType.UPDATE_PRICE : ActionType.UPDATE;
                actionService.logProductAction(userId, id, actionType);

                return result;
        }

        public ProductDetailDTO viewProduct(Long id) {
                Product p = productRepo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException(messageService.getProductNotFound() + " with ID: " + id));

                ProductHandler handler = handlerReg.getHandler(p.getCategory())
                        .orElseThrow(() -> new IllegalArgumentException(messageService.getInvalidInput() + ": No handler for category: " + p.getCategory()));

                return handler.saveAndReturnDTO(p);
        }

        @Transactional
        public void deleteProducts (Long id, List<Long> productIds) {
                if (productIds == null || productIds.isEmpty()) {
                        throw new IllegalArgumentException(messageService.getInvalidInput() + ": Product IDs cannot be empty.");
                }

                if (productIds.size() > 10) {
                        throw new IllegalArgumentException("Cannot delete more than 10 products at once.");
                }

                actionService.validateDelete(id, productIds);

                List<Product> productsToDelete = productRepo.findAllById(productIds);
                if (productsToDelete.size() != productIds.size()) {
                        throw new NoSuchElementException("One or more products not found or already deleted.");
                }

                for (Long productId : productIds) {
                        Product p = productRepo.findById(productId)
                                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found."));

                        if (p.isDeleted()) {
                                throw new IllegalStateException("Product with ID " + productId + " has already been deleted.");
                        }

                        actionService.logProductAction(id, productId, ActionType.DELETE);
                        productRepo.softDeleteById(productId);
                }
        }

        public int getAvailableQuantity(Long id) {
                Product product = productRepo.findByIdNotDeleted(id).orElseThrow(() -> new NoSuchElementException(
                                messageService.getProductNotFound() + " with ID: " + id));

                int reservedQty = reservationItemRepository.getReservedQuantityByProductId(id);
                return Math.max(0, product.getQuantity() - reservedQty);
        }

        public boolean isProductAvailable(Long id, int requiredQty) {
                if (requiredQty <= 0) {
                        throw new IllegalArgumentException(messageService.getInvalidInput() + ": Required quantity must be greater than 0");
                }
                return getAvailableQuantity(id) >= requiredQty;
        }

        public void decreaseProductQuantity(Long id, int quantity) {
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