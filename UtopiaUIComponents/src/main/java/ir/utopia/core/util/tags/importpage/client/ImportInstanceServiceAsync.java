package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImportInstanceServiceAsync {

	void deleteSettingInstance(Long settingInstanceId,
			AsyncCallback<ExecutionResult> callback);

	void loadImportInstances(Long settingId, int fileType,
			AsyncCallback<UILookupInfo> callback);

}
