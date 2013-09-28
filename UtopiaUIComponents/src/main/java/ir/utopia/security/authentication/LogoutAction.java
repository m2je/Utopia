package ir.utopia.security.authentication;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.security.SecurityProvider;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

public class LogoutAction  {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(LogoutAction.class.getName());
	}
	protected HttpServletRequest request;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3236651533300745728L;

	
	public String execute()throws Exception{
		try {
			Map<String,Object> session= ContextUtil.getContext();
			 Object user= session.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
			 ServiceFactory.getSecurityProvider().logUserLogout((Subject)user);
			if(user instanceof Subject){
				 long userId=ServiceFactory.getSecurityProvider().getUserId((Subject)user);
				 logger.log(Level.INFO,"user with id:"+userId+" exit from system at "+new Date(System.currentTimeMillis()));
			}
			session.remove(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
			request.getSession().invalidate();
		} catch (RuntimeException e) {
			logger.log(Level.WARNING,"fail to logout", e);
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	
}
