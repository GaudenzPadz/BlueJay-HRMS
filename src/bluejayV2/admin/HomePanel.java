package bluejayV2.admin;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton iconBtn;

	public HomePanel() {
		add(new Dashboard());
	}

}
