package ir.utopia.common.locality.region.action;

import ir.utopia.common.locality.region.persistent.CmRegion;
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
public class RegionForm  extends AbstractUtopiaForm<CmRegion>{

	private Long cmRegionId;
	private String name;
	private String description;
	private Long cmCountryId;
	private Long cmProvinceId;
	private String code;
	private String countryName;
	private String provinceName;


	public RegionForm(){
		
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmRegionId() {
		return this.cmRegionId;
	}

	public void setCmRegionId(Long cmRegionId) {
		this.cmRegionId = cmRegionId;
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
	public String getCountryName() {
		return countryName;
	}


	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}


	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmCountry")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=4)
	public Long getCmCountryId() {
		return cmCountryId;
	}

	public void setCmCountryId(Long cmCountryId) {
		this.cmCountryId = cmCountryId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmProvince")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=5)
	public Long getCmProvinceId() {
		return cmProvinceId;
	}

	public void setCmProvinceId(Long cmProvinceId) {
		this.cmProvinceId = cmProvinceId;
	}


}
