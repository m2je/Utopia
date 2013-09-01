package ir.utopia.common.basicinformation.organization.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class AbstractCmVOrganization extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8058570181457058561L;
	private Long cmOrganizationId;
	private String code;
	private String name;
	private String parentCode;
	private String parentName;
	
	

	// Constructors

	/** default constructor */
	public AbstractCmVOrganization() {
	}

	

	// Property accessors
	@Id
	@Column(name = "CM_ORGANIZATION_ID")
	public Long getCmOrganizationId() {
		return this.cmOrganizationId;
	}

	public void setCmOrganizationId(Long cmOrganizationId) {
		this.cmOrganizationId = cmOrganizationId;
	}

	
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
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





	@Column(name = "PARENTCODE")
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}



	@Column(name = "PARENTNAME")
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	

}