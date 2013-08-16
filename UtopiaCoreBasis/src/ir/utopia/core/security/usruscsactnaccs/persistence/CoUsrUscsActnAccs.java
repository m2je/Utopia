package ir.utopia.core.security.usruscsactnaccs.persistence;


import ir.utopia.core.security.SecurityChangeListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoUsrUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USR_USCS_ACTN_ACCS",  uniqueConstraints = {})
@TableGenerator(name = "UsrUscsActnAccsSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USR_USCS_ACTN_ACCS")
@EntityListeners(value=SecurityChangeListener.class)
public class CoUsrUscsActnAccs extends AbstractCoUsrUscsActnAccs  implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1814092114217445116L;

	// Constructors

	/** default constructor */
	public CoUsrUscsActnAccs() {
	}


}
