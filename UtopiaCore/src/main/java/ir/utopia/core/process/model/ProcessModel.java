package ir.utopia.core.process.model;


public class ProcessModel {

	String processName;
	String processBundelPath;
	String processExecutionPath;
	boolean refreshAfter;
	boolean alertSuccess;
	public String getProcessBundelPath() {
		return processBundelPath;
	}

	public void setProcessBundelPath(String processBundelPath) {
		this.processBundelPath = processBundelPath;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	private ProcessParameter[] parameters;

	public ProcessParameter[] getParameters() {
		return parameters;
	}

	public void setParameters(ProcessParameter[] parameters) {
		this.parameters = parameters;
	}
	
	private long usecaseId;

	public long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(long usecaseId) {
		this.usecaseId = usecaseId;
	}

	public String getProcessExecutionPath() {
		return processExecutionPath;
	}

	public void setProcessExecutionPath(String processExecutionPath) {
		this.processExecutionPath = processExecutionPath;
	}

	public boolean isRefreshAfter() {
		return refreshAfter;
	}

	public void setRefreshAfter(boolean refreshAfter) {
		this.refreshAfter = refreshAfter;
	}

	public boolean isAlertSuccess() {
		return alertSuccess;
	}

	public void setAlertSuccess(boolean alertSuccess) {
		this.alertSuccess = alertSuccess;
	}
	
	
}
