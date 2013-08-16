package ir.utopia.core.util.scheduler.persistent;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractQrtzTriggers entity provides the base persistence definition of the
 * QrtzTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzTriggers implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5777414176959206019L;
	private QrtzTriggersId id;
	private QrtzJobDetails qrtzJobDetails;
	private String description;
	private Long nextFireTime;
	private Long prevFireTime;
	private Long priority;
	private String triggerState;
	private String triggerType;
	private Long startTime;
	private Long endTime;
	private String calendarName;
	private Long misfireInstr;
	private String jobData;
	private Set<QrtzSimpleTriggers> qrtzSimpleTriggerses = new HashSet<QrtzSimpleTriggers>(
			0);
	private Set<QrtzBlobTriggers> qrtzBlobTriggerses = new HashSet<QrtzBlobTriggers>(
			0);
	private Set<QrtzSimpropTriggers> qrtzSimpropTriggerses = new HashSet<QrtzSimpropTriggers>(
			0);
	private Set<QrtzCronTriggers> qrtzCronTriggerses = new HashSet<QrtzCronTriggers>(
			0);

	// Constructors

	/** default constructor */
	public AbstractQrtzTriggers() {
	}

	/** minimal constructor */
	public AbstractQrtzTriggers(QrtzTriggersId id,
			QrtzJobDetails qrtzJobDetails, String triggerState,
			String triggerType, Long startTime) {
		this.id = id;
		this.qrtzJobDetails = qrtzJobDetails;
		this.triggerState = triggerState;
		this.triggerType = triggerType;
		this.startTime = startTime;
	}

	/** full constructor */
	public AbstractQrtzTriggers(QrtzTriggersId id,
			QrtzJobDetails qrtzJobDetails, String description,
			Long nextFireTime, Long prevFireTime, Long priority,
			String triggerState, String triggerType, Long startTime,
			Long endTime, String calendarName, Long misfireInstr,
			String jobData, Set<QrtzSimpleTriggers> qrtzSimpleTriggerses,
			Set<QrtzBlobTriggers> qrtzBlobTriggerses,
			Set<QrtzSimpropTriggers> qrtzSimpropTriggerses,
			Set<QrtzCronTriggers> qrtzCronTriggerses) {
		this.id = id;
		this.qrtzJobDetails = qrtzJobDetails;
		this.description = description;
		this.nextFireTime = nextFireTime;
		this.prevFireTime = prevFireTime;
		this.priority = priority;
		this.triggerState = triggerState;
		this.triggerType = triggerType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.calendarName = calendarName;
		this.misfireInstr = misfireInstr;
		this.jobData = jobData;
		this.qrtzSimpleTriggerses = qrtzSimpleTriggerses;
		this.qrtzBlobTriggerses = qrtzBlobTriggerses;
		this.qrtzSimpropTriggerses = qrtzSimpropTriggerses;
		this.qrtzCronTriggerses = qrtzCronTriggerses;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "triggerName", column = @Column(name = "TRIGGER_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "triggerGroup", column = @Column(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzTriggersId getId() {
		return this.id;
	}

	public void setId(QrtzTriggersId id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "SCHED_NAME", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName="SCHED_NAME"),
			@JoinColumn(name = "JOB_NAME", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName="JOB_NAME"),
			@JoinColumn(name = "JOB_GROUP", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName="JOB_GROUP") })
	public QrtzJobDetails getQrtzJobDetails() {
		return this.qrtzJobDetails;
	}

	public void setQrtzJobDetails(QrtzJobDetails qrtzJobDetails) {
		this.qrtzJobDetails = qrtzJobDetails;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true, length = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "NEXT_FIRE_TIME", unique = false, nullable = true, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getNextFireTime() {
		return this.nextFireTime;
	}

	public void setNextFireTime(Long nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	@Column(name = "PREV_FIRE_TIME", unique = false, nullable = true, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getPrevFireTime() {
		return this.prevFireTime;
	}

	public void setPrevFireTime(Long prevFireTime) {
		this.prevFireTime = prevFireTime;
	}

	@Column(name = "PRIORITY", unique = false, nullable = true, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Column(name = "TRIGGER_STATE", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getTriggerState() {
		return this.triggerState;
	}

	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}

	@Column(name = "TRIGGER_TYPE", unique = false, nullable = false, insertable = true, updatable = true, length = 8)
	public String getTriggerType() {
		return this.triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	@Column(name = "START_TIME", unique = false, nullable = false, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", unique = false, nullable = true, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Column(name = "CALENDAR_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getCalendarName() {
		return this.calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	@Column(name = "MISFIRE_INSTR", unique = false, nullable = true, insertable = true, updatable = true, precision = 2, scale = 0)
	public Long getMisfireInstr() {
		return this.misfireInstr;
	}

	public void setMisfireInstr(Long misfireInstr) {
		this.misfireInstr = misfireInstr;
	}

	@Column(name = "JOB_DATA", unique = false, nullable = true, insertable = true, updatable = true)
	public String getJobData() {
		return this.jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "qrtzTriggers")
	public Set<QrtzSimpleTriggers> getQrtzSimpleTriggerses() {
		return this.qrtzSimpleTriggerses;
	}

	public void setQrtzSimpleTriggerses(
			Set<QrtzSimpleTriggers> qrtzSimpleTriggerses) {
		this.qrtzSimpleTriggerses = qrtzSimpleTriggerses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "qrtzTriggers")
	public Set<QrtzBlobTriggers> getQrtzBlobTriggerses() {
		return this.qrtzBlobTriggerses;
	}

	public void setQrtzBlobTriggerses(Set<QrtzBlobTriggers> qrtzBlobTriggerses) {
		this.qrtzBlobTriggerses = qrtzBlobTriggerses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "qrtzTriggers")
	public Set<QrtzSimpropTriggers> getQrtzSimpropTriggerses() {
		return this.qrtzSimpropTriggerses;
	}

	public void setQrtzSimpropTriggerses(
			Set<QrtzSimpropTriggers> qrtzSimpropTriggerses) {
		this.qrtzSimpropTriggerses = qrtzSimpropTriggerses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "qrtzTriggers")
	public Set<QrtzCronTriggers> getQrtzCronTriggerses() {
		return this.qrtzCronTriggerses;
	}

	public void setQrtzCronTriggerses(Set<QrtzCronTriggers> qrtzCronTriggerses) {
		this.qrtzCronTriggerses = qrtzCronTriggerses;
	}

}