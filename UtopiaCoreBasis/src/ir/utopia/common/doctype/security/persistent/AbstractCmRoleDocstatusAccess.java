package ir.utopia.common.doctype.security.persistent;

import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.core.persistent.SoftDeletePersistentSupport;
import ir.utopia.core.security.role.persistence.CoRole;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmRoleDocstatusAccess 
 */
@MappedSuperclass
public abstract class AbstractCmRoleDocstatusAccess extends SoftDeletePersistentSupport implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5772531244721654356L;
	private Long cmRoleDocstatusAccessId;
	private CmDocStatus cmDocStatus;
	
	private CoRole coRole;

	// Constructors

	// Property accessors
	@Id
	@Column(name = "CM_ROLE_DOCSTATUS_ACCESS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmRoleDocstatusAccessId() {
		return this.cmRoleDocstatusAccessId;
	}

	public void setCmRoleDocstatusAccessId(Long cmRoleDocstatusAccessId) {
		this.cmRoleDocstatusAccessId = cmRoleDocstatusAccessId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_DOC_STATUS_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDocStatus getCmDocStatus() {
		return this.cmDocStatus;
	}

	public void setCmDocStatus(CmDocStatus cmDocStatus) {
		this.cmDocStatus = cmDocStatus;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "CO_ROLE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoRole getCoRole() {
		return this.coRole;
	}

	public void setCoRole(CoRole coRole) {
		this.coRole = coRole;
	}

}