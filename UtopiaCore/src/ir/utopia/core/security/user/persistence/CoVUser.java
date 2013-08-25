package ir.utopia.core.security.user.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_USER")
public class CoVUser extends AbstractCoVUser implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 4888068698179249948L;

	/** default constructor */
	public CoVUser() {
	}

}
