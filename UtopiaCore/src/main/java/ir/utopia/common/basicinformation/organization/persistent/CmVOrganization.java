package ir.utopia.common.basicinformation.organization.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "CM_V_ORGANIZATION")
public class CmVOrganization extends AbstractCmVOrganization implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8180180758996422538L;

	/** default constructor */
	public CmVOrganization() {
	}

	

}
