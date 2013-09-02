package ir.utopia.core.util.tags.process.action;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.UtopiaProcessBean;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.process.BeanProcess;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.EventType;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.struts.UtopiaTypeConverter;
import ir.utopia.core.util.exceptionhandler.AbstractExceptionHandler.Message;
import ir.utopia.core.util.exceptionhandler.DefaultExceptionHandler;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;

import com.opensymphony.xwork2.ModelDriven;

public abstract class AbstractUtopiaProcessAction<B extends UtopiaProcessBean> extends UtopiaBasicAction implements UtopiaProcessAction,ProcessListener {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractUtopiaProcessAction.class.getName());
	}
	private static final Object[] O=new Object[0];
	protected static final HashMap<Long, ProcessBeanInfo>beanMap=new HashMap<Long, ProcessBeanInfo>();
	protected static final HashMap<Long, List<MessageNamePair>>messageMap=new HashMap<Long, List<MessageNamePair>>();
	protected static final Map<Long,BeanProcessExcutionResult<?>>processFinishStatusMap=new HashMap<Long, BeanProcessExcutionResult<?>>();
	private static Long CURRENT_PROCESS_ID=1l; 
	protected static final Long MAX_CACHING_BEAN_TIME_MILLISECOUND= (long)(3 * 60 * 60 * 1000);//3 hours
	protected static final Timer timer=new Timer(){
		
	};
	static {
		
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				Long time=System.currentTimeMillis();
				ArrayList<Long>removeItems=new ArrayList<Long>();
				for(Long key: beanMap.keySet()){
					ProcessBeanInfo beanInfo=beanMap.get(key);
					if(beanInfo.getLaunchTime()-time>MAX_CACHING_BEAN_TIME_MILLISECOUND){
						removeItems.add(key);
						logger.log(Level.WARNING,beanInfo.getBean().getClass().getName()+ " removed from beanMapCache by timeout ");
					}
				}
				for(Long r:removeItems){
					beanMap.remove(r);
					messageMap.remove(r);
					processFinishStatusMap.remove(r);
				}
			}
			
		}, MAX_CACHING_BEAN_TIME_MILLISECOUND);
	}
	/**
	 * 
	 */
	Class<B> facadeClass;
	private static final long serialVersionUID = -3513991876494616933L;
	
	private ArrayList<MessageNamePair>messages;
//***************************************************************************************
	protected abstract String getProcessName();
	
	
//***************************************************************************************
	public ProcessExecutionResult execute(String[] params, String[] values) {
		boolean success = true;
		Throwable t=null;
		Long uid;
		int totalStepCount=0;
		boolean processing=false;
		try {
			initParameters(params, values);
			UtopiaProcessBean bean=(UtopiaProcessBean) ServiceFactory.lookupFacade(getFacadeClass().getName());
			BeanProcessParameter[] processParams=getProcessParameters();
			BeanProcess proc=new BeanProcess(getProcessName());
			proc.setParameters(processParams);
			Map<String,Object>context=createContext();
			 totalStepCount= bean.getTotalStepCount(proc,context);
			 uid=getProcessIdentifier();
			
			
			if(!beanMap.containsKey(uid)&&bean!=null){
				
				 ProcessBeanInfo info=new ProcessBeanInfo(uid,System.currentTimeMillis(), bean,proc,context,totalStepCount);
				beanMap.put(uid,info);
				info.addListener(this);
				info.runProcess(getProcessName());
				processing=true;
			}
			
		} catch (Exception e) {
			processing=false;
			logger.log(Level.WARNING,"", e);
			success=false;
			uid=-1l;
			t=e;
		}
		 ProcessExecutionResult result=new ProcessExecutionResult(success,
				 processing?ProcessExecutionResult.PROCESS_STATUS_PROCESSING:ProcessExecutionResult.PROCESS_STATUS_FINISHED);
		 result.setProcessIdentifier(uid);
		 result.setTotalTaskCount(totalStepCount);
		 result.setSuccess(success);
		 result.setCurrentTaskName(MessageHandler.getMessage("initializingProcess", "ir.utopia.core.util.tags.process.action.DefaulProcessConstants",getUserLocale()==null?"fa":getUserLocale().getLanguage() ));
		 initResultMessages(result,t);
		 return result;
	}
