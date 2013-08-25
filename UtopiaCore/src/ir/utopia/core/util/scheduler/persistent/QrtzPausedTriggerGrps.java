package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzPausedTriggerGrps entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_PAUSED_TRIGGER_GRPS", uniqueConstraints = {})
public class QrtzPausedTriggerGrps extends AbstractQrtzPausedTriggerGrps
		implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 651500129362678974L;

	/** default constructor */
	public QrtzPausedTriggerGrps() {
	}

	/** full constructor */
	public QrtzPausedTriggerGrps(QrtzPausedTriggerGrpsId id) {
		super(id);
	}

}
