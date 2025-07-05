package com.hustict.aims.service.product;

import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.factory.ProductFactoryProvider;
import com.hustict.aims.service.factory.ProductFactory;
import com.hustict.aims.exception.ProductNotFoundException;
import com.hustict.aims.exception.ProductTypeException;
import com.hustict.aims.exception.ProductOperationException;
import com.hustict.aims.service.rules.ActionTypeStrategy;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {
        private final ImageService imageService;
        private final ProductFactoryProvider factoryProvider;
        private final ProductActionService actionService;
        private final ProductRepository productRepo;
        private final ActionTypeStrategy actionTypeStrategy;

        public ProductService(ProductFactoryProvider factoryProvider,
                                ProductRepository productRepo,
                                ProductActionService actionService,
                                ImageService imageService,
                                ActionTypeStrategy actionTypeStrategy) {
                this.factoryProvider = factoryProvider;
                this.productRepo = productRepo;
                this.actionService = actionService;
                this.imageService = imageService;
                this.actionTypeStrategy = actionTypeStrategy;
        }

        private void uploadImage(MultipartFile image, ProductModifyRequest request) {
                if (image != null && !image.isEmpty() && request.getProduct() != null) {
                        String imageUrl = imageService.upload(image);
                        request.getProduct().setImageUrl(imageUrl);
                }
        }

        public ProductDTO createProduct(ProductModifyRequest request, MultipartFile image) {
                if (request.getProduct() == null) {
                        throw new ProductOperationException("create", "Product data is required");
                }
                
                ProductDTO productDTO = request.getProduct();
                String category = productDTO.getCategory();
                
                if (category == null) {
                        throw new ProductOperationException("create", "Missing category field");
                }

                uploadImage(image, request);

                ProductFactory factory = factoryProvider.getFactory(category)
                        .orElseThrow(() -> new ProductTypeException(category));

                return factory.createProduct(request);
        }

        public ProductDTO updateProduct(Long id, ProductModifyRequest request, MultipartFile image, Long userId) {
                if (request.getProduct() == null) {
                        throw new ProductOperationException("update", "Product data is required");
                }
                
                actionService.validateUpdate(userId, id, request);

                Product existing = productRepo.findByIdNotDeleted(id).orElseThrow(() -> new ProductNotFoundException(id));

                uploadImage(image, request);

                String category = existing.getCategory();

                ProductFactory factory = factoryProvider.getFactory(category)
                        .orElseThrow(() -> new ProductTypeException(category));

                ActionType actionType = actionTypeStrategy.determine(existing, request);
                ProductDTO result = factory.updateProduct(existing, request);
                actionService.logProductAction(userId, id, actionType);

                return result;
        }

        public ProductDTO viewProduct(Long id) {
                Product p = productRepo.findByIdNotDeleted(id)
                        .orElseThrow(() -> new ProductNotFoundException(id));

                String category = p.getCategory();

                ProductFactory factory = factoryProvider.getFactory(category)
                        .orElseThrow(() -> new ProductTypeException(category));

                return factory.viewProduct(p);
        }

        @Transactional
        public void deleteProducts(Long userId, List<Long> productIds) {
                if (productIds == null || productIds.isEmpty()) {
                        throw new ProductOperationException("delete", "Product IDs cannot be empty!");
                }

                if (productIds.size() > 10) {
                        throw new ProductOperationException("delete", "Cannot delete more than 10 products at once!");
                }

                actionService.validateDelete(userId, productIds);

                List<Product> productsToDelete = productRepo.findAllByIdNotDeleted(productIds);
                if (productsToDelete.size() != productIds.size()) {
                        throw new ProductNotFoundException("One or more products not found or already deleted!");
                }

                for (Long productId : productIds) {
                        actionService.logProductAction(userId, productId, ActionType.DELETE);
                        productRepo.softDeleteById(productId);
                }
        }
}