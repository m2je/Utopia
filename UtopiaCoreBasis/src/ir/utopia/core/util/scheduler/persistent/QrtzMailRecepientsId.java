package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * QrtzMailRecepientsId entity provides the base persistence definition of the
 * RecepientsId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class QrtzMailRecepientsId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 27523410325953376L;
	private String schedName;
	private String jobName;
	private JobGroup jobGroup;
	private Long cmBpartnerId;

	// Constructors

	/** default constructor */
	public QrtzMailRecepientsId() {
	}

	/** full constructor */
	public QrtzMailRecepientsId(String schedName, String jobName,
			JobGroup jobGroup,  Long cmBpartnerId) {
		this.schedName = schedName;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.cmBpartnerId = cmBpartnerId;
	}

	// Property accessors

	@Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)
	public String getSchedName() {
		return this.schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	@Column(name = "JOB_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "JOB_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public JobGroup getJobGroup() {
		return this.jobGroup;
	}

	public void setJobGroup(JobGroup jobGroup) {
		this.jobGroup = jobGroup;
	}
	
		
	@Column(name = "CM_BPARTNER_ID", unique = false, nullable = false, insertable = true, updatable = true, length = 200 )
	public Long getCmBpartnerId() {
		return this.cmBpartnerId;
	}

	public void setCmBpartnerId(Long cmBpartnerId) {
		this.cmBpartnerId = cmBpartnerId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof QrtzMailRecepientsId))
			return false;
		QrtzMailRecepientsId castOther = (QrtzMailRecepientsId) other;

		return ((this.getSchedName() == castOther.getSchedName()) || (this
				.getSchedName() != null
				&& castOther.getSchedName() != null && this.getSchedName()
				.equals(castOther.getSchedName())))
				&& ((this.getJobName() == castOther.getJobName()) || (this
						.getJobName() != null
						&& castOther.getJobName() != null && this.getJobName()
						.equals(castOther.getJobName())))
				&& ((this.getJobGroup() == castOther.getJobGroup()) || (this
						.getJobGroup() != null
						&& castOther.getJobGroup() != null && this
						.getJobGroup().equals(castOther.getJobGroup())))
				&& ((this.getCmBpartnerId() == castOther.getCmBpartnerId()) || (this
						.getCmBpartnerId() != null
						&& castOther.getCmBpartnerId() != null && this
						.getCmBpartnerId().equals(castOther.getCmBpartnerId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchedName() == null ? 0 : this.getSchedName().hashCode());
		result = 37 * result
				+ (getJobName() == null ? 0 : this.getJobName().hashCode());
		result = 37 * result
				+ (getJobGroup() == null ? 0 : this.getJobGroup().hashCode());
		result = 37
				* result
				+ (getCmBpartnerId() == null ? 0 : this.getCmBpartnerId()
						.hashCode());
		return result;
	}

}