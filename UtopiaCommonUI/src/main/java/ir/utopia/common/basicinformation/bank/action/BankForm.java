package ir.utopia.common.basicinformation.bank.action;

import ir.utopia.common.basicinformation.bank.persistent.CmBank;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class BankForm extends AbstractUtopiaForm<CmBank> {

	
	private Long cmBankId;
	private String code;
	private String name;
	private String latinname;
	private String descriptions;
	
	public BankForm() {
		
	}

	
	@FormId
	@FormPersistentAttribute
	public Long getCmBankId() {
		return cmBankId;
	}
	public void setCmBankId(Long cmBankId) {
		this.cmBankId = cmBankId;
	}

	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=1,breakLineAfter=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=false)
	public String getLatinname() {
		return latinname;
	}
	public void setLatinname(String latinname) {
		this.latinname = latinname;
	}

	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=4,breakLineAfter=true)
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
}
