package testApp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class connectDB {

	public static Connection connection;

	public connectDB() throws SQLException, ClassNotFoundException {
		connect();
	}

	private void connect() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite::resource:DB/bluejayDB.sqlite");
		if (connection == null) {
			throw new SQLException("Failed to establish connection to the database.");
		}
	}

	public ResultSet getAllData() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees");
		return statement.executeQuery();
	}

	public ResultSet getTypes() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT work_type FROM types");
		return statement.executeQuery();
	}

	public void insertData(String first_name, String last_name, String address, String workType) {
		try {
			String sql = "INSERT INTO employees (first_name, last_name, address, work_type) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, first_name);
			statement.setString(2, last_name);
			statement.setString(3, address);
			statement.setString(4, workType); // Set the work type in the PreparedStatement
			statement.executeUpdate();
			System.out.println("Record created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteData(int id) {
		try {
			String sql = "DELETE FROM employees WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
			System.out.println("Record deleted.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void resetTable() {
		try {
			String sql = "DELETE FROM employees";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeUpdate();
			System.out.println("Table reset.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static AddingPanel addPanel;
	public static JTable table;

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		JFrame frame = new JFrame("Employee Data");
		frame.setLayout(new BorderLayout()); // Set BorderLayout to arrange components

		// Create a JLabel with the title
		JLabel titleLabel = new JLabel("Employee Table");
		titleLabel.setHorizontalAlignment(JLabel.CENTER); // Align the title to the center
		frame.add(titleLabel, BorderLayout.NORTH); // Add the title label to the top of the frame

		connectDB cs = new connectDB();

		// Get all data from the database
		ResultSet rs = cs.getAllData();

		// Define table model and column names based on your table schema
		String[] columns = { "ID", "First Name", "Last Name", "Address", "Work Type", "Rate", "Gross Pay", "" }; // Modify
		DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

		// Add each row from ResultSet to the table model
		while (rs.next()) {
			Object[] row = { rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getString("address"), rs.getString("work_type"), rs.getInt("rate") };
			tableModel.addRow(row);
		}

		table = new JTable(tableModel);

		frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER); // Add JScrollPane for the table
		addPanel = new AddingPanel(cs);
		frame.getContentPane().add(connectDB.addPanel, BorderLayout.SOUTH); // Add AddingPanel to the bottom of the
																			// frame

		frame.setSize(600, 500); // Adjust size as needed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}

class DataReloader extends Thread {
	private connectDB db;

	public DataReloader(connectDB db) {
		this.db = db;
	}

	@Override
	public void run() {
		try {
			// Reload data from the database
			ResultSet rs = db.getAllData();
			DefaultTableModel tableModel = (DefaultTableModel) connectDB.table.getModel();
			tableModel.setRowCount(0); // Clear existing rows
			while (rs.next()) {
				Object[] row = { rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("address") };
				tableModel.addRow(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

class AddingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTextField fNameField, lNameField, addressField;
	public JButton addButton, deleteButton, resetButton;
	private connectDB db;
	private JComboBox<String> comboboxWorkType;

	/**
	 * Create the panel.
	 */
	// Constructor
	public AddingPanel(connectDB db) {
		this.db = db;
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel addPanel = new JPanel();
		panel_1.add(addPanel, BorderLayout.NORTH);
		addPanel.setBorder(new TitledBorder(null, "Add Employee", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		addPanel.setLayout(new GridLayout(5, 2, 0, 0));

		JLabel fNameLabel = new JLabel("First Name : ");
		addPanel.add(fNameLabel);

		fNameField = new JTextField();
		addPanel.add(fNameField);
		fNameField.setColumns(10);

		JLabel lNameLabel = new JLabel("Last Name : ");
		addPanel.add(lNameLabel);

		lNameField = new JTextField();
		addPanel.add(lNameField);
		lNameField.setColumns(10);

		JLabel address = new JLabel("Address : ");
		addPanel.add(address);

		addressField = new JTextField();
		addPanel.add(addressField);
		addressField.setColumns(10);

		JLabel typeLabel = new JLabel("Work Type : ");
		addPanel.add(typeLabel);

		comboboxWorkType = new JComboBox<>();
		addPanel.add(comboboxWorkType);
		populateComboBox(db); // Populate the combo box with data from the database

		addButton = new JButton("Add Employee");
		addButton.addActionListener((ActionEvent e) -> {
			db.insertData(fNameField.getText(), lNameField.getText(), addressField.getText(),
					(String) comboboxWorkType.getSelectedItem());
			DataReloader reloader = new DataReloader(db);
			reloader.start();
			fNameField.setText("");
			lNameField.setText("");
			addressField.setText("");
		});
		panel_1.add(addButton, BorderLayout.SOUTH);

		JPanel CRUDPanel = new JPanel();
		CRUDPanel.setBorder(new TitledBorder(null, "CRUD Buttons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(CRUDPanel, BorderLayout.CENTER);

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener((ActionEvent ae) -> {
			String idString = JOptionPane.showInputDialog("Enter the ID of the record to delete:");
			if (idString != null && !idString.isEmpty()) {
				int id = Integer.parseInt(idString);
				db.deleteData(id);
				DataReloader reloader = new DataReloader(db);
				reloader.start();
			}
		});
		CRUDPanel.add(deleteButton);

		resetButton = new JButton("Reset");
		resetButton.addActionListener((ActionEvent ae) -> {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the table?",
					"Reset Table", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				db.resetTable();
				DataReloader reloader = new DataReloader(db);
				reloader.start();
			}
		});
		CRUDPanel.add(resetButton);

		JButton reloadBtn = new JButton("Reload");
		reloadBtn.addActionListener((ActionEvent ae) -> {
			DataReloader reloader = new DataReloader(db);
			reloader.start();
		});
		CRUDPanel.add(reloadBtn);

	}

	// Method to populate the combo box with data from the database
	private void populateComboBox(connectDB db) {
		try {
			ResultSet rs1 = db.getTypes();
			while (rs1.next()) {
				String work_type = rs1.getString("work_type");
				comboboxWorkType.addItem(work_type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
