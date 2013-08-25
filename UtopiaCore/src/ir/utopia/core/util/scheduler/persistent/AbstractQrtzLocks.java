package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractQrtzLocks entity provides the base persistence definition of the
 * QrtzLocks entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzLocks implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2339917363480735251L;
	private QrtzLocksId id;

	// Constructors

	/** default constructor */
	public AbstractQrtzLocks() {
	}

	/** full constructor */
	public AbstractQrtzLocks(QrtzLocksId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "lockName", column = @Column(name = "LOCK_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 40)) })
	public QrtzLocksId getId() {
		return this.id;
	}

	public void setId(QrtzLocksId id) {
		this.id = id;
	}

}