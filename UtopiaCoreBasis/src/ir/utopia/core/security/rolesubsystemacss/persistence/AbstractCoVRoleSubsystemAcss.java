package ir.utopia.core.security.rolesubsystemacss.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoRoleSubsystemAcss entity provides the base persistence definition
 * of the CoRoleSubsystemAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVRoleSubsystemAcss extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8479890827907508149L;
	private Long coRoleSubsystemAcssId;
	private String roleName;
	private String subsysName;
	private String sysName;
	private Long coRoleId;
	private Long cmSystemId;
	private Long cmSubsystemId;
	// Constructors



	/** default constructor */
	public AbstractCoVRoleSubsystemAcss() {
	}

	// Property accessors
	@Id
	@Column(name = "CO_ROLE_SUBSYSTEM_ACSS_ID")
	public Long getCoRoleSubsystemAcssId() {
		return this.coRoleSubsystemAcssId;
	}

	public void setCoRoleSubsystemAcssId(Long coRoleSubsystemAcssId) {
		this.coRoleSubsystemAcssId = coRoleSubsystemAcssId;
	}

	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Column(name = "SUBSYS_NAME")	
	public String getSubsysName() {
		return subsysName;
	}

	public void setSubsysName(String subsysName) {
		this.subsysName = subsysName;
	}
	@Column(name="SYSNAME")
	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	@Column(name="CO_ROLE_ID")
	public Long getCoRoleId() {
		return coRoleId;
	}

	public void setCoRoleId(Long coRoleId) {
		this.coRoleId = coRoleId;
	}
	@Column(name="CM_SYSTEM_ID")
	public Long getCmSystemId() {
		return cmSystemId;
	}

	public void setCmSystemId(Long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}
	@Column(name="CM_SUBSYSTEM_ID")
	public Long getCmSubsystemId() {
		return cmSubsystemId;
	}

	public void setCmSubsystemId(Long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}
	

}