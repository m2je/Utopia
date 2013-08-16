package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzMailRecepients entity.
 * 
 * @author 
 */
@Entity
@Table(name = "QRTZ_MAIL_RECEPIENTS", uniqueConstraints = {})
public class QrtzMailRecepients extends AbstractQrtzMailRecepients implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 4481951965557489783L;

	/** default constructor */
	public QrtzMailRecepients() {
	}

	/** full constructor */
	public QrtzMailRecepients(QrtzMailRecepientsId id,
			QrtzJobDetails qrtzJobDetails) {
		super(id, qrtzJobDetails);
	}

}
