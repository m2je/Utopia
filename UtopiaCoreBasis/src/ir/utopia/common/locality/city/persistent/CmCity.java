package ir.utopia.common.locality.city.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CmCity entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_CITY", uniqueConstraints = {})
@TableGenerator(name = "CitySequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_CITY")

public class CmCity extends AbstractCmCity implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -5253421400728004424L;

	/** default constructor */
	public CmCity() {
	}


}
