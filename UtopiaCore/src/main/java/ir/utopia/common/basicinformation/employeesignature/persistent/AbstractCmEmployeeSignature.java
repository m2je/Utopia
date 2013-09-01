package ir.utopia.common.basicinformation.employeesignature.persistent;

import ir.utopia.common.basicinformation.employee.persistent.CmEmployee;
import ir.utopia.core.persistent.AbstractOrganizationData;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class AbstractCmEmployeeSignature extends AbstractOrganizationData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3129360731646415936L;

	private Long cmEmployeeSignatureId;
	private CmEmployee cmEmployee;
	private byte[] signatureImage;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="CmEmployeeSignSequenceGenerator")
	@Column(name = "CM_EMPLOYEE_SIGNATURE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmEmployeeSignatureId() {
		return this.cmEmployeeSignatureId;
	}

	public void setCmEmployeeSignatureId(Long cmEmployeeSignatureId) {
		this.cmEmployeeSignatureId = cmEmployeeSignatureId;
	}

	
	@ManyToOne
	@JoinColumn(name = "CM_EMPLOYEE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CmEmployee getCmEmployee() {
		return this.cmEmployee;
	}

	public void setCmEmployee(CmEmployee cmEmployee) {
		this.cmEmployee = cmEmployee;
	}

	@Column(name = "SIGNATURE_IMAGE", unique = false, nullable = false, insertable = true, updatable = true)
	public byte[] getSignatureImage() {
		return this.signatureImage;
	}

	public void setSignatureImage(byte[] signatureImage) {
		this.signatureImage = signatureImage;
	}

	
}