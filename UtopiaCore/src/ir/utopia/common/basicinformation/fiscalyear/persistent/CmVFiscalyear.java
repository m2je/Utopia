package ir.utopia.common.basicinformation.fiscalyear.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVFiscalyear entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_V_FISCALYEAR")
public class CmVFiscalyear extends AbstractCmVFiscalyear implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 3732840030470225111L;

	/** default constructor */
	public CmVFiscalyear() {
	}


}
