package ir.utopia.common.locality.region.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CmRegion entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_REGION", uniqueConstraints = {})
@TableGenerator(name = "RegionSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_REGION")
public class CmRegion extends AbstractCmRegion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -435669624660769229L;

	// Constructors

	/** default constructor */
	public CmRegion() {
	}


}
