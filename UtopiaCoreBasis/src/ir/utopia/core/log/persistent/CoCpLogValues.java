package ir.utopia.core.log.persistent;

// default package

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoCpLogValues entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_CP_LOG_VALUES",  uniqueConstraints = {})
public class CoCpLogValues extends AbstractCoCpLogValues implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 360258083878972092L;

	/** default constructor */
	public CoCpLogValues() {
	}

	

}
