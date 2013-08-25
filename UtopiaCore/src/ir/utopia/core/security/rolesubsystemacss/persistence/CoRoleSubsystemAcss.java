package ir.utopia.core.security.rolesubsystemacss.persistence;

import ir.utopia.core.security.SecurityChangeListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoRoleSubsystemAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_ROLE_SUBSYSTEM_ACSS",  uniqueConstraints = {})
@TableGenerator(name = "RoleSubsystemAcssSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_ROLE_SUBSYSTEM_ACSS")
@EntityListeners(value=SecurityChangeListener.class)		
public class CoRoleSubsystemAcss extends AbstractCoRoleSubsystemAcss implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -3819531876442156967L;

	/** default constructor */
	public CoRoleSubsystemAcss() {
	}

	

}
