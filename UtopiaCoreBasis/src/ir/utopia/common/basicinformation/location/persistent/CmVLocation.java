package ir.utopia.common.basicinformation.location.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CmVLocation entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_V_LOCATION")
public class CmVLocation extends AbstractCmVLocation implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9108097794081862492L;
	
	// Constructors
	
	/** default constructor */
	public CmVLocation(){
		
	}

}
