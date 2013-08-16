package ir.utopia.core.usecase;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.basicinformation.action.persistent.CoAction;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.process.annotation.UsecaseActions;
import ir.utopia.core.security.userlog.bean.CoUserLogFacadeRemote;
import ir.utopia.core.security.userlogdtl.bean.CoUserLogDtlFacadeRemote;
import ir.utopia.core.security.userlogdtl.persistent.LogActionStatus;
import ir.utopia.core.usecase.actionmodel.ActionParameter;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.usecase.usecase.bean.CoUsecaseFacadeRemote;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.bean.CoUsecaseActionFacadeRemote;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.util.Cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.security.auth.Subject;

public class UsecaseUtil {
	private static final Cache<String, UseCase>USECASE_CACHE=new Cache<String, UseCase>();
	private static final Cache<Long, List<ActionParameter>> ACTION_PARAMETERS_CACHE=new Cache<Long,List<ActionParameter>>();
	private static final List<ClassMethod>EXCLUDE_METHODS=new ArrayList<ClassMethod>();
	private static final Cache<Long, UseCase>USE_CASE_ACTION_CACHE=new Cache<Long, UseCase>();
	private static final Cache<Long, UseCase>USE_CASE_ID_CACHE=new Cache<Long, UseCase>();
	private static final Cache<Long, Long>USE_CASE_ACTION_TO_ACTION_CACHE=new Cache<Long, Long>();
	private static final Cache<String, UsecaseAction>USE_CASE_REMOTE_METHOD_CACHE=new Cache<String, UsecaseAction>();
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UsecaseUtil.class.getName());
	}
	static{
		try {
			EXCLUDE_METHODS.add(new ClassMethod(CoUsecaseFacadeRemote.class,CoUsecaseFacadeRemote.class.getMethod("loadUsecase", new Class<?>[]{String.class})));
			EXCLUDE_METHODS.add(new ClassMethod(CoUsecaseFacadeRemote.class,CoUsecaseFacadeRemote.class.getMethod("loadUsecase", new Class<?>[]{String.class,String.class,String.class})));
			EXCLUDE_METHODS.add(new ClassMethod(CoUsecaseFacadeRemote.class,CoUsecaseFacadeRemote.class.getMethod("loadUsecase", new Class<?>[]{Long.class})));
			
			EXCLUDE_METHODS.add(new ClassMethod(CoUserLogFacadeRemote.class,CoUserLogFacadeRemote.class.getMethod("logUserLogin", new Class<?>[]{String.class,Subject.class})));
			EXCLUDE_METHODS.add(new ClassMethod(CoUserLogFacadeRemote.class,CoUserLogFacadeRemote.class.getMethod("logUserLogout", new Class<?>[]{Subject.class})));
			EXCLUDE_METHODS.add(new ClassMethod(CoUserLogDtlFacadeRemote.class,CoUserLogDtlFacadeRemote.class.getMethod("logUserAction", new Class<?>[]{Long.class ,Long.class ,String.class ,LogActionStatus.class})));
			EXCLUDE_METHODS.add(new ClassMethod(UtopiaBasicUsecaseBean.class, UtopiaBasicUsecaseBean.class.getDeclaredMethod("getPersistentClass", new Class<?>[]{})));
//			EXCLUDE_METHODS.add(new ClassMethod(CoActionParametersFacadeRemote.class,CoActionParametersFacadeRemote.class.getMethod("loadActionParameters", new Class<?>[]{long.class})));
		} catch (Exception e) {
			logger.log(Level.ALL,"fail to load method class methods",e);
		}
	}
	
	/**
	 * 
	 * @param systemAbbrevation
	 * @param subsystemAbbrevation
	 * @param usecaseName
	 * @return
	 * @throws Exception
	 */
	public static UseCase getUseCase(String systemAbbrevation,String subsystemAbbrevation,String usecaseName)throws Exception{
		String key=systemAbbrevation+"|"+subsystemAbbrevation+"|"+usecaseName;
		if(!USECASE_CACHE.containsKey( key)){//USECASE_CACHE.clear();
			CoUsecaseFacadeRemote usecaseRemote=(CoUsecaseFacadeRemote) ServiceFactory.lookupFacade(CoUsecaseFacadeRemote.class.getName());
			 CoUsecase usecase = usecaseRemote.loadUsecase(usecaseName, systemAbbrevation, subsystemAbbrevation);
			 initUsecaseCache(key, usecase);
		}
		return	USECASE_CACHE.get(key);
	}
