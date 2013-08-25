package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractQrtzSimpleTriggers entity provides the base persistence definition of
 * the QrtzSimpleTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzSimpleTriggers implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 836234357518737484L;
	private QrtzSimpleTriggersId id;
	private QrtzTriggers qrtzTriggers;
	private Long repeatCount;
	private Long repeatInterval;
	private Long timesTriggered;

	// Constructors

	/** default constructor */
	public AbstractQrtzSimpleTriggers() {
	}

	/** full constructor */
	public AbstractQrtzSimpleTriggers(QrtzSimpleTriggersId id,
			QrtzTriggers qrtzTriggers, Long repeatCount, Long repeatInterval,
			Long timesTriggered) {
		this.id = id;
		this.qrtzTriggers = qrtzTriggers;
		this.repeatCount = repeatCount;
		this.repeatInterval = repeatInterval;
		this.timesTriggered = timesTriggered;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "triggerName", column = @Column(name = "TRIGGER_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "triggerGroup", column = @Column(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzSimpleTriggersId getId() {
		return this.id;
	}

	public void setId(QrtzSimpleTriggersId id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "SCHED_NAME", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName="SCHED_NAME"),
			@JoinColumn(name = "TRIGGER_NAME", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName="TRIGGER_NAME"),
			@JoinColumn(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName="TRIGGER_GROUP") })
	public QrtzTriggers getQrtzTriggers() {
		return this.qrtzTriggers;
	}

	public void setQrtzTriggers(QrtzTriggers qrtzTriggers) {
		this.qrtzTriggers = qrtzTriggers;
	}

	@Column(name = "REPEAT_COUNT", unique = false, nullable = false, insertable = true, updatable = true, precision = 7, scale = 0)
	public Long getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(Long repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Column(name = "REPEAT_INTERVAL", unique = false, nullable = false, insertable = true, updatable = true, precision = 12, scale = 0)
	public Long getRepeatInterval() {
		return this.repeatInterval;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	@Column(name = "TIMES_TRIGGERED", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getTimesTriggered() {
		return this.timesTriggered;
	}

	public void setTimesTriggered(Long timesTriggered) {
		this.timesTriggered = timesTriggered;
	}

}