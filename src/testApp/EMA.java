package testApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import bluejay.Employee;
import bluejayDB.EmployeeDatabase;

public class EMA extends JFrame {

	private static final long serialVersionUID = 213;
	private JTable table;
	private DefaultTableModel tableModel;
	private EmployeeDatabase db;
	private final JButton btnCalculate;
	private JTextField firstNameField, lastNameField, addressField, txtBasicSalary, txtAttendance, txtSSS, txtPagibig,
			txtPhilHealth, txtGrossPay, txtTotalDeduction, txtNetSalary;
	private JRadioButton otherRadio, maleRadio, femaleRadio;
	private static final int GRID_ROWS = 8;
	private static final int GRID_COLUMNS = 2;
	private static final int HORIZONTAL_GAP = 10;
	private static final int VERTICAL_GAP = 10;

	private final Map<JTextField, String> fieldMap;

	public EMA() {
		setTitle("Employee Management System");
		int width = 1000;
		int height = 500;
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			db = new EmployeeDatabase();
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); // Log the exception
		}

		fieldMap = new HashMap<>();

		// Create table model and table
		tableModel = new DefaultTableModel();
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel payrollPanel = new JPanel();

		JPanel inputPanel = new JPanel(new GridLayout(6, 2));

		addTextField(inputPanel, "Rate:", txtBasicSalary = new JTextField());
		addTextField(inputPanel, "Attendance:", txtAttendance = new JTextField());
		addTextField(inputPanel, "SSS:", txtSSS = new JTextField());
		addTextField(inputPanel, "Pag-IBIG:", txtPagibig = new JTextField());
		addTextField(inputPanel, "PhilHealth:", txtPhilHealth = new JTextField());

		addDocumentListener(txtBasicSalary);
		addDocumentListener(txtAttendance);
		addDocumentListener(txtSSS);
		addDocumentListener(txtPagibig);
		addDocumentListener(txtPhilHealth);

		JPanel resultPanel = new JPanel(new GridLayout(3, 2));

		addResultField(resultPanel, "Gross Pay:", txtGrossPay = new JTextField());
		addResultField(resultPanel, "Total Deduction:", txtTotalDeduction = new JTextField());
		addResultField(resultPanel, "Net Salary:", txtNetSalary = new JTextField());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnCalculate = new JButton("Calculate");
		buttonPanel.add(btnCalculate);

		JComboBox<String> highlightEmployeesByFirstName = new JComboBox<>();
		try {
			ResultSet resultSet = db.getAllData();
			DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
			while (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				comboBoxModel.addElement(firstName);
			}
			highlightEmployeesByFirstName.setModel(comboBoxModel);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		// Add action listener to highlight selected employee in the table
		highlightEmployeesByFirstName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedFirstName = (String) highlightEmployeesByFirstName.getSelectedItem();
				for (int row = 0; row < tableModel.getRowCount(); row++) {
					String firstName = (String) tableModel.getValueAt(row, 1); // first name is in the second column
					if (firstName.equals(selectedFirstName)) {
						// Get the rate of the selected employee from the table
						Object rateObj = tableModel.getValueAt(row, 5);
						if (rateObj instanceof Integer) {
							int rate = (int) rateObj;
							// Set the rate to the Rate field in the payroll panel
							txtBasicSalary.setText(String.valueOf(rate));
						}
						break;
					}
				}
			}
		});

		// Call this method when the application starts or when relevant information
		// changes
