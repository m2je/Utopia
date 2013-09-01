package ir.utopia.common.basicinformation.employee.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CmVEmployee entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_V_EMPLOYEE")
public class CmVEmployee extends AbstractCmVEmployee implements java.io.Serializable {

	
	private static final long serialVersionUID = 3637655774405669521L;

	
	/** default constructor */
	public CmVEmployee(){
		
	}
}
