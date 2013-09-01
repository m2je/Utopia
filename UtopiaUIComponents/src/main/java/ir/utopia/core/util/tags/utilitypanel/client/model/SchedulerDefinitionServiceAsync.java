package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SchedulerDefinitionServiceAsync {

	void loadConfigLookup(int type,AsyncCallback<UILookupInfo> callback);

	void saveConfiguration(SchedulerConfigurationModel config,
			AsyncCallback<ProcessExecutionResult> callback);

	void deleteConfiguration(int type,String name,
			AsyncCallback<ProcessExecutionResult> callback);

	void updateConfiguration(SchedulerConfigurationModel config,
			AsyncCallback<ProcessExecutionResult> callback);

	

	void loadMailTemplateLookup(AsyncCallback<UILookupInfo> callback);

	void loadSubSystemLookup(Long systemId, AsyncCallback<UILookupInfo> callback);

	void loadSystemLookup(AsyncCallback<UILookupInfo> callback);

	void loadUsecaseActionLookup(Long usecaseId,
			AsyncCallback<UILookupInfo> callback);

	void loadUsecaseLookup(Long subsystemId,
			AsyncCallback<UILookupInfo> callback);

	void loadUsernamesLookup(AsyncCallback<UILookupInfo> callback);



	void searchBPartner(String contains, String name, String lastname,
			String code, String email, AsyncCallback<GridValueModel> callback);

	void loadConfiguration(int configType, String configName,
			AsyncCallback<SchedulerConfigurationModel> callback);

	

	
}
