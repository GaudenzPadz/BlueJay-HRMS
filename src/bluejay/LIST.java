package bluejay;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import bluejayDB.EmployeeDatabase;

public class LIST {

	// panels for employee list
	public JScrollPane scrollPane;
	public JPanel searchPanel;

	public static JTable table;
	private static JPopupMenu popupMenu;
	private static JMenuItem menuItemRemove;
	public final Map<String, String> workTypeMap = new HashMap<>();
	private JTextField fNameField, lNameField, addressField;
	private JButton addButton, deleteButton, resetButton;
	private JComboBox<String> comboBoxWorkType;
	private static String[] column = { "ID", "First Name", "Last Name", "Address", "Work Type", "Rate", "Gross Pay",
			"Net Pay" };
	private static EmployeeDatabase db;
	private static DefaultTableModel model = new DefaultTableModel(column, 0) {

		private static final long serialVersionUID = 4L;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// Make all cells editable except the first row ID[0], Work Type[4], Rate[5],
			// Gross[6], Net[7]

			boolean[] columnEditables = new boolean[] { false, true, true, true, false, false, false, false };
			return columnEditables[columnIndex];
		}
	};

	public LIST() {
		try {
			db = new EmployeeDatabase();
			db.connect(); // Establish connection
			System.out.println("Connected to the database successfully.");
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// create a JTable
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Add a focus listener to the table to ( ?? ) when focus is lost
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// when focus is lost?
				// Save data to the database
			}
		});

		popupMenu();

		tableDesign();
		refreshTable(); // Fetch data from the database and populate the table

		// Add JTable to a scroll pane
		scrollPane = new JScrollPane(table);

		// create a panel for search
		searchPanel = new JPanel();
		JTextField searchField = new JTextField(10);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTable();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTable();
			}

			private void updateTable() {
				String searchText = searchField.getText().trim();
				if (searchText.isEmpty()) {
					@SuppressWarnings("unchecked")
					TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
					sorter.setRowFilter(null); // Removes any existing filter
				} else {
					searchTable(searchText); // Perform the search as defined earlier
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// I DONT KNOW THE USE OF THIS ONE
			}
		});
		searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		searchPanel.add(new JLabel("Search: "));
		searchPanel.add(searchField);

	}

	private static void searchTable(String searchText) {
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		// Filter based on matching case-insensitive cells
		RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
		sorter.setRowFilter(filter);

		if (searchText.trim().length() == 0) {
			sorter.setRowFilter(null);
			// Load data from the database
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
		}
	}

	private static void tableDesign() {
		TableColumnModel columnModel = table.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(0).setResizable(false);

		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(100);

		columnModel.getColumn(3).setPreferredWidth(300);
		columnModel.getColumn(4).setPreferredWidth(200);
		columnModel.getColumn(5).setPreferredWidth(100);
		columnModel.getColumn(6).setPreferredWidth(100);
		columnModel.getColumn(7).setPreferredWidth(100);
		// table.setToolTipText("Edit"); //floating text("edit") on the table when
		// inactive
		table.setCellSelectionEnabled(true);
		table.setFont(new Font("Serif", Font.PLAIN, 18));
		// table.setPreferredSize(new Dimension(1000, 500));
		table.setRowHeight(40);
	}

	private void popupMenu(){
		// constructs the popup menu
		popupMenu = new JPopupMenu();
		menuItemRemove = new JMenuItem("Remove Current Row");
		popupMenu.add(menuItemRemove);

		menuItemRemove.addActionListener((ActionEvent e) -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select an employee to delete.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Get the employee ID from the selected row
			int employeeId = (int) table.getValueAt(selectedRow, 0);

			// Confirm deletion with the user
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this employee?",
					"Confirm Deletion", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				db.deleteEmployeeData(employeeId);
				JOptionPane.showMessageDialog(null, "Employee deleted successfully.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				// Refresh table to reflect changes
				refreshTable();
			}
		});

		// sets the popup menu for the table
		table.setComponentPopupMenu(popupMenu);

	}

	public JPanel addPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Panel for adding employee information
		JPanel addEmployeePanel = new JPanel(new GridLayout(5, 2, 10, 10));
		addEmployeePanel.setBorder(new TitledBorder("Add Employee"));

		// Labels and text fields for employee information
		JLabel fNameLabel = new JLabel("First Name:");
		fNameField = new JTextField();
		JLabel lNameLabel = new JLabel("Last Name:");
		lNameField = new JTextField();
		JLabel addressLabel = new JLabel("Address:");
		addressField = new JTextField();
		JLabel typeLabel = new JLabel("Work Type:");
		comboBoxWorkType = new JComboBox<>();
		populateComboBox();

		// Add components to the add employee panel
		addEmployeePanel.add(fNameLabel);
		addEmployeePanel.add(fNameField);
		addEmployeePanel.add(lNameLabel);
		addEmployeePanel.add(lNameField);
		addEmployeePanel.add(addressLabel);
		addEmployeePanel.add(addressField);
		addEmployeePanel.add(typeLabel);
		addEmployeePanel.add(comboBoxWorkType);

		// Panel for CRUD buttons
		JPanel crudButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		addButton = new JButton("Add Employee");
		deleteButton = new JButton("Delete");
		resetButton = new JButton("Reset");
		JButton reloadButton = new JButton("Reload");

		// Add action listeners to buttons
		addButton.addActionListener(this::addButtonClicked);
		deleteButton.addActionListener(this::deleteButtonClicked);
		reloadButton.addActionListener(this::reloadButtonClicked);

		// Add buttons to CRUD panel
		crudButtonPanel.add(addButton);
		crudButtonPanel.add(deleteButton);
		crudButtonPanel.add(resetButton);
		crudButtonPanel.add(reloadButton);

		// Add components to main panel
		panel.add(addEmployeePanel, BorderLayout.CENTER);
		panel.add(crudButtonPanel, BorderLayout.SOUTH);

		return panel;
	}

	// Action listener methods for buttons

	private void addButtonClicked(ActionEvent e) {
		String firstName = fNameField.getText();
		String lastName = lNameField.getText();
		String address = addressField.getText();
		String workType = (String) comboBoxWorkType.getSelectedItem();
		String gender = null;

		if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//db.insertData(firstName, lastName, address, workType, gender);
		JOptionPane.showMessageDialog(null, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
		refreshTable();
	}

	private void refreshTable() {
		try {
			ResultSet rs = db.getAllData();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			while (rs.next()) {
				Object[] row = { rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("address"), rs.getString("work_type") };
				model.addRow(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to refresh employee data " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteButtonClicked(ActionEvent e) {
		String idString = JOptionPane.showInputDialog("Enter the ID of the record to delete:");
		if (idString != null && !idString.isEmpty()) {
			int id = Integer.parseInt(idString);
			db.deleteEmployeeData(id);
			reloadData(db);
		}
	}

	private void reloadButtonClicked(ActionEvent e) {
		reloadData(db);
	}

	private void populateComboBox() {
		try {
			ResultSet rs = db.getTypes(); 
			while (rs.next()) {
				comboBoxWorkType.addItem(rs.getString("work_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to fetch work types from the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
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
