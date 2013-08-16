package ir.utopia.core.util.tags.process.client.model;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ProcessHandlerServer extends RemoteService  {
	
	public ProcessConfigurationModel getProcessConfiguration(Long usecaseActionId);
	
	
}
