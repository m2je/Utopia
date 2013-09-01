package ir.utopia.core.util.tags.datainputform.client.model;

import com.google.gwt.user.client.rpc.RemoteService;

public interface InteractiveRemoteServiceCallSupport extends RemoteService{
	/**
	 * 
	 * @return
	 */
	public int requestWindowNumber();
	/**
	 * 
	 * @param actionName
	 * @param windoNumber
	 * @return
	 */
	public ExecutionResult getExecutionResult(String actionName,int windowNumber);
	
	
}
