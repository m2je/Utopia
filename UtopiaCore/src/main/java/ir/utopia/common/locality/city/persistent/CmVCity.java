package ir.utopia.common.locality.city.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVCity entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_V_CITY")
public class CmVCity extends AbstractCmVCity implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -8280536159732046256L;

	/** default constructor */
	public CmVCity() {
	}

}
