package ir.utopia.common.basicinformation.request.persistent;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.persistent.AbstractOrganizationData;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;


@MappedSuperclass
public class AbstractCmRequest extends AbstractOrganizationData implements java.io.Serializable {

	
	private static final long serialVersionUID = 5521316673701902699L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	//Fields
	private Long cmRequestId;
	private CoUser coUser;
	private CmSystem cmSystem;
	private CmSubsystem cmSubsystem;
	private CoUsecase coUsecase;
	private String Request;

	
	
	public AbstractCmRequest(){
		
	}



	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="RequestSequenceGenerator")
	@Column(name="CM_REQUEST_ID" , unique = true , nullable = false , insertable = true , updatable = true , precision = 10 , scale=0 )
	public Long getCmRequestId() {
		return cmRequestId;
	}
	public void setCmRequestId(Long cmRequestId) {
		this.cmRequestId = cmRequestId;
	}



	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUser getCoUser() {
		return coUser;
	}
	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}



	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_SYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmSystem getCmSystem() {
		return cmSystem;
	}
	public void setCmSystem(CmSystem cmSystem) {
		this.cmSystem = cmSystem;
	}



	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_SUBSYTEM_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmSubsystem getCmSubsystem() {
		return cmSubsystem;
	}
	public void setCmSubsystem(CmSubsystem cmSubsystem) {
		this.cmSubsystem = cmSubsystem;
	}



	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return coUsecase;
	}
	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}



	
	@Column(name = "REQUEST", unique = false, nullable = false, insertable = true, updatable = true, length = 300)
	public String getRequest() {
		return Request;
	}
	public void setRequest(String request) {
		Request = request;
	}
}
