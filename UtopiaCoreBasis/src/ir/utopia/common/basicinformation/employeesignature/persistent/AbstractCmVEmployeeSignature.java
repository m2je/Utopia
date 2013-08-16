package ir.utopia.common.basicinformation.employeesignature.persistent;

import ir.utopia.core.persistent.AbstractOrganizationData;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractCmVEmployeeSignature extends AbstractOrganizationData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8386772858559407046L;
	
	private Long cmEmployeeSignatureId;
	private Long cmEmployee;
	private String signatureImage;
	private String bpartnerName;
	private String organiztionName;

    @Id
	@Column(name = "CM_EMPLOYEE_SIGNATURE_ID")
	public Long getCmEmployeeSignatureId() {
		return this.cmEmployeeSignatureId;
	}

	public void setCmEmployeeSignatureId(Long cmEmployeeSignatureId) {
		this.cmEmployeeSignatureId = cmEmployeeSignatureId;
	}

	@Column(name = "CM_EMPLOYEE_ID")
	public Long getCmEmployee() {
		return this.cmEmployee;
	}

	public void setCmEmployee(Long cmEmployee) {
		this.cmEmployee = cmEmployee;
	}

	@Column(name = "SIGNATURE_IMAGE")
	public String getSignatureImage() {
		return this.signatureImage;
	}

	public void setSignatureImage(String signatureImage) {
		this.signatureImage = signatureImage;
	}

	@Column(name = "BPARTNER_NAME")
	public String getBpartnerName() {
		return this.bpartnerName;
	}

	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}

	@Column(name = "ORGANIZTION_NAME")
	public String getOrganiztionName() {
		return this.organiztionName;
	}

	public void setOrganiztionName(String organiztionName) {
		this.organiztionName = organiztionName;
	}
}