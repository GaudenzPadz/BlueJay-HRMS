package bluejayV2.admin;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import bluejayDB.EmployeeDatabase;
import net.miginfocom.swing.MigLayout;

public class Dashboard extends JPanel {

	 private EmployeeDatabase DB;
	    private ImageIcon listIcon = new ImageIcon(getClass().getResource("/images/list.png"));
	    private ImageIcon calendarIcon = new ImageIcon(getClass().getResource("/images/calendar.png"));
	    private ImageIcon payrollIcon = new ImageIcon(getClass().getResource("/images/paycheque.png"));
	    private ImageIcon performanceIcon = new ImageIcon(getClass().getResource("/images/performance.png"));
	    private ImageIcon recruitmentIcon = new ImageIcon(getClass().getResource("/images/recruitment.png"));

	    public Dashboard() {
	        try {
	            DB = new EmployeeDatabase();
	        } catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }

	        JPanel panel = new JPanel(new MigLayout("", "[][]", "[][][]"));

	        JPanel createEmployeeOverviewSection = createEmployeeOverviewSection();
	        JPanel createUpcomingEventsSection = createUpcomingEventsSection();
	        JPanel createPayrollOverviewSection = createPayrollOverviewSection();
	        JPanel createEmployeePerformanceSection = createEmployeePerformanceSection();
	        JPanel createRecruitmentAndOnboardingSection = createRecruitmentAndOnboardingSection();
	        // Add sections for the suggestions
	        panel.add(createEmployeeOverviewSection, "");
	        panel.add(createUpcomingEventsSection, "wrap");
	        panel.add(createPayrollOverviewSection, "");
	        panel.add(createEmployeePerformanceSection, "wrap");
	        panel.add(createRecruitmentAndOnboardingSection, "center");

	        this.add(panel);
	    }

	    private JPanel createEmployeeOverviewSection() {
	        return dashboardPanel(
	            listIcon,
	            "Employee Overview",
	            "Total Employees:",
	            "150",
	            "Departments:",
	            "5",
	            "View More"
	        );
	    }

	    private JPanel createUpcomingEventsSection() {
	        return dashboardPanel(
	            calendarIcon,
	            "Upcoming Events",
	            "Next Event:",
	            "Team Meeting",
	            "Next Deadline:",
	            "Payroll Submission",
	            "View Calendar"
	        );
	    }

	    private JPanel createPayrollOverviewSection() {
	        return dashboardPanel(
	            payrollIcon,
	            "Payroll Overview",
	            "Next Pay Date:",
	            "April 30",
	            "Total Payroll Expense:",
	            "$150,000",
	            "View Payroll"
	        );
	    }

	    private JPanel createEmployeePerformanceSection() {
	        return dashboardPanel(
	            performanceIcon,
	            "Employee Performance",
	            "Top Performers:",
	            "3",
	            "Pending Reviews:",
	            "10",
	            "View Performance"
	        );
	    }

	    private JPanel createRecruitmentAndOnboardingSection() {
	        return dashboardPanel(
	            recruitmentIcon,
	            "Recruitment & Onboarding",
	            "Open Positions:",
	            "3",
	            "New Hires:",
	            "5",
	            "View Recruitment"
	        );
	    }
	

	private JPanel dashboardPanel(ImageIcon icon, String title, String body1, String total1, String body2, String total2,
			String viewBtn) {

		JPanel panel_1 = new JPanel(new MigLayout("wrap,fillx,insets 25 35 25 35", "[][][][118.00px,center][]",
				"[][20.00][][][pref!][][][]"));
		panel_1.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");
		JButton iconBtn = new JButton(new ImageIcon(icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
		iconBtn.setHorizontalAlignment(SwingConstants.LEADING);
		iconBtn.setOpaque(false);
		iconBtn.setContentAreaFilled(false);
		iconBtn.setBorderPainted(false);

		panel_1.add(iconBtn, "cell 0 0 4 1,alignx left,aligny center");

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

		panel_1.add(titleLabel, "cell 3 1,alignx left");

		JLabel bodyLabel_1 = new JLabel(body1);
		panel_1.add(bodyLabel_1, "cell 3 3,alignx left");

		JLabel totalLabel_1 = new JLabel(total1);
		panel_1.add(totalLabel_1, "cell 4 3,alignx left");

		JButton viewPanelBtn = new JButton(viewBtn);
		viewPanelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		JLabel bodyLabel_2 = new JLabel(body2);
		panel_1.add(bodyLabel_2, "cell 3 5,alignx left");

		JLabel totalLabel_2 = new JLabel(total2);
		panel_1.add(totalLabel_2, "cell 4 5");
		panel_1.add(viewPanelBtn, "cell 3 7,grow");
	
		return panel_1;
	}

}
