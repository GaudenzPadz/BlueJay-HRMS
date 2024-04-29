package testApp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;
import net.miginfocom.swing.MigLayout;

public class EmployeeEditWindow extends JWindow {

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField addressField;
    private final JTextField departmentField;
    private final JTextField workTypeField;
    private final JTextField wageField;
    private final JTextField grossPayField;
    private final JTextField netPayField;

    public EmployeeEditWindow(JFrame parent, Employee employee, EmployeeDatabase db) {
        getContentPane().setLayout(new BorderLayout());

        // Panel for editing fields
        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[pref!][grow,fill][pref!]", "[][][][][][][][][][][][][]"));
        panel.add(new JLabel("First Name:"), "cell 0 0");
        firstNameField = new JTextField(employee.getFirstName());
        panel.add(firstNameField, "cell 1 0");

        panel.add(new JLabel("Last Name:"), "cell 0 1");
        lastNameField = new JTextField(employee.getLastName());
        panel.add(lastNameField, "cell 1 1");

        panel.add(new JLabel("Address:"), "cell 0 2");
        addressField = new JTextField(employee.getAddress());
        panel.add(addressField, "cell 1 2");

        panel.add(new JLabel("Department:"), "cell 0 3");
        departmentField = new JTextField(employee.getDepartment());
        panel.add(departmentField, "cell 1 3");

        panel.add(new JLabel("Work Type:"), "cell 0 4");
        workTypeField = new JTextField(employee.getWorkType());
        panel.add(workTypeField, "cell 1 4");

        panel.add(new JLabel("Wage:"), "cell 0 5");
        wageField = new JTextField(String.valueOf(employee.getBasicSalary()));
        panel.add(wageField, "cell 1 5");

        panel.add(new JLabel("Gross Pay:"), "cell 0 6");
        grossPayField = new JTextField(String.valueOf(employee.getGrossPay()));
        panel.add(grossPayField, "cell 1 6");

        panel.add(new JLabel("Net Pay:"), "cell 0 7");
        netPayField = new JTextField(String.valueOf(employee.getNetPay()));
        panel.add(netPayField, "cell 1 7");

        getContentPane().add(panel, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        panel.add(saveButton, "flowx,cell 1 12");
        JButton cancelButton = new JButton("Cancel");
        panel.add(cancelButton, "cell 1 12");
        
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false); // Hide the window without saving
                    }
                });
        
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        employee.setFirstName(firstNameField.getText());
                        employee.setLastName(lastNameField.getText());
                        employee.setAddress(addressField.getText());
                        employee.setDepartment(departmentField.getText());
                        employee.setWorkType(workTypeField.getText());
                        employee.setBasicSalary(Double.parseDouble(wageField.getText()));
                        employee.setGrossPay(Double.parseDouble(grossPayField.getText()));
                        employee.setNetPay(Double.parseDouble(netPayField.getText()));
        
                        // Update in database
                        db.updateEmployee(employee);
                        JOptionPane.showMessageDialog(null, "Employee data updated successfully.");
                        setVisible(false); // Hide the window
                    }
                });

        setSize(500, 350); // Fixed size
        setLocationRelativeTo(parent); // Center the window
    }
    public static void main(String[] args) {
        EmployeeDatabase db;

        try {
            db = new EmployeeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            return;
        }

        JFrame mainFrame = new JFrame("Employee Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600); 
        mainFrame.setLocationRelativeTo(null);

        JButton openEditWindowButton = new JButton("Edit Employee");

        openEditWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee employee;
                try {
                    employee = db.getEmployeeById(1);
                } catch (Exception ex) {
                    System.err.println("Failed to fetch employee data: " + ex.getMessage());
                    return;
                }

                if (employee != null) {
                    EmployeeEditWindow editWindow = new EmployeeEditWindow(mainFrame, employee, db);
                    editWindow.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Employee not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainFrame.getContentPane().add(openEditWindowButton);

        mainFrame.setVisible(true);
    }
}
