package ir.utopia.core.security.userrole.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoUserRoles entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_USER_ROLES", uniqueConstraints = {})
public class CoVUserRoles extends AbstractCoVUserRoles implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3756106513774769224L;

	// Constructors

	/** default constructor */
	public CoVUserRoles() {
	}

	

}
