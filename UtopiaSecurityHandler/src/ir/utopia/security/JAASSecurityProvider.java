package ir.utopia.security;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.Context;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UserPreferencesInfo;
import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants.Sex;
import ir.utopia.core.exception.OrganizationAccessException;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.exception.NonAuthorizedActionException;
import ir.utopia.core.security.exception.NotAuthenticatedException;
import ir.utopia.core.security.persistent.CoVUserAllValidAccess;
import ir.utopia.core.security.principal.FirstnamePrincipal;
import ir.utopia.core.security.principal.LastnamePrincipal;
import ir.utopia.core.security.principal.LocalePricipal;
import ir.utopia.core.security.principal.OrganizationPrincipal;
import ir.utopia.core.security.principal.SexPrincipal;
import ir.utopia.core.security.principal.UserIdPrincipal;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userorganizationaccess.bean.CoUserOrganizationAccessFacadeRemote;
import ir.utopia.core.security.userrole.bean.CoUserRolesFacadeRemote;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.util.Cache;
import ir.utopia.security.authentication.bean.JAASSecurityFacadeRemore;
import ir.utopia.security.authorization.UserAccessHandler;
import ir.utopia.security.beans.LoginBeanRemote;
import ir.utopia.security.log.UserLogHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.InvocationContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;

import org.jasypt.util.text.BasicTextEncryptor;


public class JAASSecurityProvider implements SecurityProvider {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(JAASSecurityProvider.class.getName());
	}
	/**
	 * login URL of security provider
	 */
	public static final String SECURITY_PROVIDER_LOGIN_URL="login-Page";
	/**
	 * login config attribute name
	 */
	public static final String LOGIN_CONFIG_NAME="login-config";
	private static final String ENCRYPTION_KEY=String.valueOf(1234567890);  
	
	private String loginURL;
	private Cache<String, Long[]>USER_USCASE_CACHE=new Cache<String, Long[]>();
	private HashMap<String, String>configurationMap=new HashMap<String, String>();
	
	public String getLoginURL() {
		return loginURL;
	}
//*****************************************************************************************	
	public String getAttributeValue(String attributeName) {
		if(attributeName==null)return null;
		if(SECURITY_PROVIDER_LOGIN_URL.equals(attributeName)){
			return getLoginURL();
		}
		return configurationMap.get(attributeName);
	}
//*****************************************************************************************
	public void setAttributeFromConfiguration(String attributeName,
			String attributeValue) {
		if(SECURITY_PROVIDER_LOGIN_URL.equals(attributeName)){
			this.loginURL=attributeValue;
		}
		else if(attributeName!=null&&attributeName.trim().length()>0
				&&attributeValue!=null&&attributeValue.trim().length()>0)
			this.configurationMap.put(attributeName, attributeValue);
			
	}
//*****************************************************************************************
	/**
	 * 
	 */
	public void doPrivilegedAction(InvocationContext invocationContext)
			throws NonAuthorizedActionException {
		Context context= ContextHolder.getContext();
		if(context!=null&&context.getUsecase()!=null){
			UseCase usecase= context.getUsecase();
			String methodName= context.getMethodName();
			Subject user=ContextUtil.getUser(context.getContextMap());
			if(user==null)throw new NonAuthorizedActionException("unkown user access , authorization failed !");
			doPrivilegedAction(usecase, methodName, user,true);
		}else{
			findExectionContextAndDoAsPrivilegedAction(invocationContext);
		}
	}
//*****************************************************************************************
	@SuppressWarnings("unchecked")
	private void findExectionContextAndDoAsPrivilegedAction(InvocationContext invocationContext)throws NonAuthorizedActionException{
		try {
			Method method=invocationContext.getMethod();
			Object target=invocationContext.getTarget();
			Class<?>clazz= target.getClass();
			Class<? extends UtopiaBean>remoteInterface=BeanUtil.getRemoteClass((Class<? extends UtopiaBean>)clazz);
			if(remoteInterface==null){
				logger.log(Level.WARNING,"invalid facade not isAssignableFrom from UtopiaBean "+clazz.getName());
				return ;
			}
			UseCase usecase= UsecaseUtil.getUseCase(remoteInterface.getName());
			String methodName=method.getName();
			if(usecase==null){
				if(logger.isLoggable(Level.FINE)){
					logger.log(Level.FINE,"no configuration found for remote interface "+remoteInterface.getName()+" no security check !!! ") ;
				}
				return ;
			}
			Subject user=getUser(invocationContext);
			doPrivilegedAction(usecase, methodName, user,true);
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			throw new NonAuthorizedActionException(e);
		}
	}
