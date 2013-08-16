package ir.utopia.common.locality.state.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVState entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_V_STATE")
public class CmVState extends AbstractCmVState implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6303918600678146378L;
	/** default constructor */
	public CmVState() {
	}


}
