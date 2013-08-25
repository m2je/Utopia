package ir.utopia.core.importer.setting.persistent;

// default package

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoImportLogMsg entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_IMPORT_LOG_MSG" )
@TableGenerator(name = "ImporterLogMessageSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_IMPORTER_SETTING")
public class CoImportLogMsg extends AbstractCoImportLogMsg implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 199711208141932090L;

	/** default constructor */
	public CoImportLogMsg() {
	}

}
