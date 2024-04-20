package bluejay;

import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;

import bluejayDB.EmployeeDatabase;

public class ADMIN extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel title;
    private JPanel welcomePanel;
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

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(10);
        setLayout(borderLayout);
        add(createSidePanel(), BorderLayout.WEST);
        welcomePanel = createWelcomePanel();
        add(welcomePanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Add some padding
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.add(createTitlePanel(), BorderLayout.NORTH);
        sidePanel.add(createButtonsPanel(), BorderLayout.CENTER);
        ActionListener logoutListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.adminGUI.dispose();
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
        title.setFont(new Font("Sans Serif", Font.BOLD, 20));
        JLabel label2 = new JLabel("HRMS for a Welding Shop");
        textPanel.add(title);
        textPanel.add(label2);
        titlePanel.add(iconLabel, BorderLayout.WEST);
        titlePanel.add(textPanel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 8));
        String[] buttonLabels = {"Employee List", "Add Employee", "Pay Roll", "Feature", "Feature"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (label.equals("Employee List")) {
                        loadEmployeeList();
                    } else {
                        // Handle other button actions
                    }
                }
            });
            buttonPanel.add(button);
        }
        return buttonPanel;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to WELD WELL HRMS");
        welcomeLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel infoLabel1 = new JLabel("This is a Human Resource Management System for Welding Shop.");
        JLabel infoLabel2 = new JLabel("Please use the buttons on the left to navigate.");
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(infoLabel1);
        infoPanel.add(infoLabel2);
        welcomePanel.add(infoPanel, BorderLayout.CENTER);
        return welcomePanel;
    }

    private void loadEmployeeList() {
        welcomePanel.removeAll();
        try {
            db.connect();
            LIST empList = new LIST();
            empList.reloadData(db);
            JScrollPane scrollPane = new JScrollPane(LIST.table);
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(empList.searchPanel, BorderLayout.NORTH);
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            welcomePanel.setLayout(new BorderLayout());
            welcomePanel.add(tablePanel, BorderLayout.CENTER);
            welcomePanel.add(empList.addPanel(), BorderLayout.SOUTH);
            welcomePanel.revalidate();
            welcomePanel.repaint();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
