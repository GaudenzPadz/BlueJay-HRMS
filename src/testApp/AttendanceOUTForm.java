package testApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import bluejayDB.EmployeeDatabase;
import bluejayV2.Employee;
import bluejayV2.Main;
import net.miginfocom.swing.MigLayout;

public class AttendanceOUTForm extends JPanel {

	private static final long serialVersionUID = 1L;
	private Employee employee;

	public AttendanceOUTForm(Employee employee) {
		// Set layout
				setLayout(new BorderLayout());
				ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));

				JPanel mainPanel = new JPanel(new MigLayout("fill,insets 20", "[center]", "[center]"));
				add(mainPanel, BorderLayout.CENTER);

				JPanel attForm = attendanceForm();
				mainPanel.add(attForm, "cell 0 0,alignx center,aligny center");


	}



	private JPanel attendanceForm() {
		JPanel attendanceOUTForm = new JPanel(
				new MigLayout("wrap,fillx,insets 25 35 25 35", "[200px,grow,center][100px][200px]",
						"[pref!,center][pref!][pref!][pref!][pref!][pref!][pref!][pref!][pref!,grow]"));
		attendanceOUTForm.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,3%);" + "[dark]background:lighten(@background,3%)");

		attendanceOUTForm.add(new JLabel("Time Out"), "cell 0 0,alignx left");

		attendanceOUTForm.add(new JLabel("Note"), "cell 2 0,alignx center,aligny center");

		// Hours Field
		JTextField hourOUTField = new JTextField(2);
		hourOUTField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceOUTForm.add(hourOUTField, "flowx,cell 0 1,alignx left");

		attendanceOUTForm.add(new JLabel(":"), "cell 0 1");

		// Minutes Field
		JTextField minutesOUTField = new JTextField(2);
		minutesOUTField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (Character.isDigit(str.charAt(0)) && getLength() < 2) {
					super.insertString(offs, str, a);
				}
			}
		});
		attendanceOUTForm.add(minutesOUTField, "cell 0 1");

		// AM/PM ComboBox
		JComboBox AmPmOUTCombo = new JComboBox<>(new String[] { "AM", "PM" });
		attendanceOUTForm.add(AmPmOUTCombo, "cell 0 1");

		// Set current time to Time In fields
//		setCurrentTimeToFields();

		JTextArea noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		attendanceOUTForm.add(noteArea, "cell 2 1 1 6,grow");

		JCheckBox OTcheck = new JCheckBox("Overtime?");
		attendanceOUTForm.add(OTcheck, "flowx,cell 0 3,alignx left");

		attendanceOUTForm.add(new JLabel("How Many Hours? "), "flowx,cell 0 4,alignx left");

		JTextField OThourField = new JTextField();
		attendanceOUTForm.add(OThourField, "cell 0 4,alignx right");
		OThourField.setColumns(5);

		JButton OUTBtn = new JButton("Time Out");
		attendanceOUTForm.add(OUTBtn, "cell 1 8,growx");
		return attendanceOUTForm;
	}

}
