package ir.utopia.common.basicinformation.employee.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * CmEmployee entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_EMPLOYEE", uniqueConstraints = {})
@TableGenerator(name = "EmployeeSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_EMPLOYEE")

public class CmEmployee extends AbstractCmEmployee implements java.io.Serializable {

	
	private static final long serialVersionUID = -8181078487850645261L;
	
	
	/** default constructor */
	public CmEmployee(){
		
	}

}
