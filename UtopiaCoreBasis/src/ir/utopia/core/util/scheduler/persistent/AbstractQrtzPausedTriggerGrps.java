package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractQrtzPausedTriggerGrps entity provides the base persistence definition
 * of the QrtzPausedTriggerGrps entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzPausedTriggerGrps implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2558080347371460434L;
	private QrtzPausedTriggerGrpsId id;

	// Constructors

	/** default constructor */
	public AbstractQrtzPausedTriggerGrps() {
	}

	/** full constructor */
	public AbstractQrtzPausedTriggerGrps(QrtzPausedTriggerGrpsId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "triggerGroup", column = @Column(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzPausedTriggerGrpsId getId() {
		return this.id;
	}

	public void setId(QrtzPausedTriggerGrpsId id) {
		this.id = id;
	}

}