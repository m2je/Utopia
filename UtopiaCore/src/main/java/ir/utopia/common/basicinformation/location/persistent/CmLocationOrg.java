package ir.utopia.common.basicinformation.location.persistent;

import java.io.Serializable;

import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CmLocationOrg entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_LOCATION",  uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME","PARENT_ID" }) })
@TableGenerator(name = "LocationOrgSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_LOCATION")
@LookupConfiguration(displayColumns={"code","name"},displayItemSeperator="-")
public class CmLocationOrg extends AbstractCmLocationOrg implements Serializable {

	// Constructors
	
	private static final long serialVersionUID = -6330275498326623805L;
	
	
	/** default constructor */
	public CmLocationOrg() {
	}
}
