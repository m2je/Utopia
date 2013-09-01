package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExecutionResult implements Serializable,IsSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6142801341349429793L;
	private boolean success=true;
	private String[] infoMessages;
	private String[] warningMessages;
	private String[] errorMessages;
	public ExecutionResult(){
		this(true);
	}
	public ExecutionResult(boolean success){
		this.success=success;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String[] getInfoMessages() {
		return infoMessages;
	}
	public void setInfoMessages(String... infoMessages) {
		this.infoMessages = infoMessages;
	}
	public String[] getWarningMessages() {
		return warningMessages;
	}
	public void setWarningMessages(String... warningMessages) {
		this.warningMessages = warningMessages;
	}
	public String[] getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(String... errorMessages) {
		this.errorMessages = errorMessages;
	}
	public void addErrorMessage(String message){
		errorMessages=appendMessage(errorMessages, message);
	}
	public void addWarningMessage(String message){
		warningMessages=appendMessage(warningMessages, message);
	}
	public void addInfoMessage(String message){
		infoMessages=appendMessage(infoMessages, message);
	}
	protected String [] appendMessage(String []currentMessages,String newMessage){
		if(newMessage==null||newMessage.trim().length()==0)return currentMessages;
		if(currentMessages==null){
			currentMessages=new String[1];
			currentMessages[0]=newMessage;
			return currentMessages;
		}
		String []temp=new String[currentMessages.length+1];
		System.arraycopy(currentMessages, 0, temp, 0, currentMessages.length);
		temp[currentMessages.length]=newMessage;
		return temp;
	} 
}
