package ir.utopia.core.usecase.usecase.action;

import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;

@PersistedMapForm
@DataInputForm
public class UseCaseActionForm extends AbstractUtopiaForm<CoUsecase> {

	
	
	public UseCaseActionForm() {
	}
	private Long coUsecaseId;
	private String name;
	private String uscsRemoteClass;
	private long subSystemId;
	private String subSystemName;
	private String systemName;
	private long systemId;
	@InputItem(isManadatory=true,index=1,breakLineAfter=true)
	@SearchItem
	@FormPersistentAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@InputItem(displayType=Constants.DisplayTypes.LargeString,index=4)
	@SearchItem
	@FormPersistentAttribute
	public String getUscsRemoteClass() {
		return uscsRemoteClass;
	}
	public void setUscsRemoteClass(String uscsRemoteClass) {
		this.uscsRemoteClass = uscsRemoteClass;
	}
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3)
	@FormPersistentAttribute(method="getCmSubsystem")
	public long getSubSystemId() {
		return subSystemId;
	}
	public void setSubSystemId(long subSystemId) {
		this.subSystemId = subSystemId;
	}
	
	@SearchItem
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSubSystemName() {
		return subSystemName;
	}
	
	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}
	@SearchItem
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	@FormId
	@FormPersistentAttribute
	public Long getCoUsecaseId() {
		return coUsecaseId;
	}
	public void setCoUsecaseId(Long coUsecaseId) {
		this.coUsecaseId = coUsecaseId;
	}
	@InputItem(isManadatory=true,index=2,displayType=DisplayTypes.lookup,lookupEntityClass=CmSystem.class)
	public long getSystemId() {
		return systemId;
	}
	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}
	
	

}
