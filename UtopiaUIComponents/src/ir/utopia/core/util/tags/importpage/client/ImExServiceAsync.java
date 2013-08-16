package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImExServiceAsync {

	void deleteSetting(Long settingId, AsyncCallback<ExecutionResult> callback);

	void getSettingData(Long settingId,
			AsyncCallback<ImExPageDataModel> callback);

}
