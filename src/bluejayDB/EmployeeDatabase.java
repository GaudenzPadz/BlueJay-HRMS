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
			String sql = "SELECT name, role FROM users WHERE username = ? AND password = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				String name = rs.getString("name");
				String role = rs.getString("role");
				return "Login successful! Welcome " + name + " (Role: " + role + ")";
			} else {
				return "Invalid username or password.";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return "Database error during login.";
		}
	}

	public Employee getEmployeeDataByUsername(String username) {
		try {
			String sql = "SELECT * FROM employees WHERE email = (SELECT email FROM users WHERE username = ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				Employee employee = new Employee();
				employee.setId(rs.getInt("id"));
				employee.setFirstName(rs.getString("first_name"));
				employee.setMiddleName(rs.getString("middle_name"));
				employee.setLastName(rs.getString("last_name"));
				employee.setAddress(rs.getString("address"));
				employee.setWorkType(rs.getString("work_type"));
				employee.setRate(rs.getInt("rate"));
				employee.setGender(rs.getString("gender"));
				employee.setTelNUmber(rs.getString("tel_number"));
				employee.setEmail(rs.getString("email"));
				employee.setDateHired(rs.getDate("date_hired"));
				employee.setDOB(rs.getDate("DOB"));

				byte[] imageData = rs.getBytes("profile_image");
				if (imageData != null) {
					employee.setProfileImage(imageData);
				}

				return employee;
			} else {
				return null; // No employee data found for this username
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null; // Handle SQL error
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
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM types");
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void insertEMPData(String first_name, String last_name, String address, String workType, double wage,
			String gender, String telNum, Date DOB, String email, byte[] imageData) {
		try {
			// Insert the new employee with the new ID
			String sql = "INSERT INTO employees (id, first_name, last_name, address, work_type, rate, gender, tel_number, DOB, email, profile_image) VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			// Increment the last ID to get the new ID
			int newId = getLastEmployeeId() + 1;
			statement.setInt(1, newId);
			statement.setString(2, first_name);
			statement.setString(3, last_name);
			statement.setString(4, address);
			statement.setString(5, workType);
			statement.setDouble(6, wage);
			statement.setString(7, gender);
			statement.setString(8, telNum);
			statement.setDate(9, DOB);
			statement.setString(10, email);
			statement.setBytes(11, imageData);

			statement.executeUpdate();
			System.out.println("Record created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertEMPCredentials(String name, String username, String passw, String role) {
		try {
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO users (name, username, password, role) VALUES (?,?,?,?)");
			statement.setString(1, name);
			statement.setString(2, username);
			statement.setString(3, passw);
			statement.setString(4, role);

			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Helper method to get the last employee ID
	public int getLastEmployeeId() throws SQLException {
		int lastId = 0;
		PreparedStatement lastIdStatement = connection.prepareStatement("SELECT MAX(id) FROM employees");
		ResultSet rs = lastIdStatement.executeQuery();
		if (rs.next()) {
			lastId = rs.getInt(1); // Get the last ID
		}
		rs.close();
		return lastId;
	}

	public void deleteEmployeeData(int id, String name) {
		try {
			connection.setAutoCommit(false); // Begin transaction

			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM employees WHERE id = ?")) {
				statement.setInt(1, id);
				statement.executeUpdate();
			}

			try (PreparedStatement st = connection.prepareStatement("DELETE FROM users WHERE name = ?")) {
				st.setString(1, name);
				st.executeUpdate();
			}

			connection.commit(); // Commit transaction
			System.out.println("Record deleted.");

		} catch (SQLException e) {
			try {
				connection.rollback(); // Rollback transaction on error
				System.err.println("Error during deletion. Rolled back.");
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true); // Restore auto-commit
			} catch (SQLException autoCommitEx) {
				autoCommitEx.printStackTrace();
			}
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

	public void insertAttendance(int employeeId, String name, String date, String timeIn, String overtime,
			String note) {
		String insertSQL = "INSERT INTO attendance (employee_id, name, date, time_in, time_out, overtime, note) VALUES (?, ?, ?, ?, ?, ?,?)";

		try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
			pstmt.setInt(1, employeeId); // Use employee ID
			pstmt.setString(2, name);
			pstmt.setString(3, date); // Current date
			pstmt.setString(4, timeIn); // Time In
			pstmt.setString(5, overtime);
			pstmt.setString(6, note);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public double countAttendances(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM attendance WHERE name = ?");
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

	public void loadAttendanceData(int employeeId, DefaultTableModel model) {
		try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM attendance WHERE employee_id = ?")) {
			pstmt.setInt(1, employeeId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				// Add data to the JTable model
				model.addRow(new Object[] { rs.getString("name"), rs.getString("date"), rs.getString("time_in"),
						rs.getString("time_out"), rs.getString("overtime"), rs.getString("note") });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTimeOut(int employeeId, String date, String timeOut) {
		String updateSQL = "UPDATE attendance SET time_out = ? WHERE employee_id = ? AND date = ? AND time_out IS NULL";

		try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
			pstmt.setString(1, timeOut);
			pstmt.setInt(2, employeeId);
			pstmt.setString(3, date);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasCheckedIn(int employeeId, String date) {
		String query = "SELECT * FROM attendance WHERE name = ? AND date = ? AND time_out IS NULL";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, employeeId); // Use employee ID
			pstmt.setString(2, date);

			ResultSet rs = pstmt.executeQuery();

			return rs.next(); // Returns true if there's a record without Time Out for this date

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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