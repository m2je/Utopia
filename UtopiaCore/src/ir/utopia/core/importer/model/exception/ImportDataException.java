/**
 * 
 */
package ir.utopia.core.importer.model.exception;

import ir.utopia.core.exception.CoreException;

/**
 * @author Salarkia
 *
 */
public class ImportDataException extends CoreException  {
	int badDataLineNumber;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6157287017817199740L;

	/**
	 * 
	 */
	public ImportDataException() {
	}
	public ImportDataException(int badDataLineNumber) {
		this.badDataLineNumber=badDataLineNumber;
	}
	/**
	 * @param message
	 * @param cause
	 */
	public ImportDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ImportDataException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ImportDataException(Throwable cause) {
		super(cause);
	}

	public int getBadDataLineNumber() {
		return badDataLineNumber;
	}

	public void setBadDataLineNumber(int badDataLineNumber) {
		this.badDataLineNumber = badDataLineNumber;
	}

}
