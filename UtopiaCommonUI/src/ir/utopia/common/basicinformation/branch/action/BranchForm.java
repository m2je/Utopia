package ir.utopia.common.basicinformation.branch.action;

import ir.utopia.common.basicinformation.branch.persistent.CmBranch;
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
public class BranchForm extends AbstractUtopiaForm<CmBranch> {

	
	private Long cmBranchId;
	private Long cmProvince;
	private String provinceName;
	private Long cmBank;
	private String bankName;
	private String code;
	private String branchName;
	private String latinname;
	private String descriptions;
	private Long precodeTelephone; 
	private Long telephone; 
	private String address;
	
	public BranchForm() {
		
	}

	
	@FormId
	@FormPersistentAttribute
	public Long getCmBranchId() {
		return cmBranchId;
	}
	public void setCmBranchId(Long cmBranchId) {
		this.cmBranchId = cmBranchId;
	}

	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM,index=1)
	@FormPersistentAttribute(method = "getCmProvince")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1,breakLineAfter=false)
	public Long getCmProvince() {
		return cmProvince;
	}
	public void setCmProvince(Long cmProvince) {
		this.cmProvince = cmProvince;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=1)
	@FormPersistentAttribute(method="getProvinceName",formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM,index=2)
	@FormPersistentAttribute(method = "getCmBank")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=2,breakLineAfter=true)
	public Long getCmBank() {
		return cmBank;
	}
	public void setCmBank(Long cmBank) {
		this.cmBank = cmBank;
	}

	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=2)
	@FormPersistentAttribute(method="getBankName",formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=4,breakLineAfter=true)
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=5,breakLineAfter=false)
	public String getLatinname() {
		return latinname;
	}
	public void setLatinname(String latinname) {
		this.latinname = latinname;
	}

	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=6,breakLineAfter=true)
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=7)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=7,breakLineAfter=false)
	public Long getPrecodeTelephone() {
		return precodeTelephone;
	}

	public void setPrecodeTelephone(Long precodeTelephone) {
		this.precodeTelephone = precodeTelephone;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=8)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=8,breakLineAfter=true)
	public Long getTelephone() {
		return telephone;
	}

	public void setTelephone(Long telephone) {
		this.telephone = telephone;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=9)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.LargeString,index=9,breakLineAfter=false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
