public class Product {
    private static int nextProductId = 1;

    private int productId;
    private String productName;
    private int quantityInStock;
    private double price;
    private String category;

    public Product(String productName, int quantityInStock, double price, String category) {
        this.productId = nextProductId++;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
