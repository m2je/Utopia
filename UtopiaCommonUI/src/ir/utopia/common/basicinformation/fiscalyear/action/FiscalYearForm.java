package ir.utopia.common.basicinformation.fiscalyear.action;

import ir.utopia.common.basicinformation.fiscalyear.persistent.CmFiscalyear;
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
public class FiscalYearForm  extends AbstractUtopiaForm<CmFiscalyear>{

	private Long cmFiscalyearId;
	private String startdate;
	private String enddate;
	private String name;

	public FiscalYearForm(){
		
	}
	
	@FormId
	@FormPersistentAttribute
	public Long getCmFiscalyearId() {
		return this.cmFiscalyearId;
	}

	public void setCmFiscalyearId(Long cmFiscalyearId) {
		this.cmFiscalyearId = cmFiscalyearId;
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
	@FormPersistentAttribute(formToEntityMap=FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=true,index=2,breakLineAfter=true,displayType=DisplayTypes.Date,endDateField="enddate")
	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	@SearchItem
	@FormPersistentAttribute(formToEntityMap=FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=true,index=3,breakLineAfter=true,displayType=DisplayTypes.Date)
	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	

}
