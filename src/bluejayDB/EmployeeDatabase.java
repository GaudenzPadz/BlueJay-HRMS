package bluejayDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bluejay.Employee;

public class EmployeeDatabase {
	private Connection connection;

	public EmployeeDatabase() throws SQLException, ClassNotFoundException {
		connect();
	}

	private void connect() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite::resource:DB/bluejayDB.sqlite");
		if (connection == null) {
			throw new SQLException("Failed to establish connection to the database.");
		}
	}

	public ResultSet getAllData() throws SQLException {
		System.out.println("method is called getAllData()");
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees");
		return statement.executeQuery();
	}

	public ResultSet getTypes() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT work_type FROM types");
		return statement.executeQuery();
	}

	public void insertData(String first_name, String last_name, String address, String workType) throws SQLException {
		String sql = "INSERT INTO employees (first_name, last_name, address, work_type) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, first_name);
		statement.setString(2, last_name);
		statement.setString(3, address);
		statement.setString(4, workType);
		statement.executeUpdate();
		System.out.println("Record created.");
	}

	public void deleteData(int id) throws SQLException {
		String sql = "DELETE FROM employees WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		statement.executeUpdate();
		System.out.println("Record deleted.");
	}

	public void resetTable() throws SQLException {
		String sql = "DELETE FROM employees";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		System.out.println("Table reset.");
	}

	public String validateLogin(String username, String password) throws SQLException {
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

	public void updateEmployee(Employee employee) throws SQLException {
		String sql = "UPDATE employees SET first_name = ?, last_name = ? WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, employee.getFirstName());
		statement.setString(2, employee.getLastName());
		statement.setInt(3, employee.getId());
		// Set other fields and parameters as needed
		statement.executeUpdate();
	}

	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
}