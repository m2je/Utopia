package ir.utopia.common.locality.currency.action;

import ir.utopia.common.locality.currency.persistent.CmCurrency;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class CurrencyForm  extends AbstractUtopiaForm<CmCurrency>{

	private Long cmCurrencyId;
	private String description;
	private String isoCode;
	private String symbol;
	private String name;

	public CurrencyForm(){
		
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmCurrencyId() {
		return this.cmCurrencyId;
	}

	public void setCmCurrencyId(Long cmCurrencyId) {
		this.cmCurrencyId = cmCurrencyId;
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
	@InputItem(isManadatory=true,index=2)
	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=3,breakLineAfter=true)
	public String getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=4,breakLineAfter=true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
