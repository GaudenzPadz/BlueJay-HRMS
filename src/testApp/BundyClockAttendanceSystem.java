package testApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class BundyClockAttendanceSystem extends JFrame {
    private JLabel statusLabel;
    private JButton clockInButton, clockOutButton;
    private JTextArea attendanceTextArea;
    private final  Map<String, List<String>> attendanceRecords;

    public BundyClockAttendanceSystem() {
        setTitle("Bundy Clock Attendance System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        statusLabel = new JLabel("Status: ");
        clockInButton = new JButton("Clock In");
        clockOutButton = new JButton("Clock Out");
        attendanceTextArea = new JTextArea();
        attendanceRecords = new HashMap<>();

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(statusLabel);
        controlPanel.add(clockInButton);
        controlPanel.add(clockOutButton);

        JScrollPane scrollPane = new JScrollPane(attendanceTextArea);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        clockInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clockIn();
            }
        });

        clockOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clockOut();
            }
        });
    }

    private void clockIn() {
        String employeeName = JOptionPane.showInputDialog("Enter your name:");
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            String currentTime = getCurrentTime();
            List<String> records = attendanceRecords.getOrDefault(employeeName, new ArrayList<>());
            records.add("Clock In: " + currentTime);
            attendanceRecords.put(employeeName, records);
            updateStatus("Clocked In at " + currentTime);
            updateAttendanceTextArea();
        }
    }

    private void clockOut() {
        String employeeName = JOptionPane.showInputDialog("Enter your name:");
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            List<String> records = attendanceRecords.get(employeeName);
            if (records != null && !records.isEmpty() && records.get(records.size() - 1).startsWith("Clock In")) {
                String currentTime = getCurrentTime();
                records.add("Clock Out: " + currentTime);
                attendanceRecords.put(employeeName, records);
                updateStatus("Clocked Out at " + currentTime);
                updateAttendanceTextArea();
            } else {
                updateStatus("Error: No clock in record found for " + employeeName);
            }
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText("Status: " + message);
    }

    private void updateAttendanceTextArea() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : attendanceRecords.entrySet()) {
            sb.append(entry.getKey()).append(":\n");
            for (String record : entry.getValue()) {
                sb.append("- ").append(record).append("\n");
            }
            sb.append("\n");
        }
        attendanceTextArea.setText(sb.toString());
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String amOrPm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        return String.format("%02d:%02d:%02d %s", hour, minute, second, amOrPm);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BundyClockAttendanceSystem system = new BundyClockAttendanceSystem();
                system.setVisible(true);
            }
        });
    }
}