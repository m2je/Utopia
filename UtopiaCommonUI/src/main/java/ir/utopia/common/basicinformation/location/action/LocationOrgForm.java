package ir.utopia.common.basicinformation.location.action;

import ir.utopia.common.basicinformation.location.persistent.CmLocationOrg;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.struts.AbstractUtopiaForm;


@PersistedMapForm
@DataInputForm
public class LocationOrgForm extends AbstractUtopiaForm<CmLocationOrg> {

	
	private Long cmLocationId;
	private String name;
	private String code;
	private Long parentId;
	private String parentLocation;
	private String organ;
	private String description;
	
	
	
	public LocationOrgForm() {
		
	}
	
	
	@FormId
	@FormPersistentAttribute
	public Long getCmLocationId() {
		return cmLocationId;
	}
	public void setCmLocationId(Long cmLocationId) {
		this.cmLocationId = cmLocationId;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=2)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=1)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=1,breakLineAfter=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	@FormPersistentAttribute(method = "getParentId",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Hidden,index=6,breakLineAfter=false)
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=6)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getParentLocation() {
		return parentLocation;
	}
	public void setParentLocation(String parentLocation) {
		this.parentLocation = parentLocation;
	}
	
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=3)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=3)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=true,displayType=DisplayTypes.LargeString)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
