package testApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OngoingTransactionMonitor extends JFrame {
    private DefaultTableModel tableModel;

    public OngoingTransactionMonitor() {
        setTitle("Ongoing Transactions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a table model with columns for Item Name and Quantity
        String[] columns = {"Item Name", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0);

        // Create a JTable with the table model
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the JFrame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to add a transaction to the table
    public void addTransaction(String itemName, int quantity) {
        Object[] rowData = {itemName, quantity};
        tableModel.addRow(rowData);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new OngoingTransactionMonitor();
            }
        });
    }
}