//*****************************************************************************************
	@Override
	public void doPrivilegedAction( String actionName,
			String usecaseName,Subject user) throws NonAuthorizedActionException {
		UseCase usecase=null;
		try {
			usecase=UsecaseUtil.getUsecaseWithName(usecaseName);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			return;
		}
		if(usecase==null){
			logger.log(Level.WARNING,"fail to find usecase :["+usecaseName+"] ,run as priviledged action : ["+actionName+"]");
			return;
		}
		doPrivilegedAction(usecase, actionName, user,false);
		
	}
//*****************************************************************************************	
	private void doPrivilegedAction(UseCase usecase,String actionName,Subject user,boolean checkMethod)throws NonAuthorizedActionException{
		for(UsecaseAction action:usecase.getUseCaseActions()){
			if(checkMethod?action.getMethodName().equals(actionName): action.getActionName().equals(actionName)){
				if(user==null){
					logger.log(Level.WARNING,"no context found continue execution of method --->"+actionName+" of class -->"+usecase.getRemoteClassName()+" with no security check!!! ");
					return;
				}else{
					Long []actions=getUsecaseAvailableActions(user, usecase.getUsecaseId());
					for(Long act:actions){
						if(act.equals(action.getActionId())){
							return ;
						}
					}
					NonAuthorizedActionException ex=new NonAuthorizedActionException("no access level found for user --->"+getFullUserName(user, null)+" in usecase---> "+usecase.getUsecaseId()+" and action--->"+action.getActionId());
					ex.setUsecaseId(usecase.getUsecaseId());
					ex.setActionId(action.getActionId());
					throw ex;
				}
			}
		}
	}
//*****************************************************************************************
	@SuppressWarnings("unchecked")
	protected Map<String,Object>getContext(InvocationContext invocationContext){
		Object []params=invocationContext.getParameters();
		for(Object param:params){
			if(Map.class.isInstance(param)){
				 ParameterizedType ptype= ((ParameterizedType)param.getClass().getGenericSuperclass());
				 Class<?> p1=(Class<?>)ptype.getActualTypeArguments()[0];
				 Class<?> p2=(Class<?>)ptype.getActualTypeArguments()[1];
				 if(String.class.equals(p1)&&Object.class.equals(p2)){
					 return (Map<String,Object>)param;
				 }
			}
		}
		return null;
	}
//*****************************************************************************************
	@SuppressWarnings("unchecked")
	protected Subject getUser(InvocationContext invocationContext){
		Object []params=invocationContext.getParameters();
		if(params!=null){
			for(Object param:params){
				if(Map.class.isInstance(param)){
					 try {
						ParameterizedType ptype= ((ParameterizedType)param.getClass().getGenericSuperclass());
						 Class<?> p1=(Class<?>)ptype.getActualTypeArguments()[0];
						 Class<?> p2=(Class<?>)ptype.getActualTypeArguments()[1];
						 if(String.class.equals(p1)&&Object.class.equals(p2)){
							 Subject result= ContextUtil.getUser((Map<String,Object>)param);
							 if(result!=null){
								 return result;
							 }
						 }
					} catch (ClassCastException e) {
						continue;
					}
				}
			}
		}
		return null;
	}
//*****************************************************************************************
	public CmSubsystem[] getAccessibleSubSystems(Subject subject) {
		long userId=getUserId(subject);
		return UserAccessHandler.getUserAccessibleSubSystems(userId);
	}

//*****************************************************************************************

	public CmSystem[] getAccessibleSystems(Subject subject) {
		long userId=getUserId(subject);
		return UserAccessHandler.getUserAccessibleSystems(userId);
	}

