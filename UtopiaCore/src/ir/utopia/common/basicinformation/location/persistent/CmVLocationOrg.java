package ir.utopia.common.basicinformation.location.persistent;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CmVLocation entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_V_LOCATION")
public class CmVLocationOrg extends AbstractCmVLocationOrg implements Serializable {

	private static final long serialVersionUID = -6529673574824290461L;

	// Constructors
	
	/** default constructor */
	public CmVLocationOrg(){
		
	}
}
