
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryManagementSystemGUI extends JFrame implements UserAuthentication, InventoryOperations {
    private InventoryManager inventoryManager;
    private static List<Transaction> transactions;
    private JTextArea displayArea;
    private List<User> users;
    private User currentUser;
    private static JTextArea salesDashboardArea;


    public InventoryManagementSystemGUI() {

        this.inventoryManager = new InventoryManager();
        this.transactions = new ArrayList<>();
        this.users = new ArrayList<>();

        // Example: Adding a user
        User adminUser = new User("admin", "admin123", UserRole.ADMIN);
        User adminUser1 = new User("Iheb", "javajava", UserRole.ADMIN);
        User adminUser2 = new User("Dorra", "poyo", UserRole.ADMIN);
        User adminUser3 = new User("Tbs", "2023", UserRole.REGULAR);
        users.add(adminUser);
        users.add(adminUser1);
        users.add(adminUser2);
        users.add(adminUser3);

        // GUI components
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        JButton loginButton = new JButton("Login");
        JButton logoutButton = new JButton("Logout");
        JButton displayButton = new JButton("Display Inventory");
        JButton generateReportButton = new JButton("Generate Sales Report");
        JButton addProductButton = new JButton("Add Product");
        JButton recordTransactionButton = new JButton("Record Transaction");
        JButton saveToFileButton = new JButton("Save Inventory to File");
        JButton loadFromFileButton = new JButton("Load Inventory from File");

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Layout
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(logoutButton);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2));
        buttonPanel.add(displayButton);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(recordTransactionButton);
        buttonPanel.add(saveToFileButton);
        buttonPanel.add(loadFromFileButton);

        add(loginPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);




        // Initialize sales dashboard area
        salesDashboardArea = new JTextArea();
        salesDashboardArea.setEditable(false);

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new GridLayout(3, 1));
        dashboardPanel.add(new JLabel("Sales Dashboard:"));
        dashboardPanel.add(new JScrollPane(salesDashboardArea));

        add(dashboardPanel, BorderLayout.EAST);







        // Event listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());
                loginUser(enteredUsername, enteredPassword);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutUser();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
                    displayInventory();
                } else {
                    JOptionPane.showMessageDialog(null, "Admin privileges required.");
                }
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
                    generateSalesReport();
                } else {
                    JOptionPane.showMessageDialog(null, "Admin privileges required.");
                }
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
                    addProduct();
                } else {
                    JOptionPane.showMessageDialog(null, "Admin privileges required.");
                }
            }
        });

        recordTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser != null) {
                    recordTransaction();
                } else {
                    JOptionPane.showMessageDialog(null, "Login required.");
                }
            }
        });

        saveToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
                    saveInventoryToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Admin privileges required.");
                }
            }
        });

        loadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
                    loadInventoryFromFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Admin privileges required.");
                }
            }
        });

        setVisible(true);
    }

    static void generateSalesReport() {
        StringBuilder reportText = new StringBuilder("Sales Report:\n");
        for (Transaction transaction : transactions) {
            reportText.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
            reportText.append("Product: ").append(transaction.getProduct().getProductName()).append("\n");
            reportText.append("Quantity: ").append(transaction.getQuantity()).append("\n");
            reportText.append("Date: ").append(transaction.getDate()).append("\n");
            reportText.append("Transaction Type: ").append(transaction.getType()).append("\n");
            reportText.append("Total Price: ").append(transaction.getTotalPrice()).append("\n");
            reportText.append("------------------------------\n");
        }

        JOptionPane.showMessageDialog(null, reportText.toString());


        // Save the report to a file
        saveSalesReportToFile(reportText.toString(), "C:\\Users\\Dorra\\Desktop\\sales_report.txt");



         updateSalesDashboard();
    }


    private static void updateSalesDashboard() {
        // Display basic sales statistics in the sales dashboard area
        StringBuilder dashboardText = new StringBuilder("Sales Statistics:\n");
        double totalSales = 0;

        // Calculate total sales and best-selling product
        Product bestSellingProduct = null;
        int maxQuantitySold = 0;

        for (Transaction transaction : transactions) {
            double totalPrice = (double) transaction.getTotalPrice();

            totalSales = totalSales + totalPrice;

            if (transaction.getQuantity() > maxQuantitySold) {
                maxQuantitySold = transaction.getQuantity();
                bestSellingProduct = transaction.getProduct();
            }
        }

        dashboardText.append("Total Sales: $").append(totalSales).append("\n");

        if (bestSellingProduct != null) {
            dashboardText.append("Best-Selling Product: ").append(bestSellingProduct.getProductName()).append("\n");
        }

        dashboardText.append("Sales Trends Over Time:\n");

        int[] monthlySales = new int[12];

        for (Transaction transaction : transactions) {
            int month = transaction.getDate().getMonthValue() - 1;
            monthlySales[month] += transaction.getQuantity();
        }

        for (int i = 0; i < 12; i++) {
            dashboardText.append("Month ").append(i + 1).append(": ").append(monthlySales[i]).append(" units\n");
        }

        // Update the sales dashboard area
        salesDashboardArea.setText(dashboardText.toString());
    }







    private static void saveSalesReportToFile(String reportText, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(reportText);
            JOptionPane.showMessageDialog(null, "Sales report saved to file: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving sales report to file: " + e.getMessage());
        }

    }

    @Override
    public void displayInventory() {
        List<Product> products = inventoryManager.getAllProducts();
        displayArea.setText("Inventory:\n");
        for (Product product : products) {
            displayArea.append("Product ID: " + product.getProductId() + "\n");
            displayArea.append("Product Name: " + product.getProductName() + "\n");
            displayArea.append("Quantity in Stock: " + product.getQuantityInStock() + "\n");
            displayArea.append("Price: " + product.getPrice() + "\n");
            displayArea.append("Category: " + product.getCategory() + "\n");
            displayArea.append("------------------------------\n");
        }
    }

    @Override
    public void addProduct() {
        String productName = JOptionPane.showInputDialog("Enter product name:");
        int quantityInStock = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity in stock:"));
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
        String category = JOptionPane.showInputDialog("Enter category:");

        Product product = new Product(productName, quantityInStock, price, category);
        inventoryManager.addProduct(product);

        JOptionPane.showMessageDialog(null, "Product added successfully.");
    }

    @Override
    public void recordTransaction() {
        String productName = JOptionPane.showInputDialog("Enter product name:");
        Product product = inventoryManager.getProductByName(productName);

        if (product == null) {
            JOptionPane.showMessageDialog(null, "Product not found.");
            return;
        }

        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
        TransactionType type = TransactionType
                .valueOf(JOptionPane.showInputDialog("Enter transaction type (SALE or PURCHASE):")
                        .toUpperCase());

        Transaction transaction = new Transaction(product, quantity, LocalDate.now(), type, product.getPrice());
        transactions.add(transaction);
        updateProductQuantity(product, quantity, type);

        JOptionPane.showMessageDialog(null, "Transaction recorded successfully.");
    }

    @Override
    public void updateProductQuantity(Product product, int quantity, TransactionType type) {
        int currentQuantity = product.getQuantityInStock();
        if (type == TransactionType.SALE) {
            product.setQuantityInStock(currentQuantity - quantity);
        } else {
            product.setQuantityInStock(currentQuantity + quantity);
        }
    }

    @Override
    public void saveInventoryToFile() {
        String filePath = JOptionPane.showInputDialog("Enter file path:");
        try (FileWriter writer = new FileWriter(filePath)) {
            List<Product> products = inventoryManager.getAllProducts();
            for (Product product : products) {
                writer.write(
                        product.getProductId() + ","
                                + product.getProductName() + ","
                                + product.getQuantityInStock() + ","
                                + product.getPrice() + ","
                                + product.getCategory() + "\n");
            }
            JOptionPane.showMessageDialog(null, "Inventory saved to file: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving inventory to file: " + e.getMessage());
        }
    }

    @Override
    public void loadInventoryFromFile() {
        String filePath = JOptionPane.showInputDialog("Enter file path:");
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
            JOptionPane.showMessageDialog(null, "Inventory loaded from file: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading inventory from file: " + e.getMessage());
        }
    }

    @Override
    public void loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                JOptionPane.showMessageDialog(null, "Login successful.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Login failed. Invalid username or password.");
    }

    @Override
    public void logoutUser() {
        currentUser = null;
        JOptionPane.showMessageDialog(null, "Logout successful.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryManagementSystemGUI();
            }
        });
    }
}
