package testApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

public class testingUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel_1;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Set FlatLaf look and feel (optional)
					FlatLightLaf.setup();
					UIManager.put("Button.arc", 999);
					UIManager.put("Component.arc", 999);
					UIManager.put("ProgressBar.arc", 999);
					UIManager.put("TextComponent.arc", 999);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					testingUI frame = new testingUI();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel mainPanel() {
		panel_1 = new JPanel();
		panel_1.setBackground(Color.BLUE);
		return panel_1;
	}

	private JLabel clockLabel;
	private SimpleDateFormat timeFormat;

	/**
	 * Create the frame.
	 */
	public testingUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(767, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel sidePanel = new JPanel();
		sidePanel.setBackground(Color.PINK);
		contentPane.add(sidePanel, BorderLayout.WEST);
		sidePanel.setLayout(new MigLayout("", "[40.00px][40px][40px]", "[48px][][]"));

		JLabel backBtn = new JLabel();

		sidePanel.add(backBtn, "cell 0 0,alignx left,aligny top");

		JPanel mainPanel = mainPanel();
		mainPanel.setBackground(Color.LIGHT_GRAY);

		// Create an instance of sideMenu
		sideMenu sideMenu = new sideMenu(sidePanel, mainPanel);
		sideMenu.setSideMaxWidth(150);
		sideMenu.setSideMinimumWidth(55);
		sideMenu.setAnimationDuration(90); // 90 millie seconds

		contentPane.add(mainPanel);
		panel_1.setLayout(null);

		JLabel welcomeLabel = new JLabel("Welcome to the Material UI Test App");
		welcomeLabel.setBounds(158, 59, 215, 35);
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(welcomeLabel);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(212, 233, 89, 23);
		panel_1.add(btnNewButton);

		textField = new JTextField();
		textField.setBounds(212, 271, 86, 20);
		panel_1.add(textField);
		textField.setColumns(10);

		JPanel clockPanel = new JPanel();
		clockPanel.setBounds(346, 117, 200, 200);
		clockLabel = new JLabel();
		timeFormat = new SimpleDateFormat("HH:mm:ss"); // 24-hour format
		clockLabel.setFont(clockLabel.getFont().deriveFont(36.0f)); // Larger font for better visibility

		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clockLabel.setText(timeFormat.format(Calendar.getInstance().getTime()));
			}
		});// Update every second
		
		timer.start();

		clockPanel.add(clockLabel);
		panel_1.add(clockPanel);

		backBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sideMenu.toggleMenuWithAnimation();
//				sideMenu.toggleMenu(); use this if animation is not needed

			}
		});
		// scaling back icon BECAUSE AYAW KONG MAG EDIT
		ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));
		Image scaledImage = backIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // w, h, scale the image
		backIcon = new ImageIcon(scaledImage);
		backBtn.setIcon(backIcon); // Set the resized icon to the button

		JLabel homeBtn = new JLabel();

		sidePanel.add(homeBtn, "cell 0 1,alignx left,aligny top");
		homeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("home");
				contentPane.remove(1); // Remove the current main panel
				JPanel home = homePanel();
				contentPane.add(home, BorderLayout.CENTER);
				contentPane.revalidate();
				contentPane.repaint();
			}
		});

		homeBtn.setIcon(new ImageIcon(getClass().getResource("/images/home.png")));

		JLabel lblNewLabel = new JLabel("Home");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sidePanel.add(lblNewLabel, "cell 1 1 2 1,alignx center");

		JLabel documentBtn = new JLabel();

		sidePanel.add(documentBtn, "cell 0 2,alignx left,aligny top");

		documentBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel documentPanel = new JPanel();
				JLabel documentLabel = new JLabel("welcomePanel");
				documentLabel.setHorizontalAlignment(SwingConstants.CENTER);
				documentPanel.add(documentLabel);
				contentPane.remove(1); // Remove the current main panel
				JPanel document = documentPanel();
				contentPane.add(document, BorderLayout.CENTER);
				contentPane.revalidate();
				contentPane.repaint();
			}
		});
		documentBtn.setIcon(new ImageIcon(getClass().getResource("/images/document.png")));
	}

	private JPanel documentPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLUE);
		JLabel welcomeLabel = new JLabel("Document's Panel");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(welcomeLabel);
		return panel;
	}

	private JPanel homePanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.GREEN);
		JLabel welcomeLabel = new JLabel("Home Panel");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(welcomeLabel);
		return panel;
	}
}

