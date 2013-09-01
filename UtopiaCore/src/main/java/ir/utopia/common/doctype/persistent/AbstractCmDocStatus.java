package ir.utopia.common.doctype.persistent;

import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
import ir.utopia.common.dashboard.persistent.CmTransition;
import ir.utopia.core.constants.Constants.BooleanType;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCmDocStatus 
 */
@MappedSuperclass
public abstract class AbstractCmDocStatus extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6931257586199638509L;
	private Long cmDocStatusId;
	private CmFiscalyear cmFiscalyear;
	private CmDoctype cmDoctype;
	private String statusName;
	private BooleanType lockAble;
	private BooleanType skipAble;
	private Set<CmTransition> cmTransitionsForCmDocStatusTo = new HashSet<CmTransition>(
			0);
	private Set<CmTransition> cmTransitionsForCmDocStatusFrom = new HashSet<CmTransition>(
			0);
	private int status;
	
	
	// Constructors

	/** default constructor */
	public AbstractCmDocStatus() {
	}

	
	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
	generator="DocstatusSequenceGenerator")
	@Column(name = "CM_DOC_STATUS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmDocStatusId() {
		return this.cmDocStatusId;
	}

	public void setCmDocStatusId(Long cmDocStatusId) {
		this.cmDocStatusId = cmDocStatusId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_FISCALYEAR_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmFiscalyear getCmFiscalyear() {
		return this.cmFiscalyear;
	}

	public void setCmFiscalyear(CmFiscalyear cmFiscalyear) {
		this.cmFiscalyear = cmFiscalyear;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_DOCTYPE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmDoctype getCmDoctype() {
		return this.cmDoctype;
	}

	public void setCmDoctype(CmDoctype cmDoctype) {
		this.cmDoctype = cmDoctype;
	}

	@Column(name = "STATUS_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getStatusName() {
		return this.statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "cmDocStatusByCmDocStatusTo")
	public Set<CmTransition> getCmTransitionsForCmDocStatusTo() {
		return this.cmTransitionsForCmDocStatusTo;
	}

	public void setCmTransitionsForCmDocStatusTo(
			Set<CmTransition> cmTransitionsForCmDocStatusTo) {
		this.cmTransitionsForCmDocStatusTo = cmTransitionsForCmDocStatusTo;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "cmDocStatusByCmDocStatusFrom")
	public Set<CmTransition> getCmTransitionsForCmDocStatusFrom() {
		return this.cmTransitionsForCmDocStatusFrom;
	}

	public void setCmTransitionsForCmDocStatusFrom(
			Set<CmTransition> cmTransitionsForCmDocStatusFrom) {
		this.cmTransitionsForCmDocStatusFrom = cmTransitionsForCmDocStatusFrom;
	}

	@Column(name="STATUS")
	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	@Enumerated(EnumType.ORDINAL)
	@Column(name = "LOCK_ABLE", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public BooleanType getLockAble() {
		return this.lockAble;
	}

	public void setLockAble(BooleanType lockAble) {
		this.lockAble = lockAble;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SKIP_ABLE", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public BooleanType getSkipAble() {
		return this.skipAble;
	}

	public void setSkipAble(BooleanType skipAble) {
		this.skipAble = skipAble;
	}
}