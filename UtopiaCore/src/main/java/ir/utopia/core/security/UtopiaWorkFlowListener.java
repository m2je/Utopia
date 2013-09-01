package ir.utopia.core.security;

import ir.utopia.common.dashboard.bean.CmTransitionFacadeRemote;
import ir.utopia.common.dashboard.persistent.CmTransition;
import ir.utopia.common.doctype.util.DocTypeUtil;
import ir.utopia.common.doctype.util.UsecaseDocInfo;
import ir.utopia.common.doctype.util.UsecaseDocStatusInfo;
import ir.utopia.core.Context;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.exception.LockRecordException;
import ir.utopia.core.persistent.DataUpdateVerifier;
import ir.utopia.core.persistent.MonitoredColumnInfo;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.security.exception.NonAuthorizedActionException;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.usecase.actionmodel.UseCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.InvocationContext;
import javax.security.auth.Subject;


public class UtopiaWorkFlowListener {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UtopiaWorkFlowListener.class.getName());
	}
	
//********************************************************************************************************
	public static void beforeInvoke(InvocationContext invocationContext){
		final UsecaseDocInfo docInfo=findUpdateDoctypeInfo();
		final UtopiaPersistent updatingObject=findUpdatingObject(invocationContext);
		if(docInfo!=null){
			final Context context= ContextHolder.getContext();
			final Map<String,Object>contextMap= context.getContextMap();
			
			if(context!=null){
					DataUpdateVerifier verifier=new DataUpdateVerifier() {
					@Override
					public void verify(Object oldValue, Object newValue) throws Exception {
						
						processDocStatusChange(docInfo, context, oldValue, newValue, updatingObject, true);
					}
				};
				MonitoredColumnInfo monitor=new MonitoredColumnInfo();
				monitor.setColumn(docInfo.getColumnName());
				monitor.setVerifier(verifier);
				ArrayList<MonitoredColumnInfo>monitorColumns=new ArrayList<MonitoredColumnInfo>();
				monitorColumns.add(monitor);
				contextMap.put(MonitoredColumnInfo.MONITORED_COLUMNS_CONTEXT_KEYS,monitorColumns);
			}
		}else{
			if(logger.isLoggable(Level.FINE)){
				logger.log(Level.FINE, "fail to find context for "+invocationContext.getTarget().getClass().getName() 
						+" method "+invocationContext.getMethod().getName()+" in workflow listener");
			}
		}
		
	}
