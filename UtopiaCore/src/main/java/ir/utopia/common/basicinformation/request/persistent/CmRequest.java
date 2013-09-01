package ir.utopia.common.basicinformation.request.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;



@Entity
@Table(name = "CM_REQUEST",  uniqueConstraints = {})
@TableGenerator(name = "RequestSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_REQUEST")


public class CmRequest extends AbstractCmRequest implements java.io.Serializable {

	
	private static final long serialVersionUID = 7531396595435378569L;
	
	
	public CmRequest() {
		
	}

}
