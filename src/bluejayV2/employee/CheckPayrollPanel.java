package bluejayV2.employee;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;
import net.miginfocom.swing.MigLayout;

public class CheckPayrollPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private EmployeeDatabase db;
    private Employee currentEmployee; // Reference to the current employee
    private JTable table;
    private DefaultTableModel model;

    public CheckPayrollPanel(Employee currentEmployee, EmployeeDatabase DB) {
        this.db = DB;
        this.currentEmployee = currentEmployee;
        setLayout(new BorderLayout(0, 0));

        JPanel mainPane = new JPanel(
                new MigLayout("wrap, fillx, insets 25 35 25 35", "[grow,center]", "[][][center][100px][grow][][][][grow][][][][]"));

        add(mainPane, BorderLayout.CENTER);

        JLabel lblNewLabel = new JLabel("Check Payroll");
        lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        mainPane.add(lblNewLabel, "cell 0 0 1 2");

        model = new DefaultTableModel(
                new String[] { "Date", "Basic Salary", "Gross Pay", "Total Deduction", "Net Pay", "Salary Period" }, 0);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadPayrollData());
        mainPane.add(refreshButton, "cell 0 2,alignx left");

        table = new JTable(model);

        JScrollPane empListScrollPane = new JScrollPane(table);
        mainPane.add(empListScrollPane, "cell 0 3,growx,aligny top");

        JTextArea textArea = new JTextArea();
        mainPane.add(textArea, "cell 0 9,grow");

        JButton printPaySlipButton = new JButton("Print PaySlip");
        printPaySlipButton.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPane.add(printPaySlipButton, "cell 0 11,alignx right");

        // Initial data load
        loadPayrollData();
    }

    private void loadPayrollData() {
        // Clear existing data in the model
        model.setRowCount(0);

        try {
            Connection connection = db.getConnection(); // Get the database connection
            String sql = "SELECT * FROM payroll WHERE employee_id = ?"; // SQL query to fetch payroll data
            PreparedStatement statement = connection.prepareStatement(sql); // Prepare the SQL statement
            statement.setInt(1, currentEmployee.getId()); // Set the employee ID parameter

            ResultSet resultSet = statement.executeQuery(); // Execute the query

            // Iterate over the result set and add rows to the table model
            while (resultSet.next()) {
                // Retrieve data from the ResultSet
                String date = resultSet.getString("created_at"); // Assuming 'created_at' represents the date
                double basicSalary = resultSet.getDouble("ratePerDay");
                double grossPay = resultSet.getDouble("grossPay");
                double totalDeduction = resultSet.getDouble("totalDeduction");
                double netPay = resultSet.getDouble("netPay");
                String salaryPeriod = "Monthly"; // Example, could be derived from other data

                // Add data to the table model
                model.addRow(new Object[] { date, basicSalary, grossPay, totalDeduction, netPay, salaryPeriod });
            }

            // Close resources
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
            JOptionPane.showMessageDialog(this, "Error loading payroll data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
