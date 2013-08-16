package ir.utopia.common.basicinformation.supplier.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * CmSupplier entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_SUPPLIER", uniqueConstraints = {})
@TableGenerator(name = "SupplierSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_SUPPLIER")

public class CmSupplier extends AbstractCmSupplier implements java.io.Serializable {

	
	private static final long serialVersionUID = -2223602680122071824L;
	
	
	/** default constructor */
	public CmSupplier() {
		
	}

}
