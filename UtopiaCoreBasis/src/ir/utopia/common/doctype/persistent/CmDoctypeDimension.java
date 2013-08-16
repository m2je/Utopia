package ir.utopia.common.doctype.persistent;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CmDoctypeDimension 
 */
@Entity
@Table(name = "CM_DOCTYPE_DIMENSION",  uniqueConstraints = { @UniqueConstraint(columnNames = {
		"CM_DOCTYPE_ID", "CM_SYSTEM_DIMENSION_ID" }) })
@TableGenerator(name = "DocTypeDimensionSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_DOCTYPE_DIMENSION")
public class CmDoctypeDimension extends AbstractCmDoctypeDimension implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -3089344533087865515L;

	/** default constructor */
	public CmDoctypeDimension() {
	}

	

}
