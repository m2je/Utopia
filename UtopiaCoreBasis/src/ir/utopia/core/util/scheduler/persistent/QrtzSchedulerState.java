package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzSchedulerState entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_SCHEDULER_STATE", uniqueConstraints = {})
public class QrtzSchedulerState extends AbstractQrtzSchedulerState implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1832573395972272491L;

	/** default constructor */
	public QrtzSchedulerState() {
	}

	/** full constructor */
	public QrtzSchedulerState(QrtzSchedulerStateId id, Long lastCheckinTime,
			Long checkinInterval) {
		super(id, lastCheckinTime, checkinInterval);
	}

}
