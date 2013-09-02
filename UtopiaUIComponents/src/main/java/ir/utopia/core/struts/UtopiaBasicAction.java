package ir.utopia.core.struts;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.form.annotation.ActionConfig;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.exception.NotAuthenticatedException;
import ir.utopia.core.util.exceptionhandler.DefaultExceptionHandler;
import ir.utopia.core.util.exceptionhandler.ExceptionHandler;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import org.apache.commons.beanutils.ConstructorUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UtopiaBasicAction extends ActionSupport {
	private static final boolean IsDebugging=Boolean.getBoolean("GwtDebug");
	private static final Logger logger;	
	public static final String ACTION_MESSAGES_SESSION_KEY="__actionMessages";
	public static final String LAST_UNSUCCESSFULL_TOKEN_KEY="__lastFailedToken";
	static {
		logger = Logger.getLogger(UtopiaBasicAction.class.getName());
	}
	protected static final Object []VALUE=new Object[0];
	protected Subject subject;
	protected Locale locale;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2424899757196990259L;
	protected Subject getSubject(){
		if(subject==null){
			Map<String,Object> session= ActionContext.getContext().getSession();
			 subject= (Subject)session.get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME);	
		}
		 return subject;
	} 
	protected Locale getUserLocale(){
		if(locale==null){
				try {
					Subject subject =getSubject();
					locale=ServiceFactory.getSecurityProvider().getLocaleOf(subject);
				} catch (NotAuthenticatedException e) {
					logger.log( Level.WARNING,"could not find locale of subject :"+subject );
				}
		}
		return locale;
	}
	
	@SuppressWarnings("unchecked")
	protected ExceptionHandler getExceptionHandler(){
		ActionConfig actionConfig= getClass().getAnnotation(ActionConfig.class);
		if(actionConfig==null)return new DefaultExceptionHandler();
		 Class<ExceptionHandler>handlerClass=(Class<ExceptionHandler>) actionConfig.exceptionHandler();
		 try {
			return (ExceptionHandler)ConstructorUtils.invokeConstructor(handlerClass, VALUE);
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			return new DefaultExceptionHandler();
		}
	}
	
	protected Map<String,Object> createContext(){
		Map<String,Object>context;
		if(ContextHolder.getContext()!=null){
			context=ContextHolder.getContext().getContextMap();
		}else{
			if(IsDebugging){
				context=ContextUtil.createAdminContext();
			}else{
				
				context=ContextUtil.createContext(ActionContext.getContext().getSession());
				
			}
		}
		return context;
	}
	
	protected void initProcessResultMessges(ProcessExecutionResult result,List<MessageNamePair>messages){
		if(messages!=null){
			List<String>errors=new ArrayList<String>();
			List<String>warnings=new ArrayList<String>();
			List<String>infos=new ArrayList<String>();
			for(MessageNamePair pair:messages){
				if(MessageType.error.equals(pair.getType())){
					errors.add(pair.getMessage());
				}else if(MessageType.warning.equals(pair.getType())){
					warnings.add(pair.getMessage());
				}else if(MessageType.info.equals(pair.getType())){
					infos.add(pair.getMessage());
				}
			}
			result.setErrorMessages(errors.toArray(new String[errors.size()]));
			result.setWarningMessages(warnings.toArray(new String[warnings.size()]));
			result.setInfoMessages(infos.toArray(new String[infos.size()]));			
		}
		
	}
}
