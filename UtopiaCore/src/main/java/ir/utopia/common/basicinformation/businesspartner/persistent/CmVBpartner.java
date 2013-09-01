package ir.utopia.common.basicinformation.businesspartner.persistent;

import ir.utopia.core.constants.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CM_V_BPARTNER")
public class CmVBpartner extends AbstractCmVBpartner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7117804063406881433L;
	private Long cmBpartnerId;
	private String code;
	private String name;
	private String secoundName;
	private Long adderss;
	private String emailaddress;
	private Constants.Sex sex;
	private String personComapny;
	// Constructors

	/** default constructor */
	
	// Property accessors
	@Id
	@Column(name = "CM_BPARTNER_ID")
	public Long getCmBpartnerId() {
		return this.cmBpartnerId;
	}

	public void setCmBpartnerId(Long cmBpartnerId) {
		this.cmBpartnerId = cmBpartnerId;
	}

	@Column(name = "CODE")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SECOUND_NAME")
	public String getSecoundName() {
		return this.secoundName;
	}

	public void setSecoundName(String secoundName) {
		this.secoundName = secoundName;
	}

	@Column(name = "ADDERSS")
	public Long getAdderss() {
		return this.adderss;
	}

	public void setAdderss(Long adderss) {
		this.adderss = adderss;
	}

	@Column(name = "EMAILADDRESS")
	public String getEmailaddress() {
		return this.emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	@Enumerated(value=EnumType.ORDINAL)
	@Column(name="SEX")
	public Constants.Sex getSex() {
		return sex;
	}

	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}
	@Column(name="PERSON_COMPANY")
	public String getPersonComapny() {
		return personComapny;
	}

	public void setPersonComapny(String personComapny) {
		this.personComapny = personComapny;
	}
	
	
}
