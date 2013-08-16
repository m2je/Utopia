package ir.utopia.common.basicinformation.request.persistent;

import ir.utopia.core.persistent.AbstractOrganizationData;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;



@MappedSuperclass
public class AbstractCmVRequest extends AbstractOrganizationData implements java.io.Serializable {

	
	private static final long serialVersionUID = -5990594281336437109L;
	// Fields
	private Long cmRequestId;
	private String request;
	private String username;
	private String organization;
	private String systemName;
	private String subsystemName;
	private String usecaseName;
	
	
	
	public AbstractCmVRequest(){
		
	}



	@Id
	@Column(name = "CM_REQUEST_ID")
	public Long getCmRequestId() {
		return cmRequestId;
	}
	public void setCmRequestId(Long cmRequestId) {
		this.cmRequestId = cmRequestId;
	}



	
	@Column(name = "REQUEST")
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}


	
	
	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	
	
	@Column(name = "ORGANIZATION")
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}


	
	
	@Column(name = "SYSTEMNAME")
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}


	
	
	@Column(name = "SUBSYSTEMNAME")
	public String getSubsystemName() {
		return subsystemName;
	}
	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}


	
	
	
	@Column(name = "USECASENAME")
	public String getUsecaseName() {
		return usecaseName;
	}
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
	
}
