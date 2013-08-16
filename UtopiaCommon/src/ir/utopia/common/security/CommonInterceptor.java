package ir.utopia.common.security;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class CommonInterceptor {
	
	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext)throws Exception {
		return invocationContext.proceed();
	}
}
