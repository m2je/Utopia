package ir.utopia.common.systems.subsystem.persistent;


import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CmSubsystem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CM_SUBSYSTEM",  uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@TableGenerator(name = "SubSystemSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_SUBSYSTEM")
	@LookupConfiguration(displayColumns={"abbreviation","name"})	
public class CmSubsystem extends AbstractCmSubsystem implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486310416090249009L;

	/** default constructor */
	public CmSubsystem() {
	}

	

}
