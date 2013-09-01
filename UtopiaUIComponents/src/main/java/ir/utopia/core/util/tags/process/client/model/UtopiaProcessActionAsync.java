package ir.utopia.core.util.tags.process.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UtopiaProcessActionAsync{
	 
	 public void confirm(String []params,String []values, AsyncCallback<ProcessExecutionResult> callback);
	 
	 public void execute(String []params,String []values, AsyncCallback<ProcessExecutionResult> callback);
	    
	 public void updateStatus(Long processUniqueId,Boolean isConfirm, AsyncCallback<ProcessExecutionResult> callback);

	

	
	 
	 
}
