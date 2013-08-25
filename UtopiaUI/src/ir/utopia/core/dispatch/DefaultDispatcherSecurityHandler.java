package ir.utopia.core.dispatch;

import javax.servlet.http.HttpServletRequest;

public class DefaultDispatcherSecurityHandler implements UtopiaDispatcherSecurityHandler{
	
	public static final String LOGIN_URI="/login";
	public boolean isAuthorized(HttpServletRequest request){
		String token=request.getParameter(TOKEN_KEY_NAME);
		if(token==null||token.length()==0){
			
		}
		
	}
	
	private boolean isLogin(HttpServletRequest request){
		return request.getRequestURI().endsWith(LOGIN_URI);
	}
}
