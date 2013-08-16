package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzLocks entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_LOCKS", uniqueConstraints = {})
public class QrtzLocks extends AbstractQrtzLocks implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -7627855219756799207L;

	/** default constructor */
	public QrtzLocks() {
	}

	/** full constructor */
	public QrtzLocks(QrtzLocksId id) {
		super(id);
	}

}
