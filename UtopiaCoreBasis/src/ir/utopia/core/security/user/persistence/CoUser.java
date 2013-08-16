package ir.utopia.core.security.user.persistence;

import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CoUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USER", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "CM_BPARTNER_ID" }),
		@UniqueConstraint(columnNames = { "USERNAME" }) })
@TableGenerator(name = "UserSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USER")
@LookupConfiguration(displayColumns={"username","cmBpartner.name","cmBpartner.secoundName"},displayItemSeperator="-")
public class CoUser extends AbstractCoUser implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 4888068698179249948L;

	/** default constructor */
	public CoUser() {
	}


}
