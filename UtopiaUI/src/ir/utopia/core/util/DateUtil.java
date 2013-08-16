package ir.utopia.core.util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.tools.xjc.Language;

public class DateUtil {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DateUtil.class.getName());
	}
	public static final int PATTERNS1  = 1; //yyyy/mm/dd
	public static final int PATTERNS2  = 2; //yyyy-mm-dd
	public static final int PATTERNS3  = 3; //yyyy-dd-mm
	private static final int MAX_CACHE_DATE_SIZE=10;
	private static transient LinkedHashMap<String, java.util.Date>solarToGregorianMap=new LinkedHashMap<String, java.util.Date>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = -5455784056511305818L;

		@Override
		protected boolean removeEldestEntry(Entry<String, java.util.Date> eldest) {
			return size()>MAX_CACHE_DATE_SIZE;
		}
		
	};
	
	private static final String DATE_IDENTIFIER = "#d#";
	private static final String SQL_DATE_IDENTIFIER = "'" + DATE_IDENTIFIER + "'";
	
	//public static int SolarYear = 0, SolarMonth = 0, SolarDay = 0;
	//public static int GregorianYear = 0, GregorianMonth = 0, GregorianDay = 0;
	public static final int dkSolar = 0;
	public static final int dkGregorian = 1;
	public static final int[] LeapMonth = { 12, 2 };
	public static final int[][] DaysOfMonths =
		{ { 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29 }, {
			31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }
	};
	//		  { Far, Ord, Kho, Tir, Mor, Sha, Meh, Aba, Aza, Day, Bah, Esf },
	//		  { Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec });
	public static final int[][] DaysToMonth =
		{ { 0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 365 }, {
			0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 }
	};
	//		  { Far, Ord, Kho, Tir, Mor, Sha, Meh, Aba, Aza, Day, Bah,^Esf, *** },
	//		  { Jan,^Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec, *** });
	
	public static String[] solarMonthName =
				{
					"\u0641\u0631\u0648\u0631\u062f\u064a\u0646",
					"\u0627\u0631\u062f\u064a\u0628\u0647\u0634\u062a",
					"\u062e\u0631\u062f\u0627\u062f",
					"\u062a\u064a\u0631",
					"\u0645\u0631\u062f\u0627\u062f",
					"\u0634\u0647\u0631\u064a\u0648\u0631",
					"\u0645\u0647\u0631",
					"\u0622\u0628\u0627\u0646",
					"\u0622\u0630\u0631",
					"\u062f\u064a",
					"\u0628\u0647\u0645\u0646",
					"\u0627\u0633\u0641\u0646\u062f" };
	public static String[] gregorianMonthName = { "Jan","Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public static boolean IsLeapYear(int DateKind, int Year) {
		if (DateKind == dkSolar)
			return ((((Year + 38) * 31) % 128) <= 30);
		return ((Year % 4) == 0)
			&& (((Year % 100) != 0) || ((Year % 400) == 0));
	}

	public static int DaysOfMonth(int DateKind, int Year, int Month) {
		int Result = 0;
		if ((Year != 0) && ((Month >= 1) && (Month <= 12))) {
			Result = DaysOfMonths[DateKind][Month - 1];
			if ((Month == LeapMonth[DateKind]) && (IsLeapYear(DateKind, Year)))
				Result = Result + 1;
		}
		return Result;
	}
	
	public static int getGregorianMonthNumber(String monthStr){
		java.text.DateFormatSymbols dfs = new java.text.DateFormatSymbols();
		String[] months = dfs.getShortMonths();
		for(int i=0; i<=11; i++)
			if(monthStr.equalsIgnoreCase(months[i]))
				return ++i; 
		return -1;
	}

	public static boolean IsDateValid(
		int DateKind,
		int Year,
		int Month,
		int Day) {
		return (Year != 0)
			&& (Month >= 1)
			&& (Month <= 12)
			&& (Day >= 1)
			&& (Day <= DaysOfMonth(DateKind, Year, Month));
	}

	public static int DaysToDate(int DateKind, int Year, int Month, int Day) {
		int Result = 0;
		if (IsDateValid(DateKind, Year, Month, Day)) {
			Result = DaysToMonth[DateKind][Month - 1] + Day;
			if ((Month > LeapMonth[DateKind]) && IsLeapYear(DateKind, Year))
				Result = Result + 1;
		}
		return Result;
	}

	public static int[] DateOfDay(int DateKind, int Days, int Year) {
		int[] MonthDay = new int[2];
		int LeapDay = 0;
		int m = 0;
		int Month = 0;
		int Day = 0;
		for (m = 2; m <= 13; m++) {
			if ((m > LeapMonth[DateKind]) && IsLeapYear(DateKind, Year))
				LeapDay = 1;
			if (Days <= (DaysToMonth[DateKind][m - 1] + LeapDay)) {
				Month = m - 1;
				if (Month <= LeapMonth[DateKind])
					LeapDay = 0;
				Day = Days - (DaysToMonth[DateKind][Month - 1] + LeapDay);
				MonthDay[0] = Month;
				MonthDay[1] = Day;
				return MonthDay;
			}
		}
		return null;
	}

	public static int[] GregorianToSolar(int Year, int Month, int Day) {
		int LeapDay = 0;
		int Days = 0;
		boolean PrevGregorianLeap = false;

		if (IsDateValid(dkGregorian, Year, Month, Day)) {
			PrevGregorianLeap = IsLeapYear(dkGregorian, Year - 1);
			Days = DaysToDate(dkGregorian, Year, Month, Day);
			Year = Year - 622;
			if (IsLeapYear(dkSolar, Year))
				LeapDay = 1;
			else
				LeapDay = 0;
			if (PrevGregorianLeap && (LeapDay == 1))
				Days = Days + 287;
			else
				Days = Days + 286;
			if (Days > (365 + LeapDay)) {
				Year = Year + 1;
				Days = Days - (365 + LeapDay);
			}

			int[] MonthDay = DateOfDay(dkSolar, Days, Year);
			int[] YearMonthDay = new int[3];
			YearMonthDay[0] = Year;
			YearMonthDay[1] = MonthDay[0];
			YearMonthDay[2] = MonthDay[1];
			return YearMonthDay;

		}

		return null;

	}
	
