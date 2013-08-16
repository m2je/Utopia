package ir.utopia.core.security;


import ir.utopia.common.doctype.util.DocTypeUtil;
import ir.utopia.common.doctype.util.UsecaseDocInfo;
import ir.utopia.core.Context;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.UsecaseUtil.ClassMethod;
import ir.utopia.core.usecase.actionmodel.UseCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class AuthorizationInterceptor {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AuthorizationInterceptor.class.getName());
	}
	private static final List<ClassMethod>EXCLUDE_METHODS=new ArrayList<ClassMethod>();
	private static final Map<String,UsecaseDocInfo>USECASE_WS_STATUS=new HashMap<String, UsecaseDocInfo>();
	private static final SecurityProvider securityProvider=ServiceFactory.getSecurityProvider();
	static {
		try {
			EXCLUDE_METHODS.addAll(UsecaseUtil.getExcludeUsecaseMethods());
		} catch (Exception e) {
			logger.log(Level.ALL,"fail to load method class methods", e);
		}
	}
//**********************************************************************************************************	
	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext)throws Exception {
		
		Method method=invocationContext.getMethod();
		Object target=invocationContext.getTarget();
		
		Class<?>clazz= target.getClass();
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.FINE,"Entering method---> "+ method.getName()+" from class --> "+clazz.getName() );
		}
		boolean shouldIntercept=UtopiaBean.class.isAssignableFrom(clazz)
				&&!isExcludedMethod(clazz,method);
		if(shouldIntercept){
			
			boolean success=true;
			if(securityProvider==null) return invocationContext.proceed();
			try {
				securityProvider.doPrivilegedAction(invocationContext);
			} catch (Exception e) {
				success=false;
			}
				securityProvider.LogAction(invocationContext, success);	
			}	
		Object result=null;
		try {
			if(shouldIntercept&&ServiceFactory.isSupportFiscalYear()&&isWorkflowAction(invocationContext))
				UtopiaWorkFlowListener.beforeInvoke(invocationContext);
			result=invocationContext.proceed();
			if(shouldIntercept&&ServiceFactory.isSupportFiscalYear()&&isWorkflowAction(invocationContext))
				UtopiaWorkFlowListener.afterInvoke(invocationContext);
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}
//**********************************************************************************************************
	private boolean isExcludedMethod(Class<?> clazz, Method method){
		if( EXCLUDE_METHODS.contains(method))return true;
		for(ClassMethod cMethod :EXCLUDE_METHODS){
			if(cMethod.getClazz().isAssignableFrom(clazz)&&
					cMethod.getMethod().getName().equals(method.getName())){
				Class<?>[]clazz1=cMethod.getMethod().getParameterTypes();
				Class<?>[]clazz2=method.getParameterTypes();
				if(clazz1.length==clazz2.length){
					for(int i=0;i<clazz1.length;i++){
						if(!clazz1[i].equals(clazz2[i])){
							return false;
						}
					}
					return true;
				}
				
			}
		}
		return false;	
	}
//********************************************************************************************************
		protected static boolean isWorkflowAction(InvocationContext invocationContext){
			Object facade= invocationContext.getTarget();
			Class<?>[] interfaces= facade.getClass().getInterfaces();
			
			if(interfaces!=null){//EXCLUDE_METHODS.contains(invocationContext.getMethod())
				UsecaseDocInfo info=null;
				Long fiscalYearId;
				Context context=ContextHolder.getContext();
				if(context!=null){
					Map<String,Object>contextMap=context.getContextMap();
					fiscalYearId = ContextUtil.getCurrentFiscalYear(contextMap);
					String key=fiscalYearId+"|"+facade.getClass().getName();
					if(!USECASE_WS_STATUS.containsKey(key)){//USECASE_WS_STATUS.clear()
					try {
						for(Class<?>interfaze:interfaces){
							UseCase usecase= UsecaseUtil.getUseCase(interfaze.getName());
							if(usecase!=null){
								info=DocTypeUtil.getUsecaseDocStatusInfo(usecase.getUsecaseId(), fiscalYearId);
							}
						}
					} catch (Exception e) {
						logger.log(Level.WARNING,"",e);
						e.printStackTrace();
					}
					USECASE_WS_STATUS.put(key, info);
					}
					return USECASE_WS_STATUS.get(key)!=null;
				}
			}
			return false;
		}
//**********************************************************************************************************		
}
