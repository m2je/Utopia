package ir.utopia.core.util.scheduler.persistent;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_TRIGGERS",uniqueConstraints = {})
public class QrtzTriggers extends AbstractQrtzTriggers implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -190049641066506538L;

	/** default constructor */
	public QrtzTriggers() {
	}

	/** minimal constructor */
	public QrtzTriggers(QrtzTriggersId id, QrtzJobDetails qrtzJobDetails,
			String triggerState, String triggerType, Long startTime) {
		super(id, qrtzJobDetails, triggerState, triggerType, startTime);
	}

	/** full constructor */
	public QrtzTriggers(QrtzTriggersId id, QrtzJobDetails qrtzJobDetails,
			String description, Long nextFireTime, Long prevFireTime,
			Long priority, String triggerState, String triggerType,
			Long startTime, Long endTime, String calendarName,
			Long misfireInstr, String jobData,
			Set<QrtzSimpleTriggers> qrtzSimpleTriggerses,
			Set<QrtzBlobTriggers> qrtzBlobTriggerses,
			Set<QrtzSimpropTriggers> qrtzSimpropTriggerses,
			Set<QrtzCronTriggers> qrtzCronTriggerses) {
		super(id, qrtzJobDetails, description, nextFireTime, prevFireTime,
				priority, triggerState, triggerType, startTime, endTime,
				calendarName, misfireInstr, jobData, qrtzSimpleTriggerses,
				qrtzBlobTriggerses, qrtzSimpropTriggerses, qrtzCronTriggerses);
	}

}
