package ir.utopia.core.process;

import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.io.Serializable;
import java.util.List;

public class ProcessStatusChangeEvent  implements Serializable{
	private static final long serialVersionUID = 364143340441326509L;
	public enum EventType{
		processFailed,processSuccess,processStatusChanged,processFinished,
		processEvent
	}
private EventType eventType;
private int processStatus;
private Throwable exception;
private MessageNamePair message;
private Long key;
private BeanProcessExcutionResult beanExecutionResult; 
private ProcessPersistentInfo persistentInfo;
public ProcessStatusChangeEvent(EventType eventType){
	this.eventType=eventType;
}
public EventType getEventType() {
	return eventType;
}
public void setEventType(EventType eventType) {
	this.eventType = eventType;
}
public int getProcessStatus() {
	return processStatus;
}
public void setProcessStatus(int processStatus) {
	this.processStatus = processStatus;
}
public Throwable getException() {
	return exception;
}
public void setException(Throwable exception) {
	this.exception = exception;
}
public MessageNamePair getMessage() {
	return message;
}
public void setMessage(MessageNamePair message) {
	this.message = message;
}
public Long getKey() {
	return key;
}
public void setKey(Long key) {
	this.key = key;
}
public BeanProcessExcutionResult<?> getBeanExecutionResult() {
	return beanExecutionResult;
}
public void setBeanExecutionResult(BeanProcessExcutionResult<?> beanExecutionResult) {
	this.beanExecutionResult = beanExecutionResult;
}


public ProcessPersistentInfo getPersistentInfo() {
	return persistentInfo;
}
public void setPersistentInfo(ProcessPersistentInfo persistentInfo) {
	this.persistentInfo = persistentInfo;
}


public static class ProcessPersistentInfo{
	UtopiaBasicPersistent []validPersistents;
	UtopiaBasicPersistent []invlidPersistents;
	List<MessageNamePair>messages;
	public ProcessPersistentInfo(UtopiaBasicPersistent[] validPersistents,
			UtopiaBasicPersistent[] invlidPersistents, List<MessageNamePair> messages) {
		super();
		this.validPersistents = validPersistents;
		this.invlidPersistents = invlidPersistents;
		this.messages = messages;
	}
	public UtopiaBasicPersistent[] getValidPersistents() {
		return validPersistents;
	}
	public void setValidPersistents(UtopiaBasicPersistent[] validPersistents) {
		this.validPersistents = validPersistents;
	}
	public UtopiaBasicPersistent[] getInvlidPersistents() {
		return invlidPersistents;
	}
	public void setInvlidPersistents(UtopiaBasicPersistent[] invlidPersistents) {
		this.invlidPersistents = invlidPersistents;
	}
	public List<MessageNamePair> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageNamePair> messages) {
		this.messages = messages;
	}
	
}

}
