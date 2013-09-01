package ir.utopia.core.util.tags.process.client.model;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UtopiaProcessAction extends RemoteService{
	 
	 public ProcessExecutionResult confirm(String []params,String []values);
	 
	 public ProcessExecutionResult execute(String []params,String []values);
	    
	 public ProcessExecutionResult updateStatus(Long processUniqueId,Boolean isConfirm);
	 
	 
}
