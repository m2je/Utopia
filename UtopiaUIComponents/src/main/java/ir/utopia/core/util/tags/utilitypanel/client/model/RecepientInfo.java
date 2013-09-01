package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RecepientInfo implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6623321583055277601L;

	UILookupInfo recepients;
	Long[] selectedRecepients;
	Long selectedUsername;
	Long selectedTemplate;
	
	

	public UILookupInfo getRecepients() {
		return recepients;
	}
	public void setRecepients(UILookupInfo recepients) {
		this.recepients = recepients;
	}
	public Long[] getSelectedRecepients() {
		return selectedRecepients;
	}
	public void setSelectedRecepients(Long[] selectedRecepients) {
		this.selectedRecepients = selectedRecepients;
	}
	public Long getSelectedUsername() {
		return selectedUsername;
	}
	public void setSelectedUsername(Long selectedUsername) {
		this.selectedUsername = selectedUsername;
	}
	public Long getSelectedTemplate() {
		return selectedTemplate;
	}
	public void setSelectedTemplate(Long selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}
	
}
