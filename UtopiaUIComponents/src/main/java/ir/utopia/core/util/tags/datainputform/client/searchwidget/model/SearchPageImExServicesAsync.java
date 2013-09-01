package ir.utopia.core.util.tags.datainputform.client.searchwidget.model;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchPageImExServicesAsync {

	void deleteSetting(Long settingId, AsyncCallback<ExecutionResult> callback);

}
