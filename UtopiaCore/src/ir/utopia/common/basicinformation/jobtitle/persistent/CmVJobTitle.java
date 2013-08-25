package ir.utopia.common.basicinformation.jobtitle.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CmVJobTitle entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_V_JOBTITLE")
public class CmVJobTitle extends AbstractCmVJobTitle implements java.io.Serializable {

	
	private static final long serialVersionUID = -6956206821365124182L;

	
	// Constructors
		/** default constructor */
		public CmVJobTitle() {
		}
}