//*****************************************************************************************

	public Collection<? extends CoVSystemMenu> getAccessibleMenus(Subject subject) {
		long userId=getUserId(subject);
		return UserAccessHandler.getUserAccessibleMenus(userId);
	}
//*****************************************************************************************
	public long getUserId(Subject user){
		if(user==null)return -1;
		Set<Principal>principals= user.getPrincipals();
		long userId=-1;
		for(Principal princ:principals ){
			if(princ instanceof UserIdPrincipal){
				userId=((UserIdPrincipal)princ).getUserId();
			}
		}
		return userId;
	}

//*****************************************************************************************
	public Long[] getUsecaseAvailableActions(Subject user, long usecaseId) {
		long userId=getUserId(user);
		String key=userId+"|"+usecaseId;
		if(!USER_USCASE_CACHE.containsKey(key)){//USER_USCASE_CACHE.clear()
				try {
				JAASSecurityFacadeRemore securityFacade=(JAASSecurityFacadeRemore)ServiceFactory.lookupFacade(JAASSecurityFacadeRemore.class.getName());
				List<CoVUserAllValidAccess>uservalidAccess= securityFacade.loadUserValidAccesses(userId,usecaseId);
				List<Long> result=new ArrayList<Long>();
				for(CoVUserAllValidAccess access:uservalidAccess){
						result.add(access.getCoActionId());
				}
				USER_USCASE_CACHE.put(key, result.toArray(new Long[result.size()]));
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		
		return USER_USCASE_CACHE.get(key);
	}


//*****************************************************************************************
	
	public Locale getLocaleOf(Subject subject) throws NotAuthenticatedException{
//		if(subject==null)throw new NotAuthenticatedException();
		if(subject==null)
				return Locale.US;
		Set<Principal>principals= subject.getPrincipals();
		for(Principal principal :principals){
			if(LocalePricipal.class.isInstance(principal)){
				return ((LocalePricipal)principal).getLocale();
			}
		}
		return null;
	}

//*****************************************************************************************

	public String decrypt(String input) {
		try {
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(ENCRYPTION_KEY);
			return textEncryptor.decrypt(input);
		} catch (Exception e) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.WARNING, "fail to decript text:"+input,e);
				e.printStackTrace();
			}
			return input;
		}
	}
//*****************************************************************************************
	public String encrypt(String input) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(ENCRYPTION_KEY);
		return textEncryptor.encrypt(input);

	}
//*****************************************************************************************
	@Override
	public void notifyMenuUpdated() {
		UserAccessHandler.clearMenuCache();
		
	}
//*****************************************************************************************
	@Override
	public void notifyRoleUpdated(Long roleId) {
		USER_USCASE_CACHE.clear();
	}
//*****************************************************************************************
	@Override
	public void notifyUserUpdated(Long userId) {
		USER_USCASE_CACHE.clear();
	}
