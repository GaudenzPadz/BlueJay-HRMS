package bluejayV2.admin;
import java.awt.Font;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;
import net.miginfocom.swing.MigLayout;

public class AttendancePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel attendanceModel;
    private EmployeeDatabase db;
    private List<Employee> employees;
    private JTable attendanceTable;

    public AttendancePanel(EmployeeDatabase db) {
        this.db = db;
        initializeUI();
        loadAttendanceData();
    }

    private void initializeUI() {
        setLayout(new MigLayout("wrap, fillx, insets 25 35 25 35", "[grow]", "[][][][grow][]"));

        attendanceModel = new DefaultTableModel(new String[] {"ID", "Date", "Employee ID", "Name", "Status", "Clock In",  "Clock Out"}, 0);

        JLabel lblNewLabel = new JLabel("Attendance Monitor");
        lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(lblNewLabel, "cell 0 1");

        attendanceTable = new JTable(attendanceModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, "cell 0 2,grow");

        JButton addButton = new JButton("Add Attendance");
        addButton.addActionListener(e -> addAttendance());
        add(addButton, "cell 0 3,sizegroupx btn,sizegroupy btn");

        JButton deleteButton = new JButton("Delete Attendance");
        deleteButton.addActionListener(e -> deleteAttendance());
        add(deleteButton, "cell 0 3,sizegroupx btn,sizegroupy btn");
    }

    private void loadAttendanceData() {
        attendanceModel.setRowCount(0);
        db.loadAttendanceData(attendanceModel);
    }

    private void addAttendance() {
        // Gather attendance details from user input
        JTextField employeeIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField overtimeField = new JTextField();
        JTextField noteField = new JTextField();
        JTextField workTypeField = new JTextField();
        
        JPanel panel = new JPanel(new MigLayout());
        panel.add(new JLabel("Employee ID:"), "cell 0 0");
        panel.add(employeeIdField, "cell 1 0");
        panel.add(new JLabel("Name:"), "cell 0 1");
        panel.add(nameField, "cell 1 1");
        panel.add(new JLabel("Status:"), "cell 0 2");
        panel.add(statusField, "cell 1 2");
        panel.add(new JLabel("Overtime:"), "cell 0 3");
        panel.add(overtimeField, "cell 1 3");
        panel.add(new JLabel("Note:"), "cell 0 4");
        panel.add(noteField, "cell 1 4");
        panel.add(new JLabel("Work Type:"), "cell 0 5");
        panel.add(workTypeField, "cell 1 5");

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Attendance", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                String name = nameField.getText();
                String status = statusField.getText();
                int overtime = Integer.parseInt(overtimeField.getText());
                String note = noteField.getText();
                String workType = workTypeField.getText();
                
                Date currentDate = new Date(System.currentTimeMillis());
                Time currentTime = new Time(System.currentTimeMillis());
                
                db.addAttendanceRecord(employeeId, name, currentDate, status, currentTime, null, overtime, note, workType);
                loadAttendanceData(); // Reload attendance data to refresh the table
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input, please enter correct data", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAttendance() {
        int row = attendanceTable.getSelectedRow();
        if (row >= 0) {
            int employeeId = Integer.parseInt(attendanceModel.getValueAt(row, 2).toString());
            String date = attendanceModel.getValueAt(row, 1).toString();
            db.deleteAttendance(employeeId, date);
            loadAttendanceData();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an attendance record to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
