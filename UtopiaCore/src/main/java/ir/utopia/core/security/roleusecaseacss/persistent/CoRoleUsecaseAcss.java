package ir.utopia.core.security.roleusecaseacss.persistent;

import ir.utopia.core.security.SecurityChangeListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoRoleUsecaseAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_ROLE_USECASE_ACSS",  uniqueConstraints = {})
@TableGenerator(name = "RoleUsecaseAcssSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_ROLE_USECASE_ACSS")
@EntityListeners(value=SecurityChangeListener.class)
public class CoRoleUsecaseAcss extends AbstractCoRoleUsecaseAcss implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6005147623109616143L;

	// Constructors

	/** default constructor */
	public CoRoleUsecaseAcss() {
	}

}
