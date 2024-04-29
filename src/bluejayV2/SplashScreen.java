package bluejayV2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;

public class SplashScreen extends JFrame {
	private JLabel statusLabel;
	private JProgressBar progressBar;
	private JPanel panel;

	public SplashScreen() {
		setTitle("Loading...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setLocationRelativeTo(null);

		// Background Image
//		logo = new ImageIcon(getClass().getResource("/images/logo.png"));

		ImageIcon backgroundImage = new ImageIcon("resource/images/logo.png"); // Replace with actual image path
		Image scaledImage = backgroundImage.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
		getContentPane().setLayout(new BorderLayout(0, 0));
		JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
		getContentPane().add(backgroundLabel);
		backgroundLabel.setLayout(new BorderLayout());

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		// Status Label
		statusLabel = new JLabel("Loading...", SwingConstants.CENTER);
		panel.add(statusLabel, BorderLayout.CENTER);
		statusLabel.setForeground(Color.WHITE); // Set text color
		statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

		// Progress Bar
		progressBar = new JProgressBar(0, 100);
		panel.add(progressBar, BorderLayout.SOUTH);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.GREEN);

		setSize(577, 319);
	}

	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	public void updateProgress(int value) {
		progressBar.setValue(value);
	}
}