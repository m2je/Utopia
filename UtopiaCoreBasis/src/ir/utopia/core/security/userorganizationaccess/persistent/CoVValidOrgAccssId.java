package ir.utopia.core.security.userorganizationaccess.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CoVValidOrgAccssId 
 */
@Embeddable
public class CoVValidOrgAccssId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1928955662989946621L;
	private Long coUserId;
	private Long cmOrganizationId;

	// Constructors

	/** default constructor */
	public CoVValidOrgAccssId() {
	}

	/** full constructor */
	public CoVValidOrgAccssId(Long coUserId, Long cmOrganizationId) {
		this.coUserId = coUserId;
		this.cmOrganizationId = cmOrganizationId;
	}

	// Property accessors

	@Column(name = "CO_USER_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCoUserId() {
		return this.coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	@Column(name = "CM_ORGANIZATION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getCmOrganizationId() {
		return this.cmOrganizationId;
	}

	public void setCmOrganizationId(Long cmOrganizationId) {
		this.cmOrganizationId = cmOrganizationId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CoVValidOrgAccssId))
			return false;
		CoVValidOrgAccssId castOther = (CoVValidOrgAccssId) other;

		return ((this.getCoUserId() == castOther.getCoUserId()) || (this
				.getCoUserId() != null
				&& castOther.getCoUserId() != null && this.getCoUserId()
				.equals(castOther.getCoUserId())))
				&& ((this.getCmOrganizationId() == castOther
						.getCmOrganizationId()) || (this.getCmOrganizationId() != null
						&& castOther.getCmOrganizationId() != null && this
						.getCmOrganizationId().equals(
								castOther.getCmOrganizationId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCoUserId() == null ? 0 : this.getCoUserId().hashCode());
		result = 37
				* result
				+ (getCmOrganizationId() == null ? 0 : this
						.getCmOrganizationId().hashCode());
		return result;
	}

}