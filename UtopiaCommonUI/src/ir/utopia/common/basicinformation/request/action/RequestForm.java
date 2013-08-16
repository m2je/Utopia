package ir.utopia.common.basicinformation.request.action;

import ir.utopia.common.basicinformation.request.persistent.CmRequest;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.struts.AbstractUtopiaForm;


@PersistedMapForm
@DataInputForm
public class RequestForm  extends AbstractUtopiaForm<CmRequest> {
	
	private Long cmRequestId;
	private Long coUser;
	private String username;
	private String organization;
	private Long cmSystem;
	private String systemName;
	private Long cmSubsystem;
	private String subsystemName;
	private Long coUsecase;
	private String usecaseName;
	private String request;
	
	
	@FormId
	@FormPersistentAttribute
	public Long getCmRequestId() {
		return cmRequestId;
	}
	public void setCmRequestId(Long cmRequestId) {
		this.cmRequestId = cmRequestId;
	}
	
	
	
	@FormPersistentAttribute(method = "getCoUser")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1,breakLineAfter=true,defaultValue="@userId@")
	@LookupConfiguration(displayColumns={"username"})
	public Long getCoUser() {
		return coUser;
	}
	public void setCoUser(Long coUser) {
		this.coUser = coUser;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=2)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=1)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	
	@FormPersistentAttribute(method = "getCmSystem")
	@InputItem(isManadatory=false,displayType=DisplayTypes.lookup,index=2,breakLineAfter=false)
	public Long getCmSystem() {
		return cmSystem;
	}
	public void setCmSystem(Long cmSystem) {
		this.cmSystem = cmSystem;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=3)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	
	
	@FormPersistentAttribute(method = "getCmSubsystem")
	@InputItem(isManadatory=false,displayType=DisplayTypes.lookup,index=3,breakLineAfter=false)
	@LookupConfiguration(condition="CmSubsystem.cmSystem.cmSystemId=@cmSystem@")
	public Long getCmSubsystem() {
		return cmSubsystem;
	}
	public void setCmSubsystem(Long cmSubsystem) {
		this.cmSubsystem = cmSubsystem;
	}
	
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=4)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSubsystemName() {
		return subsystemName;
	}
	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}
	
	
	
	@FormPersistentAttribute(method = "getCoUsecase")
	@InputItem(isManadatory=false,displayType=DisplayTypes.lookup,index=4,breakLineAfter=false)
	@LookupConfiguration(condition="CoUsecase.cmSubsystem.cmSubsystemId=@cmSubsystem@")
	public Long getCoUsecase() {
		return coUsecase;
	}
	public void setCoUsecase(Long coUsecase) {
		this.coUsecase = coUsecase;
	}
	
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=5)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getUsecaseName() {
		return usecaseName;
	}
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
	
	

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=6)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=12,breakLineAfter=true,displayType=DisplayTypes.LargeString)
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}

}
