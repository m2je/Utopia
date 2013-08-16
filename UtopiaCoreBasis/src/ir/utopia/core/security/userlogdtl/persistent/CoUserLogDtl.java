package ir.utopia.core.security.userlogdtl.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoUserLogDtl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USER_LOG_DTL", uniqueConstraints = {})
@TableGenerator(name = "UserSequenceLogDTLGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USER_LOG_DTL",allocationSize=200)
public class CoUserLogDtl extends AbstractCoUserLogDtl implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1352349400617405198L;

	/** default constructor */
	public CoUserLogDtl() {
	}

	

}
