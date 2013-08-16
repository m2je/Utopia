package ir.utopia.core.process;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.process.annotation.Processes;
import ir.utopia.core.process.model.ProcessModel;
import ir.utopia.core.process.model.ProcessParameter;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.usecase.usecase.bean.CoUsecaseFacadeRemote;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.util.Cache;

import java.security.Policy.Parameters;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultProcessHandler implements ProcessHandler{
	
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(DefaultProcessHandler.class.getName());
	}
	private static Cache<Long,List<ProcessModel>>PROCESS_COFIGURATION_CACHE=new Cache<Long,List<ProcessModel>>();
	static{
		loadAnnotatedConfiguration();
	}
	/**
	 * 	
	 * @param processName
	 * @return
	 */
	public  ProcessModel getProcesses(Long useCaseActionId){
		UseCase uscase= UsecaseUtil.getUsecaseFromUsecaseAtion(useCaseActionId);
		if(uscase!=null){
			List<UsecaseAction>actions= uscase.getUseCaseActions();
			List<ProcessModel> actionList=PROCESS_COFIGURATION_CACHE.get(uscase.getUsecaseId());
			Long actionId=UsecaseUtil.getActionId(useCaseActionId);
			if(actions!=null&&actionList!=null&&actionList.size()>0&&actions.size()>0){
				for(UsecaseAction action:actions){
					if(actionId==action.getActionId()){
						for(ProcessModel model:actionList ){
							if(model.getProcessName().equals(action.getMethodName())){
								return model;
							}
						}
						}
					}
				}
			}
		return null;
	}
//************************************************************************************************	
	private  static List<ProcessModel> loadAnnotatedConfiguration(){
		try {
			CoUsecaseFacadeRemote usecaseBean=(CoUsecaseFacadeRemote)ServiceFactory.lookupFacade(CoUsecaseFacadeRemote.class.getName());
			List<CoUsecase>usecases= usecaseBean.findAll(true, null);
			for(CoUsecase usecase:usecases){
				String remoteClass= usecase.getUscsRemoteClass();
				if(remoteClass!=null&&remoteClass.trim().length()>0){
					Class<?>clazz=null; 
					try {
						clazz= Class.forName(remoteClass);
					} catch (Exception e) {
						logger.log(Level.WARNING,"fail to load class -->"+remoteClass+ " please validate data in CO_USECASE table ");
						continue;
					}
					PROCESS_COFIGURATION_CACHE.put(usecase.getCoUsecaseId(), getClassProcesses(clazz, usecase));
				}
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//************************************************************************************************
private  static List<ProcessModel> getClassProcesses(Class<?> clazz,CoUsecase usecase){
	Processes processes= clazz.getAnnotation(Processes.class);
	
	List<ProcessModel> result=new ArrayList<ProcessModel>();
	if(processes!=null&&processes.processList()!=null){
		for(ir.utopia.core.process.annotation.Process process: processes.processList()){
			ProcessModel model=new ProcessModel();
			model.setUsecaseId(usecase.getCoUsecaseId());
			model.setProcessName(process.name());
			ir.utopia.core.process.annotation.ProcessParameter[] parameters=process.parameters();
			if(parameters!=null){
				ProcessParameter[] params=new ProcessParameter[parameters.length];
				for(int i=0;i<parameters.length;i++){
					params[i]=new ProcessParameter();
					params[i].setDisplayIndex(parameters[i].displayIndex());
					params[i].setType(parameters[i].type());
					if(ActionParameterTypes.processParameter.equals(params[i].getType())){
						params[i].setDisplayType(parameters[i].paramDisplayType());
						params[i].setDefaultValue(parameters[i].defaultValue());
						params[i].setLookupModel( parameters[i].lookupConfiguration());
						params[i].setLovConfiguration(parameters[i].LOVconfiguration());
						params[i].setMaxValue(parameters[i].maxValue());
						params[i].setMinValue(parameters[i].minValue());
						params[i].setName(parameters[i].name());
						params[i].setConfirmRequired(parameters[i].shouldConfirm());
						params[i].setMandatory(parameters[i].isMandatory());
						params[i].setDisplayLogic(parameters[i].displayLogic());
						params[i].setReadOnlyLogic(parameters[i].readOnlyLogic());
					}
				}
				model.setParameters(params);
			}
			model.setAlertSuccess(process.UIConfiguration().notifyForExecutionSuccess());
			model.setRefreshAfter(process.UIConfiguration().refreshPageAfterProcess());
			
				CmSubsystem subSystem=	usecase.getCmSubsystem();
				CmSystem system=subSystem.getCmSystem();
			model.setProcessExecutionPath(UsecaseUtil.getActionUrl( UsecaseUtil.getFullUsecaseName(system.getAbbreviation(), 
					subSystem.getAbbreviation(), usecase.getName()),model.getProcessName()) );
			model.setProcessBundelPath(clazz.getName());
			result.add(model);
		}
	}
	return result;
}	
//************************************************************************************************
	
}
