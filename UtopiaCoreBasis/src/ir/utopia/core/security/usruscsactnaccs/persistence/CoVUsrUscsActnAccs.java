package ir.utopia.core.security.usruscsactnaccs.persistence;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoUsrUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_USR_USCS_ACTN_ACCS",  uniqueConstraints = {})
public class CoVUsrUscsActnAccs extends AbstractCoVUsrUscsActnAccs  implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8682993990865892576L;

	// Constructors

	/** default constructor */
	public CoVUsrUscsActnAccs() {
	}


}
