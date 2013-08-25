package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractQrtzSchedulerState entity provides the base persistence definition of
 * the QrtzSchedulerState entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzSchedulerState implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2008638817194755023L;
	private QrtzSchedulerStateId id;
	private Long lastCheckinTime;
	private Long checkinInterval;

	// Constructors

	/** default constructor */
	public AbstractQrtzSchedulerState() {
	}

	/** full constructor */
	public AbstractQrtzSchedulerState(QrtzSchedulerStateId id,
			Long lastCheckinTime, Long checkinInterval) {
		this.id = id;
		this.lastCheckinTime = lastCheckinTime;
		this.checkinInterval = checkinInterval;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "instanceName", column = @Column(name = "INSTANCE_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzSchedulerStateId getId() {
		return this.id;
	}

	public void setId(QrtzSchedulerStateId id) {
		this.id = id;
	}

	@Column(name = "LAST_CHECKIN_TIME", unique = false, nullable = false, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getLastCheckinTime() {
		return this.lastCheckinTime;
	}

	public void setLastCheckinTime(Long lastCheckinTime) {
		this.lastCheckinTime = lastCheckinTime;
	}

	@Column(name = "CHECKIN_INTERVAL", unique = false, nullable = false, insertable = true, updatable = true, precision = 13, scale = 0)
	public Long getCheckinInterval() {
		return this.checkinInterval;
	}

	public void setCheckinInterval(Long checkinInterval) {
		this.checkinInterval = checkinInterval;
	}

}