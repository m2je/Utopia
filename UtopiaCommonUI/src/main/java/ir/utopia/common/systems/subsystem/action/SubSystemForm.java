package ir.utopia.common.systems.subsystem.action;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class SubSystemForm  extends AbstractUtopiaForm<CmSubsystem>{

	private Long cmSubsystemId;
	private Long cmSystemId;
	private String name;
	private String iconPath;
	private String abbreviation;
	private String systemName;

	public SubSystemForm(){
		
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmSubsystemId() {
		return cmSubsystemId;
	}

	public void setCmSubsystemId(Long cmSubsystemId) {
		this.cmSubsystemId = cmSubsystemId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=true)
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmSystem")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=4)
	public Long getCmSystemId() {
		return cmSystemId;
	}

	public void setCmSystemId(Long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
