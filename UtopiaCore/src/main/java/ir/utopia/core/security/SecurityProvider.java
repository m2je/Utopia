package ir.utopia.core.security;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.exception.OrganizationAccessException;
import ir.utopia.core.factory.BasicCorePlugin;
import ir.utopia.core.security.exception.NonAuthorizedActionException;
import ir.utopia.core.security.exception.NotAuthenticatedException;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.interceptor.InvocationContext;
import javax.security.auth.Subject;

public interface SecurityProvider extends BasicCorePlugin{
	public static final String USER_SESSION_ATTRIBUTE_NAME="user";
	
	public static final String FAIL_TO_LOGIN_MESSAGE_NAME="loginFailMessage";
	
	public static final String FILL_MANADTRY_LOGIN_FIELDS="filluserNameOrPassWord";
	
	public static final String LOGIN_LANGUAGE_PARAMETER_NAME="loginLanguage";
	
	public static final String LOGIN_LOCALE_PARAMETER_NAME="locale";
	
	public static final String REQUEST_USECASE_NAME_PARAMETER_NAME="usecase";
	
	public static final String TOKEN_KEY_NAME="authentication_token";
	
	/**
	 * set attribute value base of elements in side configuration at security configuration
	 * tag
	 * @param attributeName
	 * @param attributeValue
	 */ 
	public void setAttributeFromConfiguration(String attributeName,String attributeValue);
	/**
	 * 
	 * @param attributeName
	 * @return value of attribute 
	 */
	public String getAttributeValue(String attributeName);
	/**
	 * 
	 * @return login URL of securityProvider
	 */
	public String getLoginURL();
	/**
	 * 
	 * @param userId
	 * @param usecaseActionKey
	 * @throws NonAuthorizedActionException
	 */
	public void doPrivilegedAction(InvocationContext invocationContext)throws NonAuthorizedActionException;
	/**
	 * 
	 * @param actionName
	 * @param usecaseName
	 * @param user
	 * @throws NonAuthorizedActionException
	 */
	public void doPrivilegedAction(String actionName,String usecaseName,Subject user)throws NonAuthorizedActionException;
	/**
	 * return a list of accessible System related to user
	 * @param subject
	 * @return
	 */
	public CmSystem[] getAccessibleSystems(Subject subject);
	/**
	 * returns a list of accessible menus and subSytem menus related to user
	 * @param subject
	 * @return
	 */
	public Collection<? extends CoVSystemMenu> getAccessibleMenus(Subject subject);
	/**
	 * returns a list of subSystem available for Subject
	 * @param subject
	 * @return
	 */
	public CmSubsystem[] getAccessibleSubSystems(Subject subject);
	/**
	 * return userId of the subject
	 * @param user
	 * @return
	 */
	public long getUserId(Subject user);
	/**
	 * 
	 * @param user
	 * @param usecaseId
	 * @return
	 */
	public Long[] getUsecaseAvailableActions(Subject user,long usecaseId);
	/**
	 * 
	 * @param subject
	 * @return
	 */
	public Locale getLocaleOf(Subject subject)throws NotAuthenticatedException; 
	/**
	 * 
	 * @param input
	 * @return
	 */
	public String encrypt(String input);
	/**
	 * 
	 * @param input
	 * @return
	 */
	public String decrypt(String input);
	/**
	 * in case of system menu structure changed  
	 */
	public void notifyMenuUpdated();
	/**
	 * 
	 */
	public void notifyUserUpdated(Long userId);
	/**
	 * 
	 */
	public void notifyRoleUpdated(Long roleId);
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Set<Long> getUserRoles(Long userId);
	/**
	 * 
	 * @param user
	 * @return
	 */
	public Set<Long> getUserRoles(Subject user);
	/**
	 * return user name and family or other details of user
	 * @param user
	 * @return
	 */
	public String getFullUserName(Subject user,Map<String,Object>context);
	/**
	 * 
	 * @param invocationContext
	 */
	public void LogAction(InvocationContext invocationContext,boolean success);
	/**
	 * 
	 * @param actionName
	 * @param usecaseName
	 * @param user
	 */
	public void LogAction(String actionName,String usecaseName,boolean success);
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Subject loginUser(Long userId); 
	/**
	 *  
	 * @param loginUser
	 */
	public void logUserLogin(String clientAddress,Subject loginUser);
	/**
	 * 
	 * @param loginUser
	 */
	public void logUserLogout(Subject loginUser);
	/**
	 * 
	 * @param subject
	 * @return
	 */
	public Long[] getAvialableOrganizations(Subject subject);  
	/**
	 * 
	 * @param subject
	 * @return
	 */
	public Long getOrganization(Subject subject,Map<String,Object>context);
	/**
	 * 
	 * @param subject
	 */
	public void changeOrganization(Subject subject,Long newOrganizationId) throws OrganizationAccessException; 
	
	
}
