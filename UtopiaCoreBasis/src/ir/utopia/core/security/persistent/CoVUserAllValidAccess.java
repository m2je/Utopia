package ir.utopia.core.security.persistent;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "co_v_user_all_valid_access") 
public class CoVUserAllValidAccess  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5963811219484641389L;
	private long coUserId;
	private long coRoleId;
	private long coUsecaseId;
	private long coUsecaseActionId;
	private long coActionId;
	private long cmSubsystemId;
	private long cmSystemId; 
	@Column(name="Co_User_Id")
	public long getCoUserId() {
		return coUserId;
	}
	public void setCoUserId(long coUserId) {
		this.coUserId = coUserId;
	}
	@Column(name="co_role_id")
	public long getCoRoleId() {
		return coRoleId;
	}
	public void setCoRoleId(long coRoleId) {
		this.coRoleId = coRoleId;
	}
	@Column(name="co_usecase_id")
	public long getCoUsecaseId() {
		return coUsecaseId;
	}
	public void setCoUsecaseId(long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}
	@Id
	@Column(name="Co_Usecase_Action_Id")
	public long getCoUsecaseActionId() {
		return coUsecaseActionId;
	}
	public void setCoUsecaseActionId(long coUsecaseActionId) {
		this.coUsecaseActionId = coUsecaseActionId;
	}
	@Column(name="co_action_id")
	public long getCoActionId() {
		return coActionId;
	}
	public void setCoActionId(long coActionId) {
		this.coActionId = coActionId;
	}
	@Column(name="Cm_SubSystem_id")
	public long getCmSubsystemId() {
		return cmSubsystemId;
	}
	public void setCmSubsystemId(long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}
	@Column(name="Cm_System_id")
	public long getCmSystemId() {
		return cmSystemId;
	}
	public void setCmSystemId(long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}
	
	
}
