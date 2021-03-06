package ir.utopia.core.exception;

import ir.utopia.core.security.user.persistence.CoUser;

public class LockRecordException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4020187217154894980L;

	CoUser lockedUser;
	public LockRecordException() {
	}

	public LockRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockRecordException(String message) {
		super(message);
	}

	public LockRecordException(Throwable cause) {
		super(cause);
	}

	public CoUser getLockedUser() {
		return lockedUser;
	}

	public void setLockedUser(CoUser lockedUser) {
		this.lockedUser = lockedUser;
	}

	

}
