package bluejay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ADMIN extends JPanel {

	public static JPanel contentPanel(JPanel panel) {

        return panel;
    }

    public static JLabel title = new JLabel("WELD WELL");

    public static JPanel TITLE() {
       ImageIcon image = new ImageIcon(ADMIN.class.getResource("/images/user.png"));

        // Create a JLabel for the icon
        JLabel iconLabel = new JLabel(image);
        // Create a JPanel to hold the text labels
        JPanel textPanel = new JPanel();

        textPanel.setLayout(
                new GridLayout(2, 1)); // Grid layout with 2 rows and 1 column

        title.setFont(new Font("Sans Serif", Font.BOLD, 20)); // Customize font

        JLabel label2 = new JLabel("HRMS for a Welding Shop");

        textPanel.add(title);

        textPanel.add(label2);

        JPanel titlePanel = new JPanel(new BorderLayout(10, 10));

        titlePanel.add(iconLabel, BorderLayout.WEST);

        titlePanel.add(textPanel, BorderLayout.CENTER);
        return titlePanel;
    }

    public JPanel BUTTONS() {
        // Button panel
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(5, 1, 5, 8));

      
        JButton button2 = new JButton("Add Employee");
        JButton button3 = new JButton("Pay Roll");
        JButton button4 = new JButton("Feature");
        JButton button5 = new JButton("Feature");
		JButton button1 = new JButton("Employee List");
		button1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        welcome.removeAll(); // Remove existing components
		        // Reload when button clicked again
		        LIST empList = new LIST();
		        empList.reloadData();
		        // add panels to the contentPanel
		        welcome.add(empList.searchPanel, BorderLayout.NORTH);
		        welcome.add(empList.scrollPane, BorderLayout.CENTER);
		        welcome.revalidate(); // Revalidate for layout changes
		        welcome.repaint();
		    }
		});


        // button actionlistener with lambda expression
        button5.addActionListener((ActionEvent e) -> {
            // contentPanel().removeAll(); // Remove existing components
            // contentPanel().revalidate(); // Revalidate for layout changes
            // contentPanel().repaint();

        });

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);

        return buttonPanel;
    }

    public JPanel SIDE_MENU() {
        JPanel sidePanel = new JPanel(new BorderLayout());

        sidePanel.add(TITLE(), BorderLayout.NORTH);

        sidePanel.add(BUTTONS(), BorderLayout.CENTER);
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
        sidePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add some padding

        return sidePanel;
    }

    private JPanel welcomeScreen() {

        JPanel welcomePanel = new JPanel();
        // Create a welcome message panel
        welcomePanel = new JPanel(new GridLayout(3, 1));
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

    JPanel welcome = contentPanel(welcomeScreen());

    public ADMIN() {

        setLayout(new BorderLayout());
        add(SIDE_MENU(), BorderLayout.WEST);
        add(welcome, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Add some padding

    }

}
