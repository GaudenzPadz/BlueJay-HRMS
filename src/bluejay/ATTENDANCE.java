package bluejay;

import java.sql.Time;
import java.util.Date;

public class ATTENDANCE {
    private String name;
    private Date date;
    private Time clockInTime, clockOutTime;
    private double overtimeHours;

    public ATTENDANCE() {
    }

    public ATTENDANCE(String name, Date date, Time clockInTime, Time clockOutTime, double overtimeHours) {
        this.name = name;
        this.date = date;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.overtimeHours = overtimeHours;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(Time time) {
        this.clockInTime = time;
    }

    public Time getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(Time time) {
        this.clockOutTime = time;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
	
}
