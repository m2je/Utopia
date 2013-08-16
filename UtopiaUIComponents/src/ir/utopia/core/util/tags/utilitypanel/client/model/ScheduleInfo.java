package ir.utopia.core.util.tags.utilitypanel.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ScheduleInfo implements Serializable, IsSerializable {
	public static final int SCHEDULE_TYPE_HOURLY=1;
	public static final int SCHEDULE_TYPE_DAILY=2;
	public static final int SCHEDULE_TYPE_MONTHLY=3;
	public static final int SCHEDULE_TYPE_YEARLY=4;
	
	public static final int DAY_OF_WEEK_SUNDAY=0;
	public static final int DAY_OF_WEEK_MONDAY=1;
	public static final int DAY_OF_WEEK_TUSEDAY=2;
	public static final int DAY_OF_WEEK_WEDENESDAY=3;
	public static final int DAY_OF_WEEK_THUSDAY=4;
	public static final int DAY_OF_WEEK_FRIDAY=5;
	public static final int DAY_OF_WEEK_SATURDAY=6;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8247199001647599808L;
	Integer []daysOfWeek;
	String startHour,finishHour;
	int interval;
	int[] daysOfMounth;
	String dayOfYear;
	private int scheduleType;
	public Integer[] getDaysOfWeek() {
		return daysOfWeek;
	}
	public void setDaysOfWeek(Integer[] daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getFinishHour() {
		return finishHour;
	}
	public void setFinishHour(String finishHour) {
		this.finishHour = finishHour;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public int[] getDaysOfMounth() {
		return daysOfMounth;
	}
	public void setDaysOfMounth(int... daysOfMounth) {
		this.daysOfMounth = daysOfMounth;
	}
	public String getDayOfYear() {
		return dayOfYear;
	}
	public void setDayOfYear(String dayOfYear) {
		this.dayOfYear = dayOfYear;
	}
	public int getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}
	
	
}
