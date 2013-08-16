package ir.utopia.core.util.scheduler.persistent;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractQrtzJobDetails entity provides the base persistence definition of the
 * QrtzJobDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzJobDetails implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6920294194009820084L;
	private QrtzJobDetailsId id;
	private String description;
	private String jobClassName;
	private String isDurable;
	private String isNonconcurrent;
	private String isUpdateData;
	private String requestsRecovery;
	private String jobData;
	private Set<QrtzTriggers> qrtzTriggerses = new HashSet<QrtzTriggers>(0);
	private Set<QrtzMailRecepients> qrtzMailRecepientses = new HashSet<QrtzMailRecepients>(
			0);
	// Constructors

	/** default constructor */
	public AbstractQrtzJobDetails() {
	}

	/** minimal constructor */
	public AbstractQrtzJobDetails(QrtzJobDetailsId id, String jobClassName,
			String isDurable, String isNonconcurrent, String isUpdateData,
			String requestsRecovery) {
		this.id = id;
		this.jobClassName = jobClassName;
		this.isDurable = isDurable;
		this.isNonconcurrent = isNonconcurrent;
		this.isUpdateData = isUpdateData;
		this.requestsRecovery = requestsRecovery;
	}

	/** full constructor */
	public AbstractQrtzJobDetails(QrtzJobDetailsId id, String description,
			String jobClassName, String isDurable, String isNonconcurrent,
			String isUpdateData, String requestsRecovery, String jobData,
			Set<QrtzTriggers> qrtzTriggerses) {
		this.id = id;
		this.description = description;
		this.jobClassName = jobClassName;
		this.isDurable = isDurable;
		this.isNonconcurrent = isNonconcurrent;
		this.isUpdateData = isUpdateData;
		this.requestsRecovery = requestsRecovery;
		this.jobData = jobData;
		this.qrtzTriggerses = qrtzTriggerses;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "jobName", column = @Column(name = "JOB_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "jobGroup", column = @Column(name = "JOB_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzJobDetailsId getId() {
		return this.id;
	}

	public void setId(QrtzJobDetailsId id) {
		this.id = id;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true, length = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "JOB_CLASS_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 250)
	public String getJobClassName() {
		return this.jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	@Column(name = "IS_DURABLE", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsDurable() {
		return this.isDurable;
	}

	public void setIsDurable(String isDurable) {
		this.isDurable = isDurable;
	}

	@Column(name = "IS_NONCONCURRENT", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsNonconcurrent() {
		return this.isNonconcurrent;
	}

	public void setIsNonconcurrent(String isNonconcurrent) {
		this.isNonconcurrent = isNonconcurrent;
	}

	@Column(name = "IS_UPDATE_DATA", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsUpdateData() {
		return this.isUpdateData;
	}

	public void setIsUpdateData(String isUpdateData) {
		this.isUpdateData = isUpdateData;
	}

	@Column(name = "REQUESTS_RECOVERY", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getRequestsRecovery() {
		return this.requestsRecovery;
	}

	public void setRequestsRecovery(String requestsRecovery) {
		this.requestsRecovery = requestsRecovery;
	}

	@Column(name = "JOB_DATA", unique = false, nullable = true, insertable = true, updatable = true)
	public String getJobData() {
		return this.jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "qrtzJobDetails")
	public Set<QrtzTriggers> getQrtzTriggerses() {
		return this.qrtzTriggerses;
	}

	public void setQrtzTriggerses(Set<QrtzTriggers> qrtzTriggerses) {
		this.qrtzTriggerses = qrtzTriggerses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "qrtzJobDetails")
	public Set<QrtzMailRecepients> getQrtzMailRecepientses() {
		return this.qrtzMailRecepientses;
	}

	public void setQrtzMailRecepientses(
			Set<QrtzMailRecepients> qrtzMailRecepientses) {
		this.qrtzMailRecepientses = qrtzMailRecepientses;
	}
}