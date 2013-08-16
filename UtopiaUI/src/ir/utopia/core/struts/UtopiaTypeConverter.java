package ir.utopia.core.struts;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.ActionContext;

public class UtopiaTypeConverter extends StrutsTypeConverter {
	private static final Logger logger;	
	
	static {
		logger = Logger.getLogger(UtopiaTypeConverter.class.getName());
	}
	@Override
	public Object convertFromString(Map ognlMap, String[] values, Class clazz) {
//		SessionMap session=(SessionMap) ognlMap.get(ActionContext.SESSION);
		ActionContext ctx=ActionContext.getContext();
		String language=ContextUtil.getLoginLanguage( ctx.getSession());
		return convertFromString( clazz,language,values);
	}
//****************************************************************************************************************************
	@Override
	public String convertToString(Map ognlMap, Object arg1) {

		return null;
	}
//****************************************************************************************************************************	
	public static Object convertFromString( Class<?> clazz,String locale,String... values){
		if(values==null||values.length==0){
			return null;
		}
		Object[]result=new Object[values.length];
		if(Enum.class.isAssignableFrom(clazz)){
			result= convertAsEnum(values,clazz);
		}else if(Locale.class.isAssignableFrom(clazz)){
			result=new Object[]{new Locale(values[0])};
		}else if(Boolean.class.equals(clazz)||boolean.class.equals(clazz)){
			return values[0]!=null&&"true".equalsIgnoreCase(values[0])||"on".equalsIgnoreCase(values[0]);
		}else if(Double.class.equals(clazz)|| //Is number
				Long.class.equals(clazz)||
				Integer.class.equals(clazz)
				){
			try{
			result = new Object[]{ConstructorUtils.invokeConstructor(clazz, new Object[]{WebUtil.formatStringNumbersToAnsi(values[0]).replaceAll(",", "").replaceAll("\u200E", "")}, new Class[]{String.class})};
			}catch (Exception e) {
				logger.log(Level.ALL,"Exception when invoke constructor of class " + clazz.getName() ,  e);
			}
			
		}else if(double.class.equals(clazz)){
			try {
				String p=makeRaw(values[0]);
				result=new Object[]{Double.parseDouble(p)};
			} catch (Exception e) {
				logger.log(Level.ALL,"Exception parsing of class " + clazz.getName() ,  e);
			}
		}else if(long.class.equals(clazz)){
			try {
				String p=makeRaw(values[0]);
				result=new Object[]{Long.parseLong(p)};
			} catch (Exception e) {
				logger.log(Level.ALL,"Exception parsing of class " + clazz.getName() ,  e);
			}
		}else if(int.class.equals(clazz)){
			try {
				String p=makeRaw(values[0]);
				result=new Object[]{Integer.parseInt(p)};
			} catch (Exception e) {
				logger.log( Level.ALL,"Exception parsing of class " + clazz.getName(),  e);
			}
		}else if(Date.class.equals(clazz)){
			if(values[0]==null||values[0].trim().length()==0){
				result=new Object[]{null};
			}else{
				if(DateUtil.useSolarDate(locale) ){
					result=new Object[]{DateUtil.solarToDate(values[0])};
				}else{
					result=new Object[]{DateUtil.gerToDate(values[0])};
				}
			}
		}
		else{
			result=values[0]!=null?new Object[ ]{ConvertUtils.convert(values[0], clazz)}:null;
		}
		return (result!=null&&result.length==1)?result[0]:null ;
	}
//****************************************************************************************************************************	
	@SuppressWarnings("unchecked")
	private static Enum<?>[] convertAsEnum(String[] values,Class<?> clazz){
		if(values!=null&&values.length>0){
			Enum<?>[]result=new Enum<?>[values.length];
			for(int i=0;i<result.length;i++){
				if(values[i]==null||values[i].trim().length()==0){
					continue;
				}
				try {
					int index=Integer.parseInt(values[i]);
					result[i]=Enum.valueOf((Class<?extends Enum>) clazz, (clazz.getFields()[index]).getName());
				} catch (Exception e) {
					logger.log(Level.INFO,"", e);
					try {
						result[i]= Enum.valueOf((Class<?extends Enum>)clazz, values[i]);
					} catch (Exception e1) {
						logger.log(Level.INFO,"", e1);
					}
					
				}
			}
			return result;
		}
		return null;
	}
//****************************************************************************************************************************	
	public static String makeRaw(String input){
		if(input!=null){
		input=WebUtil.formatStringNumbersToAnsi(input);
		return input.replaceAll(",", "").replace("\u066C", "").replaceAll("\u200E", "");
		}
		return input;
	}
}
