package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzCronTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_CRON_TRIGGERS",  uniqueConstraints = {})
public class QrtzCronTriggers extends AbstractQrtzCronTriggers implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1575437409959811784L;

	/** default constructor */
	public QrtzCronTriggers() {
	}

	/** minimal constructor */
	public QrtzCronTriggers(QrtzCronTriggersId id, QrtzTriggers qrtzTriggers,
			String cronExpression) {
		super(id, qrtzTriggers, cronExpression);
	}

	/** full constructor */
	public QrtzCronTriggers(QrtzCronTriggersId id, QrtzTriggers qrtzTriggers,
			String cronExpression, String timeZoneId) {
		super(id, qrtzTriggers, cronExpression, timeZoneId);
	}

}
