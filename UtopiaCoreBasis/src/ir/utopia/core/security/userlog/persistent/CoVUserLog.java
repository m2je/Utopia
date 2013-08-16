package ir.utopia.core.security.userlog.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoVUserLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_USER_LOG")
public class CoVUserLog extends AbstractCoVUserLog implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043142874815302561L;

	/** default constructor */
	public CoVUserLog() {
	}

	
}
