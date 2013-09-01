package ir.utopia.common.basicinformation.employeesignature.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_V_EMPLOYEE_SIGNATURE")
public class CmVEmployeeSignature extends AbstractCmVEmployeeSignature implements
		java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4533729559655761558L;

	public CmVEmployeeSignature() {
	}

}
