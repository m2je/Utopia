package ir.utopia.common.doctype.security.persistent;

import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.core.persistent.SoftDeletePersistentSupport;
import ir.utopia.core.security.user.persistence.CoUser;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCmUserDocstatusAccess 
 */
@MappedSuperclass
public abstract class AbstractCmUserDocstatusAccess extends SoftDeletePersistentSupport implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4341226703634817181L;
	private Long cmUserDocstatusAccessId;
	private CmDocStatus cmDocStatus;
	
	private CoUser coUser;

	// Constructors

	/** default constructor */
	public AbstractCmUserDocstatusAccess() {
	}


	// Property accessors
	@Id
	@Column(name = "CM_USER_DOCSTATUS_ACCESS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmUserDocstatusAccessId() {
		return this.cmUserDocstatusAccessId;
	}

	public void setCmUserDocstatusAccessId(Long cmUserDocstatusAccessId) {
		this.cmUserDocstatusAccessId = cmUserDocstatusAccessId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_DOC_STATUS_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDocStatus getCmDocStatus() {
		return this.cmDocStatus;
	}

	public void setCmDocStatus(CmDocStatus cmDocStatus) {
		this.cmDocStatus = cmDocStatus;
	}

	@ManyToOne
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}

	

}