//************************************************************************************************************************
public static UseCase getUsecaseWithName(String fullUsecaseName)throws Exception{
	int firstIndex=fullUsecaseName.indexOf("_");
	String systemAbbrevation=fullUsecaseName.substring(0,firstIndex);
	int secoundIndex=fullUsecaseName.indexOf("_",firstIndex+1);
	String subsystemAbbrevation=fullUsecaseName.substring(firstIndex+1,secoundIndex);
	String usecaseName=fullUsecaseName.substring(secoundIndex+1);
	return getUseCase(systemAbbrevation, subsystemAbbrevation, usecaseName);
}
//************************************************************************************************************************
/**
 * 
 * @param systemAbbrevation
 * @param subsystemAbbrevation
 * @param usecaseName
 * @return
 */
public static String getFullUsecaseName(String systemAbbrevation,String subsystemAbbrevation,String usecaseName){
	String fullUscaseName=systemAbbrevation+"_"+subsystemAbbrevation+"_"+usecaseName;
	return fullUscaseName;
}
//************************************************************************************************************************
	private static UseCase initUsecaseCache(String key, CoUsecase usecase)
			throws Exception {
		UseCase result=null;
		if(usecase==null){
			USECASE_CACHE.put(key, null);
			}
		 else{
			 Set<CoUsecaseAction>actions= usecase.getCoUsecaseActions();
			 ArrayList<UsecaseAction>actionList=new ArrayList<UsecaseAction>();
			 for(CoUsecaseAction action:actions){
				 UsecaseAction uscaseAction= getUsecaseAction(usecase,action);
				 actionList.add(uscaseAction);
			 }
			  
			  CmSubsystem subSystem=usecase.getCmSubsystem();
			  result=new UseCase(usecase.getCoUsecaseId(),usecase.getName(),
					  getFullUsecaseName(subSystem.getCmSystem().getAbbreviation(), 
							  subSystem.getAbbreviation(), usecase.getName()), 
							  usecase.getUscsRemoteClass(),actionList);
			  result.setSystemId(subSystem.getCmSystem().getCmSystemId());
			  result.setSubSystemId(subSystem.getCmSubsystemId());
			  USE_CASE_ID_CACHE.put(usecase.getCoUsecaseId(), result);
			  USECASE_CACHE.put(key, result);
		 }
		return result;
	}
//************************************************************************************************************************	
	public static UseCase getUseCase(String remoteClass)throws Exception{
		Collection<UseCase> usecases= USECASE_CACHE.values();
		for(UseCase usecase:usecases){
			if(usecase!=null&&usecase.getRemoteClassName().equals(remoteClass)){
				return usecase;
			}
		}
		CoUsecase usecase= loadUsecase(remoteClass);
		if(usecase!=null){
			CmSubsystem subSystem=usecase.getCmSubsystem(); 
			String key=subSystem.getCmSystem().getAbbreviation()+"|"+subSystem.getAbbreviation()+"|"+usecase.getName();
			initUsecaseCache(key, usecase);
				return USECASE_CACHE.get(key);
		}
		return null;
	}
//************************************************************************************************************************
	public static List<UseCase> getUseCases(Set<String> remoteClasses)throws Exception{
		List<UseCase>result=new ArrayList<UseCase>();
		if(remoteClasses!=null){
		Collection<UseCase> usecases= USECASE_CACHE.values();
		List<String>all=new ArrayList<String>(remoteClasses);
		for(UseCase usecase:usecases){
			for(String remoteClass:remoteClasses){
			if(usecase!=null&&usecase.getRemoteClassName().equals(remoteClass)){
				result.add(usecase);
				all.remove(remoteClass);
			}
			}
		}
		if(all.size()>0){
			CoUsecaseFacadeRemote remote=lookupRemote();
			List<CoUsecase>uscases= remote.loadUsecases(all);
				for(CoUsecase usecase:uscases){
					CmSubsystem subSystem=usecase.getCmSubsystem(); 
					String key=subSystem.getCmSystem().getAbbreviation()+"|"+subSystem.getAbbreviation()+"|"+usecase.getName();
					result.add(initUsecaseCache(key, usecase));
				}
			}
		}
		
		return result;
	}
