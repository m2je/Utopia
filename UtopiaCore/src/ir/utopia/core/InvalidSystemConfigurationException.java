package ir.utopia.core;

import ir.utopia.core.exception.CoreRuntimeException;

public class InvalidSystemConfigurationException extends CoreRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1770414522198699755L;

	public InvalidSystemConfigurationException() {
		super();
	}

	public InvalidSystemConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSystemConfigurationException(String message) {
		super(message);
	}

	public InvalidSystemConfigurationException(Throwable cause) {
		super(cause);
	}

	
}
