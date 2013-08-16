/**
 * 
 */
package ir.utopia.core.security.action;

/**
 * @author salarkia
 * a basic authorized action in application
 * 
 */
public class UsecaseActionKey {
	
	private long usecaseId;
	private long actionId;
	
	
	
	public UsecaseActionKey(long usecaseId, long actionId) {
		this.usecaseId = usecaseId;
		this.actionId = actionId;
	}
	public long getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(long usecaseId) {
		this.usecaseId = usecaseId;
	}
	public long getActionId() {
		return actionId;
	}
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}
	
	
}
