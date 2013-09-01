package ir.utopia.core.util.tags.process.client.model;

import java.util.HashMap;

import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProcessConfigurationModel implements IsSerializable{
	InputItem [] items;
	String title;
	Long processId;
	String processSubmitPath;
	boolean refreshPageAfterProcess=false;
	boolean alertForSuccess=false;
	HashMap<String, String> defaultValues;
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public InputItem[] getItems() {
		return items;
	}

	public void setItems(InputItem[] items) {
		this.items = items;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProcessSubmitPath() {
		return processSubmitPath;
	}

	public void setProcessSubmitPath(String processSubmitPath) {
		this.processSubmitPath = processSubmitPath;
	}

	public boolean isRefreshPageAfterProcess() {
		return refreshPageAfterProcess;
	}

	public void setRefreshPageAfterProcess(boolean refreshPageAfterProcess) {
		this.refreshPageAfterProcess = refreshPageAfterProcess;
	}

	public boolean isAlertForSuccess() {
		return alertForSuccess;
	}

	public void setAlertForSuccess(boolean alertForSuccess) {
		this.alertForSuccess = alertForSuccess;
	}
	public HashMap<String, String> getDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(HashMap<String, String> defaultValues) {
		this.defaultValues = defaultValues;
	}
}
