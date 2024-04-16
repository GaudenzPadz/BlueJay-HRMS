package testApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class sidepanelTest extends JFrame {
	private JPanel sidePanel;
	private JButton toggleButton;
	private sideMenu sideMenu;

	public sidepanelTest() {
		setTitle("Side Panel Example");
		setSize(500, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Create the content panel
		JPanel contentPanel = new JPanel(new BorderLayout());

		// Create the side panel
		sidePanel = new JPanel();
		sidePanel.setBackground(Color.LIGHT_GRAY);
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical layout

		// Add some components to the side panel
		JLabel titleLabel = new JLabel("Side Panel");
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidePanel.add(titleLabel);

		JButton btnNewButton = new JButton("New button");
		sidePanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		sidePanel.add(btnNewButton_1);

		// Create an instance of sideMenu
		sideMenu = new sideMenu(sidePanel, contentPanel);
		sideMenu.setSideMaxWidth(150);
		sideMenu.setSideMinimumWidth(50);

		// Create a toggle button
		toggleButton = sideMenu.getToggleButton();
		contentPanel.add(toggleButton, BorderLayout.NORTH);

		// Add the content panel and side panel to the frame
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JButton btnNewButton_2 = new JButton("New button");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                sideMenu.toggleMenuWithAnimation();

			}
		});
		contentPanel.add(btnNewButton_2, BorderLayout.CENTER);
		getContentPane().add(sidePanel, BorderLayout.WEST);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new sidepanelTest().setVisible(true);
			}
		});
	}
}
