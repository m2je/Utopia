package ir.utopia.common.basicinformation.jobtitle.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * CmJobTitle entity.
 * 
 * @author Arsalani
 */
@Entity
@Table(name = "CM_JOBTITLE", uniqueConstraints = {})
@TableGenerator(name = "JobTitleSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_JOBTITLE")

public class CmJobTitle extends AbstractCmJobTitle implements java.io.Serializable {

	
	private static final long serialVersionUID = 2856223552547255871L;
	
	// Constructors

		/** default constructor */
	public CmJobTitle(){
		
	}

}
