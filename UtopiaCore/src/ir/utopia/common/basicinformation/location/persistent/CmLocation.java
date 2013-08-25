package ir.utopia.common.basicinformation.location.persistent;

import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CmLocation entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_LOCATION",  uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME","PARENT_ID" }) })
@TableGenerator(name = "LocationSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_LOCATION")
@LookupConfiguration(displayColumns={"code","name"},displayItemSeperator="-")
public class CmLocation extends AbstractCmLocation implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8180180758996422538L;

	/** default constructor */
	public CmLocation() {
	}

	

}
