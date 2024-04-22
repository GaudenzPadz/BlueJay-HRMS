package bluejayDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import bluejay.Employee;

public class EmployeeDatabase {
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

	public String validateLogin(String username, String password) {
		try {
			String sql = "SELECT name FROM users WHERE username = ? AND password = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				String name = rs.getString("name");
				return "Login successful! Welcome " + name;
			} else {
				return "Invalid username or password.";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return "Cant connect"; // replace with better idk
		}
	}

	public ResultSet getAllData() throws SQLException {
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}
		System.out.println("ResultSet Method is called getAllData()"); // for debugging

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees");
		return statement.executeQuery();
	}

	public ResultSet getTypes() {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT work_type FROM types");
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void insertEMPData(String first_name, String last_name, String address, String workType, String gender,
			String telNum, Date DOB, String email) {
		try {
			// Insert the new employee with the new ID
			String sql = "INSERT INTO employees (id, first_name, last_name, address, work_type, gender, tel_number, DOB, email) VALUES (?, ?, ?, ?, ?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			// Increment the last ID to get the new ID
			int newId = getLastEmployeeId() + 1;
			statement.setInt(1, newId);
			statement.setString(2, first_name);
			statement.setString(3, last_name);
			statement.setString(4, address);
			statement.setString(5, workType);
			statement.setString(6, gender);
			statement.setString(7, telNum);
			statement.setDate(8, DOB);
			statement.setString(9, email);

			statement.executeUpdate();
			System.out.println("Record created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertEMPCredentials(String name, String username, String passw) {
		try {
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO users (name, username, password) VALUES (?,?,?)");
			statement.setString(1, name);
			statement.setString(2, username);
			statement.setString(3, passw);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Helper method to get the last employee ID
	private int getLastEmployeeId() throws SQLException {
		int lastId = 0;
		PreparedStatement lastIdStatement = connection.prepareStatement("SELECT MAX(id) FROM employees");
		ResultSet rs = lastIdStatement.executeQuery();
		if (rs.next()) {
			lastId = rs.getInt(1); // Get the last ID
		}
		rs.close();
		return lastId;
	}

	public void deleteEmployeeData(int id) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM employees WHERE id = ?");
			statement.setInt(1, id);
			statement.executeUpdate();
			System.out.println("Record deleted.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateData(int id, String columnName, Object updatedValue) {
		try {
			String sql = "UPDATE employees SET " + columnName + " = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setObject(1, updatedValue);
			statement.setInt(2, id);
			statement.executeUpdate();
			System.out.println("Record updated.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Employee getEmployeeById(int id) throws SQLException {
		String sql = "SELECT * FROM employees WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();

		if (rs.next()) {
			Employee employee = new Employee();
			employee.setId(rs.getInt("id"));
			employee.setFirstName(rs.getString("first_name"));
			employee.setLastName(rs.getString("last_name"));
			// Set other fields similarly
			return employee;
		} else {
			return null; // No employee found with the given ID
		}
	}

	public void updateEmployee(Employee employee) {
		String sql = "UPDATE employees SET first_name = ?, last_name = ?, address = ?, work_type = ?, rate = ? WHERE id = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, employee.getFirstName());
			statement.setString(2, employee.getLastName());
			statement.setString(3, employee.getAddress());
			statement.setString(4, employee.getWorkType());
			statement.setDouble(5, employee.getRate());

			// ID should be the last parameter
			statement.setInt(6, employee.getId());

			statement.executeUpdate();
			System.out.println("Record updated.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertAttendanceData(String name, Date date, Time timeIn, Time timeOut, int overtime) {
		try {
			String sql = "INSERT INTO attendance (name, date, time_in, time_out, overtime) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setDate(2, date);

			// Format timeIn and timeOut as strings
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			String timeInStr = sdf.format(timeIn);
			String timeOutStr = sdf.format(timeOut);

			// Convert timeIn and timeOut strings back to Time objects
			Time timeInFormatted = Time.valueOf(timeInStr);
			Time timeOutFormatted = Time.valueOf(timeOutStr);

			statement.setTime(3, timeInFormatted);
			statement.setTime(4, timeOutFormatted);
			statement.setInt(5, overtime);
			statement.executeUpdate();
			System.out.println("Attendance record created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getAllAttendanceData() {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM attendance");
			return statement.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}

	public double countAttendances(String name) {
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT COUNT(*) AS count FROM attendance WHERE name = ?");
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void readAttendanceData(DefaultTableModel model) {
		try {
			model.setRowCount(0);
			PreparedStatement state = connection
					.prepareStatement("SELECT date, time_in, time_out, overtime FROM attendance");
			ResultSet rs = state.executeQuery();

			while (rs.next()) {
				Date date = rs.getDate("date");
				Time timeIn = rs.getTime("time_in");
				Time timeOut = rs.getTime("time_out");
				int overtime = rs.getInt("overtime");

				Object[] row = { date, timeIn, timeOut, overtime };
				model.addRow(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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