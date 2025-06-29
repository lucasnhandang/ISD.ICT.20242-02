package com.hustict.aims.service.product;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.MessageService;
import com.hustict.aims.service.factory.ProductFactoryProvider;
import com.hustict.aims.service.factory.ProductFactory;
import com.hustict.aims.exception.ProductNotFoundException;
import com.hustict.aims.exception.ProductTypeException;
import com.hustict.aims.exception.ProductOperationException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
        private final ImageService imageService;
        private final ProductFactoryProvider factoryProvider;
        private final ProductActionService actionService;
        private final ProductRepository productRepo;

        public ProductService(ProductFactoryProvider factoryProvider,
                                ProductRepository productRepo,
                                ProductActionService actionService,
                                ImageService imageService) {
                this.factoryProvider = factoryProvider;
                this.productRepo = productRepo;
                this.actionService = actionService;
                this.imageService = imageService;
        }

        private void uploadImage(MultipartFile image, Map<String, Object> data) {
                if (image != null && !image.isEmpty()) {
                        String imageUrl = imageService.upload(image);
                        data.put("imageUrl", imageUrl);
                }
        }

        public ProductDetailDTO createProduct(Map<String, Object> data, MultipartFile image) {
                String type = (String) data.get("category");
                if (type == null) {
                        throw new ProductOperationException("create", "Missing category field");
                }

                uploadImage(image, data);

                ProductFactory<Product, ProductDetailDTO> factory = factoryProvider.getFactory(type)
                        .orElseThrow(() -> new ProductTypeException(type));

                // Factory handles validate + save + toDTO
                return factory.createProduct(data);
        }

        public ProductDetailDTO updateProduct(Long id, Map<String, Object> data, MultipartFile image, Long userId) {
                // Validate action limits before processing
                actionService.validateUpdate(userId, id, data);

                Product existing = productRepo.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException(id));

                uploadImage(image, data);

                String type = existing.getCategory();
                ProductFactory<Product, ProductDetailDTO> factory = factoryProvider.getFactory(type)
                        .orElseThrow(() -> new ProductTypeException(type));

                // Factory handles validate + save + toDTO
                ProductDetailDTO result = factory.updateProduct(existing, data);

                // Log the action after successful update
                ActionType actionType = data.containsKey("currentPrice") ? ActionType.UPDATE_PRICE : ActionType.UPDATE;
                actionService.logProductAction(userId, id, actionType);

                return result;
        }

        public ProductDetailDTO viewProduct(Long id) {
                Product p = productRepo.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException(id));

                ProductFactory<Product, ProductDetailDTO> factory = factoryProvider.getFactory(p.getCategory())
                        .orElseThrow(() -> new ProductTypeException(p.getCategory()));

                return factory.viewProduct(p);
        }

        @Transactional
        public void deleteProducts(Long userId, List<Long> productIds) {
                if (productIds == null || productIds.isEmpty()) {
                        throw new ProductOperationException("delete", "Product IDs cannot be empty");
                }

                if (productIds.size() > 10) {
                        throw new ProductOperationException("delete", "Cannot delete more than 10 products at once");
                }

                actionService.validateDelete(userId, productIds);

                List<Product> productsToDelete = productRepo.findAllById(productIds);
                if (productsToDelete.size() != productIds.size()) {
                        throw new ProductNotFoundException("One or more products not found or already deleted");
                }

                for (Long productId : productIds) {
                        Product p = productRepo.findById(productId)
                                .orElseThrow(() -> new ProductNotFoundException(productId));

                        if (p.isDeleted()) {
                                throw new ProductOperationException("delete", "Product with ID " + productId + " has already been deleted");
                        }

                        actionService.logProductAction(userId, productId, ActionType.DELETE);
                        productRepo.softDeleteById(productId);
                }
        }
}