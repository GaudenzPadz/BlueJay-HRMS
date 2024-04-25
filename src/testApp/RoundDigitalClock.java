package testApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import net.miginfocom.swing.MigLayout;

public class RoundDigitalClock extends JFrame {

	private DialPanel clockPanel;
	private SimpleDateFormat timeFormat;
	private javax.swing.Timer timer;

	public RoundDigitalClock() {
		super("Round Digital Clock");
		setSize(1000, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new MigLayout("", "[10px][70px][46px][429px][][]", "[127px][][][]"));

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

		JButton addAttendanceButton = new JButton("Add Attendance");

		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.year", "Year");
		p.put("text.month", "Month");
		p.put("text.today", "Today");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		AbstractFormatter DateLabelFormatter = new AbstractFormatter() {

			private static final long serialVersionUID = -3517374967067627101L;
			private String datePattern = "yyyy-MM-dd";
			private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

			@Override
			public Object stringToValue(String text) throws ParseException {
				return dateFormatter.parseObject(text);
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				if (value != null) {
					Calendar cal = (Calendar) value;
					return dateFormatter.format(cal.getTime());
				}

				return "";
			}

		};
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, DateLabelFormatter);
		model.setSelected(true);

//		UtilCalendarModel model = new UtilCalendarModel();
//		SqlDateModel model = new SqlDateModel();

//		datePicker = new JDatePickerImpl(datePanel);
		// datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

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
			// employeeDb.insertAttendanceData(firstNameField.getText(), sqlDate, timeIn,
			// timeOut, overtimeValue);
			// Read attendance data and update the table model
			// employeeDb.readAttendanceData((DefaultTableModel)
			// attendanceTable.getModel());

			Date selectedDate = (Date) datePicker.getModel().getValue();
			JOptionPane.showMessageDialog(this, "The selected date is " + selectedDate);

		});
		JLabel ClockLabel = new JLabel("New label");

		JPanel digitalClockPanel = new JPanel();
		// HH: mm: ss
		JLabel hoursLabel = new JLabel("00 :");

		JLabel minutesLabel = new JLabel("00 :");

		JLabel secondsLabel = new JLabel("00");
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hoursLabel.setText(new SimpleDateFormat("HH: ").format(Calendar.getInstance().getTime()));
				minutesLabel.setText(new SimpleDateFormat("mm: ").format(Calendar.getInstance().getTime()));
				secondsLabel.setText(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));
				String Datetime = new Date().toString();
//                String Datetime = new Date().toLocaleString();
				ClockLabel.setText(Datetime);
				clockPanel.repaint();
			}
		});// Update every second

		timer.start();

		digitalClockPanel.add(hoursLabel);
		digitalClockPanel.add(minutesLabel);
		digitalClockPanel.add(secondsLabel);
		getContentPane().add(digitalClockPanel, "cell 2 0,alignx left,aligny center");
		inputPanel.add(datePicker);
		inputPanel.add(addAttendanceButton);

		getContentPane().add(inputPanel, "cell 3 0,alignx left,aligny top");

		getContentPane().add(ClockLabel, "cell 3 2,alignx center,aligny center");
		  clockPanel= new DialPanel();
//		JPanel clockPanel_1 = new JPanel();

		clockPanel.setPreferredSize(new Dimension(200, 200)); // Adjust size as needed

		getContentPane().add(clockPanel, "cell 4 2");

	}

	private class DialPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw the clock dial (circle, tick marks)
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			int radius = 90; // Adjust as needed
			g.setColor(Color.BLACK);
			g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

			// Draw hour labels
			for (int i = 1; i <= 12; i++) {
				double angle = Math.toRadians((i - 3) * 30); // 12 hours spaced evenly around the circle
				int labelX = (int) (centerX + Math.cos(angle) * (radius - 20)); // Adjust spacing from the circle's edge
				int labelY = (int) (centerY + Math.sin(angle) * (radius - 20));
				g.drawString(Integer.toString(i), labelX, labelY);
			}

			// Calculate hand positions
			int hours = Calendar.getInstance().get(Calendar.HOUR);
			int minutes = Calendar.getInstance().get(Calendar.MINUTE);
			int seconds = Calendar.getInstance().get(Calendar.SECOND);

			double hourAngle = Math.toRadians((hours % 12) * 30 + minutes / 2);
			double minuteAngle = Math.toRadians(minutes * 6);
			double secondAngle = Math.toRadians(seconds * 6); // Calculate angle for seconds

			int hourHandLength = 50; // Adjust as needed
			int minuteHandLength = 70; // Adjust as needed
			int secondHandLength = 80; // Adjust as needed

			int hourHandX = centerX + (int) (hourHandLength * Math.sin(hourAngle));
			int hourHandY = centerY - (int) (hourHandLength * Math.cos(hourAngle));
			int minuteHandX = centerX + (int) (minuteHandLength * Math.sin(minuteAngle));
			int minuteHandY = centerY - (int) (minuteHandLength * Math.cos(minuteAngle));
			int secondHandX = centerX + (int) (secondHandLength * Math.sin(secondAngle)); // Calculate position for
																							// seconds
			int secondHandY = centerY - (int) (secondHandLength * Math.cos(secondAngle));

			// Draw hands
			g.drawLine(centerX, centerY, hourHandX, hourHandY);
			g.drawLine(centerX, centerY, minuteHandX, minuteHandY);
			g.setColor(Color.RED); // Set color for seconds hand
			g.drawLine(centerX, centerY, secondHandX, secondHandY);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
//				new RoundDigitalClock().setVisible(true);
				new Window();
			}
		});
	}
}

class Window extends JFrame {

	private JLabel Heading;
	private JLabel ClockLabel;
	private Font font;

	Window() {
		super.setTitle("DIGITAL CLOCK");
		super.setSize(1000, 400);
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setVisible(true);
		this.createGUI();
		this.startclock();

	}

	public void createGUI() {

		// Background color
		getContentPane().setBackground(Color.BLACK);

		// Create GUI
		Heading = new JLabel("DIGITAL CLOCK");
		Heading.setForeground(Color.WHITE);
		Heading.setHorizontalAlignment(SwingConstants.CENTER);

		ClockLabel = new JLabel(" Clock");
		ClockLabel.setForeground(Color.gray);
		ClockLabel.setHorizontalAlignment(SwingConstants.CENTER);

		font = new Font("", Font.BOLD, 70);

		Heading.setFont(font);
		ClockLabel.setFont(font);
		this.setLayout(new GridLayout(2, 1));
		this.add(Heading);
		this.add(ClockLabel);
	}

	public void startclock() {
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				String Datetime = new Date().toString();
                String Datetime = new Date().toLocaleString();
				ClockLabel.setText(Datetime);
			}
		});
		timer.start();
	}
}
