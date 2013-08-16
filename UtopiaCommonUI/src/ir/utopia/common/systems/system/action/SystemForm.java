package ir.utopia.common.systems.system.action;

import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class SystemForm  extends AbstractUtopiaForm<CmSystem>{

	private Long cmSystemId;
	private String name;
	private String icon;
	private String abbreviation;

	public SystemForm(){
		
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmSystemId() {
		return cmSystemId;
	}

	public void setCmSystemId(Long cmSystemId) {
		this.cmSystemId = cmSystemId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=true)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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
