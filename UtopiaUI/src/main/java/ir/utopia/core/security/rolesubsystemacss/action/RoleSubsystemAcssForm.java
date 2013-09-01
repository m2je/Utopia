package ir.utopia.core.security.rolesubsystemacss.action;

import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityMapType;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.security.rolesubsystemacss.persistence.CoRoleSubsystemAcss;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class RoleSubsystemAcssForm  extends AbstractUtopiaForm<CoRoleSubsystemAcss>{

	private Long coRoleSubsystemAcssId;
	private Long coRoleId;
	private Long cmSubsystemId;
	private Long cmSystemId;
	private String roleName;
	private String subsysName;
	private String sysName;

	public RoleSubsystemAcssForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoRoleSubsystemAcssId() {
		return coRoleSubsystemAcssId;
	}

	public void setCoRoleSubsystemAcssId(Long coRoleSubsystemAcssId) {
		this.coRoleSubsystemAcssId = coRoleSubsystemAcssId;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCoRole")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3,breakLineAfter=true)
	public Long getCoRoleId() {
		return coRoleId;
	}

	public void setCoRoleId(Long coRoleId) {
		this.coRoleId = coRoleId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=0)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@LookupConfiguration(persistentClass=CmSystem.class)
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmSubsystem.getCmSystem",formToEntityMapType=FormToEntityMapType.virtual,formToEntityMap=FormToEntityDataTypeMap.LOOKUP_PARENT)
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=4,breakLineAfter=true)
	public Long getCmSystemId() {
		return cmSystemId;
	}

	public void setCmSystemId(Long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}
	@LookupConfiguration(condition="CmSubsystem.cmSystem.cmSystemId=@cmSystemId@")
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmSubsystem")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=5)
	public Long getCmSubsystemId() {
		return cmSubsystemId;
	}

	public void setCmSubsystemId(Long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=2)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSubsysName() {
		return subsysName;
	}

	public void setSubsysName(String subsysName) {
		this.subsysName = subsysName;
	}
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=1)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	

}
