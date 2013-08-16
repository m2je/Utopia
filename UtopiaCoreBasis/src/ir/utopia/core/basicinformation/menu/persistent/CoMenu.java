package ir.utopia.core.basicinformation.menu.persistent;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_MENU", uniqueConstraints = {})
@TableGenerator(name = "MenuSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_MENU")
@EntityListeners(ir.utopia.core.basicinformation.menu.persistent.CoMenuListener.class)
public class CoMenu extends AbstractCoMenu implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 2730011512762629988L;

	/** default constructor */
	public CoMenu() {
	}

}
