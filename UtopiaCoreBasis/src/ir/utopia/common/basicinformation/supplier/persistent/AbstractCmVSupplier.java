package ir.utopia.common.basicinformation.supplier.persistent;

import java.util.Date;

import ir.utopia.common.CommonConstants.SupplierType;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * AbstractCmVSupplier entity provides the base persistence definition of the 
 * CmVSupplier entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmVSupplier extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields
	
	private static final long serialVersionUID = 5097299258819355769L;
	private Long cmSupplierId;
	private String code;
	private Date arrivalDate;
	private SupplierType supplierType;
	
	private String legalPersonNationalId;
	private String company;
	private String ceo;
	private String companyEmail;
	private String establishedCode;
	private Date establishedDate;
	private String iranCode;
	private Date iranCodeDate;
	private String website;
	private String companyPhoneNo;
	private String zipCode;
	private String description;
	
	private String agentName;
	private String agentLastName;
	private String agentCodeMelli;
	private String agentEmail;
	private String birthCertificateNumber;
	private String birthCertificateSerial;
	private Date birthDate;
	private Constants.Sex sex;
	private Constants.MaritalStatus maritalStatus;
	private Long agentPhoneNo;
	private Long mobile;
	
	
	
	// Constructors

	/** default constructor */
	public AbstractCmVSupplier() {
		
	}


	// Property accessors
	@Id
	@Column(name = "CM_SUPPLIER_ID")
	public Long getCmSupplierId() {
		return cmSupplierId;
	}
	public void setCmSupplierId(Long cmSupplierId) {
		this.cmSupplierId = cmSupplierId;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ARRIVAL_DATE")
	public Date getArrivalDate() {
		return arrivalDate;
	}


	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	@Column(name = "SUPPLIER_TYPE")
	public SupplierType getSupplierType() {
		return supplierType;
	}


	public void setSupplierType(SupplierType supplierType) {
		this.supplierType = supplierType;
	}


	@Column(name = "LEGAL_PERSON_NATIONAL_ID")
	public String getLegalPersonNationalId() {
		return legalPersonNationalId;
	}
	public void setLegalPersonNationalId(String legalPersonNationalId) {
		this.legalPersonNationalId = legalPersonNationalId;
	}


	@Column(name = "COMPANY")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}


	@Column(name = "CEO")
	public String getCeo() {
		return ceo;
	}
	public void setCeo(String ceo) {
		this.ceo = ceo;
	}


	@Column(name = "COMPANY_EMAIL")
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}


	@Column(name = "ESTABLISHED_CODE")
	public String getEstablishedCode() {
		return establishedCode;
	}
	public void setEstablishedCode(String establishedCode) {
		this.establishedCode = establishedCode;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "ESTABLISHED_DATE")
	public Date getEstablishedDate() {
		return establishedDate;
	}
	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}


	@Column(name = "IRAN_CODE")
	public String getIranCode() {
		return iranCode;
	}
	public void setIranCode(String iranCode) {
		this.iranCode = iranCode;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "IRAN_CODE_DATE")
	public Date getIranCodeDate() {
		return iranCodeDate;
	}
	public void setIranCodeDate(Date iranCodeDate) {
		this.iranCodeDate = iranCodeDate;
	}


	@Column(name = "WEBSITE")
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}


	@Column(name = "COMPANY_PHONENO")
	public String getCompanyPhoneNo() {
		return companyPhoneNo;
	}
	public void setCompanyPhoneNo(String companyPhoneNo) {
		this.companyPhoneNo = companyPhoneNo;
	}


	@Column(name = "ZIP_CODE")
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "AGENT_NAME")
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
	@Column(name = "AGENT_LASTNAME")
	public String getAgentLastName() {
		return agentLastName;
	}
	public void setAgentLastName(String agentLastName) {
		this.agentLastName = agentLastName;
	}


	@Column(name = "AGENT_CODEMELLI")
	public String getAgentCodeMelli() {
		return agentCodeMelli;
	}
	public void setAgentCodeMelli(String agentCodeMelli) {
		this.agentCodeMelli = agentCodeMelli;
	}


	@Column(name = "AGENT_EMAIL")
	public String getAgentEmail() {
		return agentEmail;
	}
	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}


	@Column(name = "BIRTHCERTIFICATE_NUMBER")
	public String getBirthCertificateNumber() {
		return birthCertificateNumber;
	}
	public void setBirthCertificateNumber(String birthCertificateNumber) {
		this.birthCertificateNumber = birthCertificateNumber;
	}


	@Column(name = "BIRTHCERTIFICATE_SERIAL")
	public String getBirthCertificateSerial() {
		return birthCertificateSerial;
	}
	public void setBirthCertificateSerial(String birthCertificateSerial) {
		this.birthCertificateSerial = birthCertificateSerial;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDATE")
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	@Column(name = "SEX")
	public Constants.Sex getSex() {
		return sex;
	}
	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}


	@Column(name = "MARITAL_STATUS")
	public Constants.MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(Constants.MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}


	@Column(name = "AGENT_PHONENO")
	public Long getAgentPhoneNo() {
		return agentPhoneNo;
	}
	public void setAgentPhoneNo(Long agentPhoneNo) {
		this.agentPhoneNo = agentPhoneNo;
	}


	@Column(name = "MOBILE")
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

}
