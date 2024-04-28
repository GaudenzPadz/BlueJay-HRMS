package bluejayV2.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import bluejayDB.EmployeeDatabase;
import bluejayV2.LoginPanel;
import bluejayV2.Main;
import net.miginfocom.swing.MigLayout;

public class AdminPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static JPanel mainPanel;
	public JPanel sidePanel;
	private SideMenu sideMenu;
	public ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));
	public ImageIcon homeIcon = new ImageIcon(getClass().getResource("/images/home.png"));
	public ImageIcon menuIcon = new ImageIcon(getClass().getResource("/images/menu.png"));
	public ImageIcon listIcon = new ImageIcon(getClass().getResource("/images/list.png"));
	public ImageIcon addEMPIcon = new ImageIcon(getClass().getResource("/images/add_emp.png"));
	public ImageIcon payrollIcon = new ImageIcon(getClass().getResource("/images/payroll.png"));
	public ImageIcon userIcon = new ImageIcon(getClass().getResource("/images/user.png"));
	public ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/images/logout.png"));
	private ImageIcon attendanceIcon  = new ImageIcon(getClass().getResource("/images/attendance.png"));

	private EmployeeDatabase DB;

	public AdminPanel(EmployeeDatabase DB) {
		this.DB = DB;
		setLayout(new BorderLayout(5, 0));

		// intialize panels
		mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(new HomePanel());
		

		sidePanel = SidePanel();
		add(sidePanel, BorderLayout.WEST);

		add(mainPanel, BorderLayout.CENTER);

		// Create an instance of sideMenu
		sideMenu = new SideMenu(sidePanel, mainPanel, true);
		sideMenu.setOpened(true);
		sideMenu.setSideMaxWidth(230);
		sideMenu.setSideMinimumWidth(48);
		sideMenu.setAnimationDuration(90); // 90 millie seconds
	}

	private JPanel SidePanel() {

		// Initialize side panel
		JPanel panel = new JPanel(new MigLayout("", "[left][60.00]", "[top][][][][][][][][][][]"));

		// Set arc round background for the side panel
		putClientProperty(FlatClientProperties.STYLE,
				"arc:20; [light]background:darken(@background,5%); [dark]background:lighten(@background,5%)");

		JPanel titlePanel = new JPanel();

		panel.add(titlePanel, "cell 0 0 2 1,growx,aligny top");
		JLabel iconLabel = new JLabel(userIcon);
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2, 1));
		JLabel title = new JLabel("WELD WELL");
		title.setFont(new Font("Sans Serif", Font.BOLD, 20));
		JLabel label2 = new JLabel("HRMS for a Welding Shop");
		textPanel.add(title);
		textPanel.add(label2);
		titlePanel.add(iconLabel);
		titlePanel.add(textPanel);

		//a buttton to close / open the side panel
//		JButton menuBtn = new JButton();
//		panel.add(menuBtn, "cell 0 1,alignx left,aligny center");
//		menuBtn.setOpaque(false);
//		menuBtn.setContentAreaFilled(false);
//		menuBtn.setBorderPainted(false);
//		menuBtn.setIcon(new ImageIcon(menuIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
//		menuBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// close and open the side menu
//				sideMenu.toggleMenuWithAnimation();
//			}
//		});

		JButton homeBtn = new JButton();
		panel.add(homeBtn, "cell 0 2");
		homeBtn.setOpaque(false);
		homeBtn.setContentAreaFilled(false);
		homeBtn.setBorderPainted(false);
		homeBtn.setIcon(new ImageIcon(homeIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		homeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// goes to back to home
				mainPanel.removeAll();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(new HomePanel());
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		JLabel lblHome = new JLabel("Home");
		
		panel.add(lblHome, "cell 1 2");

		JButton employeeListBtn = new JButton();
		panel.add(employeeListBtn, "cell 0 3,grow");
		employeeListBtn.setOpaque(false);
		employeeListBtn.setContentAreaFilled(false);
		employeeListBtn.setBorderPainted(false);
		employeeListBtn.setIcon(new ImageIcon(listIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		employeeListBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Show Panel 1
				mainPanel.removeAll();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(new EMPListPanel(DB), BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		JLabel label = new JLabel("Employee List");
		panel.add(label, "cell 1 3,alignx left");

		JButton addEMPBtn = new JButton("");
		panel.add(addEMPBtn, "cell 0 4,grow");
		addEMPBtn.setOpaque(false);
		addEMPBtn.setContentAreaFilled(false);
		addEMPBtn.setBorderPainted(false);
		addEMPBtn.setIcon(new ImageIcon(addEMPIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		addEMPBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.setLayout(new MigLayout("", "[grow]", "[grow][grow,center]"));
				JLabel titleLabel = new JLabel("Add Employee");
				titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
				mainPanel.add(titleLabel, "cell 0 0,alignx leading,aligny center");
				mainPanel.add(new AddEMPPanel(DB), "cell 0 1,grow");
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		JLabel label_1 = new JLabel("Add Employee");
		panel.add(label_1, "cell 1 4,alignx left");

		JButton payrollBtn = new JButton("");
		panel.add(payrollBtn, "cell 0 5,grow");
		payrollBtn.setOpaque(false);
		payrollBtn.setContentAreaFilled(false);
		payrollBtn.setBorderPainted(false);
		payrollBtn.setIcon(new ImageIcon(payrollIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		payrollBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(new PayrollPanel(DB), BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		JLabel label_2 = new JLabel("Payroll");
		panel.add(label_2, "cell 1 5");
		
		JButton attendanceBtn = new JButton("");
		attendanceBtn.setOpaque(false);
		attendanceBtn.setContentAreaFilled(false);
		attendanceBtn.setBorderPainted(false);
		attendanceBtn.setIcon(new ImageIcon(attendanceIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		attendanceBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(new AttendancePanel(DB), BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		panel.add(attendanceBtn, "cell 0 6");
		
		JLabel attendanceLabel = new JLabel("Attendance");
		panel.add(attendanceLabel, "cell 1 6");

		JButton logoutBtn = new JButton();
		panel.add(logoutBtn, "cell 0 10");
		logoutBtn.setOpaque(false);
		logoutBtn.setContentAreaFilled(false);
		logoutBtn.setBorderPainted(false);
		logoutBtn.setIcon(new ImageIcon(logoutIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		logoutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.frame.replaceContentPane("Login",new LoginPanel(DB), new BorderLayout());
			}
		});
		JLabel logoutLabel = new JLabel("Logout");
		panel.add(logoutLabel, "cell 1 10");

		titlePanel.putClientProperty(FlatClientProperties.STYLE,
				"arc:20; [light]background:darken(@background,5%); [dark]background:lighten(@background,5%)");

		return panel;
	}
}
