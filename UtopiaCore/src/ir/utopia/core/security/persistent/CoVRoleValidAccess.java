package ir.utopia.core.security.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoVRoleValidAccess entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_ROLE_VALID_ACCESS")
public class CoVRoleValidAccess extends AbstractCoVRoleValidAccess implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -4676773335604070703L;

	/** default constructor */
	public CoVRoleValidAccess() {
	}

	/** full constructor */
	public CoVRoleValidAccess(CoVRoleValidAccessId id) {
		super(id);
	}

}
