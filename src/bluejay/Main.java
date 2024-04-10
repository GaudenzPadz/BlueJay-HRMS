package bluejay;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import bluejayDB.EmployeeDatabase;

public class Main {

    public static Employee employee;

    public static LOGIN loginFrame;
    public static GUI loginGUI;

    // Admin GUI Object and ADMIN object
    public static ADMIN adminPanel = new ADMIN();
    public static GUI adminGUI;

    // credentials
    // in bluejayDB.sqlite
    // users table

    // userpanel Object and
    public static GUI userGui;

    public static void main(String[] args) {
        try {
            // try if database is connected
            EmployeeDatabase database = new EmployeeDatabase();

            // Create login frame and GUI
            loginFrame = new LOGIN();
            loginFrame.componentsPanel.setOpaque(false);
            loginFrame.welcomePanel.setOpaque(false);

            loginGUI = new GUI("Login", loginFrame, 400, 650, false, true);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database." + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}
