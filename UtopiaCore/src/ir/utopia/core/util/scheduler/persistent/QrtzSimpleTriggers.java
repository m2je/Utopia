package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzSimpleTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_SIMPLE_TRIGGERS",  uniqueConstraints = {})
public class QrtzSimpleTriggers extends AbstractQrtzSimpleTriggers implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -8756664772425392279L;

	/** default constructor */
	public QrtzSimpleTriggers() {
	}

	/** full constructor */
	public QrtzSimpleTriggers(QrtzSimpleTriggersId id,
			QrtzTriggers qrtzTriggers, Long repeatCount, Long repeatInterval,
			Long timesTriggered) {
		super(id, qrtzTriggers, repeatCount, repeatInterval, timesTriggered);
	}

}
