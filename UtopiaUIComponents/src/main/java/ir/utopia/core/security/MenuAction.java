package ir.utopia.core.security;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MenuAction extends ActionSupport {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1941728790537643789L;

	public String execute()throws Exception{
		Object user= ActionContext.getContext().getSession().get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);
		if(user==null)return ERROR;
		return super.execute();
	} 

}