//		updatePayrollForAllEmployees();

		payrollPanel.add(highlightEmployeesByFirstName);

		payrollPanel.add(inputPanel, BorderLayout.NORTH);
		payrollPanel.add(resultPanel, BorderLayout.CENTER);
		payrollPanel.add(buttonPanel, BorderLayout.SOUTH);
		// Populate the table with data from the database
		try {
			populateTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Save changes listener
		saveChanges();

		setVisible(true);
		printTableData();

		// Add components to the frame
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(createPanel(), BorderLayout.EAST);
		getContentPane().add(payrollPanel, BorderLayout.SOUTH);

	}

	private void printTableData() {
		// Print to console for debugging
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			System.out.print(tableModel.getColumnName(i) + "\t");
		}
		System.out.println();

		// Print table data
		for (int row = 0; row < tableModel.getRowCount(); row++) {
			for (int col = 0; col < tableModel.getColumnCount(); col++) {
				System.out.print(tableModel.getValueAt(row, col) + "\t");
			}
			System.out.println();
		}
	}

	private JPanel createPanel() {
		JPanel panel = new JPanel();

		// Add input fields and button for adding a new employee
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameField = new JTextField(10);
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameField = new JTextField(10);
		JLabel addressLabel = new JLabel("Address:");
		addressField = new JTextField(10);
		JLabel workTypeLabel = new JLabel("Work Type:");
		JComboBox<String> workTypeComboBox = new JComboBox<>();

		// Populate the workTypeComboBox with values from the types table
		try {
			ResultSet typesResultSet = db.getTypes();
			while (typesResultSet.next()) {
				String workType = typesResultSet.getString("work_type");
				workTypeComboBox.addItem(workType);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Failed to populate table: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); // Log the exception
		}

		JButton addButton = new JButton("Add Employee");

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String address = addressField.getText();
				String selectedWorkType = (String) workTypeComboBox.getSelectedItem();
				// Get the selected gender from the radio buttons
				String gender = "";
				if (maleRadio.isSelected()) {
					gender = "Male";
				} else if (femaleRadio.isSelected()) {
					gender = "Female";
				} else if (otherRadio.isSelected()) {
					gender = "Alien";
				}
				// Insert new employee data into the database
				db.insertData(firstName, lastName, address, selectedWorkType, gender);

				// Refresh the table to reflect the new data
				try {
					refreshTable();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				// Clear input fields
				clearInputFields();
			}
		});

		JButton deleteButton = new JButton("Delete");
		JButton saveButton = new JButton("Save");

		// for debugging
		JButton reloadButton = new JButton("Reload");

		// Add action listener for the delete button
		deleteButton.addActionListener((ActionEvent e) -> {
			deleteSelectedRow();
		});

		// Add action listener for the reload button
		reloadButton.addActionListener((ActionEvent e) -> {
			try {
				// Refresh the table to display the latest data from the database
				refreshTable();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		// Add action listener for the save button
		saveButton.addActionListener((ActionEvent e) -> {
			// save method to save data from the table
			saveChanges();

		});

		panel.setLayout(new GridLayout(GRID_ROWS, GRID_COLUMNS, HORIZONTAL_GAP, VERTICAL_GAP));

		panel.add(firstNameLabel);
		panel.add(firstNameField);
		panel.add(lastNameLabel);
		panel.add(lastNameField);
		panel.add(addressLabel);
		panel.add(addressField);
		panel.add(workTypeLabel);
		panel.add(workTypeComboBox);

		// Create a button group for the radio buttons
		ButtonGroup genderGroup = new ButtonGroup();

		panel.add(addButton);

		JPanel genderPanel = new JPanel();

		panel.add(genderPanel);

		// Create and add male radio button to the panel and button group
		maleRadio = new JRadioButton("Male");
		genderPanel.add(maleRadio);
		genderGroup.add(maleRadio);

		// Create and add female radio button to the panel and button group
		femaleRadio = new JRadioButton("Female");
		genderPanel.add(femaleRadio);
		genderGroup.add(femaleRadio);

		// Create and add alien radio button to the panel and button group
		otherRadio = new JRadioButton("Alien");
		genderPanel.add(otherRadio);
		genderGroup.add(otherRadio);

		// Set a default selection for the gender radio buttons
		maleRadio.setSelected(true);
		panel.add(reloadButton);
		panel.add(deleteButton);
		panel.add(saveButton);
		try {
			ResultSet resultSet = db.getAllData();
			DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
			while (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				comboBoxModel.addElement(firstName);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return panel;
	}

	private void populateTable() throws SQLException {
		ResultSet resultSet = db.getAllData();
		ResultSetMetaData metaData = resultSet.getMetaData();

		// Clear existing columns from table model
		tableModel.setColumnCount(0);

		// Add columns to table model
		int columnCount = metaData.getColumnCount();
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			tableModel.addColumn(metaData.getColumnName(columnIndex));
		}

		// Add rows to table model
		while (resultSet.next()) {
			Object[] rowData = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				rowData[i] = resultSet.getObject(i + 1);
			}
			tableModel.addRow(rowData);
		}

		resultSet.close();
	}

	public boolean isValid(Object value, int columnIndex) {
		if (value == null) {
			return false; // Null values are not valid
		}

		// Validate based on column name
		String columnName = table.getColumnName(columnIndex);
		switch (columnName) {
		case "work_type":
			// Validate work type against values from the types table
			String workType = (String) value;
			try {
				ResultSet typesResultSet = db.getTypes();
				while (typesResultSet.next()) {
					String dbWorkType = typesResultSet.getString("work_type");
					if (dbWorkType.equals(workType)) {
						return true; // Valid work type
					}
				}
				// Work type not found in the types table
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false; // Error occurred while validating work type
			}
		case "first_name":
		case "last_name":
			// Validate first name and last name length
			String name = (String) value;
			return name.length() <= 20; // Maximum 20 characters allowed for first name and last name
		case "rate_gross":
		case "rate_net":
			// Validate rate gross and rate net types
			try {
				double rate = Double.parseDouble(value.toString());
				// Check if rate is double or int
				return rate % 1 == 0 || rate % 1 == 0.0; // Check if rate is integer or double
			} catch (NumberFormatException e) {
				return false; // Invalid rate format
			}
		default:
			return true; // For other columns, no specific validation required
		}
	}

	private void refreshTable() throws SQLException {
		// Clear existing rows from table model
		tableModel.setRowCount(0);
		// Populate table with updated data
		populateTable();
	}

	private void clearInputFields() {
		firstNameField.setText("");
		lastNameField.setText("");
		addressField.setText("");
	}

	private void deleteSelectedRow() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			int id = (int) tableModel.getValueAt(selectedRow, 0);
			db.deleteData(id);
			tableModel.removeRow(selectedRow);
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addTextField(JPanel panel, String label, JTextField textField) {
		panel.add(new JLabel(label));
		panel.add(textField);
		fieldMap.put(textField, label);
	}

	private void addDocumentListener(JTextField textField) {
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				automaticCalculate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				automaticCalculate();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				automaticCalculate();
			}
		});
	}

	private void addResultField(JPanel panel, String label, JTextField textField) {
		panel.add(new JLabel(label));
		textField.setEditable(false);
		panel.add(textField);
	}

	private void automaticCalculate() {
		clearBorders();
		if (validateInputs()) {
			try {
				double rate = Double.parseDouble(txtBasicSalary.getText());
				double attendance = Double.parseDouble(txtAttendance.getText());
				double sss = Double.parseDouble(txtSSS.getText());
				double pagibig = Double.parseDouble(txtPagibig.getText());
				double philhealth = Double.parseDouble(txtPhilHealth.getText());
				Employee emp = new Employee();

				emp.setRate(rate);
				emp.setDaysWorked(attendance);
				emp.setSSS(sss);
				emp.setPAG_IBIG(pagibig);
				emp.setPHILHEALTH(philhealth);
				double gross = emp.calculateGrossPay(emp.getPAG_IBIG(), emp.getPHILHEALTH());
				double totalDeduc = emp.totalDeductions(emp.getPHILHEALTH(), emp.getPAG_IBIG(), emp.getSSS());
				double net = emp.calculateNetPay(gross, totalDeduc);

				txtGrossPay.setText(String.valueOf(gross));
				txtTotalDeduction.setText(String.valueOf(totalDeduc));
				txtNetSalary.setText(String.valueOf(net));

			} catch (NumberFormatException ex) {
				showErrorDialog("Invalid input format. Please enter valid numbers.");
			}
		}
	}

	private void saveChanges() {
		tableModel.addTableModelListener(e -> {
			int row = e.getFirstRow();
			int column = e.getColumn();
			if (row != -1 && column != -1) {
				Object updatedValue = tableModel.getValueAt(row, column);
				// Validate updatedValue here
				if (isValid(updatedValue, column)) {
					// Show confirmation dialog
					int choice = JOptionPane.showConfirmDialog(null, "Do you want to save the changes?",
							"Confirm Changes", JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						// User confirmed changes, proceed with saving
						int id = (int) tableModel.getValueAt(row, 0);
						String columnName = tableModel.getColumnName(column);
						db.updateData(id, columnName, updatedValue); // This line updates the database

						// Update payroll information if rate or attendance is changed
						if (columnName.equals("rate") || columnName.equals("attendance")) {
							updatePayrollForEmployee(id);
						}
					} else {
						// User canceled changes, rollback or ignore
						// You can handle this case if necessary
					}
				} else {
					JOptionPane.showMessageDialog(null, "Invalid data entered.", "Error", JOptionPane.ERROR_MESSAGE);
					try {
						refreshTable(); // Refresh the table to discard invalid changes
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	private void updatePayrollForEmployee(int employeeId) {
		try {
			// Retrieve employee data from the database
			ResultSet employeeData = (ResultSet) db.getEmployeeById(employeeId);
			if (employeeData.next()) {
				double rate = employeeData.getDouble("rate");
				double attendance = employeeData.getDouble("attendance");
				double sss = employeeData.getDouble("sss");
				double pagibig = employeeData.getDouble("pag_ibig");
				double philhealth = employeeData.getDouble("philhealth");

				// Calculate payroll information
				Employee emp = new Employee();
				emp.setRate(rate);
				emp.setDaysWorked(attendance);
				emp.setSSS(sss);
				emp.setPAG_IBIG(pagibig);
				emp.setPHILHEALTH(philhealth);
				double gross = emp.calculateGrossPay(attendance, rate);
				double totalDeduc = emp.totalDeductions(philhealth, pagibig, sss);
				double net = emp.calculateNetPay(gross, totalDeduc);

				// Update the payroll information in the database
				db.updatePayrollInformation(employeeId, gross, totalDeduc, net);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean validateInputs() {
		boolean valid = true;
		for (JTextField textField : fieldMap.keySet()) {
			if (!isValidInput(textField.getText())) {
				valid = false;
				textField.setBorder(BorderFactory.createLineBorder(Color.RED));
			} else {
				textField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
			}
		}
		return valid;
	}

	private void clearBorders() {
		for (JTextField textField : fieldMap.keySet()) {
			textField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
		}
	}

	private boolean isValidInput(String text) {
		try {
			Double.parseDouble(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(EMA::new);
	}

	public void reloadData(EmployeeDatabase db) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					model.setRowCount(0); // Clear existing data in the table model
					ResultSet rs = db.getAllData(); // Fetch all employee data
					int rowCount = 0;
					while (rs.next()) {
						// Add fetched data to the table model
						model.addRow(new Object[] { rs.getInt("id"), rs.getString("first_name"),
								rs.getString("last_name"), rs.getString("address"),
								workTypeMap.getOrDefault(rs.getString("work_type"), "Unknown"), rs.getDouble("rate"),
								0.0, // Placehold er for Gross Pay, calculate as needed
								0.0 // Placeholder for Net Pay, calculate as needed
						});
						rowCount++;
						setProgress((int) ((double) rowCount / getTotalRowCount() * 100)); // Update progress
					}
					db.closeConnection(); // Close the database connection
				} catch (SQLException e) {
					// Handle database related exceptions
					JOptionPane.showMessageDialog(null, "Error fetching data from database: " + e.getMessage(),
							"Database Error", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					// Handle other exceptions
					JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					System.out.println(e.getMessage());
				}
				return null;
			}

			// Method to get total row count from database (if needed)
			private int getTotalRowCount() throws SQLException {
				ResultSet rs = db.getAllData();
				int count = 0;
				while (rs.next()) {
					count++;
				}
				return count;
			}

			@Override
			protected void done() {
				// Update UI or perform any post-processing tasks if needed
			}
		};
		worker.execute();
	}

}