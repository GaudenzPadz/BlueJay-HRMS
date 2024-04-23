package testApp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FilePicker extends JFrame {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new FilePicker();
		});
	}

	private DC databaseConnection;
	private byte[] cachedImageData; // Cache for the latest image data

	public FilePicker() {
		ImagePanel imagePanel = new ImagePanel();

		handleShowButtonClick(imagePanel);
		JButton selectButton = new JButton("Select a Picture");
		JButton showButton = new JButton("Show Image");

		selectButton.addActionListener(e -> handleSelectButtonClick());
		showButton.addActionListener(e -> handleShowButtonClick(imagePanel));

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(selectButton);
		buttonPanel.add(showButton);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(buttonPanel, BorderLayout.NORTH);

		JPanel panel = new ImagePanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.add(imagePanel);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);

		this.setSize(585, 394);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (databaseConnection != null) {
				databaseConnection.closeConnection();
			}
		}));
	}

	private void handleSelectButtonClick() {
		JFileChooser fileChooser = new JFileChooser();
		// Setting Up The Filter
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());

		// Attaching Filter to JFileChooser object
		fileChooser.setFileFilter(imageFilter);

		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					try {
						if (databaseConnection == null) {
							databaseConnection = new DC();
						}
						uploadPictureToDatabase(selectedFile);
					} catch (SQLException | ClassNotFoundException | IOException ex) {
						ex.printStackTrace();
					}
					return null;
				}
			}.execute();
		}
	}

	private void handleShowButtonClick(ImagePanel imagePanel) {
		new SwingWorker<byte[], Void>() {
			@Override
			protected byte[] doInBackground() throws Exception {
				try {
					if (databaseConnection == null) {
						databaseConnection = new DC();
					}
					return databaseConnection.getLatestImage();
				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
					return null;
				}
			}

			@Override
			protected void done() {
				try {
					byte[] imageData = get();
					if (imageData != null) {
						cachedImageData = imageData; // Cache the image data
						imagePanel.setImageData(cachedImageData);
					}
					imagePanel.repaint();
				} catch (InterruptedException | ExecutionException | IOException ex) {
					ex.printStackTrace();
				}
			}
		}.execute();
	}

	private void uploadPictureToDatabase(File file) throws SQLException, ClassNotFoundException, IOException {
		byte[] imageData = fileToByteArray(file);
		databaseConnection.insertPicture(imageData);
	}

	private byte[] fileToByteArray(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return fis.readAllBytes();
		}
	}
}

class ImagePanel extends JPanel {
	private Image scaledImage;

	public void setImageData(byte[] imageData) throws IOException {
		if (imageData != null) {
			ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
			Image originalImage = ImageIO.read(bais);

			// Scale the image to fit the panel size
			scaledImage = originalImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		} else {
			scaledImage = null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (scaledImage != null) {
			g.drawImage(scaledImage, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
}

class DC {
	private Connection connection;

	public DC() throws SQLException, ClassNotFoundException {
		connect();
	}

	private void connect() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite::resource:DB/bluejayDB.sqlite");
	}

	public void insertPicture(byte[] imageData) {
		String sql = "INSERT INTO employees (profile_image) VALUES (?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setBytes(1, imageData);
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Picture inserted successfully.");
			} else {
				System.out.println("Failed to insert the picture.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public byte[] getLatestImage() {
		String sql = "SELECT profile_image FROM employees";
		try (PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			if (resultSet.next()) {
				return resultSet.getBytes("profile_image");
			}
		} catch (SQLException e) {

		}
		return null;

	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
