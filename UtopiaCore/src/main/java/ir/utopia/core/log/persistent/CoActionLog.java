package ir.utopia.core.log.persistent;



import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoActionLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_ACTION_LOG",uniqueConstraints = {})
@TableGenerator(name = "ActionLogSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_ACTION_LOG")
public class CoActionLog extends AbstractCoActionLog implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -2565211958844115228L;

	/** default constructor */
	public CoActionLog() {
	}



}
