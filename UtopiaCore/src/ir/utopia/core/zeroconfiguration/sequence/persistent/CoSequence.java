package ir.utopia.core.zeroconfiguration.sequence.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * CoSequence entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_SEQUENCE",  uniqueConstraints = { @UniqueConstraint(columnNames = { "TABLENAME" }) })
public class CoSequence extends AbstractCoSequence implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -7948421038128010049L;

	/** default constructor */
	public CoSequence() {
	}


}
