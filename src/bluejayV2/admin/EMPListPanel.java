package bluejayV2.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import bluejay.Employee;
import bluejayDB.EmployeeDatabase;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;

public class EMPListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JScrollPane scrollPane;
	public JPanel searchPanel;

	public  JTable table;
	private  JPopupMenu popupMenu;
	private  JMenuItem menuItemRemove, menuItemEdit;
	public final Map<String, String> workTypeMap = new HashMap<>();
	private  String[] column = { "ID", "First Name", "Last Name", "Address", "Work Type", "Rate", "Gross Pay",
			"Net Pay" };
	private  EmployeeDatabase db;
	private  DefaultTableModel model = new DefaultTableModel(column, 0) {

		private static final long serialVersionUID = 4L;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// Make all cells editable except the first row ID[0], Work Type[4], Rate[5],
			// Gross[6], Net[7]

			boolean[] columnEditables = new boolean[] { false, true, true, true, false, true, false, false };
			return columnEditables[columnIndex];
		}
	};
	private JPanel panel;
	private JButton editBtn;
	private JButton saveBtn;
	private JLabel lblNewLabel;
	private JLabel errorLabel;

	public EMPListPanel() {
		try {
			db = new EmployeeDatabase();
			db.connect(); // Establish connection
			System.out.println("Connected to the database successfully.");
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to connect to the database." + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// create a JTable
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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
		table.setToolTipText("Right Click For Options"); // floating text on the table when
		table.setCellSelectionEnabled(true);
		table.setFont(new Font("Serif", Font.PLAIN, 18));
		table.setRowHeight(40);

		refreshTable();
		popupMenu();

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

			private void searchTable(String searchText) {
				TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
				table.setRowSorter(sorter);

				// Filter based on matching case-insensitive cells
				RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
				sorter.setRowFilter(filter);

				if (searchText.trim().length() == 0) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// I DONT KNOW THE USE OF THIS ONE
			}
		});
		searchPanel.setLayout(new MigLayout("", "[230.00px][40px][120.00px]", "[20px]"));
		
		errorLabel = new JLabel("Error Label");
		errorLabel.setForeground(Color.RED);
		searchPanel.add(errorLabel, "cell 0 0,alignx left,aligny center");
		searchPanel.add(new JLabel("Search: "), "cell 1 0,alignx left,aligny center");
		searchPanel.add(searchField, "cell 2 0,growx,aligny top");

		JScrollPane scrollPane = new JScrollPane(table);
		setLayout(new BorderLayout(10, 5));
		add(searchPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel, BorderLayout.SOUTH);

		ImageIcon saveIcon = new ImageIcon(getClass().getResource("/images/save.png"));
		saveBtn = new JButton(new ImageIcon(saveIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		saveBtn.setHorizontalAlignment(SwingConstants.LEADING);
		saveBtn.setOpaque(false);
		saveBtn.setContentAreaFilled(false);
		saveBtn.setBorderPainted(false);
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveToDB();

			}
		});
		saveBtn.setEnabled(false);
		panel.add(saveBtn);

		ImageIcon writeIcon = new ImageIcon(getClass().getResource("/images/write.png"));
		editBtn = new JButton(new ImageIcon(writeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		editBtn.setHorizontalAlignment(SwingConstants.LEADING);
		editBtn.setOpaque(false);
		editBtn.setContentAreaFilled(false);
		editBtn.setBorderPainted(false);
		panel.add(editBtn);

		// Add a table model listener to track changes in the table
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// Enable or disable save button based on modifications
				if (hasModifications()) {
					System.out.println("TRUE");
					saveBtn.setEnabled(true);
				} else {
					System.out.println("FALSE");
					saveBtn.setEnabled(false);
				}
			}
		});
	}

	// Method to check if there are modifications in the table
	private boolean hasModifications() {
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				Object originalValue = table.getValueAt(i, j);
				Object updatedValue = model.getValueAt(i, j);
				if (originalValue == null && updatedValue != null
						|| originalValue != null && !originalValue.equals(updatedValue)) {
					return false; // Found a modification
				}
			}
		}
		return true; // No modifications found
	}

	protected void saveToDB() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			return; // No row selected, nothing to update
		}

		// Retrieve updated information from the table
		int employeeId = (int) table.getValueAt(selectedRow, 0);
		String updatedFirstName = (String) table.getValueAt(selectedRow, 1);
		String updatedLastName = (String) table.getValueAt(selectedRow, 2);
		String updatedAddress = (String) table.getValueAt(selectedRow, 3);
		String updatedWorkType = (String) table.getValueAt(selectedRow, 4);
		Object value = table.getValueAt(selectedRow, 5);
		double updatedRate;
		if (value instanceof Double) {
			updatedRate = (Double) value;
		} else {
			updatedRate = Double.parseDouble(value.toString());
		}

		// Create an Employee object with the updated information
		Employee updatedEmployee = new Employee();
		updatedEmployee.setId(employeeId);
		updatedEmployee.setFirstName(updatedFirstName);
		updatedEmployee.setLastName(updatedLastName);
		updatedEmployee.setAddress(updatedAddress);
		updatedEmployee.setWorkType(updatedWorkType);
		updatedEmployee.setRate(updatedRate); // Set the updated rate

		saveBtn.setEnabled(false);
		// Update the employee information in the database
		db.updateEmployee(updatedEmployee);
		refreshTable();
	}

	private void popupMenu() {
		// constructs the popup menu
		popupMenu = new JPopupMenu();
		menuItemRemove = new JMenuItem("Remove Current Row");
		menuItemEdit = new JMenuItem("Edit Selected Row");
		popupMenu.add(menuItemRemove);
		popupMenu.add(menuItemEdit);

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

	private void calculatePay() {
		try {
			ResultSet rsEMP = db.getAllData();
			while (rsEMP.next()) {
				int employeeId = rsEMP.getInt("id");
				double rate = rsEMP.getDouble("rate");
				String firstName = rsEMP.getString("first_name");
				String lastName = rsEMP.getString("last_name");
				// String workType = rsEMP.getString("work_type");

				// Count the number of attendances for the current employee
				// double daysWorked = db.countAttendances(firstName + " " + lastName);

				// Calculate gross pay based on the rate and number of days worked
				Employee emp = new Employee();
				int daysworked = 15; // sample data
				emp.setPAG_IBIG(100);
				emp.setSSS(570);
				emp.setPHILHEALTH(500);
				double grossPay = emp.calculateGrossPay(daysworked, rate); // daysworked * rate;
				double netPay = emp.calculateNetPay(grossPay, emp.totalDeductions()); // grossPay - emp.total();
				emp.setNetPay(netPay);
				emp.setGrossPay(grossPay);

				// Update the corresponding cells in the table
				for (int i = 0; i < model.getRowCount(); i++) {
					if (model.getValueAt(i, 0).equals(employeeId)) {
						model.setValueAt(grossPay, i, 6); // Update Gross Pay column
						model.setValueAt(netPay, i, 7);

						db.updateData(employeeId, "grossPay", emp.getGrossPay());
						db.updateData(employeeId, "netPay", emp.getNetPay());
						System.out.println("CALCULATED!!");
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void refreshTable() {

		try {
			FlatAnimatedLafChange.showSnapshot();

			ResultSet rs = db.getAllData();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			while (rs.next()) {
				Object[] row = { rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("address"), rs.getString("work_type"), rs.getDouble("rate"),
						rs.getDouble("grossPay"), rs.getDouble("netPay") };
				System.out.println(rs.getDouble("netPay"));
				model.addRow(row);
			}
			calculatePay();
			FlatAnimatedLafChange.hideSnapshotWithAnimation();

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to refresh employee data " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
