package testApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class EMA extends JFrame {

	private static final long serialVersionUID = 213;
	private JTable table1, table2, table3, table4;
	private DefaultTableModel model1, model2, model3, model4;
	private EmployeeDatabase db;
	private JScrollPane sp1, sp2, sp3, sp4;

	public EMA() {
		setTitle("Check SQL");
		setSize(1014, 370);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new MigLayout("", "[984px,grow]", "[461px][grow]"));
		setVisible(true);

		try {
			db = new EmployeeDatabase();
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); // Log the exception
		}

		model1 = new DefaultTableModel();

		model2 = new DefaultTableModel();
		table1 = new JTable(model1);
		sp1 = new JScrollPane(table1);
		getContentPane().add(sp1, "cell 0 0,grow");
		table2 = new JTable(model2);
		sp2 = new JScrollPane(table2);
		getContentPane().add(sp2, "cell 0 1,grow");

		model3 = new DefaultTableModel();
		table3 = new JTable(model3);
		sp3= new JScrollPane(table3);
		getContentPane().add(sp3, "cell 0 2,grow");

		model4 = new DefaultTableModel();
		table4 = new JTable(model4);
		sp4= new JScrollPane(table4);
		getContentPane().add(sp4, "cell 0 3,grow");

		try {
			ResultSet resultSet = db.getEMP();
			populateTable(resultSet,model1);
			

			ResultSet resultSet2 = db.getAttendance();
			populateTable(resultSet2,model2);
			
			ResultSet resultSet3 = db.getTypes();
			populateTable(resultSet3,model3);
			
			ResultSet resultSet4 = db.getUsers();
			populateTable(resultSet4,model4);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	private void populateTable(ResultSet resultSet, DefaultTableModel model1) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();

		// Clear existing columns from table model
		model1.setColumnCount(0);

		// Add columns to table model
		int columnCount = metaData.getColumnCount();
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			model1.addColumn(metaData.getColumnName(columnIndex));
		}

		// Add rows to table model
		while (resultSet.next()) {
			Object[] rowData = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				rowData[i] = resultSet.getObject(i + 1);
			}
			model1.addRow(rowData);
		}

		resultSet.close();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(EMA::new);
	}
}


 class EmployeeDatabase {
	private Connection connection;

	public EmployeeDatabase() throws SQLException, ClassNotFoundException {
		connect();
	}

	public void connect() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite::resource:DB/bluejayDB.sqlite");
		if (connection == null) {
			JOptionPane.showMessageDialog(null, "Failed to connect to Database", "Error", JOptionPane.ERROR_MESSAGE);
			throw new SQLException("Failed to establish connection to the database.");
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public ResultSet getEMP() throws SQLException {
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees");
		return statement.executeQuery();
	}
	
	public ResultSet getUsers() throws SQLException {
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
		return statement.executeQuery();
	}
	
	public ResultSet getAttendance() throws SQLException 
	{
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM attendance");
		return statement.executeQuery();
			
	}
	
	public ResultSet getDeductions() throws SQLException 
	{
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM deductions");
		return statement.executeQuery();
			
	}

	public ResultSet getTypes() throws SQLException 
	{
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM types");
		return statement.executeQuery();
			
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