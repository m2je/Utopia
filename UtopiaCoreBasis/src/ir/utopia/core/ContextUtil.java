package ir.utopia.core;

import ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.principal.LocalePricipal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpSession;

public class ContextUtil {
	private static final String  TIME_FORMAT="HH:mm";
	
	public static final String RECORD_ID_KEY_IN_CONTEXT="RecordId";
	public static final String LOGIN_LANGUAGE_PARAMETER_NAME=SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME;
	public static final String CURRENT_DATE_CONTEXT_VARIABLE="currentDate";
	public static final String CURRENT_TIME_CONTEXT_VARIABLE="currentTime";
	private static HashSet<String>SESSION_EXCLUDED_PARAMETERS=new HashSet<String>();
	public static final String USER_ROLES_PARAMETER="_userRoles";
	public static final String CURRENT_ACTION_PARAMETER="_action";
	public static final String USER_PREFERENCES_PARAMETER="_pref";
	public static final String CURRENT_USER_CONTEXT_PARAMETER="userId";
	private static final Object[]o=new Object[]{};
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ContextUtil.class.getName());
	}
	/**
	 * 
	 * @param sesssion
	 * @return
	 */
	public static Map<String,Object> createContext(HttpSession session){
		 Enumeration<String> enumes= session.getAttributeNames();
		 HashMap<String, Object>result=new HashMap<String, Object>();
		 while(enumes.hasMoreElements()){
			 String key= enumes.nextElement();
			 if(isExcluded(key))continue;
			 Object value=session.getAttribute(key);
			 if(value!=null){
				 if(value.getClass().isPrimitive()){
					 result.put(key, value);
				 }
				 if(value instanceof Subject){
						initSubject((Subject)value, result);
					}else{
						if(isSerializeable(value)&&!isNotExcluded(key))
						result.put(key, value);
				 }
			 }
		 }
		 result.put(CURRENT_DATE_CONTEXT_VARIABLE, new Date());
		 initIfDebugContext(result);
		return result;
	}
//*****************************************************************************************************************	
	private static void initIfDebugContext(Map<String,Object>context){
		Object o=System.getProperties().get("UtopiaDebug");
		if(o!=null&&Boolean.valueOf(o.toString())){
			Subject subject= ServiceFactory.getSecurityProvider().loginUser(1l);
			subject.getPrincipals().add(new LocalePricipal(Locale.ENGLISH));
			initSubject(subject, context);
		}
	}
//*****************************************************************************************************************	
	/**
	 * 
	 * @param o
	 * @return
	 */
	private static  boolean isSerializeable(Object o){
		if(Serializable.class.isInstance(o)){
		 	try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(o);
				oos.close();
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
	}
//*****************************************************************************************************************
	private static boolean isNotExcluded(String key){
		return SESSION_EXCLUDED_PARAMETERS.contains(key);
	}
//*****************************************************************************************************************
	/**
	 * 
	 * @param session
	 * @param recordId
	 * @return
	 */
	public static Map<String,Object> createContext(HttpSession session,Long recordId){
		Map<String,Object> props=createContext(session);
		props.put(RECORD_ID_KEY_IN_CONTEXT, recordId);
		return props;
	}
//*****************************************************************************************************************
	/**
	 * 
	 * 
	 * @param session
	 * @param persistent
	 * @return
	 */
	public static Map<String,Object> createContext(HttpSession session,UtopiaPersistent persistent){
		Map<String,Object>context=createContext(session);
		if(persistent!=null){
			context.put(RECORD_ID_KEY_IN_CONTEXT, Long.toString(persistent.getRecordId()));
		Method []methods=persistent.getClass().getMethods();
		for(Method method:methods){
			String methodName= method.getName();
			if(!methodName.startsWith("get"))continue;
			try {
				Class<?>returnType= method.getReturnType();
				Object result= method.invoke(persistent, o);
				if(result!=null){
					if(List.class.isAssignableFrom(returnType)){
						List<?> l=(List<?>)result;
						for(Object cl:l){
							if(cl!=null&&LookupInfo.class.isAssignableFrom(cl.getClass())){
								Object value=((LookupInfo)cl).getLookupValue();
								String key=AnnotationUtil.getFieldName(((LookupInfo)cl).getLookupClass().getSimpleName());
								if(key!=null&&value!=null){
									if(isSerializeable(value))
										context.put(key, value);
									}
							}
						}
					}else{
						if(isSerializeable(result)){
							context.put(AnnotationUtil.getPropertyName(method.getName()), result);
						}
					}
				}
				
			} catch (Exception e) {
				logger.log(Level.WARNING,"fail to load value from method "+methodName, e);
			} 
		}
		}
		initIfDebugContext(context);
		return context;
	}
//*****************************************************************************************************************	
	public static Map<String,Object>createContext(Map<String,Object>session){
		if(session==null)return null;
		HashMap<String, Object>result=new HashMap<String, Object>();
		for(Object key: session.keySet()){
			if(isExcluded(key)) continue;
			Object value=session.get(key);
			if(value!=null){
				if(value instanceof Subject){
					initSubject((Subject)value, result);
				}else if(isSerializeable(value)){
					result.put(key.toString(), value);
				}
			}
		}
		result.put(CURRENT_DATE_CONTEXT_VARIABLE, new Date());
		initIfDebugContext(result);
		return result;
	}
//************************************************************************************************
	private static boolean isExcluded(Object key){
		return !String.class.isInstance(key)||
		"net.sf.jasperreports.j2ee.jasper_print".equals(key);
	}
//************************************************************************************************	
	public static Long getUserId(Map<String,Object>context){
		Subject subject= getUser(context);
		if(subject!=null){
			return ServiceFactory.getSecurityProvider().getUserId(subject);
		}
		return -1l;
	}
//************************************************************************************************
	private static void initSubject(Subject subject, Map<String,Object>result){
		SecurityProvider security=  ServiceFactory.getSecurityProvider();
		Long userId=security.getUserId((Subject)subject);
		result.put(CURRENT_USER_CONTEXT_PARAMETER, userId);
		result.put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME,subject);
	}
