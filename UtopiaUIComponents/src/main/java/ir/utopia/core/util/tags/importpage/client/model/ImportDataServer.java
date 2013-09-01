package ir.utopia.core.util.tags.importpage.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ImportDataServer extends RemoteService {

	
	public UILookupInfo getImportTypes();
	
	public UILookupInfo getImportConfigurations(String usecase);
	
	public UILookupInfo getImportFileFormatConfiguration(int fileFormat,String usecaseId);
	
	
	
}
