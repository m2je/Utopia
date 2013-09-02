/**
 * 
 */
package ir.utopia.core.struts;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.security.SaveTokenController;
import ir.utopia.core.security.WindowController;
import ir.utopia.core.security.exception.NotAuthenticatedException;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.ActionParameter;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * @author Salarkia
 *
 */
public abstract class AbstractUtopiaAction<T extends UtopiaBasicForm<?>> extends UtopiaBasicAction implements
	UtopiaControl<T>,ServletRequestAware{
	public static final String RESENT_REQUEST_HANDLE_PAGE_NAME="resentRequest";
	Map<String ,MessageNamePair.MessageType>messagesTypeMap =new  HashMap<String,MessageNamePair.MessageType>();
	private static final Logger logger;	
	
	static {
		logger = Logger.getLogger(AbstractUtopiaAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -765370853994080152L;
	protected HttpServletRequest request;
	protected T form;
	protected UseCase usecase;
	protected String actionName;
	protected String usecaseName;
	protected String subSystemName;
	protected String systemName;
	private static final int[] INTARRAY=new int[0];
	private static final String[] PROPERTYNAME=new String[0];
	protected Constants.predefindedActions predifindedAction=Constants.predefindedActions.noAction;
	protected String currentUri;
	private String resentHandlePage;
	private Object invokationResult;
//*******************************************************************************************
	public String getResentHandlePage() {
		return resentHandlePage;
	}
//*******************************************************************************************		
	public void setResentHandlePage(String resentHandlePage) {
		this.resentHandlePage = resentHandlePage;
	}
//*******************************************************************************************		
	protected abstract T createModelInstance();
//*******************************************************************************************		
	/**
	 * 
	 */
	public AbstractUtopiaAction() {
		
	}
//*******************************************************************************************
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		subject=getSubject();
		this.currentUri=request.getRequestURI();
		
	}
//*******************************************************************************************
	@Override
	public String execute() throws Exception {
		try {
		findClassAndMethod();
		String fullUscaseName=UsecaseUtil.getFullUsecaseName(systemName,subSystemName,usecaseName);
		ActionContext ctx=ActionContext.getContext();
		
			if(subject==null){
				throw new NotAuthenticatedException("user session attriubute not found");
			}
			UtopiaPageForm pageForm=getPageForm();
			if(FORM_SUBMIT_MODE_AJAX!=getSubmitMode()&&(Constants.predefindedActions.save.name().equals(actionName)||Constants.predefindedActions.update.name().equals(actionName))){
				boolean consumed=SaveTokenController.isConsumed(form.getSaveToken(),ctx.getSession(),pageForm.getUsecaseActionId());
				String handlePage=UsecaseUtil.getActionUrl(fullUscaseName, predefindedActions.search.toString());
				if(consumed){
					if(logger.isLoggable(Level.FINE)){
						logger.warning("A resent request found for token :"+form.getSaveToken()+" redirecting to resent hanelpage:"+handlePage);
					}
					setResentHandlePage(handlePage);
					return RESENT_REQUEST_HANDLE_PAGE_NAME;
				}else if(request.getParameterMap().isEmpty()){
					if(logger.isLoggable(Level.FINE)){
						logger.log(Level.WARNING,"a request for save/update with no parameteres found ,  redirecting to resent hanelpage:"+handlePage);
					}
					setResentHandlePage(handlePage);
					return RESENT_REQUEST_HANDLE_PAGE_NAME;
				}
			}
			invokationResult=invokeMethod();
			
			
			if(FORM_SUBMIT_MODE_AJAX==getSubmitMode()){
				initJSONResponse( invokationResult);
				return JSON_RESPONSE_NAME;
			}else{
				if(form.getSaveToken()!=null){
					SaveTokenController.consumeToken(form.getSaveToken(), ctx.getSession(),pageForm.getUsecaseActionId()) ;
					ctx.getSession().remove(form.getSaveToken());
				}
				String res=SUCCESS;
				initDefaulResponse(res,invokationResult);
				return res;
			}
			
			}
		catch (Exception e) {
			logger.log( Level.WARNING,"", e);
			handle(e);
			if(predefindedActions.search.toString().equals(actionName)){
				return SUCCESS;
			}
			return FORM_SUBMIT_MODE_AJAX==getSubmitMode()?JSON_RESPONSE_NAME:ERROR;
		}
	}
//*******************************************************************************************
	@Override
	public boolean hasFieldErrors() {
		return super.hasFieldErrors();
	}
//*******************************************************************************************
	protected void addErrorMessage(String message){
		String messageText=WebUtil.write(message,getUserLocale().getLanguage());
		messagesTypeMap.put(messageText, MessageNamePair.MessageType.error);
	}
//*******************************************************************************************
	protected void addWarningMessage(String message){
		String messageText=WebUtil.write(message,getUserLocale().getLanguage());
		messagesTypeMap.put(messageText, MessageNamePair.MessageType.warning);
	}
//*******************************************************************************************
	protected void addInfoMessage(String message){
		String messageText=WebUtil.write(message,getUserLocale().getLanguage());
		messagesTypeMap.put(messageText, MessageNamePair.MessageType.info);
	}
//*******************************************************************************************
	protected void addMessage(MessageNamePair pair){
		String message=WebUtil.write(pair.getMessage(),getUserLocale().getLanguage());
		messagesTypeMap.put(message, pair.getType());
	}
//*******************************************************************************************
	@Override
	public void addActionError(String anErrorMessage) {
		super.addActionError(anErrorMessage);
		if(anErrorMessage!=null)
			messagesTypeMap.put(anErrorMessage, MessageType.error);
	}
//*******************************************************************************************
	@Override
	public void addActionMessage(String aMessage) {
		super.addActionMessage(aMessage);
		if(aMessage!=null)
			messagesTypeMap.put(aMessage, MessageType.info);
	}
//*******************************************************************************************
	@Override
	public MessageType getMessageType(String message) {
		return messagesTypeMap.containsKey(message)?messagesTypeMap.get(message):MessageType.info;
	}
//*******************************************************************************************
	protected void handle(Exception e){
		ExceptionResult result= getExceptionHandler().handel(e, createContext());
		UtopiaPageForm pageForm=getPageForm();
		if(FORM_SUBMIT_MODE_AJAX==getSubmitMode()){
			form.setExecutionResult(result);
			if(pageForm!=null)
				pageForm.setForm(form);
			initActionMessages(ERROR);
		}else{
			pageForm.setForm(form);
			if(result!=null&&result.getMessages()!=null){
				for(MessageNamePair pair: result.getMessages()){
					addMessage(pair);
				}
			}
			initDefaulResponse(ERROR, result);
		}
		
	}
//*******************************************************************************************
	protected void initDefaulResponse(String response,Object invokationResult){
		UtopiaPageForm pageForm=getPageForm();
		List<UtopiaBasicForm<?>>forms= getExectionResults();
		 if(forms!=null){
			 pageForm.setForms(forms);
		 }
		 if(ERROR.equals(response)||INPUT.equals(response)){
			 if(form!=null&&form.getSaveToken()!=null){
					Map<String,Object>session=ActionContext.getContext().getSession();
					ActionContext.getContext().getValueStack(). set(LAST_UNSUCCESSFULL_TOKEN_KEY,form.getSaveToken());
					session.put(form.getSaveToken(), form);
				}
		 }
		initActionMessages(response);
	}
//*******************************************************************************************
	protected void initJSONResponse(Object invokationResult){
		if(UtopiaPersistent.class.isInstance(invokationResult)){
			convertToForm((UtopiaBasicPersistent)invokationResult,form);
		}
		ExecutionResult execResult=new ExecutionResult();
		ArrayList<MessageNamePair>pairs=new ArrayList<MessageNamePair>();
		for(String message:messagesTypeMap.keySet()){
			pairs.add(new MessageNamePair(messagesTypeMap.get(message),message));	
		}
		addActionSuccessOrErrorMessages(pairs,execResult.isSuccess());
		execResult.setMessages(pairs);
		form.setExecutionResult(execResult);
		UtopiaPageForm pageForm=getPageForm();
		if(pageForm!=null)//TODO not sure that we really need to set page form here or not 
			pageForm.setForm(form);
	}
//*******************************************************************************************
	protected UtopiaPageForm getPageForm(){
		UtopiaPageForm pageForm=(UtopiaPageForm) ActionContext.getContext().getValueStack().findValue(Constants.PAGE_CONFIG_FORM_NAME);
		return pageForm;
	}
//*******************************************************************************************
	@Override
	public void validate() {
		super.validate();
		boolean validated=true;
		try {
			findClassAndMethod();
			if(predefindedActions.save.name().equals(actionName)){
				validateSave();
			}else if(predefindedActions.update.name().equals(actionName)){
				validateUpdate();
			}else if(predefindedActions.delete.name().equals(actionName)){
				validateDelete();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			validated=false;
		}
		Collection<String>error= getActionErrors();
		if(error!=null&&error.size()>0||!validated){
			addActionError(MessageHandler.getMessage("internalApplicationError", "ir.utopia.core.constants.Glossory", getUserLocale().getLanguage()));
			initDefaulResponse(ERROR, null);
		}
	}
//*******************************************************************************************
	protected void validateUpdate(){
		
	} 
//*******************************************************************************************
	protected void validateSave(){
		
	}
//*******************************************************************************************
	protected void validateDelete(){
		
	}
//*******************************************************************************************
	protected void validateMandatoryFields(){
		
	}
//******************************************************************************************
	protected HttpServletRequest getRequest() {
		return request;
	}
//*******************************************************************************************
	private void findClassAndMethod(){
	try {
		Map<Integer, String>map= ActionUtil.parseClassAndMethod(currentUri);
		usecaseName=map.get(ActionUtil.USECASE);
		actionName=map.get(ActionUtil.METHOD);
		systemName=map.get(ActionUtil.SYSTEM);
		subSystemName=map.get(ActionUtil.SUB_SYSTEM);
		usecase= UsecaseUtil.getUseCase(systemName, subSystemName, usecaseName);
		predifindedAction=predefindedActions.getAction(actionName);
	} catch (Exception e) {
		e.printStackTrace();
		logger.log(Level.ALL,"error",  e);
	}
	}
//*******************************************************************************************
	public List<T>convertFormToPersistent(Collection<T> form){
		return null;
	}
//*******************************************************************************************	
	public UtopiaBasicPersistent convertFormToPersistent(T form){
		if(form==null)return null;
		return form.convertToPersistent();
	}
//*******************************************************************************************	
	@SuppressWarnings("unchecked")
	public List<UtopiaBasicForm<?>>getExectionResults(){
		List<UtopiaBasicForm<?>>result=null;
		if(invokationResult!=null){
			result=new ArrayList<UtopiaBasicForm<?>>();
			if(Collection.class.isInstance(invokationResult)) {
				for(UtopiaBasicPersistent pers:(Collection<UtopiaBasicPersistent>)invokationResult){
					UtopiaBasicForm<?> form=createModelInstance();
					convertToForm(pers,form);
					result.add(form);
				}
			}else if(UtopiaPersistent.class.isInstance(invokationResult)){
				result.add(convertPersistentToForm((UtopiaBasicPersistent)invokationResult));
			}else if(UtopiaBasicForm.class.isInstance(invokationResult)){
				result.add((UtopiaBasicForm<?>)invokationResult);
			} 
		}
		
		return result;
	}
//*******************************************************************************************	
	public UtopiaBasicForm<?> convertPersistentToForm(UtopiaBasicPersistent persistence){
		UtopiaBasicForm<?> form=this.form==null?createModelInstance():this.form;
		convertToForm(persistence, form);
		return form;
	}
//*******************************************************************************************
protected void convertToForm(UtopiaBasicPersistent persistent,UtopiaBasicForm<?> form){
	form.setLocale(getUserLocale().getLanguage());
	form.importPersistent(persistent);
}
//*******************************************************************************************
	public T getModel() {
		if(form==null){
			form=createModelInstance();
		}
		if(subject!=null){
			form.setLocale(getUserLocale().getLanguage());
		}
		form.setWindowNo(WindowController.getNextWindowNo(ActionContext.getContext().getSession()));
		return form;
	}
//*******************************************************************************************
	protected Object invokeMethod() throws Exception{
//		if(predefindedActions.save.toString().equals(actionName)||
//				predefindedActions.delete.toString().equals(actionName)||
//				predefindedActions.update.toString().equals(actionName)
//				){
//			UtopiaBean bean=findBean();
//			List<UsecaseAction> actions= usecase.getUseCaseActions();
//			for(UsecaseAction action:actions){
//				if(actionName.equals(action.getMethodName())){
//					List<ActionParameter>parameters= action.getParameters();
//					Method method= bean.getClass().getMethod(actionName, extractParmeterClass(parameters));
//					return method.invoke(bean, extractValues(parameters));
//				}
//			}
//			throw new IllegalArgumentException("Method "+actionName+" not found in class "+usecase.getRemoteClassName());
//		
//		}
		List<UsecaseAction> actions= usecase.getUseCaseActions();
		for(UsecaseAction action:actions){
			if(actionName.equals(action.getActionName())
					&&!predefindedActions.search.name().equals(action.getActionName())){//for search methods we don't need to run this method,because
																						//the data in being loaded from UIHandler class
				List<ActionParameter>parameters= action.getParameters();
				UtopiaBean bean=findBean();
				Method method= bean.getClass().getMethod(action.getMethodName(), extractParmeterClass(parameters));
				return method.invoke(bean, extractValues(parameters));
			}
		}
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.WARNING,"fail to find and invoke method related to action :"+actionName);
		}
		return null;
	}
//*******************************************************************************************
	protected UtopiaBean findBean()throws Exception{
		UtopiaBean bean=(UtopiaBean) ServiceFactory.lookupFacade(usecase.getRemoteClassName());
		return bean;
	}
//*******************************************************************************************
	/**
	 * 
	 * @param parameters
	 * @return
	 */
	protected Class<?>[] extractParmeterClass(List<ActionParameter>parameters){
		ArrayList<Class<?>>result=new ArrayList<Class<?>>();
		if(parameters!=null){
		for(ActionParameter parameter:parameters){
			if(ActionParameterTypes.PagingArray.equals(parameter.getType())){
				result.add(INTARRAY.getClass());
			}
			else if(ActionParameterTypes.PropertyArray.equals(parameter.getType())){
				result.add(PROPERTYNAME.getClass());
			}
			else if(ActionParameterTypes.PropertyValueArray.equals(parameter.getType())){
				result.add(VALUE.getClass());
			}
			else if(ActionParameterTypes.PesistentObject.equals(parameter.getType())){
				result.add(UtopiaBasicPersistent.class);
			}
			else if(ActionParameterTypes.Context.equals(parameter.getType())){
				result.add(Map.class);
			}
		}
		}
		return result.toArray(new Class<?>[result.size()]);
	}
//*******************************************************************************************		
	protected Object[] extractValues(List<ActionParameter>parameters){
		ArrayList<Object>result=new ArrayList<Object>();
		for(ActionParameter parameter:parameters){
			if(ActionParameterTypes.PagingArray.equals(parameter.getType())){
				result.add(new int[]{0,50000});
			}
			else if(ActionParameterTypes.PropertyArray.equals(parameter.getType())){
				result.add(new String[]{});
			}
			else if(ActionParameterTypes.PropertyValueArray.equals(parameter.getType())){
				result.add(new String[]{});
			}
			else if(ActionParameterTypes.PesistentObject.equals(parameter.getType())){
				result.add(convertFormToPersistent(form));
			}else if(ActionParameterTypes.Context.equals(parameter.getType())){
				result.add(createContext());
			}
		}
		return result.toArray(new Object[result.size()]);
	}

//*******************************************************************************************
	public int getSubmitMode(){
		try {
			return Integer.parseInt(request.getParameter("submitMode__").trim());
		} catch (Exception e) {
			return 0;
		}
	}
//**************************************************************************************************************************		
		protected void initActionMessages(String executeResult){
			Map<String,Object>session= ActionContext.getContext().getSession();
			@SuppressWarnings("unchecked")
			List<MessageNamePair>messages=session.containsKey(ACTION_MESSAGES_SESSION_KEY)?(List<MessageNamePair>)session.get(ACTION_MESSAGES_SESSION_KEY): new ArrayList<MessageNamePair>();
			
			
			boolean success=false;
			success=ActionSupport.SUCCESS.equals(executeResult);
			addActionSuccessOrErrorMessages(messages, success);
			Collection<String>actionMessages=null;
			Collection<String>errormessages=null;
			if(getActionMessages()!=null){
				actionMessages=getActionMessages();
			}
			if(getActionErrors()!=null){
				errormessages=getActionErrors();
			}
			
			for(String actionMessage:actionMessages){
				messages.add(new MessageNamePair(getMessageType(actionMessage),actionMessage));
			}
			if(errormessages!=null)
				for(String actionMessage:errormessages){
					messages.add(new MessageNamePair(MessageType.error,actionMessage));
				}
			if(messagesTypeMap!=null){
				for(String message:messagesTypeMap.keySet()){
					messages.add(new MessageNamePair(messagesTypeMap.get(message),message));	
				}
			}
			session.put(ACTION_MESSAGES_SESSION_KEY, messages);
		}
//**************************************************************************************************************************
		protected void addActionSuccessOrErrorMessages(List<MessageNamePair>messages,boolean success){
			String language=WebUtil.getLanguage(ActionContext.getContext().getSession());
			if(success){
				if(Constants.predefindedActions.save.toString().equals(actionName)){
					messages.add(new MessageNamePair(MessageType.info,
							MessageHandler.getMessage("recordSaved", "ir.utopia.core.struts.DefaultActionMessages", language)));
				}else if(Constants.predefindedActions.delete.toString().equals(actionName)){
					messages.add(new MessageNamePair(MessageType.info,
							MessageHandler.getMessage("recordDeleted", "ir.utopia.core.struts.DefaultActionMessages", language)));
				}else if(Constants.predefindedActions.update.toString().equals(actionName)){
					messages.add(new MessageNamePair(MessageType.info,
							MessageHandler.getMessage("recordUpdated", "ir.utopia.core.struts.DefaultActionMessages", language)));
				}else if(Constants.predefindedActions.importData.toString().equals(actionName)){
					messages.add(new MessageNamePair(MessageType.info,
							MessageHandler.getMessage("importSuccess", "ir.utopia.core.struts.DefaultActionMessages", language)));
				}
				
			}else{
				messages.add(new MessageNamePair(MessageType.error,
						MessageHandler.getMessage("operationFailed", "ir.utopia.core.struts.DefaultActionMessages", language)));
			}
		}
}
