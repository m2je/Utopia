package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QrtzSimpleTriggersId entity provides the base persistence definition of the
 * leTriggersId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class QrtzSimpleTriggersId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8011116219041890791L;
	private String schedName;
	private String triggerName;
	private String triggerGroup;

	// Constructors

	/** default constructor */
	public QrtzSimpleTriggersId() {
	}

	/** full constructor */
	public QrtzSimpleTriggersId(String schedName, String triggerName,
			String triggerGroup) {
		this.schedName = schedName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
	}

	// Property accessors

	@Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)
	public String getSchedName() {
		return this.schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	@Column(name = "TRIGGER_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getTriggerName() {
		return this.triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	@Column(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getTriggerGroup() {
		return this.triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof QrtzSimpleTriggersId))
			return false;
		QrtzSimpleTriggersId castOther = (QrtzSimpleTriggersId) other;

		return ((this.getSchedName() == castOther.getSchedName()) || (this
				.getSchedName() != null
				&& castOther.getSchedName() != null && this.getSchedName()
				.equals(castOther.getSchedName())))
				&& ((this.getTriggerName() == castOther.getTriggerName()) || (this
						.getTriggerName() != null
						&& castOther.getTriggerName() != null && this
						.getTriggerName().equals(castOther.getTriggerName())))
				&& ((this.getTriggerGroup() == castOther.getTriggerGroup()) || (this
						.getTriggerGroup() != null
						&& castOther.getTriggerGroup() != null && this
						.getTriggerGroup().equals(castOther.getTriggerGroup())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchedName() == null ? 0 : this.getSchedName().hashCode());
		result = 37
				* result
				+ (getTriggerName() == null ? 0 : this.getTriggerName()
						.hashCode());
		result = 37
				* result
				+ (getTriggerGroup() == null ? 0 : this.getTriggerGroup()
						.hashCode());
		return result;
	}

}