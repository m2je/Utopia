package ir.utopia.common.locality.currency.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVCurrency entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_V_CURRENCY")
public class CmVCurrency extends AbstractCmVCurrency implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1302293341787810757L;

	// Constructors

	/** default constructor */
	public CmVCurrency() {
	}

}