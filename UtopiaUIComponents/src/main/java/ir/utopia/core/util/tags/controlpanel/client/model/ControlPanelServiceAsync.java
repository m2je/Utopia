package ir.utopia.core.util.tags.controlpanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.InteractiveRemoteServiceCallSupport;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ControlPanelServiceAsync{
	public void loadData(int type, AsyncCallback<UILookupInfo> callback);
	
	public void loadDetailData(Long[] parentIds,int parentType,int rootType, AsyncCallback<UILookupInfo[]> callback);
	
	
	/**
	 * 
	 * @return
	 */
	public void requestWindowNumber(AsyncCallback<Integer> callback);
	/**
	 * 
	 * @param actionName
	 * @param windoNumber
	 * @return
	 */
	public void getExecutionResult(String actionName,int windowNumber, AsyncCallback<ExecutionResult> callback);
	
	
}
