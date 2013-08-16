package ir.utopia.core.security.userorganizationaccess.persistent;

// default package

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.user.persistence.CoUser;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUserOrganizationAcss
 */
@MappedSuperclass
public abstract class AbstractCoUserOrganizationAcss extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -168917982619483220L;
	private Long coUserOrganizationAcssId;
	private CoUser coUser;
	private CmOrganization cmOrganization;

	

	// Property accessors
	@Id
	@Column(name = "CO_USER_ORGANIZATION_ACSS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserOrganizationAcssId() {
		return this.coUserOrganizationAcssId;
	}

	public void setCoUserOrganizationAcssId(Long coUserOrganizationAcssId) {
		this.coUserOrganizationAcssId = coUserOrganizationAcssId;
	}

	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
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