package ir.utopia.security.log;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.principal.UserIdPrincipal;
import ir.utopia.core.security.userlog.bean.CoUserLogFacadeRemote;
import ir.utopia.core.security.userlog.persistent.CoUserLog;
import ir.utopia.core.security.userlogdtl.bean.CoUserLogDtlFacadeRemote;
import ir.utopia.core.security.userlogdtl.persistent.LogActionStatus;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.InvocationContext;
import javax.security.auth.Subject;

import com.opensymphony.xwork2.ActionContext;

public class UserLogHandler {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UserLogHandler.class.getName());
	}
	private static final String LOG_DETINATION_KEY="logDetination";
	private static final String LOG_DETINATION_KEY_DB="DB";
	private static boolean isLogToDB=false;
	private static Properties props=new Properties();
	static{
		try {
			props.load(UserLogHandler.class.getClassLoader().getResourceAsStream("LogConfig.properties"));
			isLogToDB=LOG_DETINATION_KEY_DB.equals(props.get(LOG_DETINATION_KEY));
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//**************************************************************************************************************	
	public static void logAction(InvocationContext invocationContext,boolean success){
		try {
			if(mustBeLogged(invocationContext)){
				Method method=invocationContext.getMethod();
				if(isLogToDB){
					ActionContext actContext= ActionContext.getContext();
					Map<String,Object>context;
					if(actContext!=null){
						context= actContext.getSession();
					}else{
						context=getContextFromBeanParameter(invocationContext.getParameters()); 
					}
					if(context!=null){
						Subject user=(Subject)context.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
						if(user!=null){
							Long logId= ((java.util.Set<UserIdPrincipal>)user.getPrincipals(UserIdPrincipal.class)).iterator().next().getUserLogId();
							if(logId!=null){
								CoUserLogDtlFacadeRemote logDtl=(CoUserLogDtlFacadeRemote)ServiceFactory.lookupFacade(CoUserLogDtlFacadeRemote.class);
								
								logDtl.logUserAction(logId, findUsecaseAction(invocationContext), method.getName(),success?LogActionStatus.success:LogActionStatus.fail);
							}
							
						}
						
					}	
					}
			}
		} catch (Throwable e) {
			logger.log(Level.WARNING,"",e);
		}
		
	} 
//**************************************************************************************************************	
	public static void logAction(String actionName, String usecaseName, boolean success){
		try {
			if(mustBeLogged(actionName,usecaseName)){
				if(isLogToDB){
					ActionContext actContext= ActionContext.getContext();
					Map<String,Object>context=null;
					if(actContext!=null){
						context= actContext.getSession();
					}
					if(context!=null){
						Subject user=(Subject)context.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
						if(user!=null){
							Long logId= ((java.util.Set<UserIdPrincipal>)user.getPrincipals(UserIdPrincipal.class)).iterator().next().getUserLogId();
							if(logId!=null){
								
								UseCase usecase= UsecaseUtil.getUsecaseWithName(usecaseName);
								if(usecase!=null){
									CoUserLogDtlFacadeRemote logDtl=(CoUserLogDtlFacadeRemote)ServiceFactory.lookupFacade(CoUserLogDtlFacadeRemote.class);
									UsecaseAction action= usecase.getUsecaseAction(actionName);
									logDtl.logUserAction(logId,action!=null?action.getUsecaseActionId():null,actionName,success?LogActionStatus.success:LogActionStatus.fail);
								}
							}
						}
					}	
					}
			}
		} catch (Throwable e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//**************************************************************************************************************	
	private static long findUsecaseAction(InvocationContext invocationContext){
		try {
			Method method=invocationContext.getMethod();
			Object target=invocationContext.getTarget();
			UsecaseAction action= UsecaseUtil.getUsecaseAction(target.getClass(), method.getName());
			return action==null?-1l:action.getUsecaseActionId();
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return -1l;
	}

//**************************************************************************************************************	
	private static Map<String,Object> getContextFromBeanParameter(Object []params){
		if(params!=null&&params.length>0){
			for(int j=params.length-1;j>=0;j--){
				if(Map.class.isInstance(params[j])){
					try {
						return ((Map<String,Object>) params[j]);
						
					} catch (Exception e) {
						logger.log(Level.WARNING,"could not found the context at index "+j+" of calling method");
						continue;
					}
				}
			}
		}
		return null;
	}
//*************************************************************************************************************	
	public static synchronized Long createUserLoginLog(String clintAddress,Subject user){
		try {
			if(isLogToDB){
				CoUserLogFacadeRemote bean=(CoUserLogFacadeRemote)ServiceFactory.lookupFacade(CoUserLogFacadeRemote.class);
				CoUserLog userLog=bean.logUserLogin(clintAddress,user);
				return userLog!=null?userLog.getCoUserLogId():-1l;
			}
		} catch (Throwable e) {
			logger.log(Level.WARNING,"",e);
		}
		
		return -1l;
	}
//**************************************************************************************************************
	public static void logUserLogout(Subject user){
		try {
			if(isLogToDB){
				if(user!=null){
					CoUserLogFacadeRemote bean=(CoUserLogFacadeRemote)ServiceFactory.lookupFacade(CoUserLogFacadeRemote.class);
					bean.logUserLogout(user);
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//**************************************************************************************************************
	private static boolean mustBeLogged(String actionName,String usecaseName){
		return true;
	}
//**************************************************************************************************************
private static boolean mustBeLogged(InvocationContext context){
	Method method=context.getMethod();
	Object target=context.getTarget();
	if(CoUserLogFacadeRemote.class.isInstance(target)||CoUserLogDtlFacadeRemote.class.isInstance(target)){
		String methodName=method.getName();
		if("logUserLogin".equals(methodName)||"logUserLogout".equals(methodName)||"logUserAction".equals(methodName))
				return false;
	}
	return true;
}
}
