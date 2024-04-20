package bluejayV2.admin;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import javax.swing.JComboBox;
import javax.swing.JButton;

public class AddEmpPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField mNameField;
	private JTextField fNameField;
	private JTextField lNameField;
	private JLabel fNameLabel;
	private JLabel lblNewLabel_1;
	private JTextField textField_3;
	private JComboBox workTypeCombobox;
	private JLabel lblNewLabel_2;
	private JTextField addressField;
	private JLabel lblNewLabel_3;
	private JTextField textField_5;
	private JTextField textField_6;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel;
	private JTextField textField;
	private JLabel lblNewLabel_5;
	private JTextField textField_1;
	private JSeparator separator;
	private JButton btnNewButton;
	private JLabel lblNewLabel_6;
	private JTextField textField_2;
	private JLabel lblNewLabel_7;
	private JTextField textField_4;

	/**
	 * Create the panel.
	 */
	public AddEmpPanel() {
		setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

		JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[360,grow,fill]", "[][][][grow][][][][][][][][][][][][][][][][][][][][][][][]"));

		add(panel, "alignx center,aligny center");

		JLabel titleLabel = new JLabel("Add Employee");
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

		fNameField = new JTextField(10);
		mNameField = new JTextField(10);
		lNameField = new JTextField(10);
		ButtonGroup groupGender = new ButtonGroup();

		workTypeCombobox = new JComboBox();

		textField_3 = new JTextField(6);
		textField_3.setEnabled(false);
		JLabel label = new JLabel("Address");
		addressField = new JTextField();
		lblNewLabel_2 = new JLabel("Gender");

		JPanel genderPanel = new JPanel(new MigLayout("insets 0"));
		JRadioButton radioMale = new JRadioButton("Male");
		JRadioButton radioFemale = new JRadioButton("Female");
		groupGender.add(radioMale);
		groupGender.add(radioFemale);
		radioMale.setSelected(true);
		genderPanel.add(radioMale);
		genderPanel.add(radioFemale);

		lblNewLabel_3 = new JLabel("Telephone Number");
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		lblNewLabel_4 = new JLabel("Email");
		textField_5 = new JTextField();
		textField_5.setColumns(10);

		panel.add(titleLabel, "cell 0 0");
		panel.add(new JLabel("Full Name"), "cell 0 2");
		panel.add(fNameField, "flowx,cell 0 3");
		panel.add(mNameField, "cell 0 3");
		panel.add(lNameField, "cell 0 3");

		panel.add(new JLabel("Work Type"), "cell 0 4");

		panel.add(workTypeCombobox, "flowx,cell 0 5,alignx right");
		panel.add(textField_3, "cell 0 5,alignx right");

		panel.add(label, "cell 0 6");

		panel.add(addressField, "cell 0 7,growx");
		
		lblNewLabel_7 = new JLabel("Employee's ID:");
		panel.add(lblNewLabel_7, "flowx,cell 0 8");

		panel.add(lblNewLabel_2, "cell 0 9,gapy 5");

		panel.add(genderPanel, "cell 0 10");

		panel.add(lblNewLabel_3, "cell 0 11");

		panel.add(textField_6, "cell 0 12,growx");

		panel.add(lblNewLabel_4, "cell 0 13");

		panel.add(textField_5, "cell 0 14,growx");
		
		lblNewLabel_6 = new JLabel("Date of Birth");
		panel.add(lblNewLabel_6, "cell 0 15");
		
		textField_2 = new JTextField();
		panel.add(textField_2, "cell 0 16,growx");
		textField_2.setColumns(10);
		panel.add(new JSeparator(), "cell 0 18,gapy 5 5");
		
		lblNewLabel = new JLabel("Username");
		panel.add(lblNewLabel, "cell 0 19");
		
		textField = new JTextField();
		panel.add(textField, "cell 0 20,growx");
		textField.setColumns(10);
		
		lblNewLabel_5 = new JLabel("Password");
		panel.add(lblNewLabel_5, "cell 0 21");
		
		textField_1 = new JTextField();
		panel.add(textField_1, "cell 0 22,growx");
		textField_1.setColumns(10);
		
		separator = new JSeparator();
		panel.add(separator, "cell 0 23,growx");
		
		btnNewButton = new JButton("Add Employee");
		panel.add(btnNewButton, "cell 0 25");
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		panel.add(textField_4, "cell 0 8");
		textField_4.setColumns(10);

	}

}
