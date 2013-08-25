package ir.utopia.common.locality.state.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmState entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_STATE",  uniqueConstraints = {})
public class CmState extends AbstractCmState implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -4770560671624210890L;

	/** default constructor */
	public CmState() {
	}


}
