package ir.utopia.core.struts.utils;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.security.principal.LocalePricipal;

import java.util.Locale;

import javax.security.auth.Subject;

/**
 * for setting session attributes in debug mode
 * @author Mehdi
 *
 */
public class UtopiaDebugInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7351463172787071198L;
	Subject subject;
	
	public void init() {
		Object o=System.getProperties().get("UtopiaDebug");
		if(o!=null&&Boolean.valueOf(o.toString())){
			subject=ServiceFactory.getSecurityProvider().loginUser(1l);
			subject.getPrincipals().add(new LocalePricipal(Locale.ENGLISH));
		}
		
	}

//	@Override
//	public String intercept(ActionInvocation invocation) throws Exception {
//		Object o=System.getProperties().get("UtopiaDebug");
//		if(o!=null&&Boolean.valueOf(o.toString())){
//			ActionContext.getContext().put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME, subject);
//		}
//		
//		return invocation.invoke();
//	}

}
