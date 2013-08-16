package ir.utopia.core.util.scheduler.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzSimpropTriggers entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_SIMPROP_TRIGGERS",  uniqueConstraints = {})
public class QrtzSimpropTriggers extends AbstractQrtzSimpropTriggers implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8852455945113001465L;

	/** default constructor */
	public QrtzSimpropTriggers() {
	}

	/** minimal constructor */
	public QrtzSimpropTriggers(QrtzSimpropTriggersId id,
			QrtzTriggers qrtzTriggers) {
		super(id, qrtzTriggers);
	}

	/** full constructor */
	public QrtzSimpropTriggers(QrtzSimpropTriggersId id,
			QrtzTriggers qrtzTriggers, String strProp1, String strProp2,
			String strProp3, Long intProp1, Long intProp2, Long longProp1,
			Long longProp2, Double decProp1, Double decProp2, String boolProp1,
			String boolProp2) {
		super(id, qrtzTriggers, strProp1, strProp2, strProp3, intProp1,
				intProp2, longProp1, longProp2, decProp1, decProp2, boolProp1,
				boolProp2);
	}

}
