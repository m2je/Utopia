package ir.utopia.core.util.exceptionhandler;

import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.process.ExecutionResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExceptionResult extends ExecutionResult{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4914631102412863029L;
	private Set<String> errorFields;
	public ExceptionResult(List<MessageNamePair> messages,
			Set<String> errorFields) {
		super(messages);
		this.errorFields = errorFields;
	}
	
	public ExceptionResult(){
		
	}
	
	public Set<String> getErrorFields() {
		return errorFields;
	}
	public void setErrorFields(Set<String> errorFields) {
		this.errorFields = errorFields;
	}
	
	public void addErrorField(String errorField){
		if(this.errorFields==null){
			errorFields=new HashSet<String>();
		}
		errorFields.add(errorField);
	}
	
}
