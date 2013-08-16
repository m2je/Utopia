package ir.utopia.core.security.user.action;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UserPreferencesInfo;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.bean.UserUtilityFacadeRemote;
import ir.utopia.core.security.userpreferences.bean.CoUserPreferencesFacadeRemote;
import ir.utopia.core.util.tags.process.action.AbstractUtopiaProcessAction;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import com.opensymphony.xwork2.ActionContext;

public class ChangeOrganizationAction extends AbstractUtopiaProcessAction<UserUtilityFacadeRemote> implements UtopiaProcessAction  {

	/**
	 * 
	 */
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ChangeOrganizationAction.class.getName());
	}
	private static final long serialVersionUID = -3524041071216178104L;
	Long organizationId;
	boolean setAsDefaultOrganization;
	boolean showAllOrganization;
	public boolean isSetAsDefaultOrganization() {
		return setAsDefaultOrganization;
	}
	public void setSetAsDefaultOrganization(boolean setAsDefaultOrganization) {
		this.setAsDefaultOrganization = setAsDefaultOrganization;
	}
	@Override
	protected String getProcessName() {
		
		return CoUserFacadeRemote.CHANGE_ORGANIZATION_METHODNAME;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	public boolean isShowAllOrganization() {
		return showAllOrganization;
	}
	public void setShowAllOrganization(boolean showAllOrganization) {
		this.showAllOrganization = showAllOrganization;
	}
//***************************************************************************	
	public ProcessExecutionResult execute(String[] params, String[] values) {
		ProcessExecutionResult result= super.execute(params, values);
		try {
			Map<String,Object> session= ActionContext.getContext().getSession();
			Map<String,Object>context=ContextUtil.createContext(session);
			Subject user=ContextUtil.getUser(context);
			UserPreferencesInfo userPreferences=(UserPreferencesInfo)session.get(ContextUtil.USER_PREFERENCES_PARAMETER);
		    userPreferences.setShowAllAccessibleOrganizations(showAllOrganization);
		    session.put(ContextUtil.USER_PREFERENCES_PARAMETER, userPreferences);
		    
		    if(organizationId!=null&&organizationId.longValue()>0){
		    	if(setAsDefaultOrganization)
		    		ServiceFactory.getSecurityProvider().changeOrganization(user, organizationId);
		    	userPreferences.setCurrentOrganizationId(organizationId);
		    }
		    CoUserPreferencesFacadeRemote pref=(CoUserPreferencesFacadeRemote)ServiceFactory.lookupFacade(CoUserPreferencesFacadeRemote.class);
		    pref.savePreferences(ContextUtil.getUserId(context), userPreferences.exportPreferences()); 	
		    session.put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME,user);
			
			 
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		 return result;
	}
//***************************************************************************
//	protected BeanProcessParameter[] getProcessParameters(){
//		BeanProcessParameter[] processParams=super.getProcessParameters();
//		Map<String,Object> session= ActionContext.getContext().getSession();
//		Map<String,Object>context=ContextUtil.createContext(session);
//		if(ContextUtil.isShowAllOrganizationData(context)){
//			for(BeanProcessParameter param:processParams){
//				if(param)
//				
//			}
//		}
//		
//		return processParams;
//	}
//***************************************************************************
}
