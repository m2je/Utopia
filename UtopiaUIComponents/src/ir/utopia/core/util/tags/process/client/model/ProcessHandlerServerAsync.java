package ir.utopia.core.util.tags.process.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProcessHandlerServerAsync  {
	
	public void getProcessConfiguration(Long usecaseActionId, AsyncCallback<ProcessConfigurationModel> callback);
	
	
}
