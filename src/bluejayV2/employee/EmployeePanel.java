package bluejayV2.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bluejay.Employee;
import bluejayV2.ButtonPanel;
import bluejayV2.LoginPanel;
import bluejayV2.Main;
import net.miginfocom.swing.MigLayout;
import javax.swing.Icon;

public class EmployeePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel menuPanel;
	private Employee employee;

	public EmployeePanel(Employee employee) {
		this.employee = employee;

		setLayout(new BorderLayout());

		// Header Panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(0, 191, 255));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		headerPanel.setLayout(new MigLayout("", "[30px][left][30][]", "[100px,grow]"));

		// Icon Label
		JLabel iconLabel = new JLabel("ICON");
		iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
		iconLabel.setForeground(Color.WHITE);
		headerPanel.add(iconLabel, "cell 1 0,alignx left,growy");

		// Labels Panel
		JPanel labelsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		labelsPanel.setOpaque(false);
		JLabel label1 = new JLabel("Welcome!");
		label1.setFont(new Font("SansSerif", Font.BOLD, 20));
		JLabel label2 = new JLabel("");

		label2.setText(employee.getFirstName() + " " + employee.getLastName());

		label2.setFont(new Font("SansSerif", Font.PLAIN, 15));
		JLabel label3 = new JLabel("Label 3");
		JLabel label4 = new JLabel("Label 4");
		label1.setForeground(Color.WHITE);
		label2.setForeground(Color.WHITE);
		label3.setForeground(Color.WHITE);
		label4.setForeground(Color.WHITE);
		labelsPanel.add(label1);
		labelsPanel.add(label2);
		labelsPanel.add(label3);
		labelsPanel.add(label4);
		headerPanel.add(labelsPanel, "cell 3 0,growx,aligny top");

		add(headerPanel, BorderLayout.NORTH);

		// Menu Panel
		menuPanel = new JPanel();
		menuPanel.setBackground(null);
		menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		add(menuPanel, BorderLayout.CENTER);

		menuPanel.setLayout(new MigLayout("center", "[grow,fill][200][100][200][100][200,grow][grow,fill]",
				"[100px][][10px:30px:50px][grow][10px:30px:50px]"));

		ImageIcon profileIcon = new ImageIcon(getClass().getResource("/images/96x96/profile.png"));

		ButtonPanel panel_2 = new ButtonPanel(Color.decode("#002C4B"), profileIcon, "View/Update Profile",
				" click to view or update profile information ");
		panel_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Main.frame.replaceContentPane(new ProfilePanel(employee), getLayout());
			}
		});
		menuPanel.add(panel_2, "cell 1 1,growx,aligny center");

		ImageIcon attendanceIcon = new ImageIcon(getClass().getResource("/images/96x96/attendance.png"));
		// " click to view or update profile information "
		ButtonPanel panel_1 = new ButtonPanel(Color.decode("#002C4B"), attendanceIcon, "Check/Update Attendance",
				"     click to check or update attendance     ");
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Button 2 clicked!");
			}
		});
		menuPanel.add(panel_1, "cell 3 1");

		ImageIcon payrollIcon = new ImageIcon(getClass().getResource("/images/96x96/invoice.png"));

		ButtonPanel panel_4 = new ButtonPanel(Color.decode("#002C4B"), payrollIcon, "Check/Update Payroll",
				"      click to check or update Payroll       ");
		panel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Button 3 clicked!");
			}
		});
		menuPanel.add(panel_4, "cell 5 1,aligny center");

		ImageIcon printIcon = new ImageIcon(getClass().getResource("/images/96x96/invoice.png"));

		JPanel panel_3 = new ButtonPanel(Color.decode("#002C4B"), printIcon, "Print Payslip", "click to print Payslip");
		panel_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//
			}
		});
		menuPanel.add(panel_3, "cell 1 3,growx,aligny center");
		
		ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/images/logout.png"));

		ButtonPanel panel_3_1 =  new ButtonPanel(Color.decode("#002C4B"), logoutIcon, "Logout", "");
		panel_3_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Main.frame.replaceContentPane(new LoginPanel(), new BorderLayout());
			}
		});
		menuPanel.add(panel_3_1, "cell 5 3,grow");

	}

}