class sideMenu {
	private JPanel sidePanel;
	private JPanel mainPanel;
	private JButton toggleButton;
	private boolean isOpen;
	private int sideWidth = 100; // default value when side is closed
	private int sideMaxWidth = 300; // default value when side is opened
	private int animationDuration = 100; // Animation duration in milliseconds
	private Timer timer;

	public sideMenu(JPanel sidePanel, JPanel mainPanel) {
		this.sidePanel = sidePanel;
		this.mainPanel = mainPanel;

		// Set preferred size for the side panel
		sidePanel.setPreferredSize(new Dimension(getSideMinimumWidth(), mainPanel.getHeight()));

	}

	public void setSideMaxWidth(int sideMaxWidth) {
		this.sideMaxWidth = sideMaxWidth;
	}

	public int getSideMaxWidth() {
		return sideMaxWidth;
	}

	public void setSideMinimumWidth(int sideWidth) {
		this.sideWidth = sideWidth;
	}

	public int getSideMinimumWidth() {
		return sideWidth;
	}

	public void setAnimationDuration(int animationDuration) {
		this.animationDuration = animationDuration;
	}

	public int getAnimationDuration() {
		return animationDuration;
	}

	public JButton getToggleButton() {
		return toggleButton;
	}

	public void setOpened(boolean isOpened) {
		this.isOpen = isOpened;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void toggleMenu() {
		if (isOpen) {
			// Close the side menu
			sidePanel.setPreferredSize(new Dimension(getSideMinimumWidth(), mainPanel.getHeight()));
			mainPanel.revalidate();
			isOpen = false;
		} else {
			// Open the side menu
			sidePanel.setPreferredSize(new Dimension(getSideMaxWidth(), mainPanel.getHeight()));
			mainPanel.revalidate();
			isOpen = true;
		}
	}

	public void toggleMenuWithAnimation() {
		// Calculate the target width based on the menu state

		// If the menu is open, target width is minimum width; otherwise, it's maximum
		// width
		int targetWidth = isOpen ? getSideMinimumWidth() : getSideMaxWidth();

		// Get the current width of the side panel
		int initialWidth = sidePanel.getWidth();

		// Calculate the step size for the animation
		// Calculate the step size based on animation duration; dividing by 10 for
		// smoother animation
		int stepSize = (targetWidth - initialWidth) / (getAnimationDuration() / 10);

		// Create a timer for animation
		timer = new Timer(10, new ActionListener() { // Timer triggers every 10 milliseconds
			public void actionPerformed(ActionEvent e) {
				int newWidth = sidePanel.getWidth() + stepSize; // Calculate the new width based on the step size
				if ((stepSize > 0 && newWidth >= targetWidth) || (stepSize < 0 && newWidth <= targetWidth)) {
					// If the new width is greater than or equal to the target width (for
					// expanding), or less than or equal to the target width (for collapsing)
					timer.stop(); // Stop the timer
					isOpen = !isOpen; // Toggle the menu state
					return;
				}
				sidePanel.setPreferredSize(new Dimension(newWidth, sidePanel.getHeight())); // Set the new preferred
																							// size for the side panel
				sidePanel.revalidate(); // Revalidate the side panel to update its layout
				mainPanel.revalidate(); // Revalidate the main panel to update its layout
			}
		});

		// Start the animation timer
		timer.start();
	}

}