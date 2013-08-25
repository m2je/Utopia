package ir.utopia.common.systems.subsystem.persistent;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CmSubsystem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CM_V_SUBSYSTEM")
public class CmVSubsystem extends AbstractCmVSubsystem implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486310416090249009L;

	/** default constructor */
	public CmVSubsystem() {
	}

	

}
