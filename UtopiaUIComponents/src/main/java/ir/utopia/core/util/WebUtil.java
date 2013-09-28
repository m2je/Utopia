/**
 * 
 */
package ir.utopia.core.util;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.EntityPair;
import ir.utopia.core.persistent.PersistentTranslationHelper;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 * @author salarkia
 *
 */
public abstract class WebUtil {
	/**
	 * tools use in write for jsp pages 
	 * @param input
	 * @return an empty String in Case input String is null 
	 */
	private static final String DEFAULT_LOCALE=Boolean.getBoolean("GwtDebug")?"fa":"en";
	public static String write(Object input){
		return write(input,DEFAULT_LOCALE);
	} 
//*********************************************************************************************************
	public static String write(Object input,String language){
		if(input==null||"null".equals(input)||input.toString().trim().length()==0){
			return "";
		}
		if((input instanceof Double) || (input instanceof Integer) || (input instanceof Long) || (input instanceof BigDecimal))
			return getLocaleNumber(DecimalFormat.getInstance().format(input), language);
		else if((input instanceof Date)&&WebUtil.isUsingSolarDate(language)){
			input=DateUtil.convertGregorianToSolarString((Date)input);
		}else if(Enum.class.isInstance(input)){
			input=EnumUtil.getEnumString(((Enum<?>)input).getClass(), language, ((Enum<?>)input).ordinal());
		}else if(NamePair.class.isInstance(input)){
			NamePair pair=(NamePair)input;
			initNamePairName(pair,language);
			return pair.getName();
			
			
		}
		return getLocaleNumber(input.toString(), language);
	} 
//*********************************************************************************************************	
	public static void initNamePairName(NamePair pair,String language){
		if(pair.getName()!=null){
			 pair.setName(write(pair.getName()));
		}else{
			StringBuffer result=new StringBuffer();
			Object []values=pair.getValues();
			EntityPair []entityPairs=pair.getEntityPair();
			for(int i=0;i<entityPairs.length;i++){
				if(i==pair.getKeyIndex()){
					continue;
				}else if(i== pair.getDescriptionColumnIndex()){
					pair.setDescription(WebUtil.write(values[i],language));
					continue;
				}
				
				if(i>0&&result.length()>0){
					result.append(pair.getSeparator());
				}
				result.append(entityPairs[i].isTranslated(language)?
							PersistentTranslationHelper.transalateRecord(entityPairs[i].getEntityClass(), entityPairs[i].getPropertyName(), write(values[i],language), language) 
							: write(values[i],language));
				
			}
			pair.setName(result.toString());
		}
			
	}
//*********************************************************************************************************	
	public static String write(Object input,Locale locale){
		return write(input,locale==null?DEFAULT_LOCALE:locale.getLanguage());
	}
//********************************************************************************************************
	public static String getLocaleNumber(String number,Locale locale){
		return getLocaleNumber(number, locale==null?DEFAULT_LOCALE:locale.getLanguage());
	}
//********************************************************************************************************
	public static String getLocaleNumber(String number,String language){
		if(number==null||number.trim().length()==0)return "";
		if("fa".equals(language)||"ar".equals(language)){
			char [] input=number.toCharArray();
			StringBuffer result=number.startsWith("-")?//if negative number
					new StringBuffer("\u200E"):new StringBuffer();
			for(int i=0;i<input.length;i++){
				char c=input[i];
				if(Character.isDigit(c)){
						result.append(toRigthToLeftNumber(Integer.parseInt(c+"")));
				}else{
					result.append(c);
				}
			}
			return result.toString().trim();
		}
		return number;
	}
//********************************************************************************************************
	public static String getLocaleNumber(Object number,Locale locale){
		return getLocaleNumber(number, locale==null?"en":locale.getLanguage());
	}
//********************************************************************************************************
	public static String getLocaleNumber(Object number,String language){
		if(number==null)return "";
		return getLocaleNumber(number.toString(), language);
	}
//********************************************************************************************************
	public static String toRigthToLeftNumber(int c){
		switch (c) {
		case 0:return "\u06f0";
		case 1:return "\u06f1";
		case 2:return "\u06f2";
		case 3:return "\u06f3";
		case 4:return "\u06f4";
		case 5:return "\u06f5";
		case 6:return "\u06f6";
		case 7:return "\u06f7";
		case 8:return "\u06f8";
		case 9:return "\u06f9";
		}
		return "" ;
	} 
//********************************************************************************************************
	public static String revertRightToLeft(String c){
		if(c==null||c.length()<"\u06f0".length())return null;
		
		StringBuffer result=new StringBuffer();
		char [] cc= c.toCharArray();
		for(char ccc:cc){
			result.append(doRevertRightToLeft(ccc));
		}
		return result.toString();
	}
//********************************************************************************************************
	private static char doRevertRightToLeft(char c){
		
	if('\u06f0'==c){
		return '0';
	}else if('\u06f1'==c){
		return '1';
	}else if('\u06f2'==c){
		return '2';
	}else if('\u06f3'==c){
		return '3';
	}else if('\u06f4'==c){
		return '4';
	}else if('\u06f5'==c){
		return '5';
	}else if('\u06f6'==c){
		return '6';
	}else if('\u06f7'==c){
		return '7';
	}else if('\u06f8'==c){
		return '8';
	}else if('\u06f9'==c){
		return '9';
	}
	
	return c;
	}
//********************************************************************************************************
public static String formatStringNumbersToAnsi(String input){
		if(input==null||input.trim().length()==0)return input;
		StringBuffer result=new StringBuffer();
		for(char c:input.toCharArray()){
			if(Character.isDigit(c)){
				result.append(Character.getNumericValue(c));
			}else{
				result.append(c);	
			}
			
		}
		return result.toString();
	}
//********************************************************************************************************
	/**
	 * 
	 * @param imageName
	 * @return
	 */
	public static synchronized String getImage(String imageName){
		if(imageName==null||imageName.trim().length()==0)return "";
		return "images/"+imageName;
	}
//*********************************************************************************************************
	public static synchronized String getImage(PageContext pageContext, String imageName){
		if(imageName==null||imageName.trim().length()==0)return "";
		return getRootAddress(pageContext)+"images/"+imageName;
	}
	 
//*********************************************************************************************************
	public static synchronized String getCss( String css){
		if(css==null||css.trim().length()==0)return "";
		return "css/"+css;
	}
//*********************************************************************************************************	
	public static synchronized String getCss(PageContext pageContext, String css){
		if(css==null||css.trim().length()==0)return "";
		return getRootAddress(pageContext)+"/css/"+css;
	}
//*********************************************************************************************************
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static String getLanguage(HttpSession session){
		Object l=session.getAttribute(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME);
		 String language=l==null? DEFAULT_LOCALE:l.toString();
		 return language;
	}
//*********************************************************************************************************	
	/**
	 * 
	 * @param session
	 * @return
	 */
	public static String getLanguage(Map<String,Object> session) {
		Object l=session.get(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME);
		 String language=l==null?DEFAULT_LOCALE:l.toString();
		 return language;
	}
//*********************************************************************************************************	
	/**
	 * 
	 * @param language
	 * @return
	 */
	public static synchronized boolean isRigthToLeft(String language){
		return "fa".equals(language);
	}
//*********************************************************************************************************
	/**
	 * 
	 */
	public static synchronized boolean isRigthToLeft(HttpSession session){
		Object language=session.getAttribute(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME);
		return isRigthToLeft(language==null?DEFAULT_LOCALE:language.toString());
	}
//*********************************************************************************************************
	public static synchronized String getDirection(HttpSession session) {
		return isRigthToLeft(session)?"rtl":"ltr";
	}
	
//*********************************************************************************************************
	public static synchronized String getDirection(String language) {
		return isRigthToLeft(language)?"rtl":"ltr";
	}
//*********************************************************************************************************	
	public static synchronized String getAlign(HttpSession session) {
		return isRigthToLeft(session)?"right":"left";
	}
	
//*********************************************************************************************************
	public static synchronized String getAlign(String language) {
		return isRigthToLeft(language)?"right":"left";
	}
//*********************************************************************************************************	
public static Subject getUser(HttpSession session){
	Object result=session.getAttribute(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
	if(Subject.class.isInstance(result)){
		return (Subject)result;
	}
	return null;
}	
//*********************************************************************************************************
public static Subject getUser(Map<?,?> session){
	Object result=session.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
	if(Subject.class.isInstance(result)){
		return (Subject)result;
	}
	return null;
}
//*********************************************************************************************************
/**
 * 
 * @param s
 * @return
 */
public static String encrypt(String s){
	return ServiceFactory.getSecurityProvider().encrypt(s);
}
//*********************************************************************************************************
public static int getDateFormat(HttpSession session){
	String language=getLanguage(session);
	return getDateFormat(language);
}
//*********************************************************************************************************
public static int getDateFormat(String language){
	return "fa".equals(language)?DateInfo.DATE_TYPE_SOLAR:DateInfo.DATE_TYPE_GREGORIAN;
}
//*********************************************************************************************************
public static boolean isUsingSolarDate(String language){
	return "fa".equals(language);
}
//*********************************************************************************************************
public static boolean isUsingSolarDate(Map<String,Object> context){
	return "fa".equals(ContextUtil.getLoginLanguage(context));
}
//*********************************************************************************************************
/**
 * 
 * @param jsName
 * @return
 */
public static String getJsFile(String jsFileName){
	return "../../js/"+jsFileName;
}
//*********************************************************************************************************
public static String getRootAddress(PageContext pageContext){
	HttpServletRequest req=(HttpServletRequest)pageContext.getRequest();
	return req.getRequestURL().toString().replaceAll(req.getRequestURI(),"")+pageContext.getServletContext().getContextPath();
}
//*********************************************************************************************************
public static String getJsFile(PageContext pageContext, String jsFileName){
	return getRootAddress(pageContext)+"/js/"+jsFileName ;	
}
//*********************************************************************************************************
public static Locale getLocale(HttpSession session){
	try{
		 return ServiceFactory.getSecurityProvider().getLocaleOf((Subject)session.getAttribute(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME));
	}catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}

public static Locale getLocale(){
	try{
		 return ServiceFactory.getSecurityProvider().getLocaleOf(ContextUtil.getUser());
	}catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}

}
