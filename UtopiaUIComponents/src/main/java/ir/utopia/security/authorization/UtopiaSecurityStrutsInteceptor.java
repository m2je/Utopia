package ir.utopia.security.authorization;

import ir.utopia.core.Context;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.struts.ActionUtil;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class UtopiaSecurityStrutsInteceptor  {
	private static final Logger logger;
	private static final boolean IsDebugging=Boolean.getBoolean("GwtDebug");
	static {
		logger = Logger.getLogger(AbstractBasicUsecaseBean.class.getName());
	}
	public static final String NON_AUTHORORIZED_ACTION_PAGE_URL="NonAuthorize";
	public static final String REQUEST_USECASE_NAME_PARAMETER_NAME=SecurityProvider.USER_SESSION_ATTRIBUTE_NAME;
	private static final String DATA_INPUT_PROXY_URL="DataInput-service"+Constants.STRUTS_AXAJ_EXTENSION;
	private static final String PROCESS_SERVICE_PROXY_URL="Process-Service"+Constants.STRUTS_AXAJ_EXTENSION;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2866147457543572174L;
SecurityProvider securityProvider;
	
	public void destroy() {
		securityProvider=null;
		
	}

	
	public void init() {
	
	}
//*********************************************************************************************************************************
	private void initSecurityProvider(){
		if(securityProvider==null)
			securityProvider=ServiceFactory.getSecurityProvider();
	}
//*********************************************************************************************************************************
//	@Override
//	public String intercept(ActionInvocation actionInvocation) throws Exception {
//		ContextHolder.clean();
//		HttpServletRequest request=org.apache.struts2.ServletActionContext.getRequest();
//		String requestURL=request.getRequestURI();
//		
//		String actionName;
//		UseCase usecase=null;
//		String completeUscaseName=null;
//		if(requestURL.endsWith(DATA_INPUT_PROXY_URL)){
//			actionName=Constants.predefindedActions.noAction.toString();
//		}else if(requestURL.equalsIgnoreCase(PROCESS_SERVICE_PROXY_URL)){
//			actionName=Constants.predefindedActions.noAction.toString();
//		}else{
//			String useCaseName,systemName,subSystem;
//			Map<Integer,String> map=ActionUtil.parseClassAndMethod(requestURL);
//			actionName= map.get(ActionUtil.METHOD);
//		    useCaseName=map.get(ActionUtil.USECASE);
//		    systemName=map.get(ActionUtil.SYSTEM);
//		    subSystem=map.get(ActionUtil.SUB_SYSTEM);
//		    if(isUtilityAction(systemName, subSystem, useCaseName, actionName)){
//				 completeUscaseName=securityProvider.decrypt(request.getParameter(REQUEST_USECASE_NAME_PARAMETER_NAME));
//			 }else{
//				 completeUscaseName=UsecaseUtil.getFullUsecaseName(systemName, subSystem, useCaseName);
//			 }
//		    usecase= UsecaseUtil.getUsecaseWithName(completeUscaseName);
//		 }
//		initCurrentThreadContext(usecase, actionName, request.getSession());
//		if(!IsDebugging){
//			Map<String,Object>session= actionInvocation.getInvocationContext().getSession();
//			Subject currentUser= ContextUtil.getUser(session);
//			
//			if(currentUser==null){
//				logger.log(Level.WARNING,"Non-Authorize action request from :["+request.getRemoteAddr()+"]");
//				return Action.LOGIN;
//			}
//			
//			 boolean success=true;
//			if(completeUscaseName!=null){
//					try {
//						initSecurityProvider();
//						securityProvider.doPrivilegedAction(actionName,completeUscaseName,currentUser);
//					} catch (Exception e) {
//						logger.log(Level.WARNING,"",e);
//						success=false;
//					}
//					securityProvider.LogAction(actionName,completeUscaseName, success);
//				}
//			if(!success){
//				return NON_AUTHORORIZED_ACTION_PAGE_URL;
//			}
//		}
//			
//		return actionInvocation.invoke();
//	}
//*********************************************************************************************************************************
	protected boolean isUtilityAction(String systemName,String subSystem,String useCaseName,String actionName){
		return "Co".equals(systemName)&&"Ut".equals(subSystem)&&"Attachment".equals(useCaseName)&&Constants.predefindedActions.download.name().equals(actionName);
	}
//*********************************************************************************************************************************
	private void initCurrentThreadContext(UseCase usecase,String actionName,HttpSession session){
		Map<String,Object>ctxMap= ContextUtil.createContext(session);
		String methodName=actionName;
		Long actionId=null;
		if(usecase!=null){
		List<UsecaseAction>actions= usecase.getUseCaseActions();
		if(actions!=null){
			for(UsecaseAction usAction:actions){
				if(usAction.getActionName().equals(actionName)){
					methodName=usAction.getMethodName();
					actionId=usAction.getActionId();
					break;
				}
			}
		}
		}
		Context context=new Context(ctxMap, usecase,actionId, actionName, methodName);
		ContextHolder.setContext(context);
	}

 
}
