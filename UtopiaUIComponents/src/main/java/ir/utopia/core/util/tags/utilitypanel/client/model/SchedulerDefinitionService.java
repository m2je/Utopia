package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SchedulerDefinitionService  extends RemoteService{

	public UILookupInfo loadConfigLookup(int type);
	
	public ProcessExecutionResult saveConfiguration(SchedulerConfigurationModel config);
	
	public ProcessExecutionResult deleteConfiguration(int type,String name);
	
	public ProcessExecutionResult updateConfiguration(SchedulerConfigurationModel config);
	
	public SchedulerConfigurationModel loadConfiguration(int configType,String configName);
	
	public UILookupInfo loadSystemLookup();
	
	public UILookupInfo loadSubSystemLookup(Long systemId);
	
	public UILookupInfo loadUsecaseLookup(Long subsystemId);
	
	public UILookupInfo loadUsecaseActionLookup(Long usecaseId);
	
	public UILookupInfo loadUsernamesLookup();
	
	public UILookupInfo loadMailTemplateLookup();
	
	public GridValueModel searchBPartner(String contains,String name,String lastname,String code,String email);
}
