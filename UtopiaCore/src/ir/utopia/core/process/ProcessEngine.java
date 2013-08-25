package ir.utopia.core.process;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.log.bean.CoActionLogFacadeRemote;
import ir.utopia.core.log.persistent.ActionLogMessageType;
import ir.utopia.core.log.persistent.ActionLogStatus;
import ir.utopia.core.log.persistent.ActionLogger;
import ir.utopia.core.log.persistent.CoActionLog;
import ir.utopia.core.log.persistent.CoActionLogDtl;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.MethodUtils;

public class ProcessEngine {
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ProcessEngine.class.getName());
	}
	public static ExecutionResult executeProcess(Long usecaseActionId,Map<String,Object>paramerteValues,Map<String,Object>context){
		logger.log(Level.INFO,"system is about to execute usecase action :"+usecaseActionId);
		
		ExecutionResult result=new ExecutionResult();
		try {
			String loginLanguage=ContextUtil.getLoginLanguage(context);
			CoActionLog log=new CoActionLog();
			List<LookupInfo> infos=new ArrayList<LookupInfo>();
			infos.add(new LookupInfo(CoUsecaseAction.class, usecaseActionId));
			log.setLookupInfos(infos);
			log.setLogger(ActionLogger.ProcessEngine);
			log.setStatus(ActionLogStatus.successful);
			log.setStartTime(new Date());
			try {
				UseCase usecase= UsecaseUtil.getUsecaseFromUsecaseAtion(usecaseActionId);
				List<UsecaseAction>actions= usecase.getUseCaseActions();
				String methodName=null;
				for(UsecaseAction action:actions){
					if(action.getUsecaseActionId()==usecaseActionId){
						methodName=action.getMethodName();
					}
				}
				if(methodName==null){
					logger.log(Level.WARNING,"fail to lookup method name for usecase action :"+usecaseActionId);
					result.setSuccess(false);
					result.addMessage(MessageHandler.getMessage("InternalProcessInvokationError", "ir.utopia.core.process.ProcessEngine", loginLanguage), MessageType.error);
				}else{
					UtopiaBean bean=(UtopiaBean) ServiceFactory.lookupFacade(usecase.getRemoteClassName());
					BeanProcessParameter []params=extractProcessParametres(paramerteValues);
					Object invokationResult;
					try {
						invokationResult=MethodUtils.invokeMethod(bean, methodName,new Object[]{params,context} );
					} catch (NoSuchMethodException e) {
						logger.log(Level.WARNING,"Method ["+methodName+"] with paramters : processParameters and context was not found in class:"+usecase.getRemoteClassName()+",invoking with just context",e);
						if(paramerteValues!=null)
							context.putAll(paramerteValues);
						try {
							invokationResult=MethodUtils.invokeMethod(bean, methodName,new Object[]{context} );
						} catch (NoSuchMethodException e2) {
							logger.log(Level.WARNING,"Method ["+methodName+"] with paramters : context was not found in class:"+usecase.getRemoteClassName()+",invoking with no arguments",e);
							invokationResult=MethodUtils.invokeMethod(bean, methodName,new Object[]{} );
						}
						
					}
					if(ExecutionResult.class.isInstance(invokationResult) ){
						result.addMessages(((ExecutionResult)invokationResult).getMessages());
						result.setSuccess(((ExecutionResult)invokationResult).isSuccess());
					}else{
						result.setSuccess(true);
					}
				}
				
			} catch (Exception e) {
				logger.log(Level.INFO,"fail to invoke method for usecaseAction:"+usecaseActionId,e);
				result.setSuccess(false);
				result.addMessage(MessageHandler.getMessage("InternalProcessInvokationError", "ir.utopia.core.process.ProcessEngine", loginLanguage), MessageType.error);
			}
			if(result.isSuccess()){
				List<MessageNamePair>pairs= result.getMessages();
				if(pairs!=null&&pairs.size()>0){
					HashSet<CoActionLogDtl>dtls=new HashSet<CoActionLogDtl>();
					for(MessageNamePair pair:pairs){
						if(MessageType.warning.equals(pair.getType())||
								MessageType.error.equals(pair.getType())){
							log.setStatus(ActionLogStatus.successfulWithWarninig);
						}
						CoActionLogDtl dtl=new CoActionLogDtl();
						dtl.setCoActionLog(log);
						dtl.setReferenceKey1(pair.getReferenceKey1());
						dtl.setReferenceKey2(pair.getReferenceKey2());
						dtl.setReferenceKey3(pair.getReferenceKey3());
						dtl.setMessage(pair.getMessage());
						dtl.setExtendedType(pair.getErrorCode());
						dtl.setType(ActionLogMessageType.convert(pair.getType()));
						dtls.add(dtl);
					}
					log.setCoActionLogDtls(dtls);
				}
			}else{
				log.setStatus(ActionLogStatus.failed);
			}
			log.setEndTime(new Date());
			CoActionLogFacadeRemote remote=(CoActionLogFacadeRemote)ServiceFactory.lookupFacade(CoActionLogFacadeRemote.class);
			remote.save(log);
		} catch (Exception e) {
			logger.log(Level.WARNING,"fail to invoke method for usecaseAction:"+usecaseActionId,e);
			result.setSuccess(false);
		}
		logger.log(Level.INFO,"usecase action :"+usecaseActionId+"'s execution finshed");
		return result;
	}
//*****************************************************************************************************
	public static BeanProcessParameter[] extractProcessParametres(Map<String,Object>paramerteValues){
		if(paramerteValues==null){
			return new BeanProcessParameter[0];
		}
		BeanProcessParameter[] result=new BeanProcessParameter[paramerteValues.size()];
		int index=0;
		for(String key:paramerteValues.keySet()){
			result[index]=new BeanProcessParameter(key, paramerteValues.get(key));
		}
		return result;
	}
}
