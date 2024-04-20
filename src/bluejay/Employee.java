package bluejay;

import java.sql.Date;

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
	private int absents;
	private int late;
	private int dayOff;
	private double daysWorked;
	private double overtime;
	private Date birthday;
	private double SSS;
	private double PAG_IBIG;
	private double PHILHEALTH;
	private int telNumber;
	private String email;
	private Date dateHired;


	public Employee() {

	}

	public Employee(int id, String firstName, String middleName, String lastName, String address, String workType, double rate,
			double grossPay, double netPay, String gender) {
		// Initialize
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.address = address;
		this.workType = workType;
		this.rate = rate;
		this.grossPay = grossPay;
		this.netPay = netPay;
		this.gender = gender;
		this.email = email;
		this.middleName = middleName;
		// this.birthday = birthday
		this.telNumber = telNumber;

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

	public Date getbirthday() {
		return birthday;
	}

	public int getAbsents() {
		return absents;
	}

	public int getTelNumber() {
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

	public Date getDateHired() {
		return dateHired;
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

	public void setbirthday(Date birthday) {
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

	public void setDateHired(Date dateHired) {
		this.dateHired = dateHired;
	}

	public double getDaysWorked() {
		return daysWorked;
	}

	public void setDaysWorked(double daysWorked) {
		this.daysWorked = daysWorked;
	}

	// METHODS TO CALCULATE

	// Method to calculate the total deduction
	public double totalDeductions() {
		return getPAG_IBIG() + getPHILHEALTH() + getSSS();
	}
	
	// Method to calculate gross pay
	public double calculateGrossPay(double daysWorked, double wagePerDay) {
		return daysWorked * wagePerDay;
	}

	// Method to calculate net pay
	public double calculateNetPay(double grossPay, double deductions) {
		return grossPay - deductions;
	}

}