package ir.utopia.core.dispatch;

import ir.utopia.core.form.annotation.ActionMethod;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.ClassUtility;
import ir.utopia.core.util.ClassUtility.BeanInfo;
import ir.utopia.core.util.ClassUtility.BeanMethodInfo;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtopiaAPIDispatcher {
	Logger logger = Logger.getLogger(UtopiaAPIDispatcher.class.getSimpleName());
	private static final String ACTION_CLASS="action_class";
	private Object actionObject;
	private BeanMethodInfo executeMethod; 
	private boolean executeMethodNotFound=false;
	private Class<?>modelClass ;
	private ObjectMapper mapper=new ObjectMapper();
	private static Object [] O=new Object [0];
	private static Cache<Class<?>,BeanInfo>beanInfoCache=new Cache<Class<?>, BeanInfo>();
	public void init(ServletConfig config){
		if(configExists(config,ACTION_CLASS)){
			initActionClass(config.getInitParameter(ACTION_CLASS));
		}
	}
//*****************************************************************************************************************************************	
	public void process(HttpServletRequest request,HttpServletResponse response){
		boolean success=false;
		if(actionObject!=null&&executeMethod!=null&&executeMethod.getMethod()!=null){
			try {
				Class<?>[]paramsClass= executeMethod.getMethod().getParameterTypes();
				Object [] params=O;
				if(paramsClass!=null&&params.length>0){
					params=new Object[paramsClass.length];
					int index=0;
					for(Class<?>paramClass : paramsClass){
						if(HttpServletRequest.class.isAssignableFrom(paramClass)){
							params[index]=request;
						}else if(HttpServletResponse.class.isAssignableFrom(paramClass)){
							params[index]=response;
						}
						index++;
					}
				}
				Object r= executeMethod.getMethod().invoke(actionObject, params);
				if(r!=null){
					response(r, response);
					return ;
				}
				
			} catch (Throwable e) {
				logger.log(Level.WARNING,"",e);
				success=false;
			}
		}else{
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST,"fail to find execute method or action class for request URI :"+request.getRequestURI() );
			}
		}
		response(new ExecutionResult(success),response);
	}
//*****************************************************************************************************************************************
	protected boolean configExists(ServletConfig config,String configName){
		return config.getInitParameter(configName)!=null&&
				config.getInitParameter(configName).trim().length()>0;
	}
//*****************************************************************************************************************************************
	private void response(Object result,HttpServletResponse response){
		try {
			mapper.writeValue(response.getOutputStream(), result);
		} catch (Throwable e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//*****************************************************************************************************************************************
	protected void initActionClass(String actionClass){
		try {
			Class<?> clazz=Class.forName(actionClass);
			if(executeMethod==null&&!executeMethodNotFound){
				findExecuteMethod(clazz);
			}
			if(executeMethod!=null){
				this.actionObject=clazz.newInstance();
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	}
//*****************************************************************************************************************************************
	private void findExecuteMethod(Class<?>clazz){
		BeanInfo info= ClassUtility.getBeanInfo(clazz, false, false, null);
		beanInfoCache.put(clazz, info);
		BeanMethodInfo []methodInfos= info.getMethodInfos();
		BeanMethodInfo candidate=null;
		for(BeanMethodInfo method:methodInfos){
			if(method.getMethod()!=null){
				if("execute".equals(method.getMethod())){
					candidate=method;
				}else {
					if(method.getMethod().getAnnotation(ActionMethod.class)!=null){
						candidate=method;
						break;
					}
				}
			}
		}
		if(candidate!=null){
			executeMethod=candidate;
		}else{
			executeMethodNotFound=false;
		}
	}
//*****************************************************************************************************************************************
	protected  Object processRequest(HttpServletRequest request)throws Exception{
		InputStream is=request.getInputStream();
		try {
			Object model= mapper.readValue(is,modelClass);
		} catch (Exception e) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "fail to process request ",e);
			}
		}
		return null;
	}
//*****************************************************************************************************************************************
}

