package ir.utopia.core.security.userusecaseacss.persistent;

import ir.utopia.core.security.SecurityChangeListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoUserUsecaseAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USER_USECASE_ACSS",  uniqueConstraints = {})
@TableGenerator(name = "UsrUscsAccsSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USER_USECASE_ACSS")
@EntityListeners(value=SecurityChangeListener.class)
		public class CoUserUsecaseAcss extends AbstractCoUserUsecaseAcss implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3221158708315829134L;

	// Constructors

	/** default constructor */
	public CoUserUsecaseAcss() {
	}


}
