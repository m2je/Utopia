package ir.utopia.core.basicinformation.menu.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_MENU", uniqueConstraints = {})
public class CoVMenu extends AbstractCoVMenu implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6454826861690136073L;

	// Constructors

	/** default constructor */
	public CoVMenu() {
	}

}