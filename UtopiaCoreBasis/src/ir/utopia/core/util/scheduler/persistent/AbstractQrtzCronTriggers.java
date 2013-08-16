package ir.utopia.core.util.scheduler.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;

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
 * AbstractQrtzCronTriggers entity provides the base persistence definition of
 * the QrtzCronTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzCronTriggers extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -773949470433412257L;
	private QrtzCronTriggersId id;
	private QrtzTriggers qrtzTriggers;
	private String cronExpression;
	private String timeZoneId;

	// Constructors

	/** default constructor */
	public AbstractQrtzCronTriggers() {
	}

	/** minimal constructor */
	public AbstractQrtzCronTriggers(QrtzCronTriggersId id,
			QrtzTriggers qrtzTriggers, String cronExpression) {
		this.id = id;
		this.qrtzTriggers = qrtzTriggers;
		this.cronExpression = cronExpression;
	}

	/** full constructor */
	public AbstractQrtzCronTriggers(QrtzCronTriggersId id,
			QrtzTriggers qrtzTriggers, String cronExpression, String timeZoneId) {
		this.id = id;
		this.qrtzTriggers = qrtzTriggers;
		this.cronExpression = cronExpression;
		this.timeZoneId = timeZoneId;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "triggerName", column = @Column(name = "TRIGGER_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "triggerGroup", column = @Column(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzCronTriggersId getId() {
		return this.id;
	}

	public void setId(QrtzCronTriggersId id) {
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

	@Column(name = "CRON_EXPRESSION", unique = false, nullable = false, insertable = true, updatable = true, length = 120)
	public String getCronExpression() {
		return this.cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Column(name = "TIME_ZONE_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 80)
	public String getTimeZoneId() {
		return this.timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

}