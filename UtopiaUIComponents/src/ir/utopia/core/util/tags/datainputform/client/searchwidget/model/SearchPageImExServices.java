package ir.utopia.core.util.tags.datainputform.client.searchwidget.model;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SearchPageImExServices extends RemoteService {

	public ExecutionResult deleteSetting(Long settingId); 
}
