package ir.utopia.common.basicinformation.supplier.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CmVSupplier entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_V_SUPPLIER")
public class CmVSupplier extends AbstractCmVSupplier implements java.io.Serializable {

	
	private static final long serialVersionUID = 6165029372046860422L;
	
	
	/** default constructor */
	public CmVSupplier() {
		
	}

}
