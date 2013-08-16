package ir.utopia.core.security.roleorganizationaccess.persistent;


import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.role.persistence.CoRole;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoRoleOrganizationAcss
 */
@MappedSuperclass
public abstract class AbstractCoRoleOrganizationAcss extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4821421774129392337L;
	private Long coRoleOrganizationAcssId;
	private CoRole coRole;
	private CmOrganization cmOrganization;

	// Constructors

	/** default constructor */
	public AbstractCoRoleOrganizationAcss() {
	}

	

	// Property accessors
	@Id
	@Column(name = "CO_ROLE_ORGANIZATION_ACSS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoRoleOrganizationAcssId() {
		return this.coRoleOrganizationAcssId;
	}

	public void setCoRoleOrganizationAcssId(Long coRoleOrganizationAcssId) {
		this.coRoleOrganizationAcssId = coRoleOrganizationAcssId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_ROLE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoRole getCoRole() {
		return this.coRole;
	}

	public void setCoRole(CoRole coRole) {
		this.coRole = coRole;
	}

	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "CM_ORGANIZATION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmOrganization getCmOrganization() {
		return this.cmOrganization;
	}

	public void setCmOrganization(CmOrganization cmOrganization) {
		this.cmOrganization = cmOrganization;
	}
	

}