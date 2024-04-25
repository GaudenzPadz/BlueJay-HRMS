package bluejayV2.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import bluejay.Employee;
import bluejayDB.EmployeeDatabase;
import bluejayV2.Main;
import net.miginfocom.swing.MigLayout;

public class AttendanceForm extends JPanel {

	private static final long serialVersionUID = 1L;
	private Employee employee;

	private JTextField hoursField_1;
	private JTextField minutesField_1;
	JComboBox<String> comboBox_1, AmPmCombo;
	private String[] column = { "Name", "Date", "Time In", "Time Out", "Overtime", "Note" };

	private DefaultTableModel model = new DefaultTableModel(column, 0) {

		@Override
		public boolean isCellEditable(int row, int column) {
			// all cells false
			return false;
		}
	};

	private JTable table;
	private JTextField hourField, minutesField, hoursOTField;
	private JButton timeINBtn, timeOUTBtn;
	private EmployeeDatabase DB;
	private String currentDate = LocalDate.now().toString(); // Current date as a string
	private JTextArea noteArea;
	private JCheckBox OTcheck;

	public AttendanceForm(Employee employee) {
		this.employee = employee;
		try {
			DB = new EmployeeDatabase();
			refreshTable();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (DB.hasCheckedIn(employee.getId(), currentDate)) {
			// if employee already time in
			showTimeOutButton();
		} else {
			// if new login
			showTimeInButton();
		}
		setupUI();
	}

	private void refreshTable() {
		FlatAnimatedLafChange.showSnapshot();
		model.setRowCount(0);
		DB.loadAttendanceData(employee.getId(), model);
		FlatAnimatedLafChange.hideSnapshotWithAnimation();
	}

	private void setupUI() {
		// Set layout
		setLayout(new BorderLayout());

		// Header Panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(0, 191, 255).darker());
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		headerPanel.setLayout(new MigLayout("", "[left][]", "[50px,grow]"));
		ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));

		JButton btnNewButton = new JButton("");
		btnNewButton.setOpaque(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setIcon(new ImageIcon(backIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.frame.replaceContentPane("Weld Well HRMS", new EmployeePanel(employee), getLayout());
			}
		});

		headerPanel.add(btnNewButton, "cell 0 0");

		JLabel lblNewLabel = new JLabel("Attendance Form");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		headerPanel.add(lblNewLabel, "cell 1 0");
		add(headerPanel, BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new MigLayout("fill,insets 20", "[center]", "[center][center]"));
		add(mainPanel, BorderLayout.CENTER);

		JPanel attForm = attendanceForm();
		mainPanel.add(attForm, "cell 0 0,alignx center,aligny center");

