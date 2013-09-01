package ir.utopia.core.log.persistent;

// default package

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * CoCpLogConfig entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_CP_LOG_CONFIG",  uniqueConstraints = {
		@UniqueConstraint(columnNames = { "CM_SUBSYSTEM_ID", "CO_USER_ID",
				"CO_ROLE_ID", "CO_ACTION_ID" }),
		@UniqueConstraint(columnNames = { "CO_USECASE_ID", "CO_USER_ID",
				"CO_ROLE_ID" }),
		@UniqueConstraint(columnNames = { "CM_SYSTEM_ID", "CO_USER_ID",
				"CO_ROLE_ID", "CO_ACTION_ID" }),
		@UniqueConstraint(columnNames = { "CO_USECASE_ACTION_ID", "CO_USER_ID",
				"CO_ROLE_ID" }) })
//TODO used to config the DB Action logger(not implemented yet)				
public class CoCpLogConfig extends AbstractCoCpLogConfig implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 6112972661937971371L;

	/** default constructor */
	public CoCpLogConfig() {
	}

	
}
