package bluejay;

import java.util.HashMap;

public class ATTENDANCE {
	private String timeIn;
	private String timeOut;
	private boolean overtime;

	public ATTENDANCE() {

	}

	public ATTENDANCE(String timeIn, String timeOut, boolean overtime) {
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.overtime = overtime;
	}

	// Getters and setters
	public String getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isOvertime() {
		return overtime;
	}

	public void setOvertime(boolean overtime) {
		this.overtime = overtime;
	}

	HashMap<Integer, Boolean> absences = new HashMap<>();

	public void setAbsent(int day) {
		absences.put(day, true);
	}

	public boolean isAbsent(int day) {
		return absences.getOrDefault(day, false);
	}
}
