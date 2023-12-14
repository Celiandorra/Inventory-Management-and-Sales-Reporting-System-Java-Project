import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Product> products;

    public InventoryManager() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void updateProduct(Product product) {
        Product existingProduct = getProductById(product.getProductId());
        if (existingProduct != null) {
            existingProduct.setProductName(product.getProductName());
            existingProduct.setQuantityInStock(product.getQuantityInStock());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCategory(product.getCategory());
        }
    }

    public void removeProduct(int productId) {
        products.removeIf(product -> product.getProductId() == productId);
    }

    public Product getProductById(int productId) {
        return products.stream()
                .filter(product -> product.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getProductName().equals(productName))
                .findFirst()
                .orElse(null);
    }
}
