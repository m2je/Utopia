package ir.utopia.core.security.roleusecaseacss.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoRoleUsecaseAcss entity provides the base persistence definition of
 * the CoRoleUsecaseAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVRoleUsecaseAcss extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4335973367134852988L;
	private Long coRoleUsecaseAcssId;
	private String roleName;
	private String usecaseName;

	// Constructors
	/** default constructor */
	public AbstractCoVRoleUsecaseAcss() {
	}


	// Property accessors
	@Id
	@Column(name = "CO_ROLE_USECASE_ACSS_ID")
	public Long getCoRoleUsecaseAcssId() {
		return this.coRoleUsecaseAcssId;
	}

	public void setCoRoleUsecaseAcssId(Long coRoleUsecaseAcssId) {
		this.coRoleUsecaseAcssId = coRoleUsecaseAcssId;
	}

	@Column(name = "USECASE_NAME")
	public String getUsecaseName() {
		return usecaseName;
	}


	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}

	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}