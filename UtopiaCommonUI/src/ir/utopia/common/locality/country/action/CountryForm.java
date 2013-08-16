package ir.utopia.common.locality.country.action;

import ir.utopia.common.locality.country.persistent.CmCountry;
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
public class CountryForm  extends AbstractUtopiaForm<CmCountry>{

	private Long cmCountryId;
	private String name;
	private Long cmCurrencyId;
	private String code;
	private String description;
	private String currencyName;


	public CountryForm(){
		
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmCountryId() {
		return this.cmCountryId;
	}

	public void setCmCountryId(Long cmCountryId) {
		this.cmCountryId = cmCountryId;
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
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getCurrencyName() {
		return this.currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}


	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmCurrency")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3,breakLineAfter=true)
	public Long getCmCurrencyId() {
		return cmCurrencyId;
	}

	public void setCmRegionId(Long cmCurrencyId) {
		this.cmCurrencyId = cmCurrencyId;
	}

	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=4)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
