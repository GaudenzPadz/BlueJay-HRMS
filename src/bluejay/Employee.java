package bluejay;

import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class Employee {

    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String workType;
    private double rate;
    private double grossPay;
    private double netPay;
    private String gender;
    private ArrayList<String> allowances; // Use List for type safety
    private ArrayList<String> deductions;
    private int absents;
    private int late;
    private int dayOff;
    private double overtime;
    private String birthday; // Add birthday for personal information
    private double SSS;
    private double PAG_IBIG;
    private double PHILHEALTH;
    private int telNumber;
    private String email;

    // Constants for work types and their corresponding wage per day
    private double SMAW_WAGE = 500;
    private double GTAW_WAGE = 900;
    private double FCAW_WAGE = 900;
    private double GMAW_WAGE = 1000;

    public Employee()
    {

    }

    public Employee(int id, String firstName, String lastName, String address, String workType, double rate,
            double grossPay, double netPay, String gender) {
        // Initialize
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.workType = workType;
        this.rate = rate;
        this.grossPay = grossPay;
        this.netPay = netPay;
        this.gender = gender;
        this.email = email;
        this.middleName = middleName;

        // GAGO tite
        this.telNumber = telNumber;

        // not intialize (late nalang)
        this.allowances = new ArrayList<>();
        this.deductions = new ArrayList<>();
        this.absents = 0;
        this.late = 0;
        this.dayOff = 0;
        this.overtime = 0.0;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getWorkType() {
        return workType;
    }

    public double getRate() {
        return rate;
    }

    public String getGender() {
        return gender;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public double getNetPay() {
        return netPay;
    }

    public double getPAG_IBIG() {
        return PAG_IBIG;
    }

    public double getSSS() {
        return SSS;
    }

    public double getPHILHEALTH() {
        return PHILHEALTH;
    }

    public String getbirthday() {
        return birthday;
    }

    public ArrayList<String> getAllowances() {
        return allowances;
    }

    public ArrayList<String> getDeductions() {
        return deductions;
    }

    public int getAbsents() {
        return absents;
    }

    public int getTelNum() {
        return telNumber;
    }

    public int getLate() {
        return late;
    }

    public int getDayOff() {
        return dayOff;
    }

    public double getOvertime() {
        return overtime;
    }

    public String getEmail() {
        return email;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPAG_IBIG(double PAG_IBIG) {
        this.PAG_IBIG = PAG_IBIG;
    }

    public void setSSS(double SSS) {
        this.SSS = SSS;
    }

    public void setPHILHEALTH(double PHILHEALTH) {
        this.PHILHEALTH = PHILHEALTH;
    }

    public void setbirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAbsents(int absents) {
        this.absents = absents;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public void setDayOff(int dayOff) {
        this.dayOff = dayOff;
    }

    public void setOvertime(double overtime) {
        this.overtime = overtime;
    }

    public void setTelNUmber(int telNumber) {
        this.telNumber = telNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Add method
    public void addAllowance(String allowance) {
        this.allowances.add(allowance);
    }

    public void addDeduction(String deduction) {
        this.deductions.add(deduction);
    }

    // Method to calculate deductions
    public double calculateDeductions(double grossPay) {
        double sssDeduction = grossPay * getSSS();
        double pagIbigDeduction = grossPay * getPAG_IBIG();
        double philHealthDeduction = grossPay * getPHILHEALTH();
        return sssDeduction + pagIbigDeduction + philHealthDeduction;
    }

    // Method to calculate gross pay
    public static double calculateGrossPay(double daysWorked, double wagePerDay) {
        return daysWorked * wagePerDay;
    }

    // Method to calculate net pay
    public static double calculateNetPay(double grossPay, double deductions) {
        return grossPay - deductions;
    }

    // Method to record attendance
    public void recordAttendance(String timeIn, String timeOut) {
        // Implement logic to record attendance
        LocalTime in = LocalTime.parse(timeIn);
        LocalTime out = LocalTime.parse(timeOut);

        if (in.isAfter(LocalTime.of(8, 0))) { // Assuming 8:00 AM is the start time
            // Employee is late
            this.late++;
        }

        // Assuming 5:00 PM is the end time
        if (out.isAfter(LocalTime.of(17, 0))) {
            // Calculate overtime
            this.overtime += out.getHour() - 17 + ((double) out.getMinute() / 60);
        }
    }

    // Method to apply for leave
    public void applyForLeave(int days) {
        // Implement logic to apply for leave
        this.dayOff += days;
    }

    public static ArrayList<Employee> employees = new ArrayList<>();


    public static void removeEmployee(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Please select a row to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (Integer) table.getModel().getValueAt(selectedRow, 0); // Assuming ID is in column 0
        Employee employeeToRemove = null;
        for (Employee employee : Employee.getEmployees()) {
            if (employee.getId() == id) {
                employeeToRemove = employee;
                break;
            }
        }

        if (employeeToRemove == null) {
            System.out.println("Employee not found");
            return;
        }

        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to remove " + employeeToRemove.getFirstName() + " "
                        + employeeToRemove.getLastName() + "?",
                "Remove Row Confirmation", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            Employee.getEmployees().remove(employeeToRemove); // Remove from employee list
        }
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

}