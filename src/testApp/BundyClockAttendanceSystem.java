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
    String employeeName;
    private void clockIn() {
         employeeName = JOptionPane.showInputDialog("Enter your name:");
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
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            String currentTime = getCurrentTime();
            List<String> records = attendanceRecords.getOrDefault(employeeName, new ArrayList<>());
            records.add("Clock Out: " + currentTime);
            attendanceRecords.put(employeeName, records);
            updateStatus("Clocked Out at " + currentTime);
            updateAttendanceTextArea();
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
        return String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
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