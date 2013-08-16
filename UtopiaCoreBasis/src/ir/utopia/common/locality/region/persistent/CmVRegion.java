package ir.utopia.common.locality.region.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmVRegion entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CM_V_REGION")
public class CmVRegion extends AbstractCmVRegion implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8780175925253942902L;

	// Constructors

	/** default constructor */
	public CmVRegion() {
	}


}
