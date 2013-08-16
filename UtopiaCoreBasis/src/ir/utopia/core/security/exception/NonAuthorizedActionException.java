/**
 * 
 */
package ir.utopia.core.security.exception;


/**
 * @author salarkia
 *
 */
public class NonAuthorizedActionException extends UtopiaSecurityException {

	
	private Long usecaseId;
	private Long actionId;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2717573062142666578L;

	/**
	 * 
	 */
	public NonAuthorizedActionException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NonAuthorizedActionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NonAuthorizedActionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NonAuthorizedActionException(Throwable cause) {
		super(cause);
	}

	public Long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

}
