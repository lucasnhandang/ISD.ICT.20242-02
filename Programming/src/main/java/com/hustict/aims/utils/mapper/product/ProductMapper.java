package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;
import org.springframework.beans.BeanUtils;

import java.util.Map;

public abstract class ProductMapper<T extends Product, D extends ProductDetailDTO> {

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

    public final D toDetailDTO(T product) {
        D dto = createDetailDTO();
        BeanUtils.copyProperties(product, dto);
        dto.setCategory(product.getCategory());
        return dto;
    }

    private void mapCommonFields(T product, Map<String, Object> data) {
        product.setTitle(getString(data, "title"));
        product.setValue(parseInt(data.get("value")));
        product.setCurrentPrice(parseInt(data.get("currentPrice")));
        product.setBarcode(getString(data, "barcode"));
        product.setDescription(getString(data, "description"));
        product.setQuantity(parseInt(data.get("quantity")));
        product.setEntryDate(DateUtils.parseDate(data.get("entryDate"), "entryDate"));
        product.setDimension(getString(data, "dimension"));
        product.setWeight(parseDouble(data.get("weight")));
        product.setRushOrderSupported(parseBoolean(data.get("rushOrderSupported")));
        product.setImageUrl(getString(data, "imageUrl"));
    }

    private void updateCommonFields(T product, Map<String, Object> data) {
        if (data.containsKey("title"))
            product.setTitle(getString(data, "title"));
        if (data.containsKey("value"))
            product.setValue(parseInt(data.get("value")));
        if (data.containsKey("currentPrice"))
            product.setCurrentPrice(parseInt(data.get("currentPrice")));
        if (data.containsKey("barcode"))
            product.setBarcode(getString(data, "barcode"));
        if (data.containsKey("description"))
            product.setDescription(getString(data, "description"));
        if (data.containsKey("quantity"))
            product.setQuantity(parseInt(data.get("quantity")));
        if (data.containsKey("entryDate"))
            product.setEntryDate(DateUtils.parseDate(data.get("entryDate"), "entryDate"));
        if (data.containsKey("dimension"))
            product.setDimension(getString(data, "dimension"));
        if (data.containsKey("weight"))
            product.setWeight(parseDouble(data.get("weight")));
        if (data.containsKey("rushOrderSupported"))
            product.setRushOrderSupported(parseBoolean(data.get("rushOrderSupported")));
        if (data.containsKey("imageUrl"))
            product.setImageUrl(getString(data, "imageUrl"));
    }

    protected int parseInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    protected double parseDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    protected boolean parseBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }

    protected String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }

    protected abstract T createProduct();
    protected abstract D createDetailDTO();
    protected abstract void mapSpecFields(T product, Map<String, Object> data);
    protected abstract void updateSpecFields(T product, Map<String, Object> data);
}
