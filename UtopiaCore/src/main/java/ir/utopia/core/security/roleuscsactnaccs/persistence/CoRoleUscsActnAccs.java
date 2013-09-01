package ir.utopia.core.security.roleuscsactnaccs.persistence;

import ir.utopia.core.security.SecurityChangeListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoRoleUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_ROLE_USCS_ACTN_ACCS",  uniqueConstraints = {})
@TableGenerator(name = "RoleUscsActnAccsSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_ROLE_USCS_ACTN_ACCS")
@EntityListeners(value=SecurityChangeListener.class)
public class CoRoleUscsActnAccs extends AbstractCoRoleUscsActnAccs implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -7765216543667742336L;

	/** default constructor */
	public CoRoleUscsActnAccs() {
	}

	

}
