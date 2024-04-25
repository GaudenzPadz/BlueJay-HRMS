package bluejayV2;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import bluejay.Employee;
import bluejayDB.EmployeeDatabase;

public class Main {

	public static GUI frame;

	public static EmployeeDatabase DB;
	public static Employee emp;
	public static void main(String[] a) {
		FlatLightLaf.setup();
		UIManager.put("Button.arc", 999);
		UIManager.put("TextComponent.arc", 50);
		UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 13));

		try {
			DB = new EmployeeDatabase();

			frame = new GUI("Login", new LoginPanel(), 1200, 700, true, true);
			frame.isDark(false);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to connect to the database." + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}