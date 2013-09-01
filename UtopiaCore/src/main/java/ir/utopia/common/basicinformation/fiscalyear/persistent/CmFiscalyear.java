package ir.utopia.common.basicinformation.fiscalyear.persistent;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CmFiscalyear entity.
 * 
 * @author Jahani
 */
@Entity
@Table(name = "CM_FISCALYEAR", uniqueConstraints = {})
@TableGenerator(name = "FiscalYearSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_FISCALYEAR")
@NamedQueries(
{
	@NamedQuery(name=CmFiscalyear.FIND_CURRENT_FISCAL_YEAR_QUERY,query="select model from CmFiscalyear model where model.startdate<=:intendedDate and model.enddate>=:intendedDate and model.isactive=:isactive")
})
public class CmFiscalyear extends AbstractCmFiscalyear implements
		java.io.Serializable {
	
	public static final String FIND_CURRENT_FISCAL_YEAR_QUERY="__findCurrentFiscalYearQuery";
	/**
	 * 
	 */
	private static final long serialVersionUID = 3271353878463682873L;

	// Constructors

	/** default constructor */
	public CmFiscalyear() {
	}


}
