package bluejayV2.admin;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;

public class Dashboard extends JPanel {

	private JButton iconBtn;

	public Dashboard() {

		JPanel panel = new JPanel(new MigLayout("", "[grow]", "[grow]"));

		JPanel panel_1 = new JPanel(
				new MigLayout("wrap,fillx,insets 25 35 25 35", "[][][][118.00px,center][]", "[][20.00][][][][][]"));
		panel_1.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");

		add(panel);

		panel.add(panel_1, "grow");
		ImageIcon listIcon = new ImageIcon(getClass().getResource("/images/list.png"));
		iconBtn = new JButton(new ImageIcon(listIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
		iconBtn.setHorizontalAlignment(SwingConstants.LEADING);
		iconBtn.setOpaque(false);
		iconBtn.setContentAreaFilled(false);
		iconBtn.setBorderPainted(false);

		panel_1.add(iconBtn, "cell 0 0 4 1,growx,aligny center");

		JLabel lblNewLabel = new JLabel("Employee Summary");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

		panel_1.add(lblNewLabel, "cell 3 1,alignx left");

		JLabel lblNewLabel_2 = new JLabel("Total number of employees :");
		panel_1.add(lblNewLabel_2, "cell 3 3,alignx left");

		JLabel lblNewLabel_1 = new JLabel("NUMBER");
		panel_1.add(lblNewLabel_1, "cell 4 3,alignx left");

		JLabel lblNewLabel_3 = new JLabel("New label");
		panel_1.add(lblNewLabel_3, "cell 3 4,alignx left");

		JButton btnNewButton = new JButton("New button");
		panel_1.add(btnNewButton, "cell 3 6,grow");

	}

}
