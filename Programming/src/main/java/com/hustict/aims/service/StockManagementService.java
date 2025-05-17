public class StockManagementService {
    public void restock(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive!");
        }
        product.setTotalQuantity(product.getTotalQuantity() + quantity);
    }

    // Optionally add:
    public void deduct(Product product, int quantity) { ... }
    public int getStockLevel(Product product) { ... }
}
