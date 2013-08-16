package ir.utopia.common.basicinformation.branch.persistent;

import ir.utopia.common.basicinformation.bank.persistent.CmBank;
import ir.utopia.common.locality.province.persistent.CmProvince;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractCmBranch extends AbstractUtopiaPersistent  implements java.io.Serializable {

	private static final long serialVersionUID = -9116172533610488059L;
	
	private Long cmBranchId;
	private CmProvince cmProvince;
	private CmBank cmBank;
	private String code;
	private String branchName;
	private String latinname;
	private String descriptions;
	private Long precodeTelephone; 
	private Long telephone; 
	private String address;


	@Id
	@Column(name = "CM_BRANCH_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmBranchId() {
		return this.cmBranchId;
	}

	public void setCmBranchId(Long cmBranchId) {
		this.cmBranchId = cmBranchId;
	}

	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "CM_PROVINCE_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmProvince getCmProvince() {
		return this.cmProvince;
	}

	public void setCmProvince(CmProvince cmProvince) {
		this.cmProvince = cmProvince;
	}
	
	@Transient
	public String getProvinceName(){
		return cmProvince.getName();
	}
	

	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "CM_BANK_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmBank getCmBank() {
		return this.cmBank;
	}

	public void setCmBank(CmBank cmBank) {
		this.cmBank = cmBank;
	}

	@Transient
	public String getBankName() {
		return cmBank.getName();
	}
	
	
	@Column(name = "CODE", unique = false, nullable = true, insertable = true, updatable = true)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true)
	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Column(name = "LATINNAME", unique = false, nullable = true, insertable = true, updatable = true)
	public String getLatinname() {
		return this.latinname;
	}

	public void setLatinname(String latinname) {
		this.latinname = latinname;
	}

	@Column(name = "DESCRIPTIONS", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDescriptions() {
		return this.descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	
	@Column(name = "PRECODE_TELEPHONE", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getPrecodeTelephone() {
		return precodeTelephone;
	}

	public void setPrecodeTelephone(Long precodeTelephone) {
		this.precodeTelephone = precodeTelephone;
	}

	
	@Column(name = "TELEPHONE", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getTelephone() {
		return telephone;
	}

	public void setTelephone(Long telephone) {
		this.telephone = telephone;
	}

	
	
	@Column(name = "ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}