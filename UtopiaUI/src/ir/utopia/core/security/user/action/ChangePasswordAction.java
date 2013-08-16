package ir.utopia.core.security.user.action;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.bean.UserUtilityFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.util.tags.process.action.AbstractUtopiaProcessAction;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import com.opensymphony.xwork2.ActionContext;


public class ChangePasswordAction extends AbstractUtopiaProcessAction<UserUtilityFacadeRemote>  implements UtopiaProcessAction  {

	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ChangePasswordAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8453638704974877367L;
	String currentPassword;
	String newPassword;
	Long userId;
	@Override
	protected String getProcessName() {
		return UserUtilityFacadeRemote.UTILITY_PROCESS_CHANGE_PASSWORD;
	}

	@Override
	public ProcessExecutionResult confirm(String[] params, String[] values) {
		ProcessExecutionResult result=super.confirm(params, values);
		if(result.isSuccess()){
			Map<String,Object>session=ActionContext.getContext().getSession();
			try {
				
				CoUserFacadeRemote bean=(CoUserFacadeRemote)ServiceFactory.lookupFacade(CoUserFacadeRemote.class.getName());
				Subject user=(Subject) session.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
				Long userId= ServiceFactory.getSecurityProvider().getUserId(user);
				CoUser coUser= bean.findById(userId);
				String oldPassword=ServiceFactory.getSecurityProvider().decrypt(coUser.getPassword());
				if(!oldPassword.equals(getCurrentPassword())){
					result.setSuccess(false);
					ArrayList<MessageNamePair>newMessages=new ArrayList<MessageNamePair>();
					newMessages.add(new MessageNamePair(MessageType.error,MessageHandler.getMessage("invalidPassword", 
							ChangePasswordAction.class.getName(), getUserLocale().getLanguage())));
					initResultMessages(result, newMessages);
				}else if(getCurrentPassword().equals(getNewPassword())){
					result.setSuccess(false);
					ArrayList<MessageNamePair>newMessages=new ArrayList<MessageNamePair>();
					newMessages.add(new MessageNamePair(MessageType.error,MessageHandler.getMessage("newPasswordIsequalToOldPassword", 
							ChangePasswordAction.class.getName(), getUserLocale().getLanguage())));
					initResultMessages(result, newMessages);
				}
			} catch (Exception e) {
				handelException(result,e);
				logger.log(Level.WARNING,"", e);
			}
			
		}
		return result;
	}
//****************************************************************************************
	public String getCurrentPassword() {
		return currentPassword;
	}
//****************************************************************************************
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
//****************************************************************************************
	public String getNewPassword() {
		return newPassword;
	}
//****************************************************************************************
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
//****************************************************************************************
	public Long getUserId() {
		Subject user=(Subject)ActionContext.getContext().getSession().get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
		return ServiceFactory.getSecurityProvider().getUserId(user);
	}
//****************************************************************************************

	
	
}
