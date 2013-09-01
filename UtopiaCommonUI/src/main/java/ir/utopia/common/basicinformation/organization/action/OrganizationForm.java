package ir.utopia.common.basicinformation.organization.action;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class OrganizationForm  extends AbstractUtopiaForm<CmOrganization>{

	
	private Long cmOrganizationId;
	private String code;
	private String name;
	private Long parentId;
	private String parentName;
	private String description;
//	private String parentCode;
	
	
	
	public OrganizationForm(){
		System.out.println();
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmOrganizationId() {
		return this.cmOrganizationId;
	}

	public void setCmOrganizationId(Long cmOrganizationId) {
		this.cmOrganizationId = cmOrganizationId;
	}

	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH , index = 1)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=1,breakLineAfter=false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH , index = 2)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	@FormPersistentAttribute(method = "getParent",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Hidden,index=3,breakLineAfter=true)
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(Long parentId){
		this.parentId = parentId;
	}
	
	
	
/*	public String getParentCode() {
		return parentCode;
	}
	
	public void setParentCode(String parentCode){
		this.parentCode = parentCode;
	}*/
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=3)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getParentName() {
		return parentName;
	}
	
	public void SetParentName(String parentName){
		this.parentName = parentName;
	}
	
	
	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=4,breakLineAfter=true,displayType=DisplayTypes.LargeString)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	@FormPersistentAttribute
//	@InputItem(isManadatory=true,index=2,breakLineAfter=true)



}
