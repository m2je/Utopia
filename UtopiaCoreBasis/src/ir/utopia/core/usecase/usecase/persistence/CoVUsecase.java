package ir.utopia.core.usecase.usecase.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoVUsecase entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_V_USECASE")
public class CoVUsecase extends AbstractCoVUsecase implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -3348395790026813520L;

	/** default constructor */
	public CoVUsecase() {
	}

	

}