//	public static void ConvertGregorianToSolar(int Year, int Month, int Day) {
//			int LeapDay = 0;
//			int Days = 0;
//			boolean PrevGregorianLeap = false;
//
//			if (IsDateValid(dkGregorian, Year, Month, Day)) {
//				PrevGregorianLeap = IsLeapYear(dkGregorian, Year - 1);
//				Days = DaysToDate(dkGregorian, Year, Month, Day);
//				Year = Year - 622;
//				if (IsLeapYear(dkSolar, Year))
//					LeapDay = 1;
//				else
//					LeapDay = 0;
//				if (PrevGregorianLeap && (LeapDay == 1))
//					Days = Days + 287;
//				else
//					Days = Days + 286;
//				if (Days > (365 + LeapDay)) {
//					Year = Year + 1;
//					Days = Days - (365 + LeapDay);
//				}
//
//				int[] MonthDay = DateOfDay(dkSolar, Days, Year);
//				
//				SolarYear = Year;
//				SolarMonth = MonthDay[0];
//				SolarDay = MonthDay[1];	
//			}
//		}
	public static boolean useSolarDate(Locale locale){
		return locale!=null&&useSolarDate(locale.getLanguage());
	}
	public static boolean useSolarDate(String language){
		return "fa".equals(language);
	}
	public static int[] convertGregorianToSolar(java.util.Date date) {
		Calendar c= Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		return convertGregorianToSolar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * 
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getDateString(java.util.Date date,Locale locale){
		String result;
		if(DateUtil.useSolarDate(locale) ){
			String sol = DateUtil.gerToSolar((1900 + date.getYear())+"/"+ (date.getMonth()+1)+"/"+date.getDate(), PATTERNS1);
			String[] res= sol.split("/");
			result=res[0]+"/"+(res[1].length()<2?"0"+res[1]:res[1])+"/"+(res[2].length()<2?"0"+res[2]:res[2]) ;
		}else{
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(date.getTime());
			int month=calendar.get(Calendar.MONTH) + 1;
			int day=calendar.get(Calendar.DAY_OF_MONTH);
			result=calendar.get(Calendar.YEAR)+"-"+((month<10)?"0"+month:month)+"-"+((day<10)?"0"+day:day);
		}
		return result;
	}
	
	public static String getDateAndTimeString(java.util.Date date,Locale locale){
		StringBuffer result=new StringBuffer(getDateString(date, locale));
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		result.append(" ");
		result.append(c.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(c.get(Calendar.MINUTE));
		result.append(":");
		result.append(c.get(Calendar.SECOND));
		
		return result.toString();
	}
	
	public static String convertGregorianToSolarString(java.util.Date date) {
		return getDateString(date, new Locale("fa"));
	}
	public static int[] convertGregorianToSolar(Date date) {
		Calendar c= Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		return convertGregorianToSolar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
	}
	//Golnari:this method not work properly if used alone
	private static int[] convertGregorianToSolar(int Year, int Month, int Day) {
		int LeapDay = 0;
		int Days = 0;
		boolean PrevGregorianLeap = false;

		if (IsDateValid(dkGregorian, Year, Month, Day)) {
			PrevGregorianLeap = IsLeapYear(dkGregorian, Year - 1);
			Days = DaysToDate(dkGregorian, Year, Month, Day);
			Year = Year - 622;
			if (IsLeapYear(dkSolar, Year))
				LeapDay = 1;
			else
				LeapDay = 0;
			if (PrevGregorianLeap && (LeapDay == 1))
				Days = Days + 287;
			else
				Days = Days + 286;
			if (Days > (365 + LeapDay)) {
				Year = Year + 1;
				Days = Days - (365 + LeapDay);
			}

			int[] MonthDay = DateOfDay(dkSolar, Days, Year);
			int[] result=new int[3];
			
			result[0] = Year;
			result[1] = MonthDay[0];
			result[2] = MonthDay[1];	
			
			return  result;
		}
		return null;
	}
	

	public static int[] SolarToGregorian(int Year, int Month, int Day) {
		int LeapDay = 0;
		int Days = 0;
		boolean PrevSolarLeap = false;
		if (IsDateValid(dkSolar, Year, Month, Day)) {
			PrevSolarLeap = IsLeapYear(dkSolar, Year - 1);
			Days = DaysToDate(dkSolar, Year, Month, Day);
			Year = Year + 621;
			if (IsLeapYear(dkGregorian, Year))
				LeapDay = 1;
			else
				LeapDay = 0;
			if (PrevSolarLeap && (LeapDay == 1))
				Days = Days + 80;
			else
				Days = Days + 79;
			if (Days > (365 + LeapDay)) {
				Year = Year + 1;
				Days = Days - (365 + LeapDay);
			}
			int[] MonthDay = DateOfDay(dkGregorian, Days, Year);
			int[] YearMonthDay = new int[3];
			YearMonthDay[0] = Year;
			YearMonthDay[1] = MonthDay[0];
			YearMonthDay[2] = MonthDay[1];
			return YearMonthDay;

		}

		return null;

	}
	
//	public static void ConvertSolarToGregorian(int Year, int Month, int Day) {
//			int LeapDay = 0;
//			int Days = 0;
//			boolean PrevSolarLeap = false;
//			if (IsDateValid(dkSolar, Year, Month, Day)) {
//				PrevSolarLeap = IsLeapYear(dkSolar, Year - 1);
//				Days = DaysToDate(dkSolar, Year, Month, Day);
//				Year = Year + 621;
//				if (IsLeapYear(dkGregorian, Year))
//					LeapDay = 1;
//				else
//					LeapDay = 0;
//				if (PrevSolarLeap && (LeapDay == 1))
//					Days = Days + 80;
//				else
//					Days = Days + 79;
//				if (Days > (365 + LeapDay)) {
//					Year = Year + 1;
//					Days = Days - (365 + LeapDay);
//				}
//				int[] MonthDay = DateOfDay(dkGregorian, Days, Year);
//				
//				GregorianYear = Year;
//				GregorianMonth = MonthDay[0];
//				GregorianDay = MonthDay[1];	
//			}
//		}
	public static int[] convertSolarToGregorian(int Year, int Month, int Day) {
		int LeapDay = 0;
		int Days = 0;
		boolean PrevSolarLeap = false;
		if (IsDateValid(dkSolar, Year, Month, Day)) {
			PrevSolarLeap = IsLeapYear(dkSolar, Year - 1);
			Days = DaysToDate(dkSolar, Year, Month, Day);
			Year = Year + 621;
			if (IsLeapYear(dkGregorian, Year))
				LeapDay = 1;
			else
				LeapDay = 0;
			if (PrevSolarLeap && (LeapDay == 1))
				Days = Days + 80;
			else
				Days = Days + 79;
			if (Days > (365 + LeapDay)) {
				Year = Year + 1;
				Days = Days - (365 + LeapDay);
			}
			int[] MonthDay = DateOfDay(dkGregorian, Days, Year);
			int[] result = new int[3];
			result[0] = Year;
			result[1] = MonthDay[0];
			result[2] = MonthDay[1];
			return result;			
		}
		return null;
	}

	public static int getMaximumSolarMonthDays(int year, int month) {
		if (month <= 6)
			return 31;
		if ((month >= 7) && (month <= 11))
			return 30;
		if (month == 12) {
			if (IsLeapYear(dkSolar, year))
				return 30;
			else
				return 29;
		}
		return 30;
	}

	public static boolean isValidSolar(int year, int month, int day) {
		if ((year >= 1000) && (year <= 1600))
			return IsDateValid(0, year, month, day);
		return false;
	}

	public static boolean isValidGrigor(int year, int month, int day) {
		if ((year >= 1000) && (year <= 2500))
			return IsDateValid(1, year, month, day);
		return false;
	}
	
	public static int[] getGrigorFromSolarDateString(String dateStr){
		//System.out.println("dateStr: " + dateStr);
		StringTokenizer st = new StringTokenizer(dateStr, " -/");
		int year = 0, month = 0, day = 0;
		if(st.hasMoreTokens())
			year = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			month = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			day = Integer.parseInt(st.nextToken());
		
		int[] result = new int[3];
		if(isValidSolar(year, month, day)){
			result = SolarToGregorian(year, month, day);
			return result;
		}
		if(isValidSolar(day, month, year)){
			result = SolarToGregorian(day, month, year);
			return result;
		}
		return null;
	}
	
	public static String ConverToGregorianFromSolarDateString(String dateStr){
			//System.out.println("Solar dateStr: " + dateStr);
			StringTokenizer st = new StringTokenizer(dateStr, " -/");
			int year = 0, month = 0, day = 0;
			if(st.hasMoreTokens())
				year = Integer.parseInt(st.nextToken());
			if(st.hasMoreTokens())
				month = Integer.parseInt(st.nextToken());
			if(st.hasMoreTokens())
				day = Integer.parseInt(st.nextToken());
		
			if(isValidSolar(year, month, day)){
				int temp[] = convertSolarToGregorian(year, month, day);
				return temp[0] + "-" + temp[1] + "-" + temp[2]; 				
			}
			if(isValidSolar(day, month, year)){
				int temp[] = convertSolarToGregorian(day, month, year);
				return temp[0] + "-" + temp[1] + "-" + temp[2];				
			}
			return null;			
		}
	public static int[] getSolarFromGrigorDateString(String dateStr){
			//System.out.println("dateStr: " + dateStr);
			StringTokenizer st = new StringTokenizer(dateStr, " -/");
			int year = 0, month = 0, day = 0;
			if(st.hasMoreTokens())
				year = Integer.parseInt(st.nextToken());
			if(st.hasMoreTokens())
				month = Integer.parseInt(st.nextToken());
			if(st.hasMoreTokens())
				day = Integer.parseInt(st.nextToken());
		
			int[] result = new int[3];
			if(isValidGrigor(year, month, day)){
				result = GregorianToSolar(year, month, day);
				return result;
			}
			if(isValidGrigor(day, month, year)){
				result = GregorianToSolar(day, month, year);
				return result;
			}
			return null;
		}
	
	public static String solarToGer(String dateStr){
		int []teep=solarToGerInt(dateStr);
		int year = teep[0], month = teep[1], day = teep[2];
		if(isValidSolar(year, month, day)){
			int temp[] = convertSolarToGregorian(year, month, day);
			return temp[1] + "-" + temp[2] + "-" + temp[0]; 				
		}
		if(isValidSolar(day, month, year)){
			int temp[] = convertSolarToGregorian(day, month, year);
			return temp[1] + "-" + temp[2] + "-" + temp[0];			
		}
		return null;			
	}
	
	public static int[] solarToGerInt(String dateStr){
		//System.out.println("Solar dateStr: " + dateStr);
		StringTokenizer st = new StringTokenizer(dateStr, " -/");
		int year = 0, month = 0, day = 0;
		if(st.hasMoreTokens())
			year = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			month = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			day = Integer.parseInt(st.nextToken());
	
		if(isValidSolar(year, month, day)){
			return convertSolarToGregorian(year, month, day);
		}
		if(isValidSolar(day, month, year)){
			return convertSolarToGregorian(day, month, year);
		}
		return null;			
	}

	public static java.util.Date gerToDate(String dateStr, String format){
		if(dateStr==null)return null;
		String key=new StringBuffer(format).append("|").append(dateStr).toString();
		if(!solarToGregorianMap.containsKey(key)){
			try {
				DateFormat formatter = new SimpleDateFormat(format);
				solarToGregorianMap.put(key, (java.util.Date)formatter.parse(dateStr));
			} catch (ParseException e) {
				logger.log(Level.WARNING,"fail to parse gerogorain date format "+dateStr);
				solarToGregorianMap.put(key, null);
			}	
			
		}
		return solarToGregorianMap.get(key);
		
	}

	public static java.util.Date gerToDate(String dateStr){
		 try {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			return (java.util.Date)formatter.parse(dateStr);
			
		} catch (ParseException e) {
			logger.log(Level.WARNING,"fail to parse gerogorain date format "+dateStr);
		}
		return null;
	}
	public static java.util.Date solarToDate(String dateStr){
		return gerToDate(ConverToGregorianFromSolarDateString(dateStr),"yyyy-MM-dd");
	}
	public static String solarToGerDateTime(String dateStr){
		//System.out.println("Solar dateStr: " + dateStr);
		StringTokenizer st = new StringTokenizer(dateStr, " -/:");
		int year = 0, month = 0, day = 0 ;
		int hour = 0, minute = 0 , second = 0;
		String PMorAM ="AM";
		if(st.hasMoreTokens())
			year =   Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			month =  Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			day =    Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			hour =   Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			minute = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			second = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			PMorAM = st.nextToken(); 
		if(PMorAM.equalsIgnoreCase("\u0635\u0628\u062d")) 
			PMorAM = 	"AM";
		else
		   PMorAM ="PM"  ;
		
		String hhour,mminute,ssecond;
		hhour   = (hour   <=9) ? "0"+hour   : hour+"";
		mminute = (minute <=9) ? "0"+minute : minute+"";
		ssecond = (second <=9) ? "0"+second : second+"";
		
//		if(PMorAM.equalsIgnoreCase("PM"))
//			hour += 12;
		//SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss a");
		if(isValidSolar(year, month, day)){
			int temp[] = convertSolarToGregorian(year, month, day);
			String date = gregorianMonthName[temp[1]-1]+" "+temp[2]+", "+temp[0]+" "+hhour+":"+mminute+":"+ssecond+" "+PMorAM+" "+"IRST";
//			Timestamp ts = new Timestamp(temp[1],temp[2],temp[3],hour,minute,second,0);
//			String date = format.format(ts);
			return date;
			//return temp[1] + "-" + temp[2] + "-" + temp[0];
			 
		}
		if(isValidSolar(day, month, year)){
			int temp[] = convertSolarToGregorian(day, month, year);
			return temp[1] + "-" + temp[2] + "-" + temp[0];			
		}
		return null;			
	}
	//// farshid Updated
	//// added to have month First
	public static String gerToSolar(String dateStr, SimpleDateFormat sdf){
		try {
			java.util.Date date = (java.util.Date)sdf.parse(dateStr);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			int year = gc.get(GregorianCalendar.YEAR);
			int month = gc.get(GregorianCalendar.MONTH) + 1;
			int day = gc.get(GregorianCalendar.DATE);
			if(isValidGrigor(year, month, day)){
				int[] temp = convertGregorianToSolar(year, month, day);
				String m,d;
				m = (temp[1]<=9) ? "0" + String.valueOf(temp[1]) : ""+temp[1];	
				d =	(temp[2]<=9) ? "0" + String.valueOf(temp[2]) : ""+temp[2];
				//farshid updated - correcting sequence in date fields
				return d + "-" + m + "-" + temp[0];				
			}
			
		} catch (ParseException e) {			
			e.printStackTrace();
		}		
		return null;		
	}
	public static String dateTimeGerToSolar(String dateTimeStr, SimpleDateFormat sdf){
		try {
			java.util.Date date = (java.util.Date)sdf.parse(dateTimeStr);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			int year = gc.get(GregorianCalendar.YEAR);
			int month = gc.get(GregorianCalendar.MONTH);
			int day = gc.get(GregorianCalendar.DATE);
			int hour = gc.get(GregorianCalendar.HOUR);
			int minute = gc.get(GregorianCalendar.MINUTE);
			
			if(isValidGrigor(year, month, year)){
				int[] temp = convertGregorianToSolar(year, month, day);
				String m,d;
				if((temp[1]<=9))
					m = "0" + String.valueOf(temp[1]);	
				else
					m = String.valueOf(temp[1]);
				if((temp[2]<=9))
					d = "0" + String.valueOf(temp[2]);	
				else
					d = String.valueOf(temp[2]);
				return temp[0] + "-" + m + "-" + d + " " + hour + ":" + minute;				
			}
			
		} catch (ParseException e) {			
			e.printStackTrace();
		}		
		return null;		
	}
	
	/**
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String gerToSolar(String dateStr, int pattern){
		String year = "";
		String month = "";
		String day = "";
		if(pattern == PATTERNS1){
			year = dateStr.substring(0, 4);
			month = dateStr.substring(dateStr.indexOf('/'), dateStr.lastIndexOf('/'));
			day = dateStr.substring(dateStr.lastIndexOf('/'));
		}
		else if(pattern == PATTERNS2){
			year = dateStr.substring(0, 4);
			month = dateStr.substring(dateStr.indexOf('-'), dateStr.lastIndexOf('-'));
			day = dateStr.substring(dateStr.lastIndexOf('-'));
		}
		else if(pattern == PATTERNS3){
			year = dateStr.substring(0, 4);
			day = dateStr.substring(dateStr.indexOf('-'), dateStr.lastIndexOf('-'));
			month = dateStr.substring(dateStr.lastIndexOf('-'));
		}
		
		String str = year + "/" + month + "/" + day; 

		return ConvertToSolarFromGregorianDateString(str);		
	}
	public static int[] gerToSolar(Timestamp ts){
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(ts.getTime());
		return convertGregorianToSolar(gc.get(GregorianCalendar.YEAR),
				gc.get(GregorianCalendar.MONTH)+1,gc.get(GregorianCalendar.DATE));		
	}
	/**
	 * 
	 * @param year
	 * @param month : 1,2,3
	 * @param day
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Timestamp solToGer(int year, int month, int day, int minute, int second){
		int[] gd = convertSolarToGregorian(year, month, day);
		GregorianCalendar gc = new GregorianCalendar(gd[0],gd[1]-1,gd[2],minute,second);		
		return new Timestamp(gc.getTimeInMillis());		
	}
	/**
	 * 
	 * @param year
	 * @param month : : 1,2,3
	 * @param day
	 * @return
	 */
	public static Timestamp solToGer(int year, int month, int day){
				
		return solToGer(year, month, day, 0, 0);		
	}
	public static String gerToSolTimestamp(Timestamp ts){
		int [] fd = gerToSolar(ts);		
		return fd[0]+"/"+fd[1]+"/"+fd[2];
	}
/**	public static String greToSolmmDDyyyy(String dateStr){
		//System.out.println("Gregorian dateStr: " + dateStr);
		StringTokenizer st = new StringTokenizer(dateStr, " -/");
		int year = 0, month = 0, day = 0;
		if(st.hasMoreTokens())
			month = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			day = Integer.parseInt(st.nextToken());
		if(st.hasMoreTokens())
			year = Integer.parseInt(st.nextToken());

		if(isValidGrigor(year, month, day)){
			int[] temp = convertGregorianToSolar(year, month, day);
			String m,d;
			if((temp[1]<=9))
				m = "0" + String.valueOf(temp[1]);	
			else
				m = String.valueOf(temp[1]);
			if((temp[2]<=9))
				d = "0" + String.valueOf(temp[2]);	
			else
				d = String.valueOf(temp[2]);
			return temp[0] + "-" + m + "-" + d;				
		}
		
		return null;				
	}
	*/
	/**
	 * @deprecated use gerToSol instead.
	 * @param dateStr: Format yyyy/mm/dd
	 * @return
	 */
	private static String ConvertToSolarFromGregorianDateString(String dateStr){
				//System.out.println("Gregorian dateStr: " + dateStr);
				StringTokenizer st = new StringTokenizer(dateStr, " -/");
				int year = 0, month = 0, day = 0;
				if(st.hasMoreTokens())
					year = Integer.parseInt(st.nextToken());
				if(st.hasMoreTokens())
					month = Integer.parseInt(st.nextToken());
				if(st.hasMoreTokens())
					day = Integer.parseInt(st.nextToken());
		
				if(isValidGrigor(year, month, day)){
					int[] temp = convertGregorianToSolar(year, month, day);
					String m,d;
					if((temp[1]<=9))
						m = "0" + String.valueOf(temp[1]);	
					else
						m = String.valueOf(temp[1]);
					if((temp[2]<=9))
						d = "0" + String.valueOf(temp[2]);	
					else
						d = String.valueOf(temp[2]);
					return temp[0] + "/" + m + "/" + d;				
				}
				if(isValidGrigor(day, month, year)){
					int[] temp = convertGregorianToSolar(day, month, year);
					String m,d;
					if((temp[1]<=9))
						m = "0" + String.valueOf(temp[1]);	
					else
						m = String.valueOf(temp[1]);
					if((temp[2]<=9))
						d = "0" + String.valueOf(temp[2]);	
					else
						d = String.valueOf(temp[2]);
					return temp[0] + "/" + m + "/" + d;					
				}
				return null;				
			}
	
	/**
	 * @deprecated use dateTimeGerToSolar
	 * @param dateStr
	 * @return
	 */
	public static String ConvertToSolarFromGregorianDateTimeString(String dateStr){
					//System.out.println("Gregorian dateTimeStr: " + dateStr);
					if(dateStr == null || dateStr == "")
						return null;
					//// farshid Updated
					//// Adding a '/' Character to method
					//// StringTokenizer st = new StringTokenizer(dateStr, " -,");
					StringTokenizer st = new StringTokenizer(dateStr, " /-,");
					//// end of adding
					//StringTokenizer st = new StringTokenizer(dateStr, " /-,");
					int year = 0, month = 0, day = 0;
					String monthStr = null, time = null, PMorAM = null;
					if(st.hasMoreTokens())
						monthStr = st.nextToken();
					if(st.hasMoreTokens())
						day = Integer.parseInt(st.nextToken());
					if(st.hasMoreTokens())
						year = Integer.parseInt(st.nextToken());
					if(st.hasMoreTokens())
						time = st.nextToken();
					if(st.hasMoreTokens())
						PMorAM = st.nextToken();
					month = getGregorianMonthNumber(monthStr);
		
					if(isValidGrigor(year, month, day)){
						int temp[] = convertGregorianToSolar(year, month, day);
						String m,d;
						if((temp[1]<=9))
							m = "0" + String.valueOf(temp[1]);	
						else
							m = String.valueOf(temp[1]);
						if((temp[2]<=9))
							d = "0" + String.valueOf(temp[2]);	
						else
							d = String.valueOf(temp[2]);
						return temp[0] + "-" + m + "-" + d +" " + 
								ConvertToSolarFromGregorianTimeString(time + " " + PMorAM);				
					}
					if(isValidGrigor(day, month, year)){
						int[] temp = convertGregorianToSolar(day, month, year);
						String m,d;
						if((temp[1]<=9))
							m = "0" + String.valueOf(temp[1]);	
						else
							m = String.valueOf(temp[1]);
						if((temp[2]<=9))
							d = "0" + String.valueOf(temp[2]);	
						else
							d = String.valueOf(temp[2]);
						return temp[0] + "-" + m + "-" + d +
							ConvertToSolarFromGregorianTimeString(time + " " + PMorAM);					
					}
					return null;				
				}
	public static String ConvertToSolarFromGregorianTimeString(String timeStr){
						//System.out.println("Gregorian TimeStr: " + timeStr);
						if(timeStr == null || timeStr == "")
							return null;
						StringTokenizer st = new StringTokenizer(timeStr, " :");
						int hour = 0, minute = 0, second = 0;
						String PMorAM = null, SolarPMorAM = null;
						if(st.hasMoreTokens())
							hour = Integer.parseInt(st.nextToken());
						if(st.hasMoreTokens())
							minute = Integer.parseInt(st.nextToken());
						if(st.hasMoreTokens())
							second = Integer.parseInt(st.nextToken());
						if(st.hasMoreTokens())
							PMorAM = st.nextToken();
						SolarPMorAM = getGregorianPMorAM(PMorAM);
						
						return  (hour <=9 ? "0"+hour : hour) + ":" + (minute <= 9 ? "0"+minute : minute)+ ":" + (second <=9 ? "0"+second : second) + " " + SolarPMorAM;	
															
					}
	public static String getGregorianPMorAM(String PMorAM){
		return PMorAM.equalsIgnoreCase("AM")? "\u0635\u0628\u062d" : "\u0639\u0635\u0631";
	}
	public static String getUnicodeFromMixNumberAndString(String mixstr){
				if(mixstr == null || "".equals(mixstr))
					return mixstr; 
				char[] ca = mixstr.toCharArray();
				int n = 0;
				char ch = 0x06f0 ;
				String result = "";
				for(int k = 0; k<ca.length; k++){
					if(Character.isDigit(ca[k])){
					n = Integer.parseInt(""+ca[k]);
					ch+=n;
					}
					else{
						ch=ca[k];
					}
					result+=ch;
					ch = 0x06f0;
				}
			
				return result;
			
			}
	
	public static Timestamp stringToTimestamp(String str) throws ParseException{	
		return new Timestamp(Date.valueOf(str.substring(0,str.indexOf(" "))).getTime());		
}
	public static String ConvertMultiDateToSolar(String gregorianFormat){
		String result="";
		if(gregorianFormat==null)return "";
		StringTokenizer token=new StringTokenizer(gregorianFormat," ");
		String str;
		while (token.hasMoreTokens()){
			str=token.nextToken() ;
			String solarStr = ConvertToSolarFromGregorianDateString(str);
		if(solarStr!=null){
			str = solarStr;				
			str = getUnicodeFromMixNumberAndString(str);
			result=result+" "+str;
			
		}
		}
//	      System.err.println("the result from convert is:"+result);
     return result;
		
	}

	
	public static String getDisplay(Timestamp timestamp,int displayType,Language language){		 
//		if(timestamp==null )return "";		
//		switch (displayType) {
//		case DisplayType.Date:				
//			return getDateDisplay(timestamp,language);
//		case DisplayType.Time:
//			return getTimeDisplay(timestamp,language);				
//		case DisplayType.DateTime:
//			 return getDataTimeDisplay(timestamp,language);
//		default:
//			return "";
//		}
		return timestamp.toString();
	}	
	
		
	
	
	public static String getDateDisplay(Timestamp timestamp){
		return  getDateDisplay(timestamp,null);
	}
	public static String getDateDisplay(Timestamp timestamp,Language language){
		DateFormat format =SimpleDateFormat.getDateInstance();//DisplayType.getDateFormat(DisplayType.Date);		
		 String timeStr=format.format(timestamp);
		String str = timeStr;
		if (false){
			if(str==null|| str.trim().length()<=0){
				return str;				
			}		
			String solarStr = DateUtil.ConvertToSolarFromGregorianDateString(str);
			if(solarStr!=null){						
				str = solarStr;	
				str = "\u202b" + getUnicodeFromMixNumberAndString(solarStr);
			}
			else{
				str="";
			}	
		}
		return str;
	}
	
	
	
	
	public static void main(String[] args) {
		int[] result = gerToSolar(new Timestamp(System.currentTimeMillis()));
		System.out.println(result[0]+"/"+result[1]+"/"+result[2]);
		System.out.println("solarToDate> " + solarToDate("1387/09/13"));
		System.out.println("gerToDate>" + gerToDate("17/1/2008","dd/MM/yyyy"));
		System.out.println("getDateString>" +getDateString(new Date(System.currentTimeMillis()), new Locale("en")));
		System.out.println("gerToSolar>"+gerToSolar("2008-03-12", PATTERNS3));
		//System.out.println(getDateString(new Date(System.currentTimeMillis()), new Locale("fa")));
	}
	public static String PersianAlphaDate(Timestamp time) {
		int[] result = gerToSolar(time);
		return PersianAlphaDate(result[0]+"/"+result[1]+"/"+result[2]);
	}

	public static String PersianAlphaDate(String PersianDate) {
		int fd = 0, fm = 0, fy = 0, ty1, ty2;
		String fmonth, fday, fyear;
		StringTokenizer st = new StringTokenizer(PersianDate, " -/");
		if (st.hasMoreTokens())
			fy = Integer.parseInt(st.nextToken());
		if (st.hasMoreTokens())
			fm = Integer.parseInt(st.nextToken());
		if (st.hasMoreTokens())
			fd = Integer.parseInt(st.nextToken());
		fday = fyear = fmonth = "";
		switch (fm) {
		case 1:
			fmonth = "�������";
			break;
		case 2:
			fmonth = "��������";
			break;
		case 3:
			fmonth = "�����";
			break;
		case 4:
			fmonth = "���";
			break;
		case 5:
			fmonth = "�����";
			break;
		case 6:
			fmonth = "������";
			break;
		case 7:
			fmonth = "���";
			break;
		case 8:
			fmonth = "����";
			break;
		case 9:
			fmonth = "���";
			break;
		case 10:
			fmonth = "��";
			break;
		case 11:
			fmonth = "����";
			break;
		case 12:
			fmonth = "�����";
		}

		//////////////////////////	//
		switch (fd) {
		case 1:
			fday = "���";
			break;
		case 2:
			fday = "���";
			break;
		case 3:
			fday = "���";
			break;
		case 4:
			fday = "�����";
			break;
		case 5:
			fday = "����";
			break;
		case 6:
			fday = "���";
			break;
		case 7:
			fday = "����";
			break;
		case 8:
			fday = "����";
			break;
		case 9:
			fday = "���";
			break;
		case 10:
			fday = "���";
			break;
		case 11:
			fday = "������";
			break;
		case 12:
			fday = "�������";
			break;
		case 13:
			fday = "������";
			break;
		case 14:
			fday = "?������";
			break;
		case 15:
			fday = "?������";
			break;
		case 16:
			fday = "�������";
			break;
		case 17:
			fday = "�����";
			break;
		case 18:
			fday = "�����";
			break;
		case 19:
			fday = "������";
			break;
		case 20:
			fday = "�����";
			break;
		case 21:
			fday = "���� � ��";
			break;
		case 22:
			fday = "���� � ���";
			break;
		case 23:
			fday = "���� � ���";
			break;
		case 24:
			fday = "���� � �����";
			break;
		case 25:
			fday = "���� � ����";
			break;
		case 26:
			fday = "���� � ���";
			break;
		case 27:
			fday = "���� � ����";
			break;
		case 28:
			fday = "���� � ����";
			break;
		case 29:
			fday = "���� � ���";
			break;
		case 30:
			fday = "�� ��";
			break;
		case 31:
			fday = "�� � ��";
		}

		////////////////////	/
		fy = (fy % 1000);
		ty1 = (fy % 10);
		ty2 = (fy / 10) % 10;
		fy = (fy / 100);

		fyear = "� ���� � ";
		switch (fy) {
		case 1:
			fyear = fyear + "�� � ";
			break;
		case 2:
			fyear = fyear + "����� � ";
			break;
		case 3:
			fyear = fyear + "���� � ";
			break;
		case 4:
			fyear = fyear + "������ � ";
			break;
		case 5:
			fyear = fyear + "����� � ";
			break;
		case 6:
			fyear = fyear + "���� � ";
			break;
		case 7:
			fyear = fyear + "����� � ";
			break;
		case 8:
			fyear = fyear + "����� � ";
			break;
		case 9:
			fyear = fyear + "���� � ";
			break;
		}
		switch (ty2) {
		case 1:
			switch (ty1) {
			case 1:
				fyear = fyear + "�����";
				break;
			case 2:
				fyear = fyear + "������";
				break;
			case 3:
				fyear = fyear + "�����";
				break;
			case 4:
				fyear = fyear + "������";
				break;
			case 5:
				fyear = fyear + "������";
				break;
			case 6:
				fyear = fyear + "������";
				break;
			case 7:
				fyear = fyear + "����";
				break;
			case 8:
				fyear = fyear + "����";
				break;
			case 9:
				fyear = fyear + "�����";
				break;
			}
			;
			break;
		case 2:
			fyear = fyear + "���� � ";
			break;
		case 3:
			fyear = fyear + "�� � ";
			break;
		case 4:
			fyear = fyear + "��� � ";
			break;
		case 5:
			fyear = fyear + "����� � ";
			break;
		case 6:
			fyear = fyear + "��� � ";
			break;
		case 7:
			fyear = fyear + "����� � ";
			break;
		case 8:
			fyear = fyear + "����� � ";
			break;
		case 9:
			fyear = fyear + "��� � ";
			break;
		}

		if (ty2 != 1)
			switch (ty1) {
			case 1:
				fyear = fyear + "�";
				break;
			case 2:
				fyear = fyear + "��";
				break;
			case 3:
				fyear = fyear + "��";
				break;
			case 4:
				fyear = fyear + "����";
				break;
			case 5:
				fyear = fyear + "���";
				break;
			case 6:
				fyear = fyear + "��";
				break;
			case 7:
				fyear = fyear + "���";
				break;
			case 8:
				fyear = fyear + "���";
				break;
			case 9:
				fyear = fyear + "��";
				break;
			}

		return fday + " " + fmonth + " " + fyear;
	}
	
	public static Timestamp getTimeStamp(ResultSet rs, int colunmIndex) throws SQLException{
		Object obj = rs.getObject(colunmIndex);
		if (obj==null)
			return null;
		return parseDateString(obj.toString());
	}
	
	public static Timestamp getTimeStamp(ResultSet rs, String columnName) throws SQLException{
		Object obj = rs.getObject(columnName);
		if (obj==null)
			return null;
		return parseDateString(obj.toString());
	}
	
	public static Timestamp parseDateString(String dateString){
		String dateStr = dateString.trim();
		int pos = dateStr.indexOf(DATE_IDENTIFIER);
		if(pos==0) 
			dateStr = dateStr.substring(DATE_IDENTIFIER.length());
		pos = dateStr.indexOf(DATE_IDENTIFIER);
		if(pos>=0) 
			dateStr = dateStr.substring(0,pos);
		
		Timestamp result = null;
		try{
	        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
	        java.util.Date date = (java.util.Date)formatter.parse(dateStr);
			result = new Timestamp(date.getTime()); 
		} catch (Exception e) {
		}
		
		return result;
	}
	
	public static String evaluateDateTimeValues(String displayString){
		return evaluateDateValues(displayString);
	}

	private static String evaluateDateValues(String displayString){
		return evaluateDateValues(displayString, DATE_IDENTIFIER, "dd-MMM-yy");
	}

	private static String evaluateDateValues(String displayString, String identifier, String format)
	{
		if(displayString == null ||  displayString.trim().length()==0)
			return displayString;
		
		int identifier_length = identifier.length();
		
		if (false && displayString != null) {
			String convertedName = "";

			String TempStr = displayString;
			int DateDelimiterPos = TempStr.indexOf(identifier);
			if (DateDelimiterPos >= 0) {
				while (DateDelimiterPos >= 0) {
					String DateStr = "";
					convertedName = convertedName + TempStr.substring(0, DateDelimiterPos);
					TempStr = TempStr.substring(DateDelimiterPos + identifier_length);
					DateDelimiterPos = TempStr.indexOf(identifier);
					if (DateDelimiterPos < 0)
						break;
					DateStr = TempStr.substring(0, DateDelimiterPos);
					DateFormat formatter = new SimpleDateFormat(format);
					try {
						java.util.Date date = (java.util.Date) formatter.parse(DateStr);
						Timestamp tms = new Timestamp(date.getTime());
						DateStr = DateUtil.gerToSolTimestamp(tms);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					convertedName = convertedName + DateStr;
					TempStr = TempStr.substring(DateDelimiterPos + identifier_length);
					DateDelimiterPos = TempStr.indexOf(identifier);
					if (DateDelimiterPos >= 0)
						convertedName = convertedName + TempStr.substring(0, DateDelimiterPos);
					else
						convertedName = convertedName + TempStr;
				}
				displayString = convertedName;

			} else if (displayString != null)
				displayString = displayString.replaceAll(identifier, "");

		} else {
			if (displayString != null)
				displayString = displayString.replaceAll(identifier, "");
		}
		return displayString;
	}
	
	public static String validateColumnName(String tableName, String ColumnName, int displayType){
		return validateColumnName(tableName + "." + ColumnName , displayType);
	}
	
	public static String validateColumnName(String ColumnName, int displayType){
		return ColumnName;
	} 

	@SuppressWarnings("unused")
	private static String validateDateColumnName(String tableName, String ColumnName){
		return validateDateColumnName(tableName + "." + ColumnName);
	}
	
	private static String validateDateColumnName(String ColumnName){
		return SQL_DATE_IDENTIFIER + "||" + ColumnName + "||" + SQL_DATE_IDENTIFIER;  
	} 
	
	@SuppressWarnings("unused")
	private static String validateTimeColumnName(String tableName, String ColumnName){
		return validateTimeColumnName(tableName + "." + ColumnName);
	}
	
	private static String validateTimeColumnName(String ColumnName){
		return " TO_CHAR(" + ColumnName + " , 'HH24:MI:SS') ";  
	} 
	
	public static java.util.Date getTime(String time){
		if(time==null||!time.matches("[0-9]{1,2}:[0-9]{1,2}[\\s][A|P]M")){
			return null;
		}else{
			Calendar c= Calendar.getInstance();
			int sepIndex=time.indexOf(":");
			c.set(Calendar.HOUR,Integer.parseInt(time.substring(0, sepIndex)) );
			c.set(Calendar.MINUTE,Integer.parseInt(time.substring(sepIndex+1, sepIndex+3)) );
			String amPm=time.substring(sepIndex+4, time.length());
			c.set(Calendar.AM_PM,"AM".equals(amPm)?Calendar.AM:Calendar.PM);
			c.set(Calendar.SECOND, 0);
			return c.getTime();
		}
		
	}
}
