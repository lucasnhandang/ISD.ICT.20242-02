package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;

import org.springframework.beans.BeanUtils;

import java.util.Map;

public abstract class AbstractProductMapper<T extends Product, D extends ProductDetailDTO> implements ProductMapper<T, D> {

    @Override
    public final T fromMap(Map<String, Object> data) {
        T product = createProduct();
        mapCommonFields(product, data);
        mapSpecFields(product, data);
        return product;
    }

    public final T updateFromMap(T existing, Map<String, Object> data) {
        updateCommonFields(existing, data);
        updateSpecFields(existing, data);
        return existing;
    }


    @Override
    public final D toDetailDTO(T product) {
        D dto = createDetailDTO();
        BeanUtils.copyProperties(product, dto);
        // Manually set category since setCategory is protected in Product
        dto.setCategory(product.getCategory());
        return dto;
    }

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

    protected abstract T createProduct();

    protected abstract void mapSpecFields(T product, Map<String, Object> data);

    protected abstract void updateSpecFields(T product, Map<String, Object> data);

    protected abstract D createDetailDTO();
}