//***************************************************************************************
	private void initResultMessages(ProcessExecutionResult result,Throwable e){
		if(messages!=null){
			ArrayList<String>errors=new ArrayList<String>(),warnings=new ArrayList<String>(),infos=new ArrayList<String>();
		 for(MessageNamePair pair:messages){
			 if(MessageType.error.equals(pair.getType())){
				 errors.add(pair.getMessage());
			 }else if(MessageType.warning.equals(pair.getType())){
				 warnings.add(pair.getMessage());
			 }else if(MessageType.info.equals(pair.getType())){
				 infos.add(pair.getMessage());
			 }
		 }
		 if(e!=null){
			 Message m=   DefaultExceptionHandler.getExceptionMessage(e, createContext());
			 errors.add(m.getMessage());
		 }

		 result.setErrorMessages(errors.toArray(new String[errors.size()]));
		 result.setWarningMessages(warnings.toArray(new String[warnings.size()]));
		 result.setInfoMessages(infos.toArray(new String[infos.size()]));
		 messages.clear();
		 }		
	}
//***************************************************************************************
	

	protected BeanProcessParameter[] getProcessParameters(){
		ArrayList<BeanProcessParameter>params=new ArrayList<BeanProcessParameter>();
		Map<String,Object>result;
		if(ModelDriven.class.isInstance(this)){
			result= getFieldsValue(((ModelDriven<?>)this).getModel());
		}else{
			result=getFieldsValue(this);
		}
		for(String key:result.keySet()){
			params.add(new BeanProcessParameter(key, result.get(key)));
		}
		return params.toArray(new BeanProcessParameter[params.size()]);
	} 
//***************************************************************************************
	private Map<String,Object>getFieldsValue(Object o){
		Field[]fields= o.getClass().getDeclaredFields();
		Map<String,Object>result=new HashMap<String, Object>();
		for(Field f:fields){
			String methodName=AnnotationUtil.getGetterMethodName(f.getName(),f.getType());
			logger.log(Level.ALL,"GHJL");
			try {
				Object re= MethodUtils.invokeMethod(o, methodName, O) ;
				result.put(f.getName(), re);
			} catch (Exception e) {
				logger.log(Level.INFO,"no getther method for field --->"+f.getName()+" with name :"+methodName);
			}
		}
		return result;
	}
//***************************************************************************************
	public ProcessExecutionResult updateStatus(Long processUniqueId,Boolean isConfirm) {
		if(isConfirm){
			return updateConfirmExecResult(processUniqueId);
		}else{
			return updateExecResult(processUniqueId);
		}
	
	}
	
//***************************************************************************************
	protected ProcessExecutionResult updateExecResult(Long processUniqueId) {
		
		if(beanMap.containsKey(processUniqueId)){
			ProcessBeanInfo bean=(ProcessBeanInfo)beanMap.get(processUniqueId);
			ProcessExecutionResult result=getBeanResult(bean,processUniqueId);
			if(result.getProcessStatus()==ProcessExecutionResult.PROCESS_STATUS_FINISHED){
				beanMap.remove(processUniqueId);
				messageMap.remove(processUniqueId);
			}
			return result;
		}
		
		if(processFinishStatusMap.containsKey(processUniqueId)){
			BeanProcessExcutionResult<?> beanResult=processFinishStatusMap.get(processUniqueId);
			ProcessExecutionResult exeResult=new ProcessExecutionResult(beanResult.isSuccess(),ProcessExecutionResult.PROCESS_STATUS_FINISHED);
			initResultMessages(exeResult, beanResult.getMessages());
			return exeResult;
		}else{
			return new ProcessExecutionResult(false,ProcessExecutionResult.PROCESS_STATUS_FINISHED);
		}
		
	}
//***************************************************************************************
	@Override
	public void notifyProcessStatusChanged(ProcessStatusChangeEvent e) {
		boolean finished=EventType.processFinished.equals(e.getEventType())||EventType.processFailed.equals(e.getEventType());
		if(finished){
			Long key=e.getKey();
			beanMap.remove(key);
			messageMap.remove(key);
			BeanProcessExcutionResult<?>beanResult=e.getBeanExecutionResult();
			if(beanResult!=null){
				beanResult.setSuccess(beanResult.isSuccess()&&EventType.processFinished.equals(e.getEventType()));
			}else{
				beanResult=new BeanProcessExcutionResult<UtopiaBasicPersistent>();
				beanResult.setSuccess(EventType.processFinished.equals(e.getEventType()));
			}
			
			processFinishStatusMap.put(key, beanResult);
		}
		
	}

