package com.hustict.aims.service;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ReservationItemRepository;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.handler.ProductHandler;
import com.hustict.aims.service.handler.ProductHandlerRegistry;
import com.hustict.aims.service.validation.ProductValidator;
import com.hustict.aims.service.validation.ProductValidatorRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductService {
        private final ProductHandlerRegistry handlerReg;
        private final ProductValidatorRegistry validatorReg;
        private final ProductRepository productRepo;
        private final ProductActionService actionService;
        private final ReservationItemRepository reservationItemRepository; 

        public ProductService(ProductHandlerRegistry handlerReg,
                                ProductValidatorRegistry validatorReg,
                                ProductRepository productRepo,
                                ProductActionService actionService,
                                ReservationItemRepository reservationItemRepository) {
                this.handlerReg = handlerReg;
                this.validatorReg = validatorReg;
                this.productRepo = productRepo;
                this.actionService = actionService;
                this.reservationItemRepository = reservationItemRepository; // gán vào trường
        }

        public ProductDetailDTO createProduct(Map<String, Object> data) {
                String type = (String) data.get("category");
                if (type == null) throw new IllegalArgumentException("Missing category field");

                ProductHandler handler = handlerReg.getHandler(type)
                        .orElseThrow(() -> new IllegalArgumentException("Unsupported category: " + type));

                Product product = handler.toEntity(data);

                ProductValidator<?> validator = validatorReg.getValidator(type)
                        .orElseThrow(() -> new IllegalArgumentException("No validator for: " + type));

                List<String> errors = validator.validate(product);
                if (!errors.isEmpty()) throw new IllegalArgumentException(errors.toString());

                return handler.saveAndReturnDTO(product);
        }

        public ProductDetailDTO updateProduct(Long id, Map<String, Object> data, Long userId) {
                // Validate action limits before processing
                actionService.validateProductUpdate(userId, id, data);

                Product existing = productRepo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + id));

                String type = existing.getCategory();

                ProductHandler handler = handlerReg.getHandler(type)
                        .orElseThrow(() -> new IllegalArgumentException("Unsupported category: " + type));

                Product updated = handler.updateEntity(existing, data);

                ProductValidator<?> validator = validatorReg.getValidator(type)
                        .orElseThrow(() -> new IllegalArgumentException("No validator for: " + type));

                List<String> errors = validator.validate(updated);
                if (!errors.isEmpty()) throw new IllegalArgumentException(errors.toString());

                ProductDetailDTO result = handler.saveAndReturnDTO(updated);

                // Log the action after successful update
                ActionType actionType = data.containsKey("currentPrice") ? ActionType.UPDATE_PRICE : ActionType.UPDATE;
                actionService.logProductAction(userId, id, actionType);

                return result;
        }

        public ProductDetailDTO viewProduct(Long id) {
                Product p = productRepo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + id));

                ProductHandler handler = handlerReg.getHandler(p.getCategory())
                        .orElseThrow(() -> new IllegalArgumentException("No handler for category: " + p.getCategory()));

                return handler.saveAndReturnDTO(p); 
        }

        public boolean isProductAvailable(Long id, int requiredQty) {
                Product product = productRepo.findById(id).orElseThrow(() -> new NoSuchElementException(
                                "Product not found with ID: " + id));
                if (requiredQty <= 0) {
                        throw new IllegalArgumentException("Required quantity must be greater than 0");
                }

                int reservedQty = reservationItemRepository.getReservedQuantityByProductId(id);
                //int availQty = product.getQuantity();
                
                int availQty = product.getQuantity() - reservedQty;
                return availQty >= requiredQty;
        }
}