package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ImExService extends RemoteService{
	
	public ExecutionResult deleteSetting(Long settingId); 
	
	public ImExPageDataModel getSettingData(Long settingId);
}
