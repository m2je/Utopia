package ir.utopia.core.util.scheduler.persistent;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
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
 * AbstractQrtzMailRecepients entity provides the base persistence definition of
 * the QrtzMailRecepients entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractQrtzMailRecepients extends AbstractBasicPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8049803497444794543L;
	private QrtzMailRecepientsId id;
	private QrtzJobDetails qrtzJobDetails;
	private CmBpartner cmBpartner;
	// Constructors

	/** default constructor */
	public AbstractQrtzMailRecepients() {
	}

	/** full constructor */
	public AbstractQrtzMailRecepients(QrtzMailRecepientsId id,
			QrtzJobDetails qrtzJobDetails) {
		this.id = id;
		this.qrtzJobDetails = qrtzJobDetails;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "schedName", column = @Column(name = "SCHED_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 120)),
			@AttributeOverride(name = "jobName", column = @Column(name = "JOB_NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "jobGroup", column = @Column(name = "JOB_GROUP", unique = false, nullable = false, insertable = true, updatable = true, length = 200)),
			@AttributeOverride(name = "cmBpartnerId", column = @Column(name = "CM_BPARTNER_ID",unique = false, nullable = false, insertable = true, updatable = true, length = 200)) })
	public QrtzMailRecepientsId getId() {
		return this.id;
	}

	public void setId(QrtzMailRecepientsId id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "SCHED_NAME", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName = "SCHED_NAME"),
			@JoinColumn(name = "JOB_NAME", unique = false, nullable = false, insertable = false, updatable = false ,referencedColumnName="JOB_NAME"),
			@JoinColumn(name = "JOB_GROUP", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName= "JOB_GROUP") })
	public QrtzJobDetails getQrtzJobDetails() {
		return this.qrtzJobDetails;
	}

	public void setQrtzJobDetails(QrtzJobDetails qrtzJobDetails) {
		this.qrtzJobDetails = qrtzJobDetails;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumns( {
			@JoinColumn(name = "CM_BPARTNER_ID", unique = false, nullable = false, insertable = false, updatable = false,referencedColumnName = "CM_BPARTNER_ID")
	})
	public CmBpartner getCmBpartner() {
		return this.cmBpartner;
	}

	public void setCmBpartner(CmBpartner cmBpartner) {
		this.cmBpartner = cmBpartner;
	}
}