//************************************************************************************************
	public static String getLoginLanguage(){
		Context context= ContextHolder.getContext();
		if(context!=null&&context.getContextMap()!=null){
			return getLoginLanguage(context.getContextMap());
		}
		return "en";
	}
//************************************************************************************************
	public static String getLoginLanguage(Map<String,Object>context){
		if( context.containsKey(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME)){
			return context.get(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME).toString();
		}
		Subject subject=(Subject)context.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
		if(subject!=null){
			 for(Principal p: subject.getPrincipals()){
				 if(LocalePricipal.class.isInstance(p)){
					 return ((LocalePricipal)p).getLocale().getLanguage();
				 }
			 }
		}
		return "en";
	} 
	
//************************************************************************************************
	public static Subject getUser(Map<String,Object>context){
		return (Subject)context.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
		}
//************************************************************************************************
	public static void registerSessionContextExclusionParameter(String parameterName){
		SESSION_EXCLUDED_PARAMETERS.add(parameterName);
	}
//************************************************************************************************
	public static HashMap<String, Object> createTestContext(){
		return createAdminContext();
		}
//************************************************************************************************	
	public static HashMap<String, Object> createAdminContext(){
			return createUserContext(1l);
		}
//************************************************************************************************
	public static HashMap<String, Object> createUserContext(Long userId){
		HashMap<String,Object>result=new HashMap<String, Object>();
		result.put(CURRENT_USER_CONTEXT_PARAMETER, userId);
		Subject subject=ServiceFactory.getSecurityProvider().loginUser(1l);
		subject.getPrincipals().add(new LocalePricipal(new Locale("fa")));
		result.put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME,subject);
		result.put(CURRENT_DATE_CONTEXT_VARIABLE, new Date());
		if(ServiceFactory.isSupportFiscalYear()){
			FiscalYearUtil.initContextFiscalYear(result, 1254l);
		}
		return result;
	}
//************************************************************************************************
	public static Set<Long> getUserRoles(Map<String,Object> context){
		return getUserRoles(getUser(context));
	}
//************************************************************************************************
	public static Set<Long> getUserRoles(Subject user){
		return ServiceFactory.getSecurityProvider().getUserRoles(user);
	}
//************************************************************************************************
	public static Object getContextParameter(Map<String,Object>context,String parameterName){
		if(context==null||parameterName==null)return null;
		if(USER_ROLES_PARAMETER.equals(parameterName)){
			return getUserRoles(context);
		}else if(CURRENT_DATE_CONTEXT_VARIABLE.equals(parameterName)){
			return new Date();
		}else if(CURRENT_TIME_CONTEXT_VARIABLE.equals(parameterName)){
			DateFormat df=new SimpleDateFormat(TIME_FORMAT);
			return df.format(new Date());
		}
		return context.get(parameterName);
	}
//************************************************************************************************
	public static UserPreferencesInfo getUserPreferences(Map<String,Object>context){
		return (UserPreferencesInfo)context.get(USER_PREFERENCES_PARAMETER);
	}
//************************************************************************************************
	public static UserPreferencesInfo getUserPreferences(HttpSession session){
		return (UserPreferencesInfo)session.getAttribute(USER_PREFERENCES_PARAMETER);
	}
//************************************************************************************************
	public static boolean isShowAllOrganizationData(Map<String,Object>context){
		return getUserPreferences(context).isShowAllAccessibleOrganizations();
	}
//************************************************************************************************
	public static Long getCurrentFiscalYear(Map<String,Object>context){
		UserPreferencesInfo pref= getUserPreferences(context);
		return pref==null?null:pref.getCurrentFiscalYearId();
	}
//************************************************************************************************
	public static Long getCurrentUserId(){
		return getUserId( ContextHolder.getContext().getContextMap());
	}
//************************************************************************************************
}
