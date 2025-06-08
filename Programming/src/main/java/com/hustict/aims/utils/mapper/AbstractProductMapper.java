package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;

import org.springframework.beans.BeanUtils;

import java.util.Map;

public abstract class AbstractProductMapper<T extends Product, D extends ProductDetailDTO> implements ProductMapper<T, D> {
    /**
     * Template Method cho việc chuyển đổi từ Map sang Entity.
     * Cả thuật toán được định nghĩa ở đây, lớp con không thể override (final).
     */
    @Override
    public final T fromMap(Map<String, Object> data) {
        T product = createProduct();
        mapCommonFields(product, data);
        mapSpecFields(product, data);
        return product;
    }

    /**
     * Template Method cho việc cập nhật existing entity từ Map.
     * Chỉ cập nhật các field có trong data, giữ nguyên các field không có.
     */
    public final T updateFromMap(T existing, Map<String, Object> data) {
        updateCommonFields(existing, data);
        updateSpecFields(existing, data);
        return existing;
    }

    /**
     * Template Method cho việc chuyển đổi từ Entity sang DTO.
     * Cả thuật toán được định nghĩa ở đây, lớp con không thể override (final).
     */
    @Override
    public final D toDetailDTO(T product) {
        D dto = createDetailDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    /**
     * Phương thức private chứa logic mapping các thuộc tính chung của Product.
     */
    private void mapCommonFields(T product, Map<String, Object> data) {
        product.setTitle((String) data.get("title"));
        product.setValue(Integer.parseInt(data.get("value").toString()));
        product.setCurrentPrice(Integer.parseInt(data.get("currentPrice").toString()));
        product.setBarcode((String) data.get("barcode"));
        product.setDescription((String) data.get("description"));
        product.setQuantity(Integer.parseInt(data.get("quantity").toString()));
        product.setEntryDate(DateUtils.parseDate(data.get("entryDate"), "entryDate"));
        product.setDimension((String) data.get("dimension"));
        product.setWeight(Double.parseDouble(data.get("weight").toString()));
        product.setRushOrderSupported((Boolean) data.get("rushOrderSupported"));
        product.setImageUrl((String) data.get("imageUrl"));
    }

    /**
     * Phương thức private chứa logic update các thuộc tính chung của Product.
     * Chỉ update field nếu có trong data.
     */
    private void updateCommonFields(T product, Map<String, Object> data) {
        if (data.containsKey("title")) 
            product.setTitle((String) data.get("title"));
        if (data.containsKey("value")) 
            product.setValue(Integer.parseInt(data.get("value").toString()));
        if (data.containsKey("currentPrice")) 
            product.setCurrentPrice(Integer.parseInt(data.get("currentPrice").toString()));
        if (data.containsKey("barcode")) 
            product.setBarcode((String) data.get("barcode"));
        if (data.containsKey("description")) 
            product.setDescription((String) data.get("description"));
        if (data.containsKey("quantity")) 
            product.setQuantity(Integer.parseInt(data.get("quantity").toString()));
        if (data.containsKey("entryDate")) 
            product.setEntryDate(DateUtils.parseDate(data.get("entryDate"), "entryDate"));
        if (data.containsKey("dimension")) 
            product.setDimension((String) data.get("dimension"));
        if (data.containsKey("weight")) 
            product.setWeight(Double.parseDouble(data.get("weight").toString()));
        if (data.containsKey("rushOrderSupported")) 
            product.setRushOrderSupported((Boolean) data.get("rushOrderSupported"));
        if (data.containsKey("imageUrl")) 
            product.setImageUrl((String) data.get("imageUrl"));
    }

    /**
     * Lớp con phải định nghĩa cách tạo đối tượng Product của riêng nó.
     * Ví dụ: return new Book();
     */
    protected abstract T createProduct();

    /**
     * Lớp con phải định nghĩa cách map các thuộc tính đặc thù của nó.
     */
    protected abstract void mapSpecFields(T product, Map<String, Object> data);

    /**
     * Lớp con phải định nghĩa cách update các thuộc tính đặc thù của nó.
     * Chỉ update field nếu có trong data.
     */
    protected abstract void updateSpecFields(T product, Map<String, Object> data);

    /**
     * Lớp con phải định nghĩa cách tạo đối tượng DTO của riêng nó.
     * Ví dụ: return new BookDetailDTO();
     */
    protected abstract D createDetailDTO();
}