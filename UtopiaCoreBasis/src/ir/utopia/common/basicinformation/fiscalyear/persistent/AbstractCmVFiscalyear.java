package ir.utopia.common.basicinformation.fiscalyear.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCmFiscalyear entity provides the base persistence definition of the
 * CmFiscalyear entity.
 * 
 * @author Jahani
 */
@MappedSuperclass
public abstract class AbstractCmVFiscalyear extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5155043112474470716L;
	private Long cmFiscalyearId;
	private Date startdate;
	private Date enddate;
	private String name;

	// Constructors

	/** default constructor */
	public AbstractCmVFiscalyear() {
	}

	
	// Property accessors
	@Id
	@Column(name = "CM_FISCALYEAR_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmFiscalyearId() {
		return this.cmFiscalyearId;
	}

	public void setCmFiscalyearId(Long cmFiscalyearId) {
		this.cmFiscalyearId = cmFiscalyearId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STARTDATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENDDATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}