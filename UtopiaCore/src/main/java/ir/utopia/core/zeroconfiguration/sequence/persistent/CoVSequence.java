package ir.utopia.core.zeroconfiguration.sequence.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoSequence entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_SEQUENCE")
public class CoVSequence extends AbstractCoVSequence implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7128120490656499428L;

	// Constructors


	/** default constructor */
	public CoVSequence() {
	}


}
