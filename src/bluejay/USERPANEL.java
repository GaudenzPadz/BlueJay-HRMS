package bluejay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;

// calendar
final class MonthPanel extends JPanel {

	private final int month;
	private final int year;
	private static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December" };
	private static final String[] DAY_NAMES = { "S", "M", "T", "W", "T", "F", "S" };

	public ATTENDANCE user;

	public MonthPanel(int month, int year, ATTENDANCE user) {
		this.month = month;
		this.year = year;
		this.user = user;
		setLayout(new BorderLayout());
		setOpaque(false); // Make panel transparent
		add(createGUI());
	}

	protected JPanel createGUI() {
		JPanel monthPanel = new JPanel(new BorderLayout());
		monthPanel.setBorder(BorderFactory.createLineBorder(SystemColor.activeCaption));
		monthPanel.setBackground(Color.WHITE);
		monthPanel.setForeground(Color.BLACK);
		monthPanel.add(createTitleGUI(), BorderLayout.NORTH);
		monthPanel.add(createDaysGUI(), BorderLayout.CENTER);
		return monthPanel;
	}

	protected JPanel createTitleGUI() {
		JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.setBorder(BorderFactory.createLineBorder(SystemColor.activeCaption));
		titlePanel.setBackground(Color.WHITE);
		JLabel label = new JLabel(MONTH_NAMES[month] + " " + year);
		label.setForeground(SystemColor.activeCaption);
		titlePanel.add(label);
		return titlePanel;
	}

	protected JPanel createDaysGUI() {
		JPanel dayPanel = new JPanel(new GridLayout(0, DAY_NAMES.length));
		dayPanel.setPreferredSize(new Dimension(250, 150));

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		int startDay = calendar.get(Calendar.DAY_OF_WEEK);
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int i = 1; i < startDay; i++) {
			dayPanel.add(new JPanel()); // Empty panels for days before the start of the month
		}

		for (int i = 1; i <= daysInMonth; i++) {
			JPanel dPanel = createDayPanel(i);
			dayPanel.add(dPanel);
		}

		return dayPanel;
	}

	private JPanel createDayPanel(int day) {
		JPanel dPanel = new JPanel();
		dPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel dayLabel = new JLabel(String.valueOf(day));
		dayLabel.setFont(new Font("Arial", Font.BOLD, 16));
		dPanel.add(dayLabel);

		Calendar temp = Calendar.getInstance();
		temp.set(year, month, day);
		Color bgColor = getBackgroundColor(temp);
		dPanel.setBackground(bgColor);

		return dPanel;
	}

	private Color getBackgroundColor(Calendar date) {
		int day = date.get(Calendar.DAY_OF_MONTH);
		int colorMonth = date.get(Calendar.MONTH);
		int colorYear = date.get(Calendar.YEAR);

//		if (user.isAbsent(day) && this.month == colorMonth && this.year == colorYear) {
		return Color.RED;
//		} else {
//			return Color.WHITE;
//		}
	}
}

public class USERPANEL extends JPanel implements Runnable {

	private JTable attendanceTable;
	private JTextField firstNameField, middleNameField, workTypeField, basicSalaryField, grossPayField, netPayField,
			allowancesField, taxField, absencesNumField, pagibigField, dayoffNum, SSSField, addressField, telNumField,
			emailField, lastNameField, lateNum, leaveNum, totalField, idField;

	private JButton printButton, addAttendanceButton;
	private JLabel pagibigLabel, SSSLabel, totalLabel, taxLabel, profilePicture;
	private JPanel userInfoSub, picturePanel, attendancePanel, printPanel, payReportPanel, salary, deductions;

	private MonthPanel panel;
	private ATTENDANCE attendance = new ATTENDANCE();;
	private int employeeId = 1; // Example ID
	private EmployeeDatabase employeeDb;
	private String[] column = { "Date", "Time In", "Time Out", "Overtime" };
	private DefaultTableModel model = new DefaultTableModel(column, 0) {
		private static final long serialVersionUID = 90L;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {

			return false;
		}
	};

	@Override
	public void run() {
		setMonthPanel(); // Initialize the panel
		revalidate();
		repaint();
	}

