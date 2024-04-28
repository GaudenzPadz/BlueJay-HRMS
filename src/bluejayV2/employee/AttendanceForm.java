package bluejayV2.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
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

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;
import bluejayV2.Main;
import net.miginfocom.swing.MigLayout;

public class AttendanceForm extends JPanel {

	private static final long serialVersionUID = 1L;
	private Employee employee;
	JComboBox<String> AmPmCombo;
	private String[] column = { "Name", "Date", "Time In", "Time Out", "Overtime", "Note" };

	private DefaultTableModel model = new DefaultTableModel(column, 0) {

		@Override
		public boolean isCellEditable(int row, int column) {
			// all cells false
			return false;
		}
	};

	private JTable table;
	private EmployeeDatabase db;
	private String currentDate = LocalDate.now().toString(); // Current date as a string
	private JTextField INhourField;
	private JTextField INminutesField;
	private JTextField OUThoursField;
	private JTextField OUTminutesField;
	private JComboBox<String> OUTcomboBox;
	private JComboBox<String> AmPmOUTCombo;

	public AttendanceForm(Employee employee, EmployeeDatabase DB) {
		this.employee = employee;

		this.db = DB;
		refreshTable(model);

		if (db.hasCheckedIn(employee.getId(), currentDate)) {
			// if employee already time in
			setupUI(attendanceOUTForm());
		} else {
			// if new login
			setupUI(attendanceForm());

		}

	}

	private void refreshTable(DefaultTableModel modely) {
		FlatAnimatedLafChange.showSnapshot();
		db.loadEMPAttendanceData(employee.getId(), modely);
		FlatAnimatedLafChange.hideSnapshotWithAnimation();
	}

