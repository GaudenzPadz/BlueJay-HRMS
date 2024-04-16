package bluejay;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import bluejayDB.EmployeeDatabase;

public class Main {

	public static Employee employee;

	public static GUI adminGUI;

	public static ADMIN adminPanel = new ADMIN();

	public static LOGIN loginFrame;
	public static GUI loginGUI;

	// credentials
	// in bluejayDB.sqlite
	// users table

	public static GUI userGui;

	public static void main(String[] args) {
		try {
			// try if database is connected
			EmployeeDatabase database = new EmployeeDatabase();
			try {
				UIManager.setLookAndFeel(new FlatLightLaf());
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
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
