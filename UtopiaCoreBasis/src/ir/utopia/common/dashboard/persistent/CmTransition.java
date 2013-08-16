package ir.utopia.common.dashboard.persistent;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CmTransition 
 */
@Entity
@Table(name = "CM_TRANSITION",  uniqueConstraints = { @UniqueConstraint(columnNames = { "CO_USECASE_ACTION_ID" }) })
@TableGenerator(name = "TransitionSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_TRANSITION")
public class CmTransition extends AbstractCmTransition implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -4969017855728856693L;

	/** default constructor */
	public CmTransition() {
	}

	
}
