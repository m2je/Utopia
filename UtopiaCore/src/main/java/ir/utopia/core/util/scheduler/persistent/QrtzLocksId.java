package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QrtzLocksId entity provides the base persistence definition of the sId
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class QrtzLocksId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2251314691909791775L;
	private String schedName;
	private String lockName;

	// Constructors

	/** default constructor */
	public QrtzLocksId() {
	}

	/** full constructor */
	public QrtzLocksId(String schedName, String lockName) {
		this.schedName = schedName;
		this.lockName = lockName;
	}

	// Property accessors

	@Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)
	public String getSchedName() {
		return this.schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	@Column(name = "LOCK_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 40)
	public String getLockName() {
		return this.lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof QrtzLocksId))
			return false;
		QrtzLocksId castOther = (QrtzLocksId) other;

		return ((this.getSchedName() == castOther.getSchedName()) || (this
				.getSchedName() != null
				&& castOther.getSchedName() != null && this.getSchedName()
				.equals(castOther.getSchedName())))
				&& ((this.getLockName() == castOther.getLockName()) || (this
						.getLockName() != null
						&& castOther.getLockName() != null && this
						.getLockName().equals(castOther.getLockName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchedName() == null ? 0 : this.getSchedName().hashCode());
		result = 37 * result
				+ (getLockName() == null ? 0 : this.getLockName().hashCode());
		return result;
	}

}