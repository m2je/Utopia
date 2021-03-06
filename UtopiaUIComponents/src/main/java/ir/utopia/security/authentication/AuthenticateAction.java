/**
 * 
 */
package ir.utopia.security.authentication;

import ir.utopia.common.systems.system.bean.CmSystemParameterFacadeRemote;
import ir.utopia.common.systems.system.persistent.CmSystemParameter;
import ir.utopia.common.systems.system.persistent.SystemParameterType;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UserPreferencesInfo;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.security.AfterLoginProcess;
import ir.utopia.core.security.userpreferences.bean.CoUserPreferencesFacadeRemote;
import ir.utopia.core.security.userpreferences.persistent.CoUserPreferences;
import ir.utopia.security.beans.LoginBeanRemote;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.security.auth.Subject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author salarkia
 *
 */
@ApplicationScoped 
public class AuthenticateAction  {
	
	@EJB
	LoginBeanRemote loginBean;
	
	private static final Logger logger;
	
	public static final String IS_LOCAL_APPLICATION_KEY="__is_local_application";
	static {
		logger = Logger.getLogger(AuthenticateAction.class.getName());
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6410092930893646802L;
	/**
	 * 
	 */
	private LoginBeanRemote bean;
	private AuthenticationForm form=new AuthenticationForm();
	public AuthenticateAction() {
	
	
	}
	@Path("/login")
	@POST
	public ExecutionResult login(AuthenticationForm form){
		ExecutionResult result=new ExecutionResult();
		 String username=form.getUsername();
		 String password=form.getPassword();
			if(username==null||username.trim().length()==0||password==null||password.trim().length()==0){
				result.setSuccess(false);
				result.addMessage(MessageHandler.getMessage("userpass.required", this.getClass().getName(), form.getLoginLanguage()), MessageType.error);
				
			}else{
				try {
					bean=(LoginBeanRemote)ServiceFactory.lookupFacade(LoginBeanRemote.class.getName());
					Subject user= bean.login(username, password,"test");
//						Subject userProfile=bean.getUserProfile();
//						userProfile.getPrincipals().add(new LocalePricipal(new Locale(form.getLoginLanguage())));
//						 setUser(userProfile);
//						 ContextHolder.setContext(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME,userProfile);
//						 Map<String,Object> session= ActionContext.getContext().getSession();
//						 session.put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME,userProfile);
//						 session.put(SecurityProvider.LOGIN_LANGUAGE_PARAMETER_NAME,form.getLoginLanguage());
//						
//						 initAfterLoginParameteres();
//						
//						 ServiceFactory.getSecurityProvider().logUserLogin(org.apache.struts2.ServletActionContext.getRequest().getRemoteAddr(),userProfile);
//						 return SUCCESS;
//					}else{
//						setMessage(MessageHandler.getMessage("invalidUserPass", this.getClass().getName(), form.getLoginLanguage())) ;
//						ActionContext.getContext().put(SecurityProvider.FAIL_TO_LOGIN_MESSAGE_NAME, message);
//						return ERROR;
				} catch (Exception e) {
					e.printStackTrace();
					logger.log(Level.ALL,"looking up login bean failed", e);
				}
			}
		return result;
	}
	
	

//*****************************************************************************************************************************************************
	protected void initAfterLoginParameteres(){
		 initSystemProperties();
		 initUserPreferences();
		 if(ServiceFactory.isSupportFiscalYear()){
			 initFiscalYear();
		 }
		 AfterLoginProcess []processes= ServiceFactory.getAfterLoginProcess();
		 if(processes!=null){
			 for(AfterLoginProcess process:processes){
				 process.doProcess();
			 }
		 }
	}
//*****************************************************************************************************************************************************
	protected void initFiscalYear(){
//		FiscalYearUtil.initFiscalYear(ActionContext.getContext().getSession());
	}
//*****************************************************************************************************************************************************
	protected void initSystemProperties(){
		try {
			Map<String,Object> session=null;// ActionContext.getContext().getSession();
			Map<String,Object>context= ContextUtil.createContext(session);
			CmSystemParameterFacadeRemote cmSystemParamBean=(CmSystemParameterFacadeRemote)ServiceFactory.lookupFacade(CmSystemParameterFacadeRemote.class);
			List<CmSystemParameter>systemParams= cmSystemParamBean.getCurrentUserSystemParamters(context);
			if(systemParams!=null){
				for(CmSystemParameter sysParams:systemParams){
					session.put(sysParams.getCmSystem().getAbbreviation()+"_"+sysParams.getKey(), SystemParameterType.getValueOf(sysParams.getValue(), sysParams.getType()));
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//*****************************************************************************************************************************************************
	protected void initUserPreferences(){
		try {
			Map<String,Object> session=null;// ActionContext.getContext().getSession();
			Map<String,Object>context= ContextUtil.createContext(session);
			CoUserPreferencesFacadeRemote facade=(CoUserPreferencesFacadeRemote)ServiceFactory.lookupFacade(CoUserPreferencesFacadeRemote.class);
			List<CoUserPreferences>preferences= facade.findByProperties(new String []{"coUser.coUserId"},new Object[]{ContextUtil.getUserId(context)},null);
			UserPreferencesInfo userPreferences=new UserPreferencesInfo(preferences);
			session.put(ContextUtil.USER_PREFERENCES_PARAMETER, userPreferences);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//*****************************************************************************************************************************************************
	private String message;
	private Subject user;
	
	


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Subject getUser() {
		return user;
	}


	public void setUser(Subject user) {
		this.user = user;
	}

	public AuthenticationForm getModel() {
		return form;
	}



	


	
	
	
	
}
