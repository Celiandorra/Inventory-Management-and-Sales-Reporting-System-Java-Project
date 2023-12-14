interface InventoryOperations {
    void displayInventory();

    void addProduct();

    void recordTransaction();

    void updateProductQuantity(Product product, int quantity, TransactionType type);

    void saveInventoryToFile();

    void loadInventoryFromFile();
}