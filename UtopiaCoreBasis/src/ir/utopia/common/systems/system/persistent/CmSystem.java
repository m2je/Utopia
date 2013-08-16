package ir.utopia.common.systems.system.persistent;


import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
/**
 * CmSystem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@LookupConfiguration(displayColumns="name")
@Entity
@Table(name = "CM_SYSTEM",uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@TableGenerator(name = "SystemSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_SYSTEM")
public class CmSystem extends AbstractCmSystem implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -221014243461966047L;

	/** default constructor */
	public CmSystem() {
	}

}
