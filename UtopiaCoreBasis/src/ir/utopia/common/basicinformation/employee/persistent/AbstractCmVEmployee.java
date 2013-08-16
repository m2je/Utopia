package ir.utopia.common.basicinformation.employee.persistent;

import java.util.Date;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.persistent.AbstractOrganizationData;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * AbstractCmVEmployee entity provides the base persistence definition of the 
 * CmVEmployee entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmVEmployee  extends AbstractOrganizationData implements java.io.Serializable {

	// Fields
	
	private static final long serialVersionUID = 1446683703338656800L;
	private Long cmEmployeeId;
	private String code;
	private Date hiredate;
	private Long salary;
	private Long bonus;
	private String bpartnerCode;
	private String bpartnerName;
	private String secoundName;
	private String emailAddress;
	private Constants.Sex sex;
	private Long phoneNo;
	private Long mobile;
	private Date birthdate;
	private Constants.MaritalStatus maritalStatus;
	private String birthCertificateNumber;
	private String birthCertificateSerial;
	private String org;
	private String jobName;
	private String mngr;

	
	
	// Constructors

	/** default constructor */
	public AbstractCmVEmployee() {
	}

	// Property accessors
	@Id
	@Column(name = "CM_EMPLOYEE_ID")
	public Long getCmEmployeeId() {
		return cmEmployeeId;
	}
	public void setCmEmployeeId(Long cmEmployeeId) {
		this.cmEmployeeId = cmEmployeeId;
	}



	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "HIREDATE")
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}



	@Column(name = "SALARY")
	public Long getSalary() {
		return salary;
	}
	public void setSalary(Long salary) {
		this.salary = salary;
	}



	@Column(name = "BONUS")
	public Long getBonus() {
		return bonus;
	}
	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}



	@Column(name = "BPARTNER_CODE")
	public String getBpartnerCode() {
		return bpartnerCode;
	}
	public void setBpartnerCode(String bpartnerCode) {
		this.bpartnerCode = bpartnerCode;
	}



	@Column(name = "BPARTNER_NAME")
	public String getBpartnerName() {
		return bpartnerName;
	}
	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}



	@Column(name = "SECOUND_NAME")
	public String getSecoundName() {
		return secoundName;
	}
	public void setSecoundName(String secoundName) {
		this.secoundName = secoundName;
	}

	
	@Column(name = "EMAILADDRESS")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	@Column(name = "SEX")
	public Constants.Sex getSex() {
		return sex;
	}
	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}


	@Column(name = "PHONENO")
	public Long getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(Long phoneNo) {
		this.phoneNo = phoneNo;
	}

	
	
	@Column(name = "MOBILE")
	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	
	@Temporal(TemporalType.DATE)
	@Column(name = "birthdate")
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	

	@Column(name = "ORG")
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}



	@Column(name = "JOB_NAME")
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}



	@Column(name = "MNGR")
	public String getMngr() {
		return mngr;
	}
	public void setMngr(String mngr) {
		this.mngr = mngr;
	}

	
	@Column(name ="MARITAL_STATUS" )
	public Constants.MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(Constants.MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	
	@Column(name = "BIRTHCERTIFICATE_NUMBER" )
	public String getBirthCertificateNumber() {
		return birthCertificateNumber;
	}

	public void setBirthCertificateNumber(String birthCertificateNumber) {
		this.birthCertificateNumber = birthCertificateNumber;
	}

	
	@Column(name = "BIRTHCERTIFICATE_SERIAL" )
	public String getBirthCertificateSerial() {
		return birthCertificateSerial;
	}

	public void setBirthCertificateSerial(String birthCertificateSerial) {
		this.birthCertificateSerial = birthCertificateSerial;
	}

}
