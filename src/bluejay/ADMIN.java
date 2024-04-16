package bluejay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bluejayDB.EmployeeDatabase;

public class ADMIN extends JPanel {

	/**
	 * Version: 1L Employee list features: JTable, Search Table, csv database
	 * 
	 * to implement: rate calculation
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel title;
	private JPanel welcome;
	public EmployeeDatabase db;

	public ADMIN() {
		try {
			db = new EmployeeDatabase();
			db.connect(); // Establish connection

			System.out.println("Connected to the database successfully.");
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		setLayout(new BorderLayout());
		add(sidePanel(), BorderLayout.WEST);
		welcome = createWelcomePanel();
		add(welcome, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Add some padding
	}

	public JPanel sidePanel() {
		JPanel sidePanel = new JPanel(new BorderLayout());
		sidePanel.add(createTitlePanel(), BorderLayout.NORTH);
		sidePanel.add(createButtonsPanel(), BorderLayout.CENTER);
		ActionListener logoutListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Implement context-specific logic for frame disposal
				// userGui.dispose(); // Or adminGUI.dispose();
				Main.adminGUI.dispose();

				// revalidate loginGUI
				Main.loginGUI = new GUI("Login", Main.loginFrame, 400, 650, false, true);
				Main.loginGUI.revalidate();
				Main.loginGUI.repaint();
				Main.loginGUI.setVisible(true);
			}
		};

		sidePanel.add(LOGIN.logoutPanel(logoutListener), BorderLayout.SOUTH);

		return sidePanel;
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel(new BorderLayout(10, 10));
		ImageIcon image = new ImageIcon(ADMIN.class.getResource("/images/user.png"));
		JLabel iconLabel = new JLabel(image);
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2, 1));
		title = new JLabel("WELD WELL");
		title.setFont(new Font("Sans Serif", Font.BOLD, 20)); // Customize font
		JLabel label2 = new JLabel("HRMS for a Welding Shop");
		textPanel.add(title);
		textPanel.add(label2);
		titlePanel.add(iconLabel, BorderLayout.WEST);
		titlePanel.add(textPanel, BorderLayout.CENTER);
		return titlePanel;
	}

	private JPanel createButtonsPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1, 5, 8));
		JButton button1 = new JButton("Employee List");
		JButton button2 = new JButton("Add Employee");
		JButton button3 = new JButton("Pay Roll");
		JButton button4 = new JButton("Feature");
		JButton button5 = new JButton("Feature");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadEmployeeList();
			}
		});
		// Add action listeners for other buttons if needed
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		buttonPanel.add(button4);
		buttonPanel.add(button5);
		return buttonPanel;
	}

	private JPanel createWelcomePanel() {
		JPanel welcomePanel = new JPanel(new GridLayout(3, 1));
		welcomePanel.setPreferredSize(new Dimension(500, 300)); // Set preferred size
		JLabel welcomeLabel = new JLabel("Welcome to WELD WELL HRMS");
		welcomeLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		JLabel infoLabel1 = new JLabel("This is a Human Resource Management System for Welding Shop.");
		JLabel infoLabel2 = new JLabel("Please use the buttons on the left to navigate.");
		welcomePanel.add(welcomeLabel);
		welcomePanel.add(infoLabel1);
		welcomePanel.add(infoLabel2);
		return welcomePanel;
	}

	private void loadEmployeeList() {
		welcome.removeAll(); // Remove existing components
		;
		try {
			db.connect(); // Connect to the database

			LIST empList = new LIST();
			empList.reloadData(db);

			welcome.add(empList.searchPanel, BorderLayout.NORTH);
			welcome.add(empList.scrollPane, BorderLayout.CENTER);
			welcome.add(empList.addPanel(), BorderLayout.SOUTH);

			welcome.revalidate(); // Revalidate for layout changes
			welcome.repaint();

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Instantiate and initialize the EmployeeDatabase object

	}

}
