package ir.utopia.core.importer.setting.persistent;

// default package

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoImportLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_IMPORT_LOG",  uniqueConstraints = {})
@TableGenerator(name = "ImporterLogSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_IMPORT_LOG")
public class CoImportLog extends AbstractCoImportLog implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1466853363040038403L;

	/** default constructor */
	public CoImportLog() {
	}

	

}
