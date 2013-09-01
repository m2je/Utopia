package ir.utopia.common.locality.province.action;

import ir.utopia.common.locality.province.persistent.CmProvince;
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
public class ProvinceForm  extends AbstractUtopiaForm<CmProvince>{

	private Long cmProvinceId;
	private String name;
	private String description;
	private String stateName;
	private String code;
	private Long cmStateId;

	
	
	@FormId
	@FormPersistentAttribute
	public Long getCmProvinceId() {
		return this.cmProvinceId;
	}

	public void setCmProvinceId(Long cmProvinceId) {
		this.cmProvinceId = cmProvinceId;
	}

	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1,breakLineAfter=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=3,breakLineAfter=true)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=6,breakLineAfter=true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getStateName() {
		return stateName;
	}


	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmState")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=5)
	public Long getCmStateId() {
		return cmStateId;
	}

	public void setCmStateId(Long cmStateId) {
		this.cmStateId = cmStateId;
	}


}