//*****************************************************************************************
	@Override
	public Set<Long> getUserRoles(Long userId) {
		try {
			CoUserRolesFacadeRemote bean=(CoUserRolesFacadeRemote)ServiceFactory.lookupFacade(CoUserRolesFacadeRemote.class.getName());
			return  bean.getUserRoles(userId);
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return new HashSet<Long>();
	}
//*****************************************************************************************
	@Override
	public String getFullUserName(Subject user,Map<String,Object>context) {
		if(user==null){
			return "";
		}
		Set<Principal>principals= user.getPrincipals();
		String []result=new String[]{"","",""};
		for(Principal p:principals){
			if(p instanceof FirstnamePrincipal){
				result[1]=p.getName();
			}else if(p instanceof LastnamePrincipal){
				result[2]=p.getName();
			}else if(p instanceof SexPrincipal){
				Sex sex=((SexPrincipal)p).getSex();
				String language;
				if(context!=null){
					language=ContextUtil.getLoginLanguage(context);	
				}else{
					language="en";
				}
				result[0]=Sex.male.equals(sex)?MessageHandler.getMessage("Mr", "ir.utopia.core.constants.Glossory",language ):
					MessageHandler.getMessage("Mrs", "ir.utopia.core.constants.Glossory", language);
			} 
		}
		return result[0]+" "+result[1]+" "+result[2];
	}

//*****************************************************************************************
	@Override
	public void LogAction(InvocationContext invocationContext,boolean success) {
		UserLogHandler.logAction(invocationContext, success);
	}
//*****************************************************************************************	
	@Override
	public void LogAction(String actionName, String usecaseName, boolean success) {
		UserLogHandler.logAction(actionName,usecaseName,success);
	}
//*****************************************************************************************
	@Override
	public Subject loginUser(Long userId) {
		try {
			CoUserFacadeRemote userFacade=(CoUserFacadeRemote)ServiceFactory.lookupFacade(CoUserFacadeRemote.class.getName());
			LoginBeanRemote loginBean=(LoginBeanRemote)ServiceFactory.lookupFacade(LoginBeanRemote.class.getName());
			CoUser user=userFacade.findById(userId);
			if(user!=null)
				if(loginBean.login(user.getUsername(), decrypt(user.getPassword()))){
					return loginBean.getUserProfile();
				}else{
					logger.log(Level.WARNING,"invalid user credential stored in database,fail to login ");
				}
					
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		
	return null;
}
//*****************************************************************************************
	@Override
	public void logUserLogin(String clientAddress,Subject loginUser) {
		Long userLogId=UserLogHandler.createUserLoginLog(clientAddress,loginUser);
		((java.util.Set<UserIdPrincipal>)loginUser.getPrincipals(UserIdPrincipal.class)).iterator().next().setUserLogId(userLogId);
	}
//*****************************************************************************************
	@Override
	public void logUserLogout(Subject loginUser) {
		UserLogHandler.logUserLogout(loginUser);
	}
//*****************************************************************************************
	@Override
	public Long[] getAvialableOrganizations(Subject subject) {
		
		try {
			CoUserOrganizationAccessFacadeRemote bean=(CoUserOrganizationAccessFacadeRemote)ServiceFactory.lookupFacade(CoUserOrganizationAccessFacadeRemote.class);
			Long []availableOrgs= bean.getUserAccessibleOrganizations(getUserId(subject));
			return availableOrgs;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//*****************************************************************************************
	@Override
	public Long getOrganization(Subject subject,Map<String,Object>context) {
		if(ServiceFactory.isSupportOrganization()){
			Long organization=null;
			UserPreferencesInfo userpref= ContextUtil.getUserPreferences(context);
			if(userpref!=null){
				organization=userpref.getCurrentOrganizationId();
			}
			
			if(organization==null){
				Set<Principal>principals= subject.getPrincipals();
				for(Principal principal:principals){
					if(OrganizationPrincipal.class.isInstance( principal)){
						organization= ((OrganizationPrincipal)principal).getOrganizationId();
					}
				}
			}
			return organization;
		}
		return 0l;
	}
//*****************************************************************************************
	@Override
	public void changeOrganization(Subject subject,Long newOrganizationId) throws OrganizationAccessException{
		try {
			Long[] availables= getAvialableOrganizations(subject);
			boolean found=false;
			if(availables!=null&&availables.length>0){
				for(long l:availables){
					if(newOrganizationId.equals(l)){
						found=true;
						break;
					}
				}
				if(!found){
					throw new OrganizationAccessException("You don't have access permission to change your organization to :"+newOrganizationId);
				}
			}else{
				throw new OrganizationAccessException("You don't have access permission to change your organization to :"+newOrganizationId);
			}

			Set<OrganizationPrincipal> organizations =subject.getPrincipals(OrganizationPrincipal.class);
			if(organizations!=null&&organizations.size()>0){
				subject.getPrincipals().removeAll(organizations);
				
			}
			OrganizationPrincipal organizationPrincipal=new OrganizationPrincipal();
        	organizationPrincipal.setOrganizationId(newOrganizationId);
        	subject.getPrincipals().add(organizationPrincipal);
			
		}catch(OrganizationAccessException e1){
			throw e1;
		} 
		catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		
	}
	@Override
	public Set<Long> getUserRoles(Subject user) {
		UserIdPrincipal principal=(UserIdPrincipal) user.getPrincipals(UserIdPrincipal.class).iterator().next();
		return principal.getUserRoles();
	}
	

	
}
