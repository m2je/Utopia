package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * QrtzJobDetailsId entity provides the base persistence definition of the
 * etailsId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class QrtzJobDetailsId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6313685170939473302L;
	private String schedName;
	private String jobName;
	private JobGroup jobGroup;

	// Constructors

	/** default constructor */
	public QrtzJobDetailsId() {
	}

	/** full constructor */
	public QrtzJobDetailsId(String schedName, String jobName, JobGroup jobGroup) {
		this.schedName = schedName;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof QrtzJobDetailsId))
			return false;
		QrtzJobDetailsId castOther = (QrtzJobDetailsId) other;

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
						.getJobGroup().equals(castOther.getJobGroup())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchedName() == null ? 0 : this.getSchedName().hashCode());
		result = 37 * result
				+ (getJobName() == null ? 0 : this.getJobName().hashCode());
		result = 37 * result
				+ (getJobGroup() == null ? 0 : this.getJobGroup().hashCode());
		return result;
	}

}