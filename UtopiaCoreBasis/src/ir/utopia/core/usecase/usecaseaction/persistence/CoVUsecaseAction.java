package ir.utopia.core.usecase.usecaseaction.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoUsecaseAction entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_USECASE_ACTION")
public class CoVUsecaseAction extends AbstractCoVUsecaseAction implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6474238858968909514L;

	// Constructors


	/** default constructor */
	public CoVUsecaseAction() {
	}


}
