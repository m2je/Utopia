package ir.utopia.core;

import ir.utopia.core.exception.CoreRuntimeException;

public class InvalidAfterLoginProcessException extends CoreRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1770414522198699755L;

	public InvalidAfterLoginProcessException() {
		super();
	}

	public InvalidAfterLoginProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAfterLoginProcessException(String message) {
		super(message);
	}

	public InvalidAfterLoginProcessException(Throwable cause) {
		super(cause);
	}

	
}
