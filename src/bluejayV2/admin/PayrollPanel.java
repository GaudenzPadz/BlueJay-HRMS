package bluejayV2.admin;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;
import net.miginfocom.swing.MigLayout;
import javax.swing.ScrollPaneConstants;

public class PayrollPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField tfEmployeeName, tfEmployeeDepartment, tfEmployeeWorkType, tfRatePerDay, tfDaysWorked,
			tfOvertimeHours, tfGrossPay, tfOvertimeRate, tfDeductionsSSS, tfDeductionsPagIbig, tfDeductionsPhilHealth,
			tfTotalDeductions, tfAdvanced, tfBonus, tfNetPay;
	private JButton btnCalculate, btnClear;
	private JTable payrollHistoryTable;
	private EmployeeDatabase db;
	private JTextField tfEmployeeID;
	private JComboBox<String> employeeComboBox = new JComboBox<String>();
	private List<Employee> employees;
	private JTextField searchEMPField;
	private JTable EMPListTable;
	private DefaultTableModel EMPListModel;
	private JScrollPane scrollToCalculationPane;
	private JTextField textField;
	private DefaultTableModel tableModel = new DefaultTableModel(
			new String[] { "ID", "Name", "Department", "Work Type", "Gross Pay", "Rate Per Day",
					"Days Worked", "Overtime Hours", "Bonus", "Total Deductions", "Net Pay" },
			0);
	
	public PayrollPanel(EmployeeDatabase DB) {
		this.db = DB;
		employees = DB.getAllEmployees(); // Fetch all employees

		// setLayout(new MigLayout("wrap, fillx, insets 25 35 25 35", "[center]",
		// "[center][grow][][]"));

		scrollToCalculationPane = new JScrollPane();
		add(scrollToCalculationPane, "cell 0 0, grow");

		JPanel mainPane = new JPanel(
				new MigLayout("wrap, fillx, insets 25 35 25 35", "[center]", "[center][grow][][]"));
		scrollToCalculationPane.setViewportView(mainPane);
		// Set up the employee selector panel
		JPanel selectEMP = setupEmployeeSelectorPanel();
		mainPane.add(selectEMP, "growx");
		// Set up the payroll calculation panel
		JPanel payrollCalculationPanel = setupPayrollCalculationPanel();
		mainPane.add(payrollCalculationPanel);
	}

	private JPanel setupEmployeeSelectorPanel() {
		JPanel selectEmployeePanel = new JPanel(
				new MigLayout("", "[100px,grow,center][][][][grow][][][][][100px,grow,center]", "[center][][100px]"));

		EMPListModel = new DefaultTableModel(
				new String[] { "Employee ID", "First Name", "Last Name", "Department", "Work Type", "Select" }, 0);

		EMPListTable = new JTable(EMPListModel);

		// Add a custom renderer for the "Select" button
		EMPListTable.getColumn("Select").setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				return new JButton("Select"); // The button for selection
			}
		});

		// Add an action listener for button clicks in the "Select" column
		EMPListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = EMPListTable.rowAtPoint(e.getPoint());
				int col = EMPListTable.columnAtPoint(e.getPoint());

				if (col == 5) { // The "Select" button column
					updateEmployeeDataFromSelection(row);
				}
			}
		});

		JScrollPane empListScrollPane = new JScrollPane(EMPListTable);

		populateEmployeeList();

		JLabel lblSelectEmployee = new JLabel("Select Employee");
		lblSelectEmployee.setFont(new Font("SansSerif", Font.BOLD, 20));
		selectEmployeePanel.add(lblSelectEmployee, "cell 0 0 10 1,alignx center");

		JLabel lblNewLabel = new JLabel("Search");
		selectEmployeePanel.add(lblNewLabel, "cell 1 1 3 1");

		textField = new JTextField();
		selectEmployeePanel.add(textField, "cell 4 1,growx");
		textField.setColumns(10);

		selectEmployeePanel.add(empListScrollPane, "cell 0 2 10 1,growx");

		return selectEmployeePanel;

	}

	private void updateEmployeeDataFromSelection(int rowIndex) {

		Employee selectedEmployee = employees.get(rowIndex);
		FlatAnimatedLafChange.showSnapshot();

		tfEmployeeID.setText(String.valueOf(selectedEmployee.getId()));
		tfEmployeeName.setText(selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
		tfEmployeeDepartment.setText(selectedEmployee.getDepartment());
		tfEmployeeWorkType.setText(selectedEmployee.getWorkType());
		tfRatePerDay.setText(String.valueOf(selectedEmployee.getRatePerDay()));

		calculatePayroll(); // Calculate payroll after updating data

		revalidate();
		repaint();
		FlatAnimatedLafChange.hideSnapshotWithAnimation();

	}

	private void populateEmployeeList() {
		if (employees == null) {
			employees = new ArrayList<>();
			return;
		}

		EMPListModel.setRowCount(0); // Clear existing rows

		for (Employee employee : employees) {
			EMPListModel.addRow(new Object[] { employee.getId(), employee.getFirstName(), employee.getLastName(),
					employee.getDepartment(), employee.getWorkType(), "Select" // Add the "Select" button
			});
		}
	}

	private JPanel setupPayrollCalculationPanel() {

		JPanel payrollCalculationPanel = new JPanel(
				new MigLayout("", "[100px,grow,center][][][][][][][][][100px,grow,center]",
						"[center][][][][][][][][][][][100px,grow][center]"));
		JLabel lblEmployeeID = new JLabel("Employee ID:");
		payrollCalculationPanel.add(lblEmployeeID, "cell 1 0,alignx left");

		tfEmployeeID = new JTextField(4);
		payrollCalculationPanel.add(tfEmployeeID, "cell 2 0,growx");

		JLabel lblEmployeeName = new JLabel("Employee Name:");
		payrollCalculationPanel.add(lblEmployeeName, "cell 1 1,alignx left");

		tfEmployeeName = new JTextField();
		payrollCalculationPanel.add(tfEmployeeName, "cell 2 1,growx");
		tfEmployeeName.setEditable(false);

		JLabel lblPayrollCalculation = new JLabel("Payroll Calculation");
		payrollCalculationPanel.add(lblPayrollCalculation, "cell 4 1 2 1,alignx left");
		lblPayrollCalculation.setFont(new Font("SansSerif", Font.BOLD, 20));

		JLabel lblDeductions = new JLabel("Deductions");
		payrollCalculationPanel.add(lblDeductions, "cell 7 1");
		lblDeductions.setFont(new Font("SansSerif", Font.BOLD, 20));

		JLabel lblEmployeeDepartment = new JLabel("Department:");
		payrollCalculationPanel.add(lblEmployeeDepartment, "cell 1 2,alignx left");

		tfEmployeeDepartment = new JTextField();
		payrollCalculationPanel.add(tfEmployeeDepartment, "cell 2 2,growx");
		tfEmployeeDepartment.setEditable(false);

		JLabel lblRatePerDay = new JLabel("Rate Per Day:");
		payrollCalculationPanel.add(lblRatePerDay, "cell 4 2,alignx left");

		tfRatePerDay = new JTextField();
		payrollCalculationPanel.add(tfRatePerDay, "cell 5 2,growx");

		JLabel lblDeductionsSSS = new JLabel("SSS:");
		payrollCalculationPanel.add(lblDeductionsSSS, "cell 7 2,alignx left");

		tfDeductionsSSS = new JTextField();
		payrollCalculationPanel.add(tfDeductionsSSS, "cell 8 2,growx");
		tfDeductionsSSS.setColumns(10);

		JLabel lblEmployeeWorkType = new JLabel("Work Type:");
		payrollCalculationPanel.add(lblEmployeeWorkType, "cell 1 3,alignx left");

		tfEmployeeWorkType = new JTextField();
		payrollCalculationPanel.add(tfEmployeeWorkType, "cell 2 3,growx");
		tfEmployeeWorkType.setEditable(false);

		JLabel lblDaysWorked = new JLabel("Days Worked:");
		payrollCalculationPanel.add(lblDaysWorked, "cell 4 3,alignx left");

		tfDaysWorked = new JTextField();
		payrollCalculationPanel.add(tfDaysWorked, "cell 5 3,growx");

		JLabel lblDeductionsPagIbig = new JLabel("PAG-Ibig:");
		payrollCalculationPanel.add(lblDeductionsPagIbig, "cell 7 3,alignx left");

		tfDeductionsPagIbig = new JTextField();
		payrollCalculationPanel.add(tfDeductionsPagIbig, "cell 8 3,growx");
		tfDeductionsPagIbig.setColumns(10);

		JLabel lblGrossPay = new JLabel("Gross Pay:");
		payrollCalculationPanel.add(lblGrossPay, "cell 1 4,alignx left");

		tfGrossPay = new JTextField();
		payrollCalculationPanel.add(tfGrossPay, "cell 2 4,growx");
		tfGrossPay.setColumns(10);

		JLabel lblOvertimeHours = new JLabel("Overtime Hours:");
		payrollCalculationPanel.add(lblOvertimeHours, "cell 4 4,alignx left");

		tfOvertimeHours = new JTextField();
		payrollCalculationPanel.add(tfOvertimeHours, "cell 5 4,growx");
		tfOvertimeHours.setColumns(10);

		JLabel lblDeductionsPhilHealth = new JLabel("PhilHealth:");
		payrollCalculationPanel.add(lblDeductionsPhilHealth, "cell 7 4,alignx left");

		tfDeductionsPhilHealth = new JTextField();
		payrollCalculationPanel.add(tfDeductionsPhilHealth, "cell 8 4,growx");

		JLabel lblBasicSalary = new JLabel("Basic Salary:");
		payrollCalculationPanel.add(lblBasicSalary, "cell 1 5,alignx left");

		JTextField tfBasicSalary = new JTextField();
		payrollCalculationPanel.add(tfBasicSalary, "cell 2 5,growx");
		tfBasicSalary.setColumns(10);

		JLabel lblOvertimeRate = new JLabel("Overtime Rate:");
		payrollCalculationPanel.add(lblOvertimeRate, "cell 4 5,alignx left");

		tfOvertimeRate = new JTextField();
		payrollCalculationPanel.add(tfOvertimeRate, "cell 5 5,growx");
		tfOvertimeRate.setColumns(10);

		JLabel lblAdvanced = new JLabel("Advanced:");
		payrollCalculationPanel.add(lblAdvanced, "cell 7 5,alignx left");

		tfAdvanced = new JTextField();
		payrollCalculationPanel.add(tfAdvanced, "cell 8 5,growx");
		tfAdvanced.setColumns(10);

		JLabel lblBonus = new JLabel("Bonus:");
		payrollCalculationPanel.add(lblBonus, "cell 4 6,alignx left");

		tfBonus = new JTextField();
		payrollCalculationPanel.add(tfBonus, "cell 5 6,growx");
		tfBonus.setColumns(10);

		JLabel lblTotalDeductions = new JLabel("Total Deductions:");
		payrollCalculationPanel.add(lblTotalDeductions, "cell 7 6,alignx left");

		tfTotalDeductions = new JTextField();
		payrollCalculationPanel.add(tfTotalDeductions, "cell 8 6,growx");
		tfTotalDeductions.setColumns(10);

		JLabel lblNetPay = new JLabel("Net Pay:");
		payrollCalculationPanel.add(lblNetPay, "cell 7 7,alignx center");

		tfNetPay = new JTextField();
		payrollCalculationPanel.add(tfNetPay, "cell 8 7,growx");
		tfNetPay.setEditable(false);

		btnClear = new JButton("Clear");
		payrollCalculationPanel.add(btnClear, "cell 7 8,growx");

		btnCalculate = new JButton("Calculate");
		payrollCalculationPanel.add(btnCalculate, "cell 7 9,growx");
		
		payrollHistoryTable = new JTable(tableModel);
		payrollHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableColumnModel columnModel = payrollHistoryTable.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(0).setResizable(false);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(2).setPreferredWidth(200);
		columnModel.getColumn(3).setPreferredWidth(200);
		columnModel.getColumn(4).setPreferredWidth(100);
		columnModel.getColumn(5).setPreferredWidth(100);
		columnModel.getColumn(6).setPreferredWidth(100);
		columnModel.getColumn(7).setPreferredWidth(100);
		columnModel.getColumn(8).setPreferredWidth(100);
		columnModel.getColumn(9).setPreferredWidth(100);
		columnModel.getColumn(10).setPreferredWidth(100);
		refreshTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(payrollHistoryTable);
		payrollCalculationPanel.add(scrollPane, "cell 0 11 10 1,grow");
		btnCalculate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calculatePayroll();
			}
		});

		payrollCalculationPanel.add(scrollPane, "cell 0 11 10 1, grow");

		return payrollCalculationPanel;

	}

	private void calculatePayroll() {
		try {
			int employeeId = Integer.parseInt(tfEmployeeID.getText());
			String employeeName = tfEmployeeName.getText();
			String employeeDepartment = tfEmployeeDepartment.getText();
			String employeeWorkType = tfEmployeeWorkType.getText();

			double ratePerDay = Double.parseDouble(tfRatePerDay.getText());
			int daysWorked = Integer.parseInt(tfDaysWorked.getText());
			double overtimeHours = Double.parseDouble(tfOvertimeHours.getText());
			double overtimeRate = Double.parseDouble(tfOvertimeRate.getText());
			double advanced = Double.parseDouble(tfAdvanced.getText());
			double bonus = Double.parseDouble(tfBonus.getText());

			double basicSalary = ratePerDay * daysWorked;
			double overtimePay = overtimeHours * overtimeRate;
			double grossPay = basicSalary + overtimePay + bonus;

			double sss = Double.parseDouble(tfDeductionsSSS.getText());
			double pagIbig = Double.parseDouble(tfDeductionsPagIbig.getText());
			double philHealth = Double.parseDouble(tfDeductionsPhilHealth.getText());

			double totalDeductions = sss + pagIbig + philHealth + advanced;
			double netPay = grossPay - totalDeductions;

			tfGrossPay.setText(String.format("%.2f", grossPay));
			tfTotalDeductions.setText(String.format("%.2f", totalDeductions));
			tfNetPay.setText(String.format("%.2f", netPay));

			tableModel.addRow(new Object[] { employeeId, employeeName, employeeDepartment, employeeWorkType,
					String.format("%.2f", grossPay), String.format("%.2f", ratePerDay), daysWorked,
					String.format("%.2f", overtimeHours), String.format("%.2f", bonus),
					String.format("%.2f", totalDeductions), String.format("%.2f", netPay) });

			db.insertPayroll(employeeId, employeeName, employeeDepartment, employeeWorkType, grossPay, ratePerDay,
					daysWorked, (int) overtimeHours, bonus, totalDeductions, netPay,
					new Date(System.currentTimeMillis()));
			
			db.loadPayrollHistory(tableModel);
			} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid numeric values for all payroll fields.",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void refreshTable(DefaultTableModel model) {
		model.setRowCount(0); // Clear existing rows
		db.loadPayrollHistory(model); // Load the updated payroll history

		FlatAnimatedLafChange.hideSnapshotWithAnimation(); // Provide smooth transition
	}

}
