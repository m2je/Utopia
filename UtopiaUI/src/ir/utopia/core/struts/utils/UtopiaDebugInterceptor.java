package ir.utopia.core.struts.utils;

import java.util.Locale;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.principal.LocalePricipal;

import javax.security.auth.Subject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * for setting session attributes in debug mode
 * @author Mehdi
 *
 */
public class UtopiaDebugInterceptor implements Interceptor{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7351463172787071198L;
	Subject subject;
	@Override
	public void destroy() {
		
		
	}

	@Override
	public void init() {
		Object o=System.getProperties().get("UtopiaDebug");
		if(o!=null&&Boolean.valueOf(o.toString())){
			subject=ServiceFactory.getSecurityProvider().loginUser(1l);
			subject.getPrincipals().add(new LocalePricipal(Locale.ENGLISH));
		}
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object o=System.getProperties().get("UtopiaDebug");
		if(o!=null&&Boolean.valueOf(o.toString())){
			ActionContext.getContext().put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME, subject);
		}
		
		return invocation.invoke();
	}

}