	public USERPANEL() {
		try {
			employeeDb = new EmployeeDatabase(); // Initialize the EmployeeDatabase

			setMonthPanel();
			setSize(932, 572);
			setLayout(new BorderLayout(10, 10));

			JPanel userInfoPanel = createUserInfoPanel(); // This method will now use the database
			add(userInfoPanel, BorderLayout.WEST);

			// Other initializations...
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions (e.g., ClassNotFoundException, SQLException)
		}

		setMonthPanel();
		setSize(932, 572);
		setLayout(new BorderLayout(10, 10));

		JPanel userInfoPanel = createUserInfoPanel();
		add(userInfoPanel, BorderLayout.WEST);

		attendancePanel = createAttendancePanel();
		add(attendancePanel, BorderLayout.CENTER);

		JPanel payrollPanel = createPayrollPanel();
		add(payrollPanel, BorderLayout.EAST);

		JPanel summaryPanel = createSummaryPanel();
		add(summaryPanel, BorderLayout.NORTH);

		ActionListener logoutListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Implement context-specific logic for frame disposal
				// userGui.dispose(); // Or adminGUI.dispose();
				Main.userGui.dispose();

				// revalidate loginGUI
				Main.loginGUI = new GUI("Login", Main.loginFrame, 400, 650, false, true);
				Main.loginGUI.revalidate();
				Main.loginGUI.repaint();
				Main.loginGUI.setVisible(true);
			}
		};

		add(LOGIN.logoutPanel(logoutListener), BorderLayout.SOUTH);
	}

	private JPanel createUserInfoPanel() {
		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "User Information"));
		userInfoPanel.setLayout(new GridLayout(3, 1, 5, 3)); // Reduced to 1 column

		// Profile Picture Panel
		picturePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout instead of BoxLayout
		userInfoPanel.add(picturePanel);

		// Profile Picture Label
		profilePicture = new JLabel("Profile Picture Here:");
		picturePanel.add(profilePicture);

		// Upload Button
		JButton uploadBtn = new JButton("Upload");
		uploadBtn.addActionListener((ActionEvent evt) -> {
			// method to upload an image from local drive into the database
		});
		picturePanel.add(uploadBtn);

		// User Information Subpanel
		userInfoSub = new JPanel(new GridLayout(5, 2, 5, 3));
		userInfoPanel.add(userInfoSub);

		// Labels and Text Fields
		userInfoSub.add(new JLabel("ID : "));
		idField = new JTextField();

		userInfoSub.add(idField);
		userInfoSub.add(new JLabel("First Name : "));
		firstNameField = new JTextField();
		userInfoSub.add(firstNameField);

		userInfoSub.add(new JLabel("Middle Name:"));
		middleNameField = new JTextField();
		userInfoSub.add(middleNameField);
		// middleNameField.setText(employee.getMiddleName());

		userInfoSub.add(new JLabel("Last Name :"));
		lastNameField = new JTextField();
		userInfoSub.add(lastNameField);

		userInfoSub.add(new JLabel("Work Type :"));
		workTypeField = new JTextField();
		workTypeField.setEditable(false);
		userInfoSub.add(workTypeField);

		// Other Information Subpanel
		JPanel otherInfo = new JPanel(new GridLayout(4, 2, 5, 3));
		userInfoPanel.add(otherInfo);

		// Labels and Text Fields
		otherInfo.add(new JLabel("Address : "));
		addressField = new JTextField();
		otherInfo.add(addressField);

		otherInfo.add(new JLabel("Tel. Number : "));
		telNumField = new JTextField();
		otherInfo.add(telNumField);

		otherInfo.add(new JLabel("Email : "));
		emailField = new JTextField();
		otherInfo.add(emailField);

		otherInfo.add(new JLabel("Gender : "));

		// Gender Radio Buttons
		JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ButtonGroup genderGroup = new ButtonGroup();
		JRadioButton maleRadio = new JRadioButton("Male");
		JRadioButton femaleRadio = new JRadioButton("Female");
		genderGroup.add(maleRadio);
		genderGroup.add(femaleRadio);
		genderPanel.add(maleRadio);
		genderPanel.add(femaleRadio);
		otherInfo.add(genderPanel);

		try {
			Employee employee = employeeDb.getEmployeeById(employeeId);
			if (employee != null) {
				// Update UI components with employee data
				idField.setText(String.valueOf(employee.getId()));
				firstNameField.setText(employee.getFirstName());
				lastNameField.setText(employee.getLastName());
				// Assume middleName and workType are available in the Employee class
				middleNameField.setText(employee.getMiddleName());
				workTypeField.setText(employee.getWorkType());
			} else {
				// Handle case where employee is not found
				System.out.println("Employee not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle SQL exceptions
		}

		return userInfoPanel;
	}

	private JPanel createAttendancePanel() {
		attendancePanel = new JPanel(new BorderLayout());
		attendancePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Attendance"));

		JPanel calendarPanel = new JPanel();
		calendarPanel.add(panel, BorderLayout.CENTER);
		attendancePanel.add(calendarPanel, BorderLayout.NORTH);

		attendanceTable = new JTable(model);
		attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		attendanceTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
		JScrollPane sp = new JScrollPane(attendanceTable);
		attendancePanel.add(sp, BorderLayout.CENTER);

		JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel timeInLabel = new JLabel("Time In:");
		inputPanel.add(timeInLabel);

		// Create the SpinnerDateModel for timeInSpinner
		SpinnerDateModel timeInSpinnerModel = new SpinnerDateModel();
		timeInSpinnerModel.setCalendarField(Calendar.MINUTE); // Set to minute precision

		JSpinner timeInSpinner = new JSpinner(timeInSpinnerModel);
		JSpinner.DateEditor timeInEditor = new JSpinner.DateEditor(timeInSpinner, "hh:mm a");
		timeInSpinner.setEditor(timeInEditor);
		inputPanel.add(timeInSpinner);

		JLabel timeOutLabel = new JLabel("Time Out:");
		inputPanel.add(timeOutLabel);

		// Create a separate SpinnerDateModel for timeOutSpinner
		SpinnerDateModel timeOutSpinnerModel = new SpinnerDateModel();
		timeOutSpinnerModel.setCalendarField(Calendar.MINUTE); // Set to minute precision

		JSpinner timeOutSpinner = new JSpinner(timeOutSpinnerModel);
		JSpinner.DateEditor timeOutEditor = new JSpinner.DateEditor(timeOutSpinner, "hh:mm a");
		timeOutSpinner.setEditor(timeOutEditor);
		inputPanel.add(timeOutSpinner);

		JLabel overtimeLabel = new JLabel("Overtime (how many hours?) :");
		inputPanel.add(overtimeLabel);

		JSpinner overtime = new JSpinner();
		inputPanel.add(overtime);

		addAttendanceButton = new JButton("Add Attendance");
		addAttendanceButton.addActionListener(e -> {
			// Retrieve data from input fields
			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();

			// Convert util Date to sql Date
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());

			// Get time from Spinner
			Date timeInDate = (Date) timeInSpinner.getValue();
			Time timeIn = new Time(timeInDate.getTime());

			Date timeOutDate = (Date) timeOutSpinner.getValue();
			Time timeOut = new Time(timeOutDate.getTime());

			int overtimeValue = (int) overtime.getValue();

			// Call method to insert attendance data into database
//			employeeDb.insertAttendanceData(firstNameField.getText(), sqlDate, timeIn, timeOut, overtimeValue);
			// Read attendance data and update the table model
//			employeeDb.readAttendanceData((DefaultTableModel) attendanceTable.getModel());
		});

		inputPanel.add(addAttendanceButton);

		attendancePanel.add(inputPanel, BorderLayout.SOUTH);

		return attendancePanel;
	}

	private JPanel createPayrollPanel() {
		JPanel payrollPanel = new JPanel();
		payrollPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Payroll"));
		add(payrollPanel, BorderLayout.EAST);
		payrollPanel.setLayout(new BorderLayout(0, 0));

		JPanel payrollSub = new JPanel(new GridLayout(2, 2, 5, 5));
		payrollSub.setBorder(new EmptyBorder(10, 10, 10, 10));

		salary = new JPanel();
		payrollSub.add(salary);
		salary.setLayout(new GridLayout(4, 2, 0, 0));

		JLabel basicSalLabel = new JLabel("Basic Salary:");
		salary.add(basicSalLabel);
		basicSalaryField = new JTextField(15);
		salary.add(basicSalaryField);

		JLabel allowancesLabel = new JLabel("Allowances : ");
		salary.add(allowancesLabel);
		allowancesField = new JTextField(15);
		salary.add(allowancesField);

		JLabel grossPayLabel = new JLabel("Gross Salary : ");
		salary.add(grossPayLabel);
		grossPayField = new JTextField(15);
		salary.add(grossPayField);

		JLabel netPayLabel = new JLabel("Net Salary : ");
		salary.add(netPayLabel);
		netPayField = new JTextField(15);
		salary.add(netPayField);

		payrollPanel.add(payrollSub, BorderLayout.NORTH);

		deductions = new JPanel();
		deductions.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Deductions"));
		payrollSub.add(deductions);
		deductions.setLayout(new GridLayout(4, 2, 0, 0));

		taxLabel = new JLabel("Tax :");
		deductions.add(taxLabel);

		taxField = new JTextField(15);
		deductions.add(taxField);

		pagibigLabel = new JLabel("Pag-Ibig :");
		deductions.add(pagibigLabel);

		pagibigField = new JTextField(15);
		deductions.add(pagibigField);

		SSSLabel = new JLabel("SSS :");
		deductions.add(SSSLabel);

		SSSField = new JTextField(15);
		deductions.add(SSSField);

		totalLabel = new JLabel("Total Deduction : ");
		deductions.add(totalLabel);

		totalField = new JTextField();
		totalField.setColumns(10);
		deductions.add(totalField);
		// Set the maximum width for the payrollPanel
		payrollPanel.setPreferredSize(new Dimension(300, 100)); // Adjust width and height as needed

		payReportPanel = new JPanel();
		payReportPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Payroll Report"));
		payReportPanel.setLayout(new GridLayout(1, 1));

		JTextArea reportArea = new JTextArea(20, 20);
		reportArea.setPreferredSize(new Dimension(200, 100)); // Adjust width and height as needed
		JScrollPane spReport = new JScrollPane(reportArea);
		payReportPanel.add(spReport);

		payrollPanel.add(payReportPanel, BorderLayout.CENTER);

		printPanel = new JPanel();
		payrollPanel.add(printPanel, BorderLayout.SOUTH);

		printButton = new JButton("Print Report");
		printPanel.add(printButton);

		return payrollPanel;
	}

	private JPanel createSummaryPanel() {
		JPanel summary = new JPanel();
		summary.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Performance Summary", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(summary, BorderLayout.NORTH);

		JPanel absencesPanel = new JPanel();
		summary.add(absencesPanel);
		absencesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel absenText = new JPanel();
		absencesPanel.add(absenText);
		absenText.setLayout(new BorderLayout(0, 0));

		JLabel absencesTitle = new JLabel("Absences");
		absencesTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		absenText.add(absencesTitle, BorderLayout.NORTH);

		JLabel absencesSubText = new JLabel("Number of Absences");
		absenText.add(absencesSubText, BorderLayout.SOUTH);

		absencesNumField = new JTextField();
		absencesNumField.setEditable(false);
		absencesNumField.setText("2");
		absencesNumField.setFont(new Font("Tahoma", Font.BOLD, 20));
		absencesNumField.setColumns(2);
		absencesPanel.add(absencesNumField);

		JPanel dayoffPanel = new JPanel();
		summary.add(dayoffPanel);
		dayoffPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel dayoffText = new JPanel();
		dayoffPanel.add(dayoffText);
		dayoffText.setLayout(new BorderLayout(0, 0));

		JLabel dayoffTitle = new JLabel("Day off");
		dayoffTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		dayoffText.add(dayoffTitle, BorderLayout.NORTH);

		JLabel dayoffSub = new JLabel("Available Day Off");
		dayoffText.add(dayoffSub, BorderLayout.SOUTH);

		dayoffNum = new JTextField();
		dayoffNum.setEditable(false);
		dayoffNum.setText("9");
		dayoffNum.setFont(new Font("Tahoma", Font.BOLD, 20));
		dayoffNum.setColumns(2);
		dayoffPanel.add(dayoffNum);

		JPanel leavePanel = new JPanel();
		summary.add(leavePanel);
		leavePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel leaveText = new JPanel();
		leavePanel.add(leaveText);
		leaveText.setLayout(new BorderLayout(0, 0));

		JLabel leaveTitle = new JLabel("Leave");
		leaveTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		leaveText.add(leaveTitle, BorderLayout.NORTH);

		JLabel leaveSub = new JLabel("Available Leave");
		leaveText.add(leaveSub, BorderLayout.SOUTH);

		leaveNum = new JTextField();
		leaveNum.setEditable(false);
		leaveNum.setText("3");
		leaveNum.setFont(new Font("Tahoma", Font.BOLD, 20));
		leaveNum.setColumns(2);
		leavePanel.add(leaveNum);

		JPanel latePanel = new JPanel();
		summary.add(latePanel);
		latePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel lateText = new JPanel();
		latePanel.add(lateText);
		lateText.setLayout(new BorderLayout(0, 0));

		JLabel lateTitle = new JLabel("Late");
		lateTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lateText.add(lateTitle, BorderLayout.NORTH);

		JLabel lateSub = new JLabel("Number of Late");
		lateText.add(lateSub, BorderLayout.SOUTH);

		lateNum = new JTextField();
		lateNum.setText("2");
		lateNum.setFont(new Font("Tahoma", Font.BOLD, 20));
		lateNum.setEditable(false);
		lateNum.setColumns(2);
		latePanel.add(lateNum);

		return summary;
	}

	private void setMonthPanel() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		// attendance.setAbsent(21); // Mark the 5th day as absent
		panel = new MonthPanel(month, year, attendance); // Initialize panel here

	}

}

