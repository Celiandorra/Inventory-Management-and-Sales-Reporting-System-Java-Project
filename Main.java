import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private InventoryManager inventoryManager;
    private List<Transaction> transactions;
    private SalesReportGenerator salesReportGenerator;
    private List<User> users;

    public Main() {
        this.inventoryManager = new InventoryManager();
        this.transactions = new ArrayList<>();
        // this.salesReportGenerator = new SalesReportGenerator(transactions);
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addProduct(Product product) {
        inventoryManager.addProduct(product);
    }

    public void updateProduct(Product product) {
        inventoryManager.updateProduct(product);
    }

    public void removeProduct(int productId) {
        inventoryManager.removeProduct(productId);
    }

    public void recordTransaction(Transaction transaction) {
        transactions.add(transaction);
        updateProductQuantity(transaction.getProduct(), transaction.getQuantity(), transaction.getType());
    }

    private void updateProductQuantity(Product product, int quantity, TransactionType type) {
        int currentQuantity = product.getQuantityInStock();
        if (type == TransactionType.SALE) {
            product.setQuantityInStock(currentQuantity - quantity);
        } else {
            product.setQuantityInStock(currentQuantity + quantity);
        }
    }

    public void displayInventory() {
        List<Product> products = inventoryManager.getAllProducts();
        System.out.println("Inventory:");
        for (Product product : products) {
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Quantity in Stock: " + product.getQuantityInStock());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Category: " + product.getCategory());
            System.out.println("------------------------------");
        }
    }

    public void generateSalesReport() {
        generateSalesReport();
    }

    public boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void saveInventoryToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            List<Product> products = inventoryManager.getAllProducts();
            for (Product product : products) {
                writer.write(
                        product.getProductId() + ","
                                + product.getProductName() + ","
                                + product.getQuantityInStock() + ","
                                + product.getPrice() + ","
                                + product.getCategory() + "\n"
                );
            }
            System.out.println("Inventory saved to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving inventory to file: " + e.getMessage());
        }
    }

    public void loadInventoryFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                // int productId = Integer.parseInt(parts[0]);
                String productName = parts[1];
                int quantityInStock = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[3]);
                String category = parts[4];
                Product product = new Product(productName, quantityInStock, price, category);
                inventoryManager.addProduct(product);
            }
            System.out.println("Inventory loaded from file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error loading inventory from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Main ims = new Main();
        Scanner scanner = new Scanner(System.in);

        // Example: Adding a user
        User adminUser = new User("admin", "admin123", UserRole.ADMIN);
        User adminUser1 = new User("Iheb", "javajava", UserRole.ADMIN);
        User adminUser2 = new User("Dorra", "poyo", UserRole.ADMIN);
        User adminUser3 = new User("Tbs", "2023", UserRole.ADMIN);
        ims.addUser(adminUser);
        ims.addUser(adminUser1);
        ims.addUser(adminUser2);
        ims.addUser(adminUser3);

        // Example: Authenticating a user
        System.out.println("Enter username:");
        String enteredUsername = scanner.nextLine();
        System.out.println("Enter password:");
        String enteredPassword = scanner.nextLine();

        if (ims.authenticateUser(enteredUsername, enteredPassword)) {
            System.out.println("Authentication successful.");

            // Continue with other operations, e.g., adding products, recording transactions
            Product laptop = new Product("Laptop", 10, 350, "Electronics");
            Product phone = new Product("Iphone", 10, 800, "Electronics");
            Product headphones = new Product("Airpods", 10, 290, "Electronics");
            ims.addProduct(laptop);
            ims.addProduct(phone);
            ims.addProduct(headphones);

            // Transaction saleTransaction = new Transaction(laptop, 2, LocalDate.now(), TransactionType.SALE, 120.0);
            // Transaction saleTransaction1 = new Transaction(phone, 8, LocalDate.now(), TransactionType.SALE, 800.50);
            // Transaction saleTransaction2 = new Transaction(headphones, 9, LocalDate.now(), TransactionType.SALE, 45.750);
            // ims.recordTransaction(saleTransaction);
            // ims.recordTransaction(saleTransaction1);
            // ims.recordTransaction(saleTransaction2);

            // Display inventory and generate sales report
            ims.displayInventory();
            ims.generateSalesReport();

            // Save and load inventory from a file
            ims.saveInventoryToFile("inventory.txt");
            ims.loadInventoryFromFile("inventory.txt");
            scanner.close();
        } else {
            System.out.println("Authentication failed. Exiting program.");
        }
    }
}









