package testApp;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;

public class PayrollSystem extends JFrame implements ActionListener {
	private JTextField tfEmployeeName, tfDaysWorked, tfRatePerDay;
	private JButton btnCalculate, btnClear;
	private JLabel lblEmployeeName, lblDaysWorked, lblRatePerDay, lblTotalPay;
	private JTable payrollHistoryTable;
	private DefaultTableModel tableModel;

	public PayrollSystem() {
		setTitle("Welding Shop Payroll System");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		lblEmployeeName = new JLabel("Employee Name:");
		lblEmployeeName.setBounds(10, 10, 100, 20);
		add(lblEmployeeName);

		tfEmployeeName = new JTextField();
		tfEmployeeName.setBounds(120, 10, 150, 20);
		add(tfEmployeeName);

		lblDaysWorked = new JLabel("Days Worked:");
		lblDaysWorked.setBounds(10, 40, 100, 20);
		add(lblDaysWorked);

		tfDaysWorked = new JTextField();
		tfDaysWorked.setBounds(120, 40, 150, 20);
		add(tfDaysWorked);

		lblRatePerDay = new JLabel("Rate Per Day:");
		lblRatePerDay.setBounds(10, 70, 100, 20);
		add(lblRatePerDay);

		tfRatePerDay = new JTextField();
		tfRatePerDay.setBounds(120, 70, 150, 20);
		add(tfRatePerDay);

		btnCalculate = new JButton("Calculate");
		btnCalculate.setBounds(300, 10, 100, 20);
		btnCalculate.addActionListener(this);
		add(btnCalculate);

		btnClear = new JButton("Clear");
		btnClear.setBounds(300, 40, 100, 20);
		btnClear.addActionListener(this);
		add(btnClear);

		lblTotalPay = new JLabel();
		lblTotalPay.setBounds(10, 100, 300, 20);
		add(lblTotalPay);

		tableModel = new DefaultTableModel();
		payrollHistoryTable = new JTable(tableModel);
		tableModel.addColumn("Employee Name");
		tableModel.addColumn("Days Worked");
		tableModel.addColumn("Rate Per Day");
		tableModel.addColumn("Total Pay");
		JScrollPane scrollPane = new JScrollPane(payrollHistoryTable);
		scrollPane.setBounds(10, 130, 460, 100);
		add(scrollPane);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCalculate) {
			String name = tfEmployeeName.getText();

			try {
				int daysWorked = Integer.parseInt(tfDaysWorked.getText());
				double ratePerDay = Double.parseDouble(tfRatePerDay.getText());

				if (daysWorked <= 15) {
					double totalPay = daysWorked * ratePerDay;
					lblTotalPay.setText("Total Pay for " + name + ": PHP " + totalPay);

					tableModel.addRow(new Object[] { name, daysWorked, ratePerDay, totalPay });

					//method to save to database 
				} else {
					lblTotalPay.setText("Maximum days for calculation is 15.");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a valid number for Days Worked and Rate Per Day.",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == btnClear) {
			tfEmployeeName.setText("");
			tfDaysWorked.setText("");
			tfRatePerDay.setText("");
			lblTotalPay.setText("");
		}
	}

	public static void main(String[] args) {
		PayrollSystem payrollSystem = new PayrollSystem();
		payrollSystem.setVisible(true);
	}
}