//********************************************************************************************************	
	@SuppressWarnings("unchecked")
	public static void afterInvoke(InvocationContext invocationContext){
		try {
			UsecaseDocInfo docInfo=findUpdateDoctypeInfo();
			UtopiaPersistent updatingObject=findUpdatingObject(invocationContext);
			if(docInfo!=null){
				Context context= ContextHolder.getContext();
				Map<String,Object>contextMap= context.getContextMap();
				if(contextMap!=null&&contextMap.containsKey(MonitoredColumnInfo.MONITORED_COLUMNS_CONTEXT_KEYS)){
					List<MonitoredColumnInfo>monitorColumns=(List<MonitoredColumnInfo>) contextMap.get(MonitoredColumnInfo.MONITORED_COLUMNS_CONTEXT_KEYS);
					if(monitorColumns!=null){
						MonitoredColumnInfo monitorColumn= monitorColumns.get(0);
						Object oldValue= monitorColumn.getOldValue();
						Object newValue=monitorColumn.getNewValue();
						processDocStatusChange(docInfo, context, oldValue, newValue,updatingObject,false);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		}
	}
//********************************************************************************************************	
private static void processDocStatusChange(UsecaseDocInfo docInfo,
		Context context, Object oldValue, Object newValue,UtopiaPersistent updatingObject,boolean securityCheck)
		throws Exception {
	if(oldValue!=null&&newValue!=null&&!oldValue.equals(newValue)&&Enum.class.isInstance(oldValue)&&Enum.class.isInstance(newValue)){
		CmTransitionFacadeRemote transitionFacade=(CmTransitionFacadeRemote)ServiceFactory.lookupFacade(CmTransitionFacadeRemote.class);
		Long actionId= context.getActionId();
		Long docStatusFromId=null;
		Long docStatusToId=null;
		CmTransition transition=null;
		Subject currentUser= ContextUtil.getUser(context.getContextMap());
		if(actionId!=null){
			if(predefindedActions.update.ordinal()==actionId.intValue()){
				List<UsecaseDocStatusInfo>docStatuses=  docInfo.getStatuses();
				if(docStatuses!=null){
					for(UsecaseDocStatusInfo info:docStatuses){
						if(info.getStatus()==((Enum<?>)oldValue).ordinal()){
							docStatusFromId=info.getStatusId();
						}else if(info.getStatus()==((Enum<?>)newValue).ordinal()){
							docStatusToId=info.getStatusId();
						}
					}
					if(docStatusToId!=null&&docStatusFromId!=null){
						List<CmTransition>transitions= transitionFacade.findByProperties(new String[]{"cmDocStatusByCmDocStatusTo.cmDocStatusId","cmDocStatusByCmDocStatusFrom.cmDocStatusId"},
								new Object[]{ docStatusToId,docStatusFromId}, 0,100);
						if(transitions!=null&&transitions.size()>0){
								
								Long []avaiableActions= ServiceFactory.getSecurityProvider().getUsecaseAvailableActions(currentUser, context.getUsecase().getUsecaseId());
								List<Long>actionsList= Arrays.asList(avaiableActions);
								for(CmTransition ctransition:transitions){
									if(actionsList.contains(ctransition.getCoUsecaseAction().getCoUsecaseActionId())){
										transition=ctransition;
										break;
									}
								}
						
							
						}
					}
				}
			}else{
				
				List<CmTransition>transitions= transitionFacade.findByProperty("coUsecaseAction.coActionId", actionId, 0,1);
				if(transitions!=null&&transitions.size()>0){
					transition=transitions.get(0);
				}
				
			}
		}
		if(securityCheck){
			if(transition==null){
				NonAuthorizedActionException ex= new NonAuthorizedActionException("could not find an accessible transition for current user to update the status of this record");
				ex.setActionId(actionId);
				ex.setUsecaseId(context.getUsecase().getUsecaseId());
				throw ex;
			}
			if(updatingObject!=null){
				Long currentUserId= ContextUtil.getUserId(context.getContextMap());
				CoUser locker=transitionFacade.getRecordLockedBy(context.getUsecase().getUsecaseId(), updatingObject.getRecordId(),currentUserId );
				if(locker!=null&&!locker.getCoUserId().equals(currentUserId)){
					LockRecordException ex= new LockRecordException("record has been lock by user "+locker.getUsername());
					ex.setLockedUser(locker);
					Map<String,Object>exContext=new HashMap<String, Object>();
					exContext.put("user", locker.getUsername());
					ex.setContext(exContext);
					throw ex;
				}
			}
		}
		if(transition!=null&&updatingObject!=null){
			transitionFacade.saveTransitionHistory(transition.getCmTransitionId(), ContextUtil.getUserId(context.getContextMap()), updatingObject.getRecordId());
		}
	}
}
//********************************************************************************************************
	private static UsecaseDocInfo findUpdateDoctypeInfo() {
		Context context=ContextHolder.getContext();
		if(context!=null&&context.getUsecase()!=null){
			UseCase usecase=context.getUsecase();
			String methodName=context.getMethodName();
			if("update".equalsIgnoreCase(methodName)||(methodName!=null&&methodName.indexOf("update")>=0)&&context.getActionId()!=null){
				Map<String,Object>contextMap=context.getContextMap();
				Long fiscalYearId= ContextUtil.getCurrentFiscalYear(contextMap);
				if(fiscalYearId!=null){
					UsecaseDocInfo docInfo= DocTypeUtil.getUsecaseDocStatusInfo(usecase.getUsecaseId(), fiscalYearId);
					return docInfo;
				}
			}
			
		}
		return null;
	}
//********************************************************************************************************
	protected static UtopiaPersistent findUpdatingObject(InvocationContext context){
		Object[] params= context.getParameters();
		if(params!=null){
			for(Object param:params){
				if(UtopiaPersistent.class.isInstance(param)){
					return (UtopiaPersistent)param;
				}
			}
		}
		return null;
	} 

//********************************************************************************************************	
}
