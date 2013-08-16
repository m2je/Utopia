package ir.utopia.common.locality.province.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVProvince entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_V_PROVINCE" )
public class CmVProvince extends AbstractCmVProvince implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733173255004869979L;

	// Constructors

	/** default constructor */
	public CmVProvince() {
	}


}
