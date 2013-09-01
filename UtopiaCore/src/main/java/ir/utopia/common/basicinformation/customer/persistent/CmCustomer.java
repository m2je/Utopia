package ir.utopia.common.basicinformation.customer.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * BpBill entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CM_CUSTOMER", uniqueConstraints = {})
@TableGenerator(name = "CustomerSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_CUSTOMER")
	
public class CmCustomer extends AbstractCmCustomer implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 2963056961376103567L;

	/** default constructor */
	public CmCustomer() {
	}

}
