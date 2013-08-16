package ir.utopia.core.util.tags.datainputform.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InteractiveRemoteServiceCallSupportAsync{
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
