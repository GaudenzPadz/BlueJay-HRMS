package bluejay;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import bluejayDB.EmployeeDatabase;

public class LIST {

	public static DefaultTableModel model;
	private static JTable table;
	private static JPopupMenu popupMenu;
	private static JMenuItem menuItemAdd, menuItemRemove;
	public static final Map<String, String> workTypeMap = new HashMap<>();

	public static JComboBox<String> comboBox;
	public JScrollPane scrollPane;
	public JPanel searchPanel;
	// Create column names
	private String[] column = { "ID", "First Name", "Last Name", "Address", "Work Type", "Rate", "Gross Pay",
			"Net Pay" };

	public final void workType(TableColumn column) {

		workTypeMap.put("Shielded Metal Arc Welding", "SMAW");
		workTypeMap.put("Gas Tungsten Arc Welding", "GTAW");
		workTypeMap.put("Flux-cored Arc Welding", "FCAW");
		workTypeMap.put("Gas Metal Arc Welding", "GMAW");
		workTypeMap.put("Manager", "CEO");
		workTypeMap.put("Other", "DEV");

		comboBox = new JComboBox<>();
		comboBox.addItem((String) getKeyFromValue(workTypeMap, "SMAW"));
		comboBox.addItem((String) getKeyFromValue(workTypeMap, "GTAW"));
		comboBox.addItem((String) getKeyFromValue(workTypeMap, "FCAW"));
		comboBox.addItem((String) getKeyFromValue(workTypeMap, "GMAW"));
		comboBox.addItem((String) getKeyFromValue(workTypeMap, "CEO"));
		comboBox.addItem((String) getKeyFromValue(workTypeMap, "DEV"));
		column.setCellEditor(new DefaultCellEditor(comboBox));

		// Set up tool tips for the sport cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click to Choose Work Type");
		column.setCellRenderer(renderer);
	}

	@SuppressWarnings("rawtypes")
	public static Object getKeyFromValue(Map hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o;
			}
		}
		return null;
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

	public static void idGeneration(JTable table, DefaultTableModel model) {
		// Get the selected work type from the table (assuming it's the 4th column)
		int selectedRow = table.getSelectedRow();
		String workType = (selectedRow != -1) ? (String) table.getValueAt(selectedRow, 4) : "";

		if (workType.isEmpty()) {
			// Handle no work type selected
			System.out.println("Please select a work type first.");
			return;
		}

		String workTypeAbbreviation = workTypeMap.get(workType);
		if (workTypeAbbreviation == null) {
			// Handle unknown work type (optional, can be left as is)
			System.out.println("Abbreviation not found for work type: " + workType);
			return;
		}

		// Find the maximum ID for the chosen work type
		int maxId = 0;
		for (int i = 0; i < model.getRowCount(); i++) {
			String id = (String) model.getValueAt(i, 0);
			if (id != null && id.startsWith(workTypeAbbreviation)) {
				try {
					int currentId = Integer.parseInt(id.substring(workTypeAbbreviation.length()));
					maxId = Math.max(maxId, currentId);
				} catch (NumberFormatException e) {
					// Ignore invalid ID format
				}
			}
		}

		// Generate a new ID based on the maximum ID and abbreviation
		int newId = maxId + 1;
		String generatedId = workTypeAbbreviation + String.format("%03d", newId);

		// Add a new row with the generated ID

		// Add the employee to the employee manager

		// Save the new employee to the database
	}

	private void popupMenu() {
		// constructs the popup menu
		popupMenu = new JPopupMenu();
		menuItemAdd = new JMenuItem("Add New Row");
		menuItemRemove = new JMenuItem("Remove Current Row");

		menuItemAdd.addActionListener((ActionEvent e) -> {
			idGeneration(table, model);
			// Load data from the database
			reloadData();
		});

		popupMenu.add(menuItemAdd);
		popupMenu.add(menuItemRemove);

		menuItemRemove.addActionListener((ActionEvent e) -> {
			int rowIndex = table.getSelectedRow();

			if (rowIndex < 0) {
				System.out.println("No row selected"); // For debugging
				return; // Exit if no row is selected
			}

			EmployeeDatabase db;
			try {
				// Create an instance of EmployeeDatabase
				db = new EmployeeDatabase();
				int id = (int) model.getValueAt(rowIndex, 0); // Assuming ID is at column 0
				db.deleteData(id); // Delete the employee from the database
				reloadData(); // Reload data to reflect changes
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		popupMenu.add(menuItemAdd);
		popupMenu.add(menuItemRemove);

		// sets the popup menu for the table
		table.setComponentPopupMenu(popupMenu);

	}

	private static void tableDesign() {
		TableColumnModel columnModel = table.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(90);
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

	public LIST() {

		// Create a data model/TableModel
		model = new DefaultTableModel(column, 0) {

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				// Make all cells editable except the first row ID[0], Rate[5], Gross[6], Net[7]

				boolean[] columnEditables = new boolean[] { false, true, true, true, true, false, false, false };
				return columnEditables[columnIndex];
			}
		};

		// create a JTable
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		workType(table.getColumnModel().getColumn(4));

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
		
		
		// Add JTable to a scroll pane
		scrollPane = new JScrollPane(table);
		// frame.add(scrollPane, BorderLayout.CENTER);

		// create a panel for search
		searchPanel = new JPanel();
		JTextField searchField = new JTextField(20);
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
		searchPanel.add(new JLabel("Search: "));
		searchPanel.add(searchField);

	}
	

	public void reloadData() {
		try {
			System.out.println("method is called reloadData()");

			model.setRowCount(0); // Clear existing data in the table model

			EmployeeDatabase db = new EmployeeDatabase(); // Create an instance of EmployeeDatabase
			ResultSet rs = db.getAllData(); // Fetch all employee data

			while (rs.next()) {
				// Add fetched data to the table model
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("address"), workTypeMap.getOrDefault(rs.getString("work_type"), "Unknown"), // Map
																													// work
																													// type
																													// to
																													// abbreviation
						rs.getDouble("rate"), 0.0, // Placeholder for Gross Pay, calculate as needed
						0.0 // Placeholder for Net Pay, calculate as needed
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