class payrollprogram {
	static int presntdays(int workdays, int salperday) {
		int monthsal = workdays * salperday;
		return monthsal;
	}

	static double deduc(double philhealth, double pagibig, double sss) {
		double totaldeduc = philhealth + pagibig + sss;
		return totaldeduc;
	}

	public static void main(String a[]) {
		Scanner s = new Scanner(System.in);

		System.out.print("Enter full name : ");
		String name = s.nextLine();
		System.out.print("Enter basic salary : ");
		int salperday = s.nextInt();

		System.out.print("enter the day today(day of the month): ");
		int DayOfMonth = s.nextInt();

		// kapag ang date ng buwan ay 15
		if (DayOfMonth == 15) {
			System.out.print("(period of 1 - 15)\ndays of absence(1 - 15) : ");
			int absent = s.nextInt();
			int workingdays = 15 - absent;
			int monthlysal = presntdays(salperday, workingdays);

			System.out.println("\nWELDWELL PAYROLL");
			System.out.println(" Name : " + name);
			System.out.println(" Salary per day: PHP " + salperday);
			System.out.println(" Days of work : " + workingdays);
			System.out.println(" Absent : " + absent);
			System.out.println(" Overtime : N/A");
			System.out.println("\nDeductions:");
			System.out.println("- SSS : N/A");
			System.out.println("- Pag-IBIG : N/A ");
			System.out.println("- PhilHealth : N/A ");
			System.out.println("\nTotal Deductions: N/A ");
			System.out.println("Net Salary: PHP " + monthlysal);
		}
		// kapag ang date ng buwan ay 30
		else if (DayOfMonth == 30) {
			double sss = 570.00;
			double pagibig = 100.00;
			double philhealth = 500.00;

			System.out.print(" (period of 16 - 30)\ndays of absence(1 - 15) : ");
			int absence = s.nextInt();
			int workday = 15 - absence;
			int monthsalary = presntdays(salperday, workday);

			double deduction = deduc(philhealth, pagibig, sss);
			double netsal = monthsalary - deduction;

			System.out.println("\nWELDWELL PAYROLL");
			System.out.println("Name : " + name);
			System.out.println(" Salary per day: PHP " + salperday);
			System.out.println(" Days of work : " + workday);
			System.out.println(" Absent : " + absence);
			System.out.println(" Overtime : N/A");
			System.out.println("\nDeductions:");
			System.out.println("- SSS : PHP " + sss);
			System.out.println("- Pag-IBIG : PHP " + pagibig);
			System.out.println("- PhilHealth : PHP " + philhealth);
			System.out.println("\nTotal Deductions: PHP " + deduction);
			System.out.println("Net Salary: PHP " + netsal);
		} else {
			System.out.println("today is not a Pay day");
		}

	}
}