package ir.utopia.core.util.tags.datainputform.client;


import java.util.Date;

public class ValidationFunctions {
private static final int MINIMUM_SOLAR_YEAR=1300;
private static final int MINIMUM_GREGORIAN_YEAR=1921;
private static final int MAXIMUM_DATE_RANGE=20;

public static boolean validateGregorianDate(String fieldName,Date date,DataInputFormConstants constants){
		
		int currentYear=new Date(System.currentTimeMillis()).getYear()+1900;
		int year=date.getYear()+1900;
		if(year<MINIMUM_GREGORIAN_YEAR){
			String inValidDateMessage=constants.invalidMinDate();
			inValidDateMessage=inValidDateMessage.replaceAll("@dateItem@", fieldName).
					replaceAll("@minDate@",String.valueOf(MINIMUM_GREGORIAN_YEAR));
			MessageUtility.error(constants.error(), inValidDateMessage);
			return false;
		}else if(year>MAXIMUM_DATE_RANGE+currentYear){
			String inValidDateMessage=constants.invalidMaxDate();
			inValidDateMessage=inValidDateMessage.replaceAll("@dateItem@", fieldName).
					replaceAll("@maxDate@",String.valueOf(currentYear+20));
			MessageUtility.error(constants.error(), inValidDateMessage);
			return false;
		}
		return true;
	}
//**********************************************************************************************	
	public static boolean validateSolarDate(String fieldName,String input,DataInputFormConstants constants){
		String inValidDateMessage=constants.inValidDate();
		inValidDateMessage=inValidDateMessage.replaceAll("@dateItem@", fieldName);
		try {
			input=input==null?null:input.trim();
			if(input==null||input.length()<"****/*/*".length()||
					input.length()>"*****/**/**".length()){
				MessageUtility.error(constants.error(),inValidDateMessage ) ;
				return false;
			}
			int year,month,day;
			int []d=getDateFromSolar(input);
			if(d==null){
				MessageUtility.error(constants.error(),inValidDateMessage ) ;
				return false;
			}
			year=d[0];
			month=d[1];
			day=d[2];
			if(year<MINIMUM_SOLAR_YEAR){
				inValidDateMessage=constants.invalidMinDate();
				inValidDateMessage=inValidDateMessage.replaceAll("@dateItem@", fieldName).
						replaceAll("@minDate@",String.valueOf(MINIMUM_SOLAR_YEAR));
				MessageUtility.error(constants.error(), inValidDateMessage);
				return false;
			
			}
			
			int currentYear=new Date(System.currentTimeMillis()).getYear()+1900 ;
			if(year>currentYear+20){
				inValidDateMessage=constants.invalidMaxDate();
				inValidDateMessage=inValidDateMessage.replaceAll("@dateItem@", fieldName).
						replaceAll("@maxDate@",String.valueOf(currentYear+20));
				MessageUtility.error(constants.error(), inValidDateMessage);
				return false;
			}
			if(month<1||month>12){
				inValidDateMessage=constants.invalidMonth();
				MessageUtility.error(constants.error(), inValidDateMessage);
				return false;
			}
			
			if(day<1){
				inValidDateMessage=constants.invalidMonth();
				MessageUtility.error(constants.error(), inValidDateMessage);
				return false;
			}
			int maxDay;
			if(month<=6){
				maxDay=31;
			}else if(month<=11){
				maxDay=30;
			}else{
				maxDay=isSolarLeapYear(year)?30:29;
			}
			if(day>maxDay){
				inValidDateMessage=constants.invalidDay();
				MessageUtility.error(constants.error(), inValidDateMessage);
				return false;	
			}
			return true;	
			}
		 catch (Exception e) {
			e.printStackTrace();
			MessageUtility.error(constants.error(),inValidDateMessage ) ;
			return false;
		}
	} 
//**********************************************************************************************
	public static int[] getDateFromSolar(String input){
		int firstSepIndex=input.indexOf("/");
		if(firstSepIndex<0)	{
			return null;
		}
		int year=Integer.parseInt(input.substring(0,firstSepIndex));
		int secoundIndex=input.indexOf("/",firstSepIndex+1);
		if(secoundIndex<0){
			return null;
		}
		int month=Integer.parseInt(input.substring(firstSepIndex+1,secoundIndex));
		int day=Integer.parseInt(input.substring(secoundIndex+1));
		return new int[]{year,month,day};
	} 
//**********************************************************************************************
	public static int[] getDate(Date date){
		return new int[]{date.getYear()+1900,date.getMonth(),date.getDay()} ; 
	}
//**********************************************************************************************
private static boolean isSolarLeapYear(int year){
		return ((((year + 38) * 31) % 128) <= 30);
	}
//**********************************************************************************************	
public static boolean validateStartEndDate(String startDateField,String endDateField, int startYear,int startMonth,int startDay,int endYear,int endMounth,int endDay,DataInputFormConstants constants){
	if(endYear>startYear){
			return true;
		}else if(endYear==startYear){
			if(endMounth>startMonth)return true;
			if(endMounth==startMonth){
				if(endDay>startDay){
					return true;
				}
				
			}
		}
		String invaliStartEndDateMessage=constants.startEndDateError();
		invaliStartEndDateMessage=invaliStartEndDateMessage.replaceAll("@startDate@", startDateField).replaceAll("@endDate@", endDateField);
		MessageUtility.error(constants.error(), invaliStartEndDateMessage);
		return false;
	}

}
