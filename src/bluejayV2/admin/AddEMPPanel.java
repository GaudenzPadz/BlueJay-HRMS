package bluejayV2.admin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;

import bluejay.Employee;
import bluejayDB.EmployeeDatabase;
import net.miginfocom.swing.MigLayout;

public class AddEMPPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField fNameField;
	private JTextField lNameField;
	private JTextField workTypeField;
	private JComboBox<String> workTypeCombobox;
	private JTextField addressField;
	private JTextField emailField;
	private JTextField telField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JSeparator separator;
	private JButton btnNewButton;
	private JTextField DOBField;
	private JTextField textField_4;
	private JRadioButton radioMale, radioFemale;

	private JPanel panel;
	private JScrollPane sp;
	private EmployeeDatabase db;
	protected String input;
	private JLabel lblNewLabel_1;
	private JButton uploadBtn;
	private BufferedImage image; // Store the uploaded image here
	private int newId;

	public AddEMPPanel() {
		try {
			db = new EmployeeDatabase();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		setLayout(new MigLayout("center", "[center]", "[100px:300px,center][center]"));

		panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[pref!,grow,fill]",
				"[][][grow][][][][][][][][][][][][][][][][][][][][]"));
		sp = new JScrollPane(panel);
		sp.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");

		uploadPanel = new JPanel();
		add(uploadPanel, "cell 0 0,grow");
		uploadPanel.setLayout(new MigLayout("", "[pref!,grow,fill][100px:150px]", "[100px:150px]"));

		// set imagePanel size as an 1x1
		imagePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g); // Ensure the panel is painted correctly

				if (image != null) { // Check if an image is loaded
					// Draw the image to fit the panel
					g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
				}
			}
		};
		uploadPanel.add(imagePanel, "cell 1 0,grow");
		imagePanel.repaint(); // Repaint to update the display

		lblNewLabel_1 = new JLabel("Picture (use 1x1)");
		uploadPanel.add(lblNewLabel_1, "flowx,cell 0 0,alignx center,aligny center");

		uploadBtn = new JButton("Upload");
		uploadPanel.add(uploadBtn, "cell 0 0,aligny center");
		uploadBtn.addActionListener((ActionEvent e) -> {
			uploadImage();
			SwingUtilities.invokeLater(() -> {
			});

		});
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		add(sp, "flowx,cell 0 1,alignx center,aligny center");

		fNameField = new JTextField(20);
		lNameField = new JTextField(20);
		fNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter First Name");
		lNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Last Name");

		ButtonGroup groupGender = new ButtonGroup();

		workTypeCombobox = new JComboBox<String>();
		workTypeCombobox.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Work Type");
		populateComboBox(workTypeCombobox);
		// Adding a new item for "Other"
		workTypeCombobox.addItem("Other");

		// Add ActionListener to JComboBox
		workTypeCombobox.addActionListener((ActionEvent e) -> {
			if ("Other".equals(workTypeCombobox.getSelectedItem())) {
				workTypeField.setEnabled(true); // Enable the JTextField for custom input
			} else {
				workTypeField.setEnabled(false); // Disable if "Other" is not selected
			}
		});

		workTypeField = new JTextField(6);
		workTypeField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Work Type");

		workTypeField.setEnabled(false);
		JLabel label = new JLabel("Address");
		addressField = new JTextField();
		addressField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Address");
		JLabel lblNewLabel_2 = new JLabel("Gender");

		JPanel genderPanel = new JPanel(new MigLayout("insets 0"));
		radioMale = new JRadioButton("Male");
		radioFemale = new JRadioButton("Female");
		groupGender.add(radioMale);
		groupGender.add(radioFemale);
		radioMale.setSelected(true);
		genderPanel.add(radioMale);
		genderPanel.add(radioFemale);

		JLabel lblNewLabel_3 = new JLabel("Telephone Number");
		telField = new JTextField(10);
		telField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Telephone Number");

		panel.add(new JLabel("Full Name"), "cell 0 1");
		panel.add(fNameField, "flowx,cell 0 2");
		panel.add(lNameField, "cell 0 2");

		panel.add(new JLabel("Work Type"), "cell 0 3");

		panel.add(workTypeCombobox, "flowx,cell 0 4,alignx right");
		panel.add(workTypeField, "cell 0 4,alignx right");

		panel.add(label, "cell 0 5");

		panel.add(addressField, "cell 0 6,growx");

		JLabel lblNewLabel_7 = new JLabel("Employee's ID:");
		panel.add(lblNewLabel_7, "flowx,cell 0 7");

		panel.add(lblNewLabel_2, "cell 0 8,gapy 5");

		panel.add(genderPanel, "cell 0 9");

		panel.add(lblNewLabel_3, "cell 0 10");

		panel.add(telField, "flowx,cell 0 11,growx");
		JLabel lblNewLabel_4 = new JLabel("Email");

		panel.add(lblNewLabel_4, "cell 0 12");
		emailField = new JTextField(10);
		emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter email");

		panel.add(emailField, "cell 0 13,growx");

		JLabel lblNewLabel_6 = new JLabel("Date of Birth");
		panel.add(lblNewLabel_6, "cell 0 14");

		// date of birth field
		DOBField = new JTextField(10);
		DOBField.putClientProperty("JTextField.placeholderText", "dd/MM/yyyy"); // Placeholder text

		panel.add(DOBField, "cell 0 15,growx");
		panel.add(new JSeparator(), "cell 0 16,gapy 5 5");

		JLabel lblNewLabel = new JLabel("Username");
		panel.add(lblNewLabel, "cell 0 17");

		usernameField = new JTextField(10);
		usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter username");
		panel.add(usernameField, "cell 0 18,growx");

		JLabel lblNewLabel_5 = new JLabel("Password");
		panel.add(lblNewLabel_5, "cell 0 19");

		passwordField = new JPasswordField();
		passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter password");

		panel.add(passwordField, "cell 0 20,growx");
		passwordField.setColumns(10);

		separator = new JSeparator();
		panel.add(separator, "cell 0 21,growx");

		btnNewButton = new JButton("Add Employee");
		btnNewButton.addActionListener(this::addButtonClicked);
		panel.add(btnNewButton, "cell 0 22");

		textField_4 = new JTextField();
		try {
			newId = db.getLastEmployeeId() + 1;
			textField_4.setText(String.valueOf(newId)); // Convert int to String
		} catch (SQLException e) {
			// Handle SQLException here
			// e.printStackTrace(); // For debugging
			JOptionPane.showMessageDialog(null, "An error occurred while retrieving employee ID.");
		}

		textField_4.setEditable(false);
		panel.add(textField_4, "cell 0 7");
		textField_4.setColumns(10);

	}

	private void populateComboBox(JComboBox<String> workTypeBox) {
		try {
			ResultSet rs = db.getTypes();
			while (rs.next()) {
				workTypeBox.addItem(rs.getString("work_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to fetch work types from the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	File selectedFile;
	private JPanel imagePanel;
	private JPanel uploadPanel;

	private void uploadImage() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg");
		fileChooser.setFileFilter(imageFilter);

		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			try {
				// Load the selected file into the BufferedImage property
				image = ImageIO.read(selectedFile);

				// Repaint the imagePanel to display the newly uploaded image
				SwingUtilities.invokeLater(() -> {
					imagePanel.repaint();
				});

			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error loading image. Please try again.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void addButtonClicked(ActionEvent e) {
		// Retrieve text from fields
		String firstName = fNameField.getText();
		String lastName = lNameField.getText();
		String address = addressField.getText();
		String workType = (String) workTypeCombobox.getSelectedItem();
		String gender = radioMale.isSelected() ? "Male" : "Female";
		String telNum = telField.getText();
		String email = emailField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();

		// Validate and parse date of birth
		String dobText = DOBField.getText();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false); // Ensures strict parsing
		java.sql.Date DOB = null; // Initialize DOB as null
		try {
			java.util.Date parsedDate = sdf.parse(dobText); // Parse the date
			DOB = new java.sql.Date(parsedDate.getTime()); // Convert to java.sql.Date
			DOBField.putClientProperty("JComponent.outline", null); // Clear red outline
		} catch (ParseException ex) {
			DOBField.putClientProperty("JComponent.outline", Color.RED); // Set red outline
			JOptionPane.showMessageDialog(null, "Invalid date format. Please use dd/MM/yyyy.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit if DOB is invalid
		}

		// Ensure required fields are filled
		if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return; // Exit early if mandatory fields are empty
		}

		// Insert data into the database and show success message
		try {
			byte[] imageData = null;
			if (selectedFile == null) {

				imageData = null;

			} else if (selectedFile != null) {
				imageData = fileToByteArray(selectedFile);

			}
			String middleName = null;
			db.insertEMPData(firstName, lastName, address, workType, gender, telNum, DOB, email, imageData);
			String Role = "Employee";
			db.insertEMPCredentials(firstName + " " + lastName, username, password, Role);
			JOptionPane.showMessageDialog(null, "Employee added successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			// Employee emp = new Employee(newId, firstName, middleName, lastName, address,
			// workType, 0, 0, 0, gender);

			// Clear the fields after successful insertion
			fNameField.setText("");
			lNameField.setText("");
			addressField.setText("");
			telField.setText("");
			emailField.setText("");
			DOBField.setText("");
			workTypeCombobox.setSelectedItem(null);
			usernameField.setText("");
			passwordField.setText("");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "An error occurred while adding the employee. Please try again.",
					"Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private byte[] fileToByteArray(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			return fis.readAllBytes();
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error reading file. Please try again.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

}
