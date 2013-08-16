package ir.utopia.common.locality.country.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVCountry entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_V_COUNTRY")
public class CmVCountry extends AbstractCmVCountry implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -3170348186023510536L;

	/** default constructor */
	public CmVCountry() {
	}


}
