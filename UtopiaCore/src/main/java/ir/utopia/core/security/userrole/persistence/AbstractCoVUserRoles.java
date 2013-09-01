package ir.utopia.core.security.userrole.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUserRoles entity provides the base persistence definition of the
 * CoUserRoles entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUserRoles extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8673851899060681188L;
	private Long coUserRolesId;
	private String username;
	private String roleName;
	

	// Constructors

	/** default constructor */
	public AbstractCoVUserRoles() {
	}
	// Property accessors
	
	@Id
	@Column(name = "CO_USER_ROLES_ID")
	public Long getCoUserRolesId() {
		return this.coUserRolesId;
	}

	public void setCoUserRolesId(Long coUserRolesId) {
		this.coUserRolesId = coUserRolesId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


}