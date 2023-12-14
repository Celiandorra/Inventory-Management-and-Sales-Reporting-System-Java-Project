import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class SalesReportGenerator {
    private static List<Transaction> transactions;
    JTextArea displayArea;

    public SalesReportGenerator(List<Transaction> transactions, JTextArea displayArea) {
        this.transactions = transactions;
        this.displayArea = displayArea;
    }}
 