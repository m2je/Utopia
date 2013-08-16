package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzFiredTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_FIRED_TRIGGERS",  uniqueConstraints = {})
public class QrtzFiredTriggers extends AbstractQrtzFiredTriggers implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1327281800834503682L;

	/** default constructor */
	public QrtzFiredTriggers() {
	}

	/** minimal constructor */
	public QrtzFiredTriggers(QrtzFiredTriggersId id, String triggerName,
			String triggerGroup, String instanceName, Long firedTime,
			Long priority, String state) {
		super(id, triggerName, triggerGroup, instanceName, firedTime, priority,
				state);
	}

	/** full constructor */
	public QrtzFiredTriggers(QrtzFiredTriggersId id, String triggerName,
			String triggerGroup, String instanceName, Long firedTime,
			Long priority, String state, String jobName, String jobGroup,
			String isNonconcurrent, String requestsRecovery) {
		super(id, triggerName, triggerGroup, instanceName, firedTime, priority,
				state, jobName, jobGroup, isNonconcurrent, requestsRecovery);
	}

}