	private void setupUI(JPanel form) {
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
				Main.frame.replaceContentPane("Weld Well HRMS", new EmployeePanel(employee, db), getLayout());
			}
		});

		headerPanel.add(btnNewButton, "cell 0 0");

		JLabel lblNewLabel = new JLabel("Attendance Form");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		headerPanel.add(lblNewLabel, "cell 1 0");
		add(headerPanel, BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new MigLayout("fill,insets 20", "[center]", "[center][center]"));
		add(mainPanel, BorderLayout.CENTER);
		JPanel attendanceform = form;

		mainPanel.add(attendanceform, "cell 0 0,alignx center,aligny center");

		JPanel attTable = attendanceTable();
		mainPanel.add(attTable, "cell 0 1,growx");

	}

	private JPanel attendanceForm() {
		JPanel attendanceForm = new JPanel(new MigLayout("wrap,fillx,insets 25 35 25 35",
				"[200px,center][100px][200px]", "[center][20.00][20px][20px][20px][][20px][20px][20px][20px][20px][]"));
		attendanceForm.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");

		// Time In Label
		JLabel lblTimeIn = new JLabel("Time In");
		attendanceForm.add(lblTimeIn, "cell 0 0,alignx left");

		JLabel noteLabel = new JLabel("Note");
		attendanceForm.add(noteLabel, "cell 2 0,alignx center,aligny center");

		// Hours Field
		INhourField = new JTextField(2);
		INhourField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceForm.add(INhourField, "flowx,cell 0 1,alignx left");

		// Colon Label
		JLabel colon = new JLabel(":");
		attendanceForm.add(colon, "cell 0 1");

		// Minutes Field
		INminutesField = new JTextField(2);
		INminutesField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceForm.add(INminutesField, "cell 0 1");

		// AM/PM ComboBox
		AmPmCombo = new JComboBox<>(new String[] { "AM", "PM" });
		attendanceForm.add(AmPmCombo, "cell 0 1");

		// Set current time to Time In fields
		setCurrentTimeToFields(INhourField, INminutesField);
		ActionListener updateExpectedTimeOut = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int hours = Integer.parseInt(INhourField.getText());
					int minutes = Integer.parseInt(INminutesField.getText());
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

					// expected out fields/Update Expected Time Out
					OUThoursField.setText(String.format("%02d", hours));
					OUTminutesField.setText(String.format("%02d", minutes));
					OUTcomboBox.setSelectedItem(expectedTimeOutAmpm);

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid time format. Please enter valid hours and minutes.");
				}
			}
		};

		// Attach the action listener to the Time In components
		INhourField.addActionListener(updateExpectedTimeOut);
		INminutesField.addActionListener(updateExpectedTimeOut);
		AmPmCombo.addActionListener(updateExpectedTimeOut);

		JTextArea clockINnote = new JTextArea();
		clockINnote.setLineWrap(true);
		attendanceForm.add(clockINnote, "cell 2 1 1 7,grow");

		// Expected Time Out Label
		JLabel expectedOutLabel = new JLabel("Expected Time Out");
		attendanceForm.add(expectedOutLabel, "cell 0 3,alignx left");

		// Expected Time Out Hours Field
		OUThoursField = new JTextField(2); // Two digits for hours
		attendanceForm.add(OUThoursField, "flowx,cell 0 4,alignx left");

		// Colon Label for Expected Time Out
		attendanceForm.add(new JLabel(":"), "cell 0 4");

		// Expected Time Out Minutes Field
		OUTminutesField = new JTextField(2); // Two digits for minutes
		attendanceForm.add(OUTminutesField, "cell 0 4");

		// Expected Time Out AM/PM ComboBox
		OUTcomboBox = new JComboBox<>(new String[] { "AM", "PM" });
		attendanceForm.add(OUTcomboBox, "cell 0 4");

		JButton timeINBtn = new JButton("Time In");
		timeINBtn.addActionListener(e -> {
			try {
				// Extract time components
				int hours = Integer.parseInt(INhourField.getText());
				int minutes = Integer.parseInt(INminutesField.getText());
				String ampm = (String) AmPmCombo.getSelectedItem();

				// Convert to 24-hour format
				if (ampm.equals("PM") && hours < 12) {
					hours += 12;
				} else if (ampm.equals("AM") && hours == 12) {
					hours = 0; // Midnight is 0
				}

				// Validate hour and minute ranges
				if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
					throw new IllegalArgumentException("Invalid time format.");
				}

				// Create time string in HH:MM:SS format
				String timeInStr = String.format("%02d:%02d:00", hours, minutes);
				Time timeInObj = Time.valueOf(timeInStr); // Now safe to convert

				// Additional note
				String note = clockINnote.getText();

				// Insert into the database
				db.addTimeIn(employee.getId(), employee.getFirstName() + " " + employee.getLastName(), currentDate,
						timeInObj, note);

				// Display success message and refresh table
				JOptionPane.showMessageDialog(this, "Time In recorded.");
				refreshTable(model);

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter valid numbers for hours and minutes.", "Input Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Format Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		attendanceForm.add(timeINBtn, "cell 1 10,growx");

		return attendanceForm;
	}

	private JPanel attendanceOUTForm() {
		JPanel attendanceOUTForm = new JPanel(
				new MigLayout("wrap,fillx,insets 25 35 25 35", "[200px,grow,center][100px][200px]",
						"[pref!,center][pref!][pref!][pref!][pref!][pref!][pref!][pref!][pref!,grow]"));
		attendanceOUTForm.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");

		attendanceOUTForm.add(new JLabel("Time Out"), "cell 0 0,alignx left");

		attendanceOUTForm.add(new JLabel("Note"), "cell 2 0,alignx center,aligny center");

		// Hours Field
		JTextField hourOUTField = new JTextField(2);
		hourOUTField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceOUTForm.add(hourOUTField, "flowx,cell 0 1,alignx left");

		attendanceOUTForm.add(new JLabel(":"), "cell 0 1");

		// Minutes Field
		JTextField minutesOUTField = new JTextField(2);
		minutesOUTField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceOUTForm.add(minutesOUTField, "cell 0 1");

		// AM/PM ComboBox
		AmPmOUTCombo = new JComboBox<>(new String[] { "AM", "PM" });
		attendanceOUTForm.add(AmPmOUTCombo, "cell 0 1");

		// Set current time to Time In fields
		setCurrentTimeToFields(hourOUTField, minutesOUTField);

		JTextArea clockOUTnote = new JTextArea();
		clockOUTnote.setLineWrap(true);
		attendanceOUTForm.add(clockOUTnote, "cell 2 1 1 6,grow");

		JCheckBox OTcheck = new JCheckBox("Overtime?");
		attendanceOUTForm.add(OTcheck, "flowx,cell 0 3,alignx left");

		JLabel howCome = new JLabel("How Many Hours? ");
		attendanceOUTForm.add(howCome, "flowx,cell 0 4,alignx left");

		JTextField OTINhourField = new JTextField();
		attendanceOUTForm.add(OTINhourField, "cell 0 4,alignx right");
		OTINhourField.setColumns(5);
		if (OTcheck.isSelected()) {
			howCome.setEnabled(true);
			OTINhourField.setEnabled(true);
		} else {
			howCome.setEnabled(false);
			OTINhourField.setEnabled(false);
		}

		JButton OUTBtn = new JButton("Time Out");
		OUTBtn.addActionListener(e -> {
			String timeOut = hourOUTField.getText() + ":" + minutesOUTField.getText() + " "
					+ AmPmOUTCombo.getSelectedItem();

			int overtime = OTcheck.isSelected() ? Integer.parseInt(OTINhourField.getText()) : 0;
			String note = clockOUTnote.getText();

			db.updateTimeOut(employee.getId(), currentDate, timeOut, note, overtime);

			JOptionPane.showMessageDialog(this, "Time Out recorded.");
		});
		attendanceOUTForm.add(OUTBtn, "cell 1 8,growx");
		return attendanceOUTForm;
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

	private void setCurrentTimeToFields(JTextField hour, JTextField minutesField) {
		Calendar now = Calendar.getInstance();
		int hours = now.get(Calendar.HOUR);
		int minutes = now.get(Calendar.MINUTE);
		boolean isPM = now.get(Calendar.AM_PM) == Calendar.PM;

		hour.setText(String.format("%02d", hours));
		minutesField.setText(String.format("%02d", minutes));
		if (this.AmPmCombo == null) {
			AmPmOUTCombo.setSelectedItem(isPM ? "PM" : "AM");

		} else if (this.AmPmOUTCombo == null) {
			AmPmCombo.setSelectedItem(isPM ? "PM" : "AM");

		}
	}

}
