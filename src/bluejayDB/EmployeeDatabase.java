package bluejayDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import bluejayV2.Employee;

public class EmployeeDatabase {
	private Connection connection;

	public EmployeeDatabase() throws SQLException, ClassNotFoundException {
		connect();
	}

	public void connect() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		// Ensure connection is closed if previously opened
		if (this.connection != null) {
			this.connection.close();
		}
		this.connection = DriverManager.getConnection("jdbc:sqlite::resource:DB/bluejayDB.sqlite");
		if (this.connection == null) {
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
			String sql = "SELECT e.*, t.work_type FROM employees e " + "LEFT JOIN types t ON e.work_type_id = t.id "
					+ "WHERE e.email = (SELECT email FROM users WHERE username = ?)";
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
				employee.setBasicSalary(rs.getInt("rate"));
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

	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		String sql = "SELECT e.id, e.first_name, e.middle_name, e.last_name, e.address, d.department_name AS department, "
				+ "t.work_type, e.rate, e.grossPay, e.netPay, e.gender, e.tel_Number, e.email, e.profile_image, e.date_hired, e.DOB "
				+ "FROM employees e " + "LEFT JOIN types t ON e.work_type_id = t.id "
				+ "LEFT JOIN department d ON e.department_id = d.department_id";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Employee employee = new Employee();
				employee.setId(rs.getInt("id"));
				employee.setFirstName(rs.getString("first_name"));
				employee.setMiddleName(rs.getString("middle_name"));
				employee.setLastName(rs.getString("last_name"));
				employee.setAddress(rs.getString("address"));
				employee.setDepartment(rs.getString("department")); // Ensure this is correct
				employee.setWorkType(rs.getString("work_type"));
				employee.setBasicSalary(rs.getDouble("rate"));
				employee.setGrossPay(rs.getDouble("grossPay"));
				employee.setNetPay(rs.getDouble("netPay"));
				employee.setGender(rs.getString("gender"));
				employee.setTelNUmber(rs.getString("tel_number"));
				employee.setEmail(rs.getString("email"));
				byte[] imageData = rs.getBytes("profile_image");
				if (imageData != null) {
					employee.setProfileImage(imageData);
				} else {
					employee.setProfileImage(null); // Handle null image data
				}
				employee.setDateHired(rs.getDate("date_hired"));
				employee.setDOB(rs.getDate("DOB"));
				employees.add(employee);
			}
			rs.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error Getting All Employees Data " + e.getMessage(),
					"Initialization Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return employees;
	}

	public ResultSet getAllData() throws SQLException {
		if (connection == null) {
			throw new SQLException("Connection is null. Make sure to establish the connection.");
		}
		String query = "SELECT e.id, e.first_name, e.last_name, e.address, d.department_name AS department, "
				+ "t.work_type, e.rate, e.grossPay, e.netPay " + "FROM employees e "
				+ "LEFT JOIN types t ON e.work_type_id = t.id "
				+ "LEFT JOIN department d ON e.department_id = d.department_id";

		PreparedStatement statement = connection.prepareStatement(query);

		return statement.executeQuery();
	}

	public ResultSet getWorkTypesByDepartment(int departmentId) throws SQLException {
		String query = "SELECT work_type FROM types WHERE department_id = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(1, departmentId);
		return ps.executeQuery();
	}

	public ResultSet getDepartments() throws SQLException {
		String query = "SELECT department_name FROM department";
		PreparedStatement ps = connection.prepareStatement(query);
		return ps.executeQuery();
	}

	public int getDepartmentId(String departmentName) {
		String query = "SELECT department_id FROM department WHERE department_name = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, departmentName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("department_id"); // Return the found department_id
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // Return -1 if not found
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

	public void insertEMPData(String first_name, String last_name, String address, String workTypeId, double wage,
			String gender, String telNum, java.sql.Date DOB, String email, byte[] imageData, int departmentId) { // Add
																													// departmentId
																													// parameter
		try {
			String sql = "INSERT INTO employees (id, first_name, last_name, address, work_type_id, rate, gender, tel_number, DOB, email, profile_image, department_id) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			int newId = getLastEmployeeId() + 1;
			statement.setInt(1, newId);
			statement.setString(2, first_name);
			statement.setString(3, last_name);
			statement.setString(4, address);
			statement.setString(5, workTypeId); // Corrected column name
			statement.setDouble(6, wage);
			statement.setString(7, gender);
			statement.setString(8, telNum);
			statement.setDate(9, DOB);
			statement.setString(10, email);
			statement.setBytes(11, imageData);
			statement.setInt(12, departmentId); // Insert the correct department_id

			statement.executeUpdate(); // Execute the insert
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
			String sql = "DELETE FROM employees WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
			System.out.println("Record deleted.");

			// Deleting from another table (example)
			try (PreparedStatement st = connection.prepareStatement("DELETE FROM users WHERE name = ?")) {
				st.setString(1, name);
				st.executeUpdate();
			}
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

	public synchronized void updateEmployee(Employee employee) {
		String sql = "UPDATE employees SET first_name = ?, last_name = ?, address = ?, work_type = ?, rate = ? WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, employee.getFirstName());
			statement.setString(2, employee.getLastName());
			statement.setString(3, employee.getAddress());
			statement.setString(4, employee.getWorkType());
			statement.setDouble(5, employee.getBasicSalary());
			statement.setInt(6, employee.getId());
			statement.executeUpdate();
			System.out.println("Record updated.");
		} catch (SQLException e) {
			System.err.println("Failed to update employee: " + e.getMessage());

			e.printStackTrace();
		}
	}

	// attendance DB methods
	public boolean hasCheckedIn(int employeeId, String date) {
		try {
			String query = "SELECT COUNT(*) AS count FROM attendance WHERE employee_id = ? AND date = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, employeeId);
			stmt.setString(2, date);
			ResultSet rs = stmt.executeQuery();
			boolean hasCheckedIn = rs.getInt("count") > 0;
			rs.close();
			stmt.close();
			return hasCheckedIn;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void addTimeIn(int employeeId, String name, String date, Time timeIn, String note) {
		try {
			String query = "INSERT INTO attendance (employee_id, name, date, time_in, note) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, employeeId);
			stmt.setString(2, name);
			stmt.setString(3, date);
			stmt.setTime(4, timeIn);
			stmt.setString(5, note);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace(); // Check if any SQL errors are raised
		}
	}

	public void updateTimeOut(int employeeId, String date, String timeOut, String clockOUTnote, int overtime) {
	    try {
	        // SQL query to update time_out, note, and overtime
	        String query = "UPDATE attendance SET time_out = ?, note = ?, overtime = ? WHERE employee_id = ? AND date = ?";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        
	        // Set parameters for the prepared statement
	        stmt.setString(1, timeOut);  // Time out value
	        stmt.setString(2, clockOUTnote);    // Note text
	        stmt.setInt(3, overtime);   // Overtime hours
	        stmt.setInt(4, employeeId); // Employee ID
	        stmt.setString(5, date);    // Attendance date
	        
	        // Execute the update
	        stmt.executeUpdate();
	        
	        // Close the prepared statement
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();  // Log any SQL errors
	    }
	}


	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public void loadEMPAttendanceData(int employeeId, DefaultTableModel model) {
		try {
			String query = "SELECT * FROM attendance WHERE employee_id = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, employeeId);
			ResultSet rs = stmt.executeQuery();

			// Clear existing rows
			model.setRowCount(0);

			while (rs.next()) {
				// Get Time In and Time Out as java.sql.Time
				Time timeIn = rs.getTime("time_in");
				Time timeOut = rs.getTime("time_out");

				// Convert to readable format
				String formattedTimeIn = (timeIn != null) ? timeFormat.format(timeIn) : "N/A";
				String formattedTimeOut = (timeOut != null) ? timeFormat.format(timeOut) : "N/A";

				// Add row to the table model with formatted time
				model.addRow(new Object[] { rs.getString("name"), rs.getString("date"), formattedTimeIn,
						formattedTimeOut, rs.getInt("overtime"), rs.getString("note") });
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadAttendanceData(DefaultTableModel model) {
		try {
			String query = "SELECT * FROM attendance";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getDate("date"), rs.getInt("employee_id"),
						rs.getString("name"), rs.getString("status"), rs.getTime("time_in"), rs.getTime("time_out"),
						rs.getInt("overtime"), rs.getString("note"), rs.getString("work_type") });
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addAttendanceRecord(int employeeId, String name, Date date, String status, Time timeIn, Time timeOut,
			int overtime, String note, String workType) {
		try {
			String query = "INSERT INTO attendance (employee_id, name, date, status, time_in, time_out, overtime, note, work_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, employeeId);
			stmt.setString(2, name);
			stmt.setDate(3, date);
			stmt.setString(4, status);
			stmt.setTime(5, timeIn);
			stmt.setTime(6, timeOut);
			stmt.setInt(7, overtime);
			stmt.setString(8, note);
			stmt.setString(9, workType);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteAttendance(int employeeId, String date) {
		try {
			String query = "DELETE FROM attendance WHERE employee_id = ? AND date = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, employeeId);
			stmt.setString(2, date);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// payroll DB methods
	public void insertPayroll(int employeeId, String name, String Department, String workType, Double grossPay,
			Double ratePerDay, int daysWorked, int overtimeHours, double bonus, double totalDeduction, double netPay,
			Date created_at) {
		String sql = "INSERT INTO payroll (employee_id, name, Department, workType, grossPay, ratePerDay, daysWorked, overtimeHours, bonus, totalDeduction, netPay, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId); // Employee ID
			pstmt.setString(2, name);
			pstmt.setString(3, Department);
			pstmt.setString(4, workType);
			pstmt.setDouble(5, grossPay);
			pstmt.setDouble(6, ratePerDay);
			pstmt.setInt(7, daysWorked);
			pstmt.setInt(8, overtimeHours);
			pstmt.setDouble(9, bonus);
			pstmt.setDouble(10, totalDeduction);
			pstmt.setDouble(11, netPay);
			pstmt.setDate(12, created_at);

			pstmt.executeUpdate(); // Executes the INSERT statement
		} catch (SQLException e) {
			System.err.println("Failed to insert data into the payroll table: " + e.getMessage());
		}
	}

	public void loadPayrollHistory(DefaultTableModel model) {

		String sql = "SELECT employee_id, name, Department, workType, grossPay, ratePerDay, daysWorked, "
				+ "overtimeHours, bonus, totalDeduction, netPay FROM payroll";

		try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			// Clear existing rows in the table model
			model.setRowCount(0);

			while (rs.next()) {
				int employeeId = rs.getInt("employee_id");
				String name = rs.getString("name");
				String department = rs.getString("Department");
				String workType = rs.getString("workType");
				double grossPay = rs.getDouble("grossPay");
				double ratePerDay = rs.getDouble("ratePerDay");
				double daysWorked = rs.getDouble("daysWorked");
				double overtimeHours = rs.getDouble("overtimeHours");
				double bonus = rs.getDouble("bonus");
				double totalDeduction = rs.getDouble("totalDeduction");
				double netPay = rs.getDouble("netPay");

				model.addRow(new Object[] { employeeId, name, department, workType, grossPay, ratePerDay, daysWorked,
						overtimeHours, bonus, totalDeduction, netPay });
			}

		} catch (SQLException e) {
			System.err.println("Failed to load payroll history: " + e.getMessage());
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