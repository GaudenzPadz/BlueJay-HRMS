package bluejayV2.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bluejayV2.Main;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class ProfilePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;

	/**
	 * Create the panel.
	 */
	public ProfilePanel() {
        setLayout(new BorderLayout());

        
		 // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 191, 255).darker());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new MigLayout("", "[left][]", "[50px,grow]"));

        add(headerPanel, BorderLayout.NORTH);

    	 ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));

        
        JButton btnNewButton = new JButton("");
        btnNewButton.setOpaque(false);
        btnNewButton.setContentAreaFilled(false);
        btnNewButton.setBorderPainted(false);
        btnNewButton.setIcon(new ImageIcon(backIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//		sideMenu.toggleMenuWithAnimation();
				Main.frame.replaceContentPane(new EmployeePanel(), getLayout());
			}
		});

        headerPanel.add(btnNewButton, "cell 0 0");
        
        JLabel lblNewLabel = new JLabel("Profile");
        lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerPanel.add(lblNewLabel, "cell 1 0");
        
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new MigLayout("", "[][][pref!,grow][][grow][]", "[][][][][][][][][][][][][][][]"));
        
        JLabel lblNewLabel_1 = new JLabel("PICTURE");
        lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 25));
        panel.add(lblNewLabel_1, "cell 2 1");
        
        JLabel lblNewLabel_8 = new JLabel("Note");
        lblNewLabel_8.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(lblNewLabel_8, "cell 4 2");
        
        JLabel lblNewLabel_2 = new JLabel("First Name");
        panel.add(lblNewLabel_2, "flowx,cell 2 3");
        
        JTextArea textArea = new JTextArea();
        panel.add(textArea, "cell 4 3 1 4,grow");
        
        textField = new JTextField();
        panel.add(textField, "cell 2 4,alignx left");
        textField.setColumns(10);
        
        JLabel lblNewLabel_3 = new JLabel("Middle Name");
        panel.add(lblNewLabel_3, "cell 2 5");
        
        textField_1 = new JTextField();
        panel.add(textField_1, "cell 2 6,alignx left");
        textField_1.setColumns(10);
        
        JLabel lblNewLabel_4 = new JLabel("Surname");
        panel.add(lblNewLabel_4, "cell 2 7");
        
        textField_2 = new JTextField();
        panel.add(textField_2, "cell 2 8,alignx left");
        textField_2.setColumns(10);
        
        JLabel lblNewLabel_5 = new JLabel("New label");
        panel.add(lblNewLabel_5, "cell 2 9");
        
        textField_3 = new JTextField();
        panel.add(textField_3, "cell 2 10,alignx left");
        textField_3.setColumns(10);
        
        JLabel lblNewLabel_6 = new JLabel("New label");
        panel.add(lblNewLabel_6, "cell 2 11");
        
        textField_4 = new JTextField();
        panel.add(textField_4, "cell 2 12,alignx left");
        textField_4.setColumns(10);
        
        JLabel lblNewLabel_7 = new JLabel("New label");
        panel.add(lblNewLabel_7, "cell 2 13");
        
        JRadioButton rdbtnNewRadioButton = new JRadioButton("Male");
        panel.add(rdbtnNewRadioButton, "flowx,cell 2 14");
	}

}
