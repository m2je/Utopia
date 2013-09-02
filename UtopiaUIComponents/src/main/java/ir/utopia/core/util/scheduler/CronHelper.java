package ir.utopia.core.util.scheduler;

import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CronHelper {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CronHelper.class.getName());
	}
public static final int CRON_ITEM_SECOND=0;
public static final int CRON_ITEM_MINUTE=1;
public static final int CRON_ITEM_HOUR=2;
public static final int CRON_ITEM_DAY_OF_MONTH=3;
public static final int CRON_ITEM_MONTH=4;
public static final int CRON_ITEM_DAY_OF_WEEK=5;
public static final int CRON_ITEM_YEAR=6;

public static final int SCHEDULE_TYPE_HOURLY=ScheduleInfo.SCHEDULE_TYPE_HOURLY;
public static final int SCHEDULE_TYPE_DAILY=ScheduleInfo.SCHEDULE_TYPE_DAILY;
public static final int SCHEDULE_TYPE_MONTHLY=ScheduleInfo.SCHEDULE_TYPE_MONTHLY;
public static final int SCHEDULE_TYPE_YEARLY=ScheduleInfo.SCHEDULE_TYPE_YEARLY;
//******************************************************************************************************
	public static  String[] parseCron(String cronStr){
		try {
			StringTokenizer tokener=new StringTokenizer(cronStr," ");
			String []result=new String[7];
			int index=0;
			while(tokener.hasMoreElements()){
				result[index++]=tokener.nextToken().trim();
			}
			return result;
		} catch (Exception e) {
				logger.log(Level.WARNING,"CronHelper failed to parse"+cronStr);
		}
		return null;
	}	
//*****************************************************************************************************
	public static int getScheduleType(String cronStr){
		try {
			String []items=parseCron(cronStr);
			if(items[CRON_ITEM_DAY_OF_MONTH].matches("[[0-9]{1,2}]+")){
				return SCHEDULE_TYPE_MONTHLY;
			}else{
				if(items[CRON_ITEM_DAY_OF_WEEK].matches("[[[1-7]{1}][,]{0,1}]{1,7}")){
					if(items[CRON_ITEM_MINUTE].indexOf(",")!=-1||items[CRON_ITEM_MINUTE].matches("[*]/[0-9]+")
						||items[CRON_ITEM_HOUR].indexOf(",")!=-1||items[CRON_ITEM_HOUR].matches("[*]/[0-9]+")){
						return SCHEDULE_TYPE_HOURLY;
					}else{
						return SCHEDULE_TYPE_DAILY;
					}
				}else{
					return SCHEDULE_TYPE_YEARLY;
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"fail to find out the schedule type from : "+cronStr);
		} 
		return -1;
	}
//*****************************************************************************************************
	public static Integer [] getDayofWeek(String cronStr){
		String []items=parseCron(cronStr);
		if("*".equals(items[CRON_ITEM_DAY_OF_WEEK])||"?".equals(items[CRON_ITEM_DAY_OF_WEEK])){
			return null;
		}
		Integer []result=new Integer[items[CRON_ITEM_DAY_OF_WEEK].length()/2+1];
		StringTokenizer tokener=new StringTokenizer(items[CRON_ITEM_DAY_OF_WEEK],",");
		int index=0;
		while(tokener.hasMoreElements()){
			result[index++]=Integer.parseInt(tokener.nextToken());
		}
		return result;
	}
//*****************************************************************************************************
	public static String getHourAndMinute(Date date){
		if(date==null){
			return "";
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int hour=c.get(Calendar.HOUR_OF_DAY);
		int minut=c.get(Calendar.MINUTE);
		if(hour==0&&minut==0){
			return "";	
		}
		return hour+":"+minut;
	}
//*****************************************************************************************************
	public static int getInterval(String cronStr){
		String []items=parseCron(cronStr);
		if(items[CRON_ITEM_MINUTE].indexOf(",")!=-1||items[CRON_ITEM_HOUR].indexOf(",")!=-1){
			int beginIndex=items[CRON_ITEM_HOUR].indexOf(",");
			int hour1=Integer.parseInt(items[CRON_ITEM_HOUR].substring(0,beginIndex));
			int hour2=Integer.parseInt(items[CRON_ITEM_HOUR].substring(beginIndex+1,items[CRON_ITEM_HOUR].indexOf(",",beginIndex)));
			
			
			 beginIndex=items[CRON_ITEM_MINUTE].indexOf(",");
			int minute1=Integer.parseInt(items[CRON_ITEM_MINUTE].substring(0,beginIndex));
			int minute2=Integer.parseInt(items[CRON_ITEM_MINUTE].substring(beginIndex+1,items[CRON_ITEM_MINUTE].indexOf(",",beginIndex)));
			Calendar c1=Calendar.getInstance(),c2=Calendar.getInstance();
			c1.set(Calendar.HOUR_OF_DAY, hour1);
			c1.set(Calendar.MINUTE, minute1);
			c2.set(Calendar.HOUR_OF_DAY, hour2);
			c2.set(Calendar.MINUTE, minute2);
			long diff= Math.abs(c1.getTimeInMillis()-c2.getTimeInMillis());
			return (int)(diff/60000);
		}else if(items[CRON_ITEM_MINUTE].matches("[*]/[0-9]+")){
			return Integer.parseInt(items[CRON_ITEM_MINUTE].substring(items[CRON_ITEM_MINUTE].indexOf("/")+1));
		}else if(items[CRON_ITEM_HOUR].matches("[*]/[0-9]+")){
			return Integer.parseInt(items[CRON_ITEM_HOUR].substring(items[CRON_ITEM_HOUR].indexOf("/")+1))*60;
		}
		return 0;
	}
//*****************************************************************************************************
	public static int[] getDaysOfMonth(String cronStr){
		String []items=parseCron(cronStr);
		int []result=new int[items[CRON_ITEM_DAY_OF_MONTH].length()/2+1];
		StringTokenizer tokener=new StringTokenizer(items[CRON_ITEM_DAY_OF_MONTH],",");
		int index=0;
		while(tokener.hasMoreElements()){
			String cuDay=tokener.nextToken();
			if("*".equals(cuDay)||"?".equals(cuDay)){
				result=null;
				return result;
			}else{
				result[index++]=Integer.parseInt(cuDay);
			}
			
		}
		return result;
	}
//*****************************************************************************************************
	public static void main(String args[]){
//		System.out.println("12".matches("[0-9]{1,2}"));
//		System.out.println(getInterval("* */7 * * 3,6 ?"));
//		System.out.println(getScheduleType("40 14 21,20,23,22 1-12 * ?"));
		
	}
}