		JPanel attTable = attendanceTable();
		mainPanel.add(attTable, "cell 0 1,growx");

	}

	private JPanel attendanceForm() {
		JPanel attendanceForm = new JPanel(new MigLayout("wrap,fillx,insets 25 35 25 35",
				"[200px,center][30px][][200px]", "[][20.00][][][][][][]"));
		attendanceForm.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");

		// Time In Label
		JLabel lblTimeIn = new JLabel("Time In");
		attendanceForm.add(lblTimeIn, "cell 0 0,alignx left");

		// Hours Field
		hourField = new JTextField(2);
		hourField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceForm.add(hourField, "flowx,cell 0 1,alignx left");

		// Colon Label
		JLabel colon = new JLabel(":");
		attendanceForm.add(colon, "cell 0 1");

		// Minutes Field
		minutesField = new JTextField(2);
		minutesField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceForm.add(minutesField, "cell 0 1");

		// AM/PM ComboBox
		AmPmCombo = new JComboBox<>(new String[] { "AM", "PM" });
		attendanceForm.add(AmPmCombo, "cell 0 1");

		// Set current time to Time In fields
		setCurrentTimeToFields();
		ActionListener updateExpectedTimeOut = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int hours = Integer.parseInt(hourField.getText());
					int minutes = Integer.parseInt(minutesField.getText());
					String ampm = (String) AmPmCombo.getSelectedItem();

					if (ampm.equals("PM") && hours < 12) {
						hours += 12; // Convert to 24-hour time
					} else if (ampm.equals("AM") && hours == 12) {
						hours = 0; // Midnight is 0 in 24-hour time
					}

					// Add 8 hours to calculate expected time out
					hours += 8;

					if (hours >= 24) {
						hours -= 24; // Keep it within 24-hour range
					}

					String expectedTimeOutAmpm = (hours >= 12) ? "PM" : "AM";
					if (hours > 12) {
						hours -= 12;
					}

					hoursField_1.setText(String.format("%02d", hours)); // Update Expected Time Out
					minutesField_1.setText(String.format("%02d", minutes));
					comboBox_1.setSelectedItem(expectedTimeOutAmpm);

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid time format. Please enter valid hours and minutes.");
				}
			}
		};

		// Attach the action listener to the Time In components
		hourField.addActionListener(updateExpectedTimeOut);
		minutesField.addActionListener(updateExpectedTimeOut);
		AmPmCombo.addActionListener(updateExpectedTimeOut);

		JLabel lblHowManyHours = new JLabel("How many Hours? :");
		lblHowManyHours.setEnabled(false);
		attendanceForm.add(lblHowManyHours, "flowx,cell 3 1,alignx left");

		hoursOTField = new JTextField();
		hoursOTField.setEnabled(false);
		attendanceForm.add(hoursOTField, "cell 3 1");
		hoursOTField.setColumns(5);
		OTcheck = new JCheckBox("Overtime?");
		OTcheck.addActionListener((ActionEvent e) -> {
			boolean selected = OTcheck.isSelected();
			lblHowManyHours.setEnabled(selected);
			hoursOTField.setEnabled(selected);
		});
		if (OTcheck.isSelected()) {
			lblHowManyHours.setEnabled(true);
			hoursOTField.setEnabled(true);
		} else {
			hoursOTField.setEnabled(false);
			lblHowManyHours.setEnabled(false);
		}
		attendanceForm.add(OTcheck, "cell 3 0");

		JLabel noteLabel = new JLabel("Note");
		attendanceForm.add(noteLabel, "cell 3 2,alignx center,aligny center");

		noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		attendanceForm.add(noteArea, "cell 3 3 1 2,grow");

		JButton timeINBtn = showTimeInButton();
		attendanceForm.add(timeINBtn, "cell 3 6,alignx center,aligny center");

		// Expected Time Out Label
		JLabel expectedOutLabel = new JLabel("Expected Time Out");
		attendanceForm.add(expectedOutLabel, "cell 0 3,alignx left");

		// Expected Time Out Hours Field
		hoursField_1 = new JTextField(2); // Two digits for hours
		attendanceForm.add(hoursField_1, "flowx,cell 0 4,alignx left");

		// Colon Label for Expected Time Out
		JLabel colonLabel2 = new JLabel(":");
		attendanceForm.add(colonLabel2, "cell 0 4");

		// Expected Time Out Minutes Field
		minutesField_1 = new JTextField(2); // Two digits for minutes
		attendanceForm.add(minutesField_1, "cell 0 4");

		// Expected Time Out AM/PM ComboBox
		comboBox_1 = new JComboBox<>(new String[] { "AM", "PM" });
		attendanceForm.add(comboBox_1, "cell 0 4");
		return attendanceForm;
	}

	private JPanel attendanceTable() {
		// create a JTable
		table = new JTable(model);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableColumnModel columnModel = table.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(200);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(300);
		columnModel.getColumn(3).setPreferredWidth(300);
		columnModel.getColumn(4).setPreferredWidth(100);
		table.setFont(new Font("Serif", Font.PLAIN, 18));
		table.setRowHeight(40);
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 25 35 25 35", "[200px,center]", "[]"));
		panel.add(scrollPane, "grow");
		return panel;
	}

	private void setCurrentTimeToFields() {
		Calendar now = Calendar.getInstance();
		int hours = now.get(Calendar.HOUR);
		int minutes = now.get(Calendar.MINUTE);
		boolean isPM = now.get(Calendar.AM_PM) == Calendar.PM;

		hourField.setText(String.format("%02d", hours));
		minutesField.setText(String.format("%02d", minutes));
		AmPmCombo.setSelectedItem(isPM ? "PM" : "AM");
	}

	private JButton showTimeInButton() {
		timeINBtn = new JButton("Time In");
		timeINBtn.addActionListener(e -> {
			String timeIn = hourField.getText() + ":" + minutesField.getText() + " " + AmPmCombo.getSelectedItem();
			// public void insertAttendance(int employeeId, String date, String timeIn,
			// String overtime, String note) {
			DB.insertAttendance(employee.getId(), employee.getFirstName() + " " + employee.getLastName(), currentDate,
					timeIn, hoursOTField.getText(), noteArea.getText());

			JOptionPane.showMessageDialog(this, "Time In recorded.");
			refreshTable();
		});

		// Add the button to your UI
		return timeINBtn;
	}

	private void showTimeOutButton() {
		timeOUTBtn = new JButton("Time Out");
		timeOUTBtn.addActionListener(e -> {
			String timeOut = hourField.getText() + ":" + minutesField.getText() + " " + AmPmCombo.getSelectedItem();
			DB.updateTimeOut(employee.getId(), currentDate, timeOut);

			JOptionPane.showMessageDialog(this, "Time Out recorded.");
		});

	}
}