//***************************************************************************************
	
	protected ProcessExecutionResult updateConfirmExecResult(Long processUniqueId){
		ProcessExecutionResult result=new ProcessExecutionResult();
		result.setSuccess(true);
		result.setConfirmUser(false);
		result.setProcessIdentifier(processUniqueId);
		result.setProcessStatus(ProcessExecutionResult.PROCESS_STATUS_FINISHED);
		return result;
	}
//***************************************************************************************
	
//***************************************************************************************
//	protected static String getKey(Class<?>clazz, Long processUniqueId){
//		return  clazz.getName()+"|"+processUniqueId;
//	}
//***************************************************************************************
	private ProcessExecutionResult getBeanResult(ProcessBeanInfo bean,Long processUniqueId){
		if(bean==null){
			new ProcessExecutionResult(false,ProcessExecutionResult.PROCESS_STATUS_FINISHED);
		}
		boolean success=bean.isSuccess();
		int step=bean.getStep();
		int status=bean.isFinished()?ProcessExecutionResult.PROCESS_STATUS_FINISHED:ProcessExecutionResult.PROCESS_STATUS_PROCESSING;
		ProcessExecutionResult result=new ProcessExecutionResult(success,status);
		int totalStepCount=bean.getTotalStepCount();
		result.setCurrentTask(step+1>=totalStepCount?totalStepCount:step);
		result.setCurrentTaskName(getTaskName(step, getUserLocale().getLanguage()));
		result.setTotalTaskCount(totalStepCount);
		result.setProcessIdentifier(processUniqueId);
		if(bean.isFinished()){
			if(bean.getException()!=null){
				Map<String,Object>context=createContext();
				DefaultExceptionHandler exHandler=new DefaultExceptionHandler();
				ExceptionResult exResult= exHandler.handel(bean.getException(), context);
				List<MessageNamePair>messages=new ArrayList<MessageNamePair>();
				if(exResult.getMessages()!=null){
					messages.addAll(exResult.getMessages());
				}
				
				if(messageMap.containsKey(processUniqueId)){
					messages.addAll(messageMap.get(processUniqueId));
				}
				initProcessResultMessges(result, messages);
			}
		}
		return result;
	}
//***************************************************************************************
	protected String getTaskName(int step,String language){
		return MessageHandler.getMessage("task"+step, this.getClass().getName(),language );
	}
//***************************************************************************************
	@SuppressWarnings("unchecked")
	public Class<? extends B> getFacadeClass(){
		if(facadeClass==null)
		 this.facadeClass=(Class<B>) (((ParameterizedType)getClass().getGenericSuperclass())).getActualTypeArguments()[0];
		return this.facadeClass;
	}	
//***************************************************************************************
	protected static synchronized Long getProcessIdentifier() {
		return CURRENT_PROCESS_ID++;
	}
//***************************************************************************************
	protected  void initParameters( String params[], String[] values) {
		initParameters(this,params, values,getUserLocale().getLanguage());
	}
//***************************************************************************************
	public static void initParameters(Object invokingObject, String params[], String[] values,String locale) {
		if (ModelDriven.class.isAssignableFrom(invokingObject.getClass())) {
			initParametersValue(((ModelDriven<?>) invokingObject).getModel(), params,
					values,locale);
		} else {
			initParametersValue(invokingObject, params, values,locale);
		}
	}
