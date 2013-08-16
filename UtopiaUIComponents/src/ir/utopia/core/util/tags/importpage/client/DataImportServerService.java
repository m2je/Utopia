package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.importpage.client.model.ImportDataServer;
import ir.utopia.core.util.tags.importpage.client.model.ImportDataServerAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DataImportServerService {
	private static DataImportServerService instance;
	private ImportDataServerAsync proxy;
	
	private DataImportServerService(){
		  proxy = (ImportDataServerAsync) GWT
          .create(ImportDataServer.class);
				((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "DataInput-service.json");
	}

  
	
	public static DataImportServerService getServer(){
		if(instance==null){
			instance=new DataImportServerService();
		}
		return instance;
	}

	public void loadFileFormats(AsyncCallback<UILookupInfo> callback){
		proxy.getImportTypes(callback);
	}
	
	public void loadConfigurationsOfFormat(Long format,String usecaseId, AsyncCallback<UILookupInfo> callback){
		proxy.getImportFileFormatConfiguration(format.intValue(),usecaseId, callback);
	}
	
	public void loadImportConfigurations(String usecase, AsyncCallback<UILookupInfo> callback){
		proxy.getImportConfigurations(usecase, callback);
	}
}