//************************************************************************************************************************	
	/**
	 * 
	 * @param actionId
	 * @return
	 * @throws Exception
	 */
	private static UsecaseAction getUsecaseAction(CoUsecase usecase ,CoUsecaseAction usAction)throws Exception{
		CoAction action;
		try {
			action = usAction.getCoAction();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		long actionId=action.getCoActionId();
		List<ActionParameter>result;
		if(!ACTION_PARAMETERS_CACHE.containsKey(action.getCoActionId())){//ACTION_PARAMETERS_CACHE.clear();
			result=loadActionParameters(usecase.getUscsRemoteClass(),action.getMethodName());
			if(result!=null){
				Collections.sort(result, new Comparator<ActionParameter>(){
					public int compare(ActionParameter o1, ActionParameter o2) {
						return o1.getIndex()-o2.getIndex();
					}}); 
			}
				ACTION_PARAMETERS_CACHE.put(actionId,result ) ;
		}else{
			result=ACTION_PARAMETERS_CACHE.get(actionId);
		}
		
		return new UsecaseAction(usAction.getCoUsecaseActionId(),action.getCoActionId(), action.getName(), action.getMethodName(),result);
	}

//************************************************************************************************************************	
	private static CoUsecaseFacadeRemote lookupRemote(){
		CoUsecaseFacadeRemote result=null;
		try {
			result = (CoUsecaseFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseFacadeRemote.class.getName());
		} catch (NamingException e) {
			logger.log(Level.ALL,"", e);
		}
		
		return result;
	}
//************************************************************************************************************************    
	private static CoUsecase loadUsecase(String remoteClassName){
		CoUsecaseFacadeRemote remote=lookupRemote();
		return remote.loadUsecase(remoteClassName);
			
		}
//************************************************************************************************************************
	public CoUsecase loadUsecase(String uscaseName, String systemAbbreviation,
			String subSystemAbbreviation) {
		CoUsecaseFacadeRemote remote=lookupRemote();
		return remote.loadUsecase(uscaseName, systemAbbreviation,subSystemAbbreviation);
	}
//************************************************************************************************************************	
	public static List<ActionParameter> loadActionParameters(String usecaseFacade,String actionName) {
		if(usecaseFacade==null||usecaseFacade.toString().trim().length()==0||actionName==null||actionName.trim().length()==0)
			return null;
		Class<?> clazz;
		try {
			clazz = Class.forName(usecaseFacade.trim());
			return loadActionParameters(clazz,actionName);
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
		
	}
//************************************************************************************************************************
	private static List<ActionParameter> loadActionParameters(Class<?>clazz,String actionName){
		if(clazz==null||clazz.equals(Object.class))return null;
		UsecaseActions actions= clazz.getAnnotation(UsecaseActions.class);
		if(actions==null){
			Class<?>[]classes=clazz.getInterfaces();
			return classes.length>0?loadActionParameters(classes[0],actionName):null;
		}
		ir.utopia.core.process.annotation.UsecaseAction []actionss= actions.actions();
		if(actionss==null||actionss.length==0)return loadActionParameters(clazz.getSuperclass(),actionName);
		for(ir.utopia.core.process.annotation.UsecaseAction action: actionss){
			if(actionName.equals(action.name())){
				List<ActionParameter> result =new ArrayList<ActionParameter>();
				 for(ir.utopia.core.process.annotation.ActionParameter parameter: action.parameters()){
					 result.add(new ActionParameter(parameter.type(),parameter.index()));
				 }
				 return result;
			}
		}
		return null;
	}
//************************************************************************************************************************	
	public static List<ClassMethod> getExcludeUsecaseMethods(){
		return EXCLUDE_METHODS;
	}
//************************************************************************************************************************
	public static String getActionUrl(String usecaseName,String actionName){
		return new StringBuffer(actionName).append(Constants.JSP_SEPERATOR).append(usecaseName).
		append(Constants.STRUTS_EXTENSION_NAME).toString();
	}
//************************************************************************************************************************
	public static UseCase getUsecaseFromUsecaseAtion(Long coUsecaseactionId){
		if(!USE_CASE_ACTION_CACHE.containsKey(coUsecaseactionId)){
			try {
				CoUsecaseActionFacadeRemote facade=(CoUsecaseActionFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseActionFacadeRemote.class.getName());
				CoUsecaseAction caction= facade.loadById(coUsecaseactionId,null);
				USE_CASE_ACTION_CACHE.put(coUsecaseactionId, getUseCase(caction.getCoUsecase().getUscsRemoteClass()));
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
				USE_CASE_ACTION_CACHE.put(coUsecaseactionId, null) ;
			}
		}
		return USE_CASE_ACTION_CACHE.get(coUsecaseactionId);
	}
//************************************************************************************************************************
	public static Long getActionId(Long usecaseActionId){
		if(!USE_CASE_ACTION_TO_ACTION_CACHE.containsKey(usecaseActionId))
		try {
			CoUsecaseActionFacadeRemote facade=(CoUsecaseActionFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseActionFacadeRemote.class.getName());
			USE_CASE_ACTION_TO_ACTION_CACHE.put(usecaseActionId, facade.loadById(usecaseActionId,null).getCoAction().getCoActionId());
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			USE_CASE_ACTION_TO_ACTION_CACHE.put(usecaseActionId, null) ;
		}
		return USE_CASE_ACTION_TO_ACTION_CACHE.get(usecaseActionId);
	}
//************************************************************************************************************************
	public static UsecaseAction getUsecaseAction(Class<?> remoteClass,String method){
		String key=initRemoteMethodCashe(remoteClass, method);
		return USE_CASE_REMOTE_METHOD_CACHE.get(key);
	}
//************************************************************************************************************************
	private static Class<?> extractInterface(Class<?> target){
		if(target.isInterface()){
			return target;
		}
		Class<?>[]interfaces= target.getInterfaces();
		if(interfaces!=null&&interfaces.length>0){
			for(Class<?> bean:interfaces){
				if(UtopiaBasicUsecaseBean.class.isAssignableFrom(bean)){
					return bean;
				}
			}
			return interfaces[0];
		}
		return target;
	}
//************************************************************************************************************************
	private static String getKey(Class<?> remoteClass,String method){
		return new StringBuffer(remoteClass.getName()).append("|").append(method).toString();
	}
//************************************************************************************************************************
	private static String initRemoteMethodCashe(Class<?> remoteClass,String method){
		String key=getKey(remoteClass, method);
		if(!USE_CASE_REMOTE_METHOD_CACHE.containsKey(key)){//USE_CASE_REMOTE_METHOD_CACHE.clear()
			try {
				UsecaseAction result=null;
				Class<?>interfaceClass=extractInterface(remoteClass);
				UseCase uscase=UsecaseUtil.getUseCase(interfaceClass.getName());
				if(uscase!=null){
					List<UsecaseAction>actions= uscase.getUseCaseActions();
					for(UsecaseAction action:actions){
						if(method.equals(action.getMethodName())){
							result=action;
							break;
						}
					}
				}
				USE_CASE_REMOTE_METHOD_CACHE.put(key, result);
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
			}

		}
		return key;
	}
//************************************************************************************************************************
	public static UseCase getUsecase(Long usecaseId){
		if(!USE_CASE_ID_CACHE.containsKey(usecaseId)){//USE_CASE_ID_CACHE.clear();
			try {
				CoUsecaseFacadeRemote facade=(CoUsecaseFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseFacadeRemote.class);
				CoUsecase usecase= facade.loadUsecase(usecaseId);
				if(usecase==null){
					USE_CASE_ID_CACHE.put(usecaseId, null);
				}else{
					String key=usecase.getCmSubsystem().getCmSystem().getAbbreviation()+"|"+usecase.getCmSubsystem().getAbbreviation()+"|"+usecase.getName();
					initUsecaseCache(key, usecase);
				}
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
		}
		return USE_CASE_ID_CACHE.get(usecaseId);
	} 
//************************************************************************************************************************
	public static class ClassMethod{
		Class<?>clazz;
		Method method;
		public ClassMethod(Class<?> clazz, Method method) {
			this.clazz = clazz;
			this.method = method;
		}
		public Class<?> getClazz() {
			return clazz;
		}
		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		
	}
}
