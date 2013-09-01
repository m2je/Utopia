package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ImportInstanceService extends RemoteService{

	public ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult deleteSettingInstance(Long settingInstanceId);
	
	public UILookupInfo loadImportInstances(Long settingId,int fileType);
}
