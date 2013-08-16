package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzBlobTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_BLOB_TRIGGERS", uniqueConstraints = {})
public class QrtzBlobTriggers extends AbstractQrtzBlobTriggers implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1550047706372820730L;

	/** default constructor */
	public QrtzBlobTriggers() {
	}

	/** minimal constructor */
	public QrtzBlobTriggers(QrtzBlobTriggersId id, QrtzTriggers qrtzTriggers) {
		super(id, qrtzTriggers);
	}

	/** full constructor */
	public QrtzBlobTriggers(QrtzBlobTriggersId id, QrtzTriggers qrtzTriggers,
			String blobData) {
		super(id, qrtzTriggers, blobData);
	}

}
