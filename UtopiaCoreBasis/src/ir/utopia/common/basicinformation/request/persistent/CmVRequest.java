package ir.utopia.common.basicinformation.request.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "CM_V_REQUEST",  uniqueConstraints = {})

public class CmVRequest extends AbstractCmVRequest implements java.io.Serializable {

	
	private static final long serialVersionUID = 1352896470544245219L;
	
	public CmVRequest() {
		
	}

}
