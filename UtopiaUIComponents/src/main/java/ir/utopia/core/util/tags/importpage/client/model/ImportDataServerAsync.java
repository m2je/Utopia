package ir.utopia.core.util.tags.importpage.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImportDataServerAsync {

	void getImportTypes(AsyncCallback<UILookupInfo> callback);

	void getImportFileFormatConfiguration(int fileFormat,String usecaseId,
			AsyncCallback<UILookupInfo> callback);

	void getImportConfigurations(String usecase,
			AsyncCallback<UILookupInfo> callback);

	

	

}
