package ir.utopia.core.log.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoActionLogDtl entity.
 * 
 * @author
 */
@Entity
@Table(name = "CO_ACTION_LOG_DTL",uniqueConstraints = {})
@TableGenerator(name = "ActionDtlLogSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_ACTION_LOG_DTL")
public class CoActionLogDtl extends AbstractCoActionLogDtl implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8391886985206980596L;

	/** default constructor */
	public CoActionLogDtl() {
	}

	/** minimal constructor */
	public CoActionLogDtl(Long coActionLogDtlId, CoActionLog coActionLog,
			ActionLogMessageType type) {
		super(coActionLogDtlId, coActionLog, type);
	}

	/** full constructor */
	public CoActionLogDtl(Long coActionLogDtlId, CoActionLog coActionLog,
			ActionLogMessageType type, int extendedType, String referenceKey1,
			String referenceKey2, String referenceKey3, String message) {
		super(coActionLogDtlId, coActionLog, type, extendedType, referenceKey1,
				referenceKey2, referenceKey3, message);
	}

}
