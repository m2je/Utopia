package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractQrtzFiredTriggers entity provides the base persistence definition of
 * the QrtzFiredTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzFiredTriggers implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2489578158394107943L;
	private QrtzFiredTriggersId id;
	private String triggerName;
	private String triggerGroup;
	private String instanceName;
	private Long firedTime;
	private Long priority;
	private String state;
	private String jobName;
	private String jobGroup;
	private String isNonconcurrent;
	private String requestsRecovery;

	// Constructors

	/** default constructor */
	public AbstractQrtzFiredTriggers() {
	}

	/** minimal constructor */
	public AbstractQrtzFiredTriggers(QrtzFiredTriggersId id,
			String triggerName, String triggerGroup, String instanceName,
			Long firedTime, Long priority, String state) {
		this.id = id;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.instanceName = instanceName;
		this.firedTime = firedTime;
		this.priority = priority;
		this.state = state;
	}

	/** full constructor */
	public AbstractQrtzFiredTriggers(QrtzFiredTriggersId id,
			String triggerName, String triggerGroup, String instanceName,
			Long firedTime, Long priority, String state, String jobName,
			String jobGroup, String isNonconcurrent, String requestsRecovery) {
		this.id = id;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.instanceName = instanceName;
		this.firedTime = firedTime;
		this.priority = priority;
		this.state = state;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.isNonconcurrent = isNonconcurrent;
		this.requestsRecovery = requestsRecovery;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "entryId", column = @Column(name = "ENTRY_ID", unique = false, nullable = false, insertable = true, updatable = true, length = 95)) })
	public QrtzFiredTriggersId getId() {
		return this.id;
	}

	public void setId(QrtzFiredTriggersId id) {
		this.id = id;
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

	@Column(name = "INSTANCE_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getInstanceName() {
		return this.instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@Column(name = "FIRED_TIME", unique = false, nullable = false, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getFiredTime() {
		return this.firedTime;
	}

	public void setFiredTime(Long firedTime) {
		this.firedTime = firedTime;
	}

	@Column(name = "PRIORITY", unique = false, nullable = false, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Column(name = "STATE", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "JOB_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "JOB_GROUP", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getJobGroup() {
		return this.jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	@Column(name = "IS_NONCONCURRENT", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsNonconcurrent() {
		return this.isNonconcurrent;
	}

	public void setIsNonconcurrent(String isNonconcurrent) {
		this.isNonconcurrent = isNonconcurrent;
	}

	@Column(name = "REQUESTS_RECOVERY", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getRequestsRecovery() {
		return this.requestsRecovery;
	}

	public void setRequestsRecovery(String requestsRecovery) {
		this.requestsRecovery = requestsRecovery;
	}

}