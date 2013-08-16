package ir.utopia.core.process;

import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.util.List;
	
public class BeanProcessExcutionResult<P extends UtopiaBasicPersistent> extends ExecutionResult{

	public P[]validPersistents;
	public P[]invalidPersistents;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4828752330969943367L;

	public BeanProcessExcutionResult() {
		super();
	}
//***************************************************************************
	public BeanProcessExcutionResult(List<MessageNamePair> messages) {
		super(messages);
	}
//***************************************************************************	
	public BeanProcessExcutionResult(P []validResult,List<MessageNamePair> messages){
		this(validResult,null,messages);
	}
//***************************************************************************
	public BeanProcessExcutionResult(P []validResult,P []invalidResult,List<MessageNamePair> messages){
		super(messages);
		this.validPersistents=validResult;
		this.invalidPersistents=invalidResult;
	}
//***************************************************************************
	public P[] getValidPersistents() {
		return validPersistents;
	}
//***************************************************************************
	public void setValidPersistents(P[] validPersistents) {
		this.validPersistents = validPersistents;
	}
//***************************************************************************
	public P[] getInvalidPersistents() {
		return invalidPersistents;
	}
//**************************************************************************
	public void setInvalidPersistents(P[] invalidPersistents) {
		this.invalidPersistents = invalidPersistents;
	}
	
	
}
