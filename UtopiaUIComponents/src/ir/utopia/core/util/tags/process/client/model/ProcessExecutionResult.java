package ir.utopia.core.util.tags.process.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProcessExecutionResult extends ExecutionResult implements IsSerializable{
	
	public static transient final int PROCESS_STATUS_FINISHED=1;
	public static transient final int PROCESS_STATUS_PROCESSING=2;
	
	
	private int totalTaskCount ;
	private int currentTask;
	private String currentTaskName;
	private long processIdentifier=-1;
	private int processStatus;
	private boolean refreshFormAfterProcess=false;
	private static final long serialVersionUID = -3652904671470818014L;
	
	boolean confirmUser=false;
	String confirmMessages="";
	
	public boolean isConfirmUser() {
		return confirmUser;
	}
	public void setConfirmUser(boolean confirmUser) {
		this.confirmUser = confirmUser;
	}
	public String getConfirmMessages() {
		return confirmMessages;
	}
	public void setConfirmMessages(String confirmMessages) {
		this.confirmMessages = confirmMessages;
	}
	
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	public long getProcessIdentifier() {
		return processIdentifier;
	}
	public void setProcessIdentifier(long processIdentifier) {
		this.processIdentifier = processIdentifier;
	}
	public ProcessExecutionResult(){
		this(true,PROCESS_STATUS_FINISHED);
	}
	public ProcessExecutionResult(boolean success,int processStatus){
		super(success);
		this.processStatus=processStatus;
	}
	
	
	public int getTotalTaskCount() {
		return totalTaskCount;
	}
	public void setTotalTaskCount(int totalTaskCount) {
		this.totalTaskCount = totalTaskCount;
	}
	public int getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(int currentTask) {
		this.currentTask = currentTask;
	}
	public String getCurrentTaskName() {
		return currentTaskName;
	}
	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}
	public boolean isRefreshFormAfterProcess() {
		return refreshFormAfterProcess;
	}
	public void setRefreshFormAfterProcess(boolean refreshFormAfterProcess) {
		this.refreshFormAfterProcess = refreshFormAfterProcess;
	}
}
