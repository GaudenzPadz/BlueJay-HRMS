package testApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.stream.IntStream;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class TimePicker extends JPanel {
    private JComboBox<String> hoursBox;
    private JComboBox<String> minutesBox;
    private JComboBox<String> periodBox;
    private JTextField textField;
    private JLayeredPane layeredPane;  // Layered pane for floating effect
    private JPanel comboPanel;  // Combo panel for time selection

    public TimePicker() {
        setLayout(new BorderLayout(0, 0));

        // Create the layered pane to contain different layers
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(200, 100)); // Adjust as needed

        // Create combo panel to add to the layered pane (for floating effect)
        comboPanel = createComboPanel(); // Initialize combo panel
        comboPanel.setBounds(10, 30, 180, 40); // Position the combo panel on the layered pane
        comboPanel.setVisible(false); // Initially hidden

        // Add combo panel to the layered pane
        layeredPane.add(comboPanel, JLayeredPane.POPUP_LAYER); // Set it to the popup layer for floating effect

        // Create the text field panel and add to the layered pane
        JPanel textFieldPanel = createTextFieldPanel(); // Reuse object creation
        textFieldPanel.setBounds(10, 10, 180, 20); // Position the text field panel on the layered pane

        layeredPane.add(textFieldPanel, JLayeredPane.DEFAULT_LAYER); // Main layer for the text field

        // Add the layered pane to the main layout
        add(layeredPane, BorderLayout.CENTER); // Place the layered pane at the center

        // Initialize with the current time
        setCurrentTime();
        updateTextField();  // Ensure the text field reflects the selected time
    }

    private JPanel createComboPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(
            new EtchedBorder(EtchedBorder.LOWERED, Color.WHITE, new Color(160, 160, 160)),
            "ChatGPT Time Selector", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK
        ));

        // Initialize combo boxes with hours, minutes, and AM/PM periods
        hoursBox = new JComboBox<>(IntStream.rangeClosed(1, 12).mapToObj(String::valueOf).toArray(String[]::new));
        minutesBox = new JComboBox<>(IntStream.rangeClosed(0, 59).mapToObj(i -> String.format("%02d", i)).toArray(String[]::new));
        periodBox = new JComboBox<>(new String[] { "AM", "PM" });

        panel.add(hoursBox);
        panel.add(new JLabel(":"));
        panel.add(minutesBox);
        panel.add(periodBox);

        // Add "Set" button to the panel
        JButton setTimeBtn = new JButton("Set");
        panel.add(setTimeBtn);

        setTimeBtn.addActionListener(e -> {
            updateTextField();
            comboPanel.setVisible(false); // Explicitly close the combo panel when "Set" is clicked
        });

        return panel;  // Return the created combo panel
    }

    private JPanel createTextFieldPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Text Field to Select a Time",
            TitledBorder.LEADING, TitledBorder.TOP, null, null
        ));

        textField = new JTextField(10);
        textField.setToolTipText("Click to Select Time");

        // Toggle the visibility of the combo panel when the text field is clicked
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                comboPanel.setVisible(!comboPanel.isVisible()); // Toggle visibility
            }
        });

        panel.add(textField); // Add the text field to the panel
        return panel; // Return the created text field panel
    }

    private void updateTextField() {
        textField.setText(getTime());  // Update the text field with the selected time
    }

    private void setCurrentTime() {
        LocalTime now = LocalTime.now();

        // Determine AM/PM and convert 24-hour to 12-hour format
        boolean isPM = now.getHour() >= 12;
        int hour = now.getHour() % 12;
        if (hour == 0) {
            hour = 12;  // Adjustment for 12-hour clock
        }

        // Set the selected items in the combo boxes
        hoursBox.setSelectedItem(String.valueOf(hour));
        minutesBox.setSelectedItem(String.format("%02d", now.getMinute()));
        periodBox.setSelectedItem(isPM ? "PM" : "AM");
    }

    public String getTime() {
        return String.format("%s:%s %s",
                             (String) hoursBox.getSelectedItem(),
                             (String) minutesBox.getSelectedItem(),
                             (String) periodBox.getSelectedItem()
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Time Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        TimePicker timePicker = new TimePicker();
        frame.getContentPane().add(timePicker);

        frame.setVisible(true);  // Show the frame
    }
}
