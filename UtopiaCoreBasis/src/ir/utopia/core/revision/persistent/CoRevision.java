package ir.utopia.core.revision.persistent;



import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoRevision
 */
@Entity
@Table(name = "CO_REVISION", uniqueConstraints = {})
@TableGenerator(name = "RevisionSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_REVISION")
public class CoRevision extends AbstractCoRevision implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 9017529778023535801L;

	/** default constructor */
	public CoRevision() {
	}


}
