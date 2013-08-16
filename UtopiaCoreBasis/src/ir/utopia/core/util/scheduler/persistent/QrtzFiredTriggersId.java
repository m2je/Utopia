package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QrtzFiredTriggersId entity provides the base persistence definition of the
 * dTriggersId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class QrtzFiredTriggersId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3296238600474491888L;
	private String schedName;
	private String entryId;

	// Constructors

	/** default constructor */
	public QrtzFiredTriggersId() {
	}

	/** full constructor */
	public QrtzFiredTriggersId(String schedName, String entryId) {
		this.schedName = schedName;
		this.entryId = entryId;
	}

	// Property accessors

	@Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)
	public String getSchedName() {
		return this.schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	@Column(name = "ENTRY_ID", unique = false, nullable = false, insertable = true, updatable = true, length = 95)
	public String getEntryId() {
		return this.entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof QrtzFiredTriggersId))
			return false;
		QrtzFiredTriggersId castOther = (QrtzFiredTriggersId) other;

		return ((this.getSchedName() == castOther.getSchedName()) || (this
				.getSchedName() != null
				&& castOther.getSchedName() != null && this.getSchedName()
				.equals(castOther.getSchedName())))
				&& ((this.getEntryId() == castOther.getEntryId()) || (this
						.getEntryId() != null
						&& castOther.getEntryId() != null && this.getEntryId()
						.equals(castOther.getEntryId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchedName() == null ? 0 : this.getSchedName().hashCode());
		result = 37 * result
				+ (getEntryId() == null ? 0 : this.getEntryId().hashCode());
		return result;
	}

}