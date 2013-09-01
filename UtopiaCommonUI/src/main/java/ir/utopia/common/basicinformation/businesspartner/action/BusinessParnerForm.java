package ir.utopia.common.basicinformation.businesspartner.action;

import ir.utopia.common.CommonConstants.BusinessPartnerType;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;
@DataInputForm
public class BusinessParnerForm extends AbstractUtopiaForm<CmBpartner> {

	private String code;
	private String name;
	private String secoundName;
	private Long adderss;
	private String emailaddress;
	private Constants.Sex sex;
	private String personComapnyName;
	private long bpartnerId;
	private BusinessPartnerType partnerType; 
	@SearchItem
	@InputItem(index=2,displayType=DisplayTypes.RadioButton)
	@FormPersistentAttribute
	public BusinessPartnerType getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(BusinessPartnerType partnerType) {
		this.partnerType = partnerType;
	}
	@FormId
	@FormPersistentAttribute(method="getCmBpartnerId")
	public long getBpartnerId() {
		return bpartnerId;
	}
	public void setBpartnerId(long bpartnerId) {
		this.bpartnerId = bpartnerId;
	}
	@InputItem(index=5)
	@SearchItem
	@FormPersistentAttribute
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@InputItem(index=3)
	@SearchItem
	@FormPersistentAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@InputItem(index=4)
	@SearchItem
	@FormPersistentAttribute
	public String getSecoundName() {
		return secoundName;
	}
	public void setSecoundName(String secoundName) {
		this.secoundName = secoundName;
	}
	@InputItem(index=6)
	@FormPersistentAttribute
	public Long getAdderss() {
		return adderss;
	}
	public void setAdderss(Long adderss) {
		this.adderss = adderss;
	}
	@InputItem(index=7)
	@SearchItem
	@FormPersistentAttribute
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	@InputItem(index=8)
	@SearchItem
	@FormPersistentAttribute
	public Constants.Sex getSex() {
		return sex;
	}
	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}
	@SearchItem
	@FormPersistentAttribute(method="getPersonComapny")
	public String getPersonComapnyName() {
		return personComapnyName;
	}
	public void setPersonComapnyName(String personComapnyName) {
		this.personComapnyName = personComapnyName;
	}
	
}
