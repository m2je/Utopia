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
 * AbstractQrtzBlobTriggers entity provides the base persistence definition of
 * the QrtzBlobTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzBlobTriggers implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2187953156480424129L;
	private QrtzBlobTriggersId id;
	private QrtzTriggers qrtzTriggers;
	private String blobData;

	// Constructors

	/** default constructor */
	public AbstractQrtzBlobTriggers() {
	}

	/** minimal constructor */
	public AbstractQrtzBlobTriggers(QrtzBlobTriggersId id,
			QrtzTriggers qrtzTriggers) {
		this.id = id;
		this.qrtzTriggers = qrtzTriggers;
	}

	/** full constructor */
	public AbstractQrtzBlobTriggers(QrtzBlobTriggersId id,
			QrtzTriggers qrtzTriggers, String blobData) {
		this.id = id;
		this.qrtzTriggers = qrtzTriggers;
		this.blobData = blobData;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "triggerName", column = @Column(name = "TRIGGER_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "triggerGroup", column = @Column(name = "TRIGGER_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzBlobTriggersId getId() {
		return this.id;
	}

	public void setId(QrtzBlobTriggersId id) {
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

	@Column(name = "BLOB_DATA", unique = false, nullable = true, insertable = true, updatable = true)
	public String getBlobData() {
		return this.blobData;
	}

	public void setBlobData(String blobData) {
		this.blobData = blobData;
	}

}