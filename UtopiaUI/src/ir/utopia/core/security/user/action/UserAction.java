package ir.utopia.core.security.user.action;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.security.user.bean.UserUtilityFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.struts.AbstractUtopiaAction;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAction extends AbstractUtopiaAction<UserForm> {
	
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractBasicUsecaseBean.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 8193214439239146562L;

	protected UserForm createModelInstance() {
		return new UserForm();
	}

	@Override
	public String execute() throws Exception {
		if(Constants.predefindedActions.save.toString().equals(actionName)){
			String invokationResult=invokeBean(false);
			return invokationResult;
		}else if(Constants.predefindedActions.update.toString().equals(actionName)){
			String invokationResult=invokeBean(true);
			return invokationResult;
		}else{
			return super.execute();
		}
	}
	
	private CoUser getUser(){
		return (CoUser) form.convertToPersistent();
	}
	
	private String invokeBean(boolean update)throws Exception{
		try {
			
			UserUtilityFacadeRemote bean= (UserUtilityFacadeRemote)ServiceFactory.lookupFacade(UserUtilityFacadeRemote.class.getName()); 
			Map<String,Object>context=createContext();
			if(update){
				bean.updateUser(getUser(),form.getName(),form.getLastName(),null,form.getSex(),null,null);
			}else{
				bean.creatUser(getUser(),form.getName(),form.getLastName(),null,form.getSex(),null,null);
			}
			
			return SUCCESS;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			handle(e);
		}
		return ERROR;
	}
	
}
