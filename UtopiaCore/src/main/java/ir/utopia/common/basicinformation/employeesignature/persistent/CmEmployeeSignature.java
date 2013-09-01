package ir.utopia.common.basicinformation.employeesignature.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "CM_EMPLOYEE_SIGNATURE",uniqueConstraints = { @UniqueConstraint(columnNames = {"CM_EMPLOYEE_ID", "CM_ORGANIZATION_ID" }) })
@TableGenerator(name = "CmEmployeeSignSequenceGenerator", 
table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CM_EMPLOYEE_SIGNATURE")
public class CmEmployeeSignature extends AbstractCmEmployeeSignature implements
		java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7407717761235479004L;

	public CmEmployeeSignature() {
	}

}
