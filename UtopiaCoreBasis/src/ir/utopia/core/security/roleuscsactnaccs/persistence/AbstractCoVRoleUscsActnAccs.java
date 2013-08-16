package ir.utopia.core.security.roleuscsactnaccs.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoRoleUscsActnAccs entity provides the base persistence definition of
 * the CoRoleUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVRoleUscsActnAccs extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -395347736302789466L;
	private Long coRoleUscsActnAccsId;
	private String usecaseactionName;
	private String roleName;
	private String actionName;
	// Constructors
	@Column(name="ACTION_NAME")
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	/** default constructor */
	public AbstractCoVRoleUscsActnAccs() {
	}
	// Property accessors
	@Id
	@Column(name = "CO_ROLE_USCS_ACTN_ACCS_ID")
	public Long getCoRoleUscsActnAccsId() {
		return this.coRoleUscsActnAccsId;
	}

	public void setCoRoleUscsActnAccsId(Long coRoleUscsActnAccsId) {
		this.coRoleUscsActnAccsId = coRoleUscsActnAccsId;
	}

	
	@Column(name = "USECASEACTION_NAME")
	public String getUsecaseactionName() {
		return usecaseactionName;
	}
	public void setUsecaseactionName(String usecaseactionName) {
		this.usecaseactionName = usecaseactionName;
	}

	
	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


}