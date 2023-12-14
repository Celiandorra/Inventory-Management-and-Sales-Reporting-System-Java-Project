import java.time.LocalDate;

public class Transaction {
    private static int nextTransactionId = 1;

    private int transactionId;
    private Product product;
    private int quantity;
    private LocalDate date;
    private double price;
    private TransactionType type;

    public Transaction(Product product, int quantity, LocalDate date, TransactionType type, double price) {
        this.transactionId = nextTransactionId++;
        this.product = product;
        this.quantity = quantity;
        this.date = date;
        this.type = type;
        this.price = price;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public Object getTotalPrice() {
        return (price*quantity);
    }
}
