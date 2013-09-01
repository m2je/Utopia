package ir.utopia.core.util.tags.process.client;



import ir.utopia.core.util.tags.process.client.model.ProcessConfigurationModel;
import ir.utopia.core.util.tags.process.client.model.ProcessHandlerServer;
import ir.utopia.core.util.tags.process.client.model.ProcessHandlerServerAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ProcessHandlerServerService {

	private static ProcessHandlerServerService instance;
	private ProcessHandlerServerAsync proxy;
	private ProcessHandlerServerService(){
		  proxy = (ProcessHandlerServerAsync) GWT
        .create(ProcessHandlerServer.class);

			  ((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL()
			          + "Process-Service.json");

			 
}
	public static ProcessHandlerServerService getServer(){
		if(instance==null){
			instance=new ProcessHandlerServerService();
		}
		return instance;
	}
	
	public  void loadProcessConfiguration(Long useCaseActionId,AsyncCallback<ProcessConfigurationModel>handler){
		proxy.getProcessConfiguration(useCaseActionId, handler);
	}
	
	
	
}
