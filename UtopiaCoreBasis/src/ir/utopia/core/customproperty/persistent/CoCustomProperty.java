package ir.utopia.core.customproperty.persistent;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CoCustomProperty entity.
 * 
 * @author M2je
 */

@Entity
@Table(name = "CO_CUSTOM_PROPERTY",  uniqueConstraints = { @UniqueConstraint(columnNames = {
		"PROPERTY_NAME", "CO_USECASE_ID", "RECORD_ID" }) })
@TableGenerator(name = "CustomPropertyGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_CUSTOM_PROPERTY")
		@NamedQueries(
{
@NamedQuery(name="DeleteUsecaseCustomProperty",query="DELETE  from CoCustomProperty model where model.coUsecase.coUsecaseId=:coUsecaseId and model.recordId=:recordId")})
public class CoCustomProperty extends AbstractCoCustomProperty implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 426633351759470544L;
	public static final String DELETE_USECASE_CUSTOM_PROPERTIES = "DeleteUsecaseCustomProperty";

	/** default constructor */
	public CoCustomProperty() {
	}

	

}
