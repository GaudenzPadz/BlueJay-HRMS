package testApp;

import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class RoundDigitalClock extends JFrame {

	private DialPanel clockPanel;
	private SimpleDateFormat timeFormat;
	private javax.swing.Timer timer;

	public RoundDigitalClock() {
		super("Round Digital Clock");
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		clockPanel = new DialPanel();
		clockPanel.setPreferredSize(new Dimension(200, 200)); // Adjust size as needed

		JPanel digitalClockPanel = new JPanel();

//		HH: mm: ss
		JLabel hoursLabel = new JLabel("00 :");

		JLabel minutesLabel = new JLabel("00 :");

		JLabel secondsLabel = new JLabel("00");

		javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hoursLabel.setText(new SimpleDateFormat("HH: ").format(Calendar.getInstance().getTime()));
				minutesLabel.setText(new SimpleDateFormat("mm: ").format(Calendar.getInstance().getTime()));
				secondsLabel.setText(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));

				clockPanel.repaint();
			}
		});// Update every second

		timer.start();

		digitalClockPanel.add(hoursLabel);
		digitalClockPanel.add(minutesLabel);
		digitalClockPanel.add(secondsLabel);

		getContentPane().add(clockPanel);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		getContentPane().add(digitalClockPanel);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

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
			int secondHandX = centerX + (int) (secondHandLength * Math.sin(secondAngle)); // Calculate position for seconds
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
				new RoundDigitalClock().setVisible(true);
			}
		});
	}
}