//***************************************************************************************
	private static  void initParametersValue(Object bean, String params[], String[] values,String locale) {
		if (params != null && values != null) {
			HashMap<String,Object>formatedParams=new HashMap<String, Object>();
			
			int index=0;
			for(String param:params){
				if(param==null||param.trim().length()==0)continue;
				if(param.indexOf("[")>0){
					String temp=param.substring(0,param.indexOf("["));
					if(!formatedParams.containsKey(temp)){
						formatedParams.put(temp,new String[]{values[index]});
					}else{
						String[] val=(String[]) formatedParams.get(temp);
						String [] val2=new String[val.length+1];
						System.arraycopy(val, 0, val2,0 , val.length);
						val2[val.length]=values[index];
						formatedParams.put(temp, val2);
					}
					
				}else{
					formatedParams.put(param, values[index]);
				}
				index++;
			}
			for (String param: formatedParams.keySet()) {
				Object value=formatedParams.get(param);
				try {
					Field field = bean.getClass().getDeclaredField(param);
					if(value instanceof String){
				
						BeanUtils.setProperty(bean, param, UtopiaTypeConverter
								.convertFromString( field.getType(),locale, (String)value));
					}else{
						if(field.getType().isArray()){
							String [] valArr=(String[])value;
							Class<?>type= field.getType().getComponentType();
							Object []objArr=getObjectArray(type, valArr.length);
							for(int i=0;i<objArr.length;i++){
								objArr[i]=UtopiaTypeConverter.convertFromString(type,locale, valArr[i]);
							}
							BeanUtils.setProperty(bean,param,objArr);
						}
						
					}
					
				} catch (Exception e) {
					logger.log(Level.WARNING," fail to set value of parameter--->"
							+ param + " in class --->" + bean.getClass()
							+ " to value --->" + value);
					continue;
				}
			}
		}
	}
//***************************************************************************************
	private static Object[] getObjectArray(Class<?>type,int size){
		if(Boolean.class.equals(type)){
			return new Boolean[size];
		}else if(boolean.class.equals(type)){
			return new Boolean[size];
		} 
		return new Object[size];
	}
//***************************************************************************************
	public void addMessage(MessageNamePair message){
		if(messages==null){
			messages=new ArrayList<MessageNamePair>();
		}
		this.messages.add(message);
	}
//***************************************************************************************
	public void addMessage(Collection<MessageNamePair> messages){
		if(messages!=null){
			for(MessageNamePair pair:messages){
				addMessage(pair);
			}
		}
			
	}
//***************************************************************************************
	public List<MessageNamePair> getMessages(){
		return messages;
	}
//***************************************************************************************
	public void addErrorMessage(String message){
		addMessage(new MessageNamePair(MessageType.error,message));
	}
//***************************************************************************************
	public void addWarningMessage(String message){
		addMessage(new MessageNamePair(MessageType.warning,message));
	}
//***************************************************************************************
	public void addInfoMessage(String message){
		addMessage(new MessageNamePair(MessageType.info,message));
	}
//***************************************************************************************
	@Override
	public ProcessExecutionResult confirm(String[] params, String[] values) {
		initParameters(params, values);
		ProcessExecutionResult result=new ProcessExecutionResult(true,ProcessExecutionResult.PROCESS_STATUS_PROCESSING);
		return result ;
	}
//*********************************************************************************************************
	protected void handelException(ProcessExecutionResult result,Exception e){
		List<MessageNamePair>messages= new DefaultExceptionHandler().handel(e, createContext()).getMessages();
		result.setSuccess(false);
		initResultMessages(result, messages);
	}
//*********************************************************************************************************
	public void initResultMessages(ProcessExecutionResult result,List<MessageNamePair>messages){
		if(messages!=null){
			List<String>errors=new ArrayList<String>();
			List<String>infos=new ArrayList<String>();
			List<String>warning=new ArrayList<String>();
			for(MessageNamePair pair:messages){
				if(MessageNamePair.MessageType.info.equals(pair.getType())){
					infos.add(pair.getMessage());
				}else if(MessageNamePair.MessageType.warning.equals(pair.getType())){
					warning.add(pair.getMessage());
				}else if(MessageNamePair.MessageType.error.equals(pair.getType())){
					errors.add(pair.getMessage());
				}
			}
			
			appendPreviousMessages(infos, result.getInfoMessages());
			appendPreviousMessages(errors, result.getErrorMessages());
			appendPreviousMessages(warning, result.getWarningMessages());
			result.setErrorMessages(errors.toArray(new String[errors.size()]));
			result.setWarningMessages(warning.toArray(new String[warning.size()]));
			result.setInfoMessages(infos.toArray(new String[infos.size()]));
		}
	}
//*********************************************************************************************************
	private void appendPreviousMessages(List<String>messages,String[]previousMessages){
		if(messages!=null&&previousMessages!=null&&previousMessages.length>0){
			for(String p:previousMessages){
				messages.add(p);
			}
		}
	}
	
	
}
