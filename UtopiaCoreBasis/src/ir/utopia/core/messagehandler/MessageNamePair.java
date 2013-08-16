package ir.utopia.core.messagehandler;

import java.io.Serializable;

public class MessageNamePair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8480049160004521480L;
	public enum MessageType{
		error,warning,info
	} 
	public enum ExceptionType{
		internalError,databaseError,applicationSpecified
	}
	private int errorCode;
	private String referenceKey1;
	private String referenceKey2;
	private String referenceKey3;
	
	private String message;
	private MessageType type;
	private ExceptionType exceptionType;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public MessageNamePair( MessageType type,String message,ExceptionType exceptionType) {
		super();
		this.message = message;
		this.type = type;
		this.exceptionType=exceptionType;
		
	}
	public MessageNamePair( MessageType type,String message) {
		this(type,message,ExceptionType.internalError);
	}
	public ExceptionType getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getReferenceKey1() {
		return referenceKey1;
	}
	public void setReferenceKey1(String referenceKey1) {
		this.referenceKey1 = referenceKey1;
	}
	public String getReferenceKey2() {
		return referenceKey2;
	}
	public void setReferenceKey2(String referenceKey2) {
		this.referenceKey2 = referenceKey2;
	}
	public String getReferenceKey3() {
		return referenceKey3;
	}
	public void setReferenceKey3(String referenceKey3) {
		this.referenceKey3 = referenceKey3;
	}

	
}
