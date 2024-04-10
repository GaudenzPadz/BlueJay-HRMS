package bluejayDB;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel; 

public class EmployeeManagementApp extends JFrame {

    private static final String WINDOW_TITLE = "Employee Management";
    private static final String[] TABLE_COLUMNS = { "ID", "First Name", "Last Name", "Address", "Work Type" };

    private final EmployeeDatabase database;

    private JTable table;
    private JTextField fNameField, lNameField, addressField;
    private JComboBox<String> comboBoxWorkType;

    public EmployeeManagementApp(EmployeeDatabase database) {
        this.database = database;

        setTitle(WINDOW_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createAddEmployeePanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the window
    }

    private JPanel createTitlePanel() {
        JLabel titleLabel = new JLabel("Employee Table");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        return new JPanel(new FlowLayout(FlowLayout.CENTER)) {{
            add(titleLabel);
        }};
    }

    private JPanel createTablePanel() {
        table = new JTable(new DefaultTableModel(TABLE_COLUMNS, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        return new JPanel(new BorderLayout()) {{
            add(scrollPane);
        }};
    }

    private JPanel createAddEmployeePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBorder(new TitledBorder("Add Employee"));

        panel.add(new JLabel("First Name:"));
        fNameField = new JTextField();
        panel.add(fNameField);

        panel.add(new JLabel("Last Name:"));
        lNameField = new JTextField();
        panel.add(lNameField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Work Type:"));
        comboBoxWorkType = new JComboBox<>();
        populateComboBox();
        panel.add(comboBoxWorkType);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(this::addEmployee);
        panel.add(addButton);

        return panel;
    }

    private void populateComboBox() {
        try {
            ResultSet rs = database.getTypes();
            while (rs.next()) {
                comboBoxWorkType.addItem(rs.getString("work_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch work types from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee(ActionEvent e) {
        String firstName = fNameField.getText();
        String lastName = lNameField.getText();
        String address = addressField.getText();
        String workType = (String) comboBoxWorkType.getSelectedItem();

        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            database.insertData(firstName, lastName, address, workType);
            JOptionPane.showMessageDialog(this, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add employee to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        try {
            ResultSet rs = database.getAllData();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = { rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("address"), rs.getString("work_type") };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to refresh employee data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            EmployeeDatabase database = new EmployeeDatabase();
            SwingUtilities.invokeLater(() -> new EmployeeManagementApp(database).setVisible(true));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
