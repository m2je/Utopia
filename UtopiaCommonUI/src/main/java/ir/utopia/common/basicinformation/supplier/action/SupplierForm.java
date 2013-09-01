package ir.utopia.common.basicinformation.supplier.action;

import java.util.Locale;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

import ir.utopia.common.CommonConstants.SupplierType;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmCompanyBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.common.basicinformation.supplier.persistent.CmSupplier;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.NativeEvent;
import ir.utopia.core.form.annotation.NativeEventType;
import ir.utopia.core.form.annotation.NativeScript;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;


@PersistedMapForm
@DataInputForm(nativeScript=@NativeScript(script="" +
		"function personNationalCodeCheck(){" +(char)13+
		"var nationCode = document.getElementById('agentCodeMelli').value;"+(char)13+
        "Ext.Ajax.request({"+
		   "url: 'personNationalCodeCheck_Cm_Bi_Supplier.json'," +(char)13+
		   "success: function(response, opts) {" +(char)13+
		    "var jsonObj = Ext.decode(response.responseText);" +(char)13+
		    "document.getElementById('agentName').value = jsonObj.agentName"+(char)13+
		    "document.getElementById('agentLastName').value = jsonObj.agentLastName"+(char)13+
		    "document.getElementById('agentEmail').value = jsonObj.agentEmail"+(char)13+
		    "document.getElementById('supPBpartnerId').value = jsonObj.supPBpartnerId"+(char)13+
		    "document.getElementById('birthCertificateNumber').value = jsonObj.birthCertificateNumber"+(char)13+
		    "document.getElementById('birthCertificateSerial').value = jsonObj.birthCertificateSerial"+(char)13+
		    "document.getElementById('agentPhoneNo').value = jsonObj.agentPhoneNo"+(char)13+
		    "document.getElementById('mobile').value = jsonObj.mobile"+(char)13+
		    "document.getElementById('supPersonBpartnerId').value = jsonObj.supPersonBpartnerId"+(char)13+
		    "document.getElementById('birthDate').value = jsonObj.birthDate"+(char)13+
		    "var maritalStatusId = getGWTComponentId('maritalStatus');"+(char)13+
            "var maritalStatus = Ext.getCmp(maritalStatusId);" +(char)13+
            "maritalStatus.setValue(jsonObj.maritalStatus);"+(char)13+
            "var sexId = getGWTComponentId('sex');"+(char)13+
            "var sex = Ext.getCmp(sexId);" +(char)13+
            "sex.setValue(jsonObj.sex);"+(char)13+
		   "}," +(char)13+
		   "failure: function(response, opts) {" +(char)13+
		     " console.log('server-side failure with status code ' + response.status);" +(char)13+
		   "}," +(char)13+
		   "params:{'agentCodeMelli':nationCode}" +(char)13+
		"});"+(char)13+
		"}"+(char)13+
		"function companyNationalCodeCheck(){" +(char)13+
		"var nationCode = document.getElementById('legalPersonNationalId').value;"+(char)13+
        "Ext.Ajax.request({"+
		   "url: 'companyNationalCodeCheck_Cm_Bi_Supplier.json'," +(char)13+
		   "success: function(response, opts) {" +(char)13+
		    "var jsonObj = Ext.decode(response.responseText);" +(char)13+
		    "document.getElementById('company').value = jsonObj.company"+(char)13+
		    "document.getElementById('ceo').value = jsonObj.ceo"+(char)13+
		    "document.getElementById('companyEmail').value = jsonObj.companyEmail"+(char)13+
		    "document.getElementById('supCBpartnerId').value = jsonObj.supCBpartnerId"+(char)13+
		    "document.getElementById('establishedCode').value = jsonObj.establishedCode"+(char)13+
		    "document.getElementById('establishedDate').value = jsonObj.establishedDate"+(char)13+
		    "document.getElementById('iranCode').value = jsonObj.iranCode"+(char)13+
		    "document.getElementById('iranCodeDate').value = jsonObj.iranCodeDate"+(char)13+
		    "document.getElementById('website').value = jsonObj.website"+(char)13+
		    "document.getElementById('companyPhoneNo').value = jsonObj.companyPhoneNo"+(char)13+
		    "document.getElementById('zipCode').value = jsonObj.zipCode"+(char)13+
		    "document.getElementById('description').value = jsonObj.description"+(char)13+
		    "document.getElementById('supCompanyBpartnerId').value = jsonObj.supCompanyBpartnerId"+(char)13+
		   "}," +(char)13+
		   "failure: function(response, opts) {" +(char)13+
		     " console.log('server-side failure with status code ' + response.status);" +(char)13+
		   "}," +(char)13+
		   "params:{'legalPersonNationalId':nationCode}" +(char)13+
		"});"+(char)13+
		"}"))

public class SupplierForm extends AbstractUtopiaForm<CmSupplier> {
	
	
	private Long cmSupplierId;
	private String code;
	private String arrivalDate;
	private SupplierType supplierType;
	
	private Long cmCompanyBpartner;
	private String legalPersonNationalId;
	private String company;
	private String ceo;
	private String companyEmail;
	private String establishedCode;
	private String establishedDate;
	private String iranCode;
	private String iranCodeDate;
	private String website;
	private String companyPhoneNo;
	private String zipCode;
	private String description;
	
	private Long cmPersonBpartner;
	private String agentName;
	private String agentLastName;
	private String agentCodeMelli;
	private String agentEmail;
	private String birthCertificateNumber;
	private String birthCertificateSerial;
	private String birthDate;
	private Constants.Sex sex;
	private Constants.MaritalStatus maritalStatus;
	private Long agentPhoneNo;
	private Long mobile;
	
	private Long supPBpartnerId;
	private Long supCBpartnerId;
	private Long supPersonBpartnerId;
	private Long supCompanyBpartnerId;
	
	public SupplierForm () {
		
	}


	
	@FormId
	@FormPersistentAttribute
	public Long getCmSupplierId() {
		return cmSupplierId;
	}
	public void setCmSupplierId(Long cmSupplierId) {
		this.cmSupplierId = cmSupplierId;
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


	
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Date,index=2,breakLineAfter=false)
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}


	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=2)
	@FormPersistentAttribute
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@InputItem(index=3,isManadatory=true,displayType=DisplayTypes.list,breakLineAfter=true)
	public SupplierType getSupplierType() {
		return supplierType;
	}
	public void setSupplierType(SupplierType supplierType) {
		this.supplierType = supplierType;
	}



//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//
	
	
	
	
	@FormPersistentAttribute(method="getCmCompanyBpartner")
	public Long getCmCompanyBpartner() {
		return cmCompanyBpartner;
	}
	public void setCmCompanyBpartner(Long cmCompanyBpartner) {
		this.cmCompanyBpartner = cmCompanyBpartner;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=3)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=4,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$"
	           ,nativeEvents=@NativeEvent(eventType=NativeEventType.onValueChanged,function="companyNationalCodeCheck")
               ,readonlyLogic="groovy:\"update\".equals(@"+ContextUtil.CURRENT_ACTION_PARAMETER+"@)")
	public String getLegalPersonNationalId() {
		return legalPersonNationalId;
	}
	public void setLegalPersonNationalId(String legalPersonNationalId) {
		this.legalPersonNationalId = legalPersonNationalId;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=4)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=5,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=5)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=6,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getCeo() {
		return ceo;
	}
	public void setCeo(String ceo) {
		this.ceo = ceo;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=8)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=11,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=6)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=7,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getEstablishedCode() {
		return establishedCode;
	}
	public void setEstablishedCode(String establishedCode) {
		this.establishedCode = establishedCode;
	}



	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=true,displayType=DisplayTypes.Date,index=8,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getEstablishedDate() {
		return establishedDate;
	}
	public void setEstablishedDate(String establishedDate) {
		this.establishedDate = establishedDate;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=7)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=9,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getIranCode() {
		return iranCode;
	}
	public void setIranCode(String iranCode) {
		this.iranCode = iranCode;
	}



	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Date,index=10,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getIranCodeDate() {
		return iranCodeDate;
	}
	public void setIranCodeDate(String iranCodeDate) {
		this.iranCodeDate = iranCodeDate;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=9)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=12,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=10)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=13,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getCompanyPhoneNo() {
		return companyPhoneNo;
	}
	public void setCompanyPhoneNo(String companyPhoneNo) {
		this.companyPhoneNo = companyPhoneNo;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=11)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=14,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}



	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=26,breakLineAfter=true,displayType=DisplayTypes.LargeString,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.legal$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//
	
	
	@FormPersistentAttribute(method="getCmPersonBpartner")
	public Long getCmPersonBpartner() {
		return cmPersonBpartner;
	}
	public void setCmPersonBpartner(Long cmPersonBpartner) {
		this.cmPersonBpartner = cmPersonBpartner;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=12)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=15,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=13)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=16,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getAgentLastName() {
		return agentLastName;
	}
	public void setAgentLastName(String agentLastName) {
		this.agentLastName = agentLastName;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=14)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=17,breakLineAfter=true,displayType=DisplayTypes.String,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$"
	    		,nativeEvents=@NativeEvent(eventType=NativeEventType.onValueChanged,function="personNationalCodeCheck")
	            ,readonlyLogic="groovy:\"update\".equals(@"+ContextUtil.CURRENT_ACTION_PARAMETER+"@)")
	public String getAgentCodeMelli() {
		return agentCodeMelli;
	}
	public void setAgentCodeMelli(String agentCodeMelli) {
		this.agentCodeMelli = agentCodeMelli;
	}



	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=25,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getAgentEmail() {
		return agentEmail;
	}
	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}



	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=18,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getBirthCertificateNumber() {
		return birthCertificateNumber;
	}
	public void setBirthCertificateNumber(String birthCertificateNumber) {
		this.birthCertificateNumber = birthCertificateNumber;
	}



	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=19,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getBirthCertificateSerial() {
		return birthCertificateSerial;
	}
	public void setBirthCertificateSerial(String birthCertificateSerial) {
		this.birthCertificateSerial = birthCertificateSerial;
	}



	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Date,index=20,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}



	@FormPersistentAttribute
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@InputItem(index=21,isManadatory=false,displayType=DisplayTypes.list,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public Constants.Sex getSex() {
		return sex;
	}
	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}



	@FormPersistentAttribute
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@InputItem(index=22,isManadatory=false,displayType=DisplayTypes.list,breakLineAfter=true,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public Constants.MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(Constants.MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=15)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=23,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public Long getAgentPhoneNo() {
		return agentPhoneNo;
	}
	public void setAgentPhoneNo(Long agentPhoneNo) {
		this.agentPhoneNo = agentPhoneNo;
	}



	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=16)
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=24,breakLineAfter=false,displayLogic="@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.real$|@supplierType@=$ir.utopia.common.CommonConstants.SupplierType.marketer$")
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	
	@InputItem(isManadatory=false,index=33,breakLineAfter=true,displayType=DisplayTypes.Hidden)
	public Long getSupPBpartnerId() {
		return supPBpartnerId;
	}

	public void setSupPBpartnerId(Long supPBpartnerId) {
		this.supPBpartnerId = supPBpartnerId;
	}

	@InputItem(isManadatory=false,index=33,breakLineAfter=true,displayType=DisplayTypes.Hidden)
	public Long getSupCBpartnerId() {
		return supCBpartnerId;
	}

	public void setSupCBpartnerId(Long supCBpartnerId) {
		this.supCBpartnerId = supCBpartnerId;
	}

	@InputItem(isManadatory=false,index=33,breakLineAfter=true,displayType=DisplayTypes.Hidden)
	public Long getSupPersonBpartnerId() {
		return supPersonBpartnerId;
	}

	public void setSupPersonBpartnerId(Long supPersonBpartnerId) {
		this.supPersonBpartnerId = supPersonBpartnerId;
	}

	@InputItem(isManadatory=false,index=33,breakLineAfter=true,displayType=DisplayTypes.Hidden)
	public Long getSupCompanyBpartnerId() {
		return supCompanyBpartnerId;
	}

	public void setSupCompanyBpartnerId(Long supCompanyBpartnerId) {
		this.supCompanyBpartnerId = supCompanyBpartnerId;
	}



	@Override
	public UtopiaPersistent convertToPersistent() {
		
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		
		CmBpartner compbpart = new CmBpartner();
		if(supCBpartnerId!=null){
			compbpart.setCmBpartnerId(getSupCBpartnerId());
		}
		compbpart.setCode(WebUtil.formatStringNumbersToAnsi((String)getLegalPersonNationalId()));
		compbpart.setName(getCompany());
		compbpart.setSecoundName(getCeo());
		compbpart.setEmailaddress(getCompanyEmail());
		try {
			BeanUtil.initPersistentObject(compbpart, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CmCompanyBpartner company = new CmCompanyBpartner();
		if(supCompanyBpartnerId!=null){
			company.setCmCompanyBpartnerId(getSupCompanyBpartnerId());
		}
		company.setCmBpartner(compbpart);
		company.setWebsite(getWebsite());
		company.setPhoneNo(WebUtil.formatStringNumbersToAnsi((String)getCompanyPhoneNo()));
		company.setEstablishedCode(WebUtil.formatStringNumbersToAnsi((String)getEstablishedCode()));
		if (getEstablishedDate() instanceof String){
			String strValue=getEstablishedDate().toString().trim();
			if(DateUtil.useSolarDate(locale)){
				company.setEstablishedDate(strValue!=null&&strValue.trim().length()>0?DateUtil.solarToDate(strValue):null);
			}else{
				company.setEstablishedDate(strValue!=null&&strValue.trim().length()>0?DateUtil.gerToDate(strValue):null);
			}
		}
		company.setZipCode(WebUtil.formatStringNumbersToAnsi((String)getZipCode()));
		company.setIranCode(WebUtil.formatStringNumbersToAnsi((String)getIranCode()));
		if (getIranCodeDate() instanceof String) {
			String iranCodeDate=getIranCodeDate().toString().trim();
			if(DateUtil.useSolarDate(locale)){
				company.setIranCodeDate(iranCodeDate!=null&&iranCodeDate.trim().length()>0?DateUtil.solarToDate(iranCodeDate):null);
			}else{
				company.setIranCodeDate(iranCodeDate!=null&&iranCodeDate.trim().length()>0?DateUtil.gerToDate(iranCodeDate):null);
			}
		}
		company.setDescription(getDescription());
		try {
			BeanUtil.initPersistentObject(company, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//
		
		CmBpartner personbpart = new CmBpartner();
		if(supPBpartnerId!=null){
			personbpart.setCmBpartnerId(getSupPBpartnerId());
		}
		personbpart.setCode(WebUtil.formatStringNumbersToAnsi((String)getAgentCodeMelli()));
		personbpart.setName(getAgentName());
		personbpart.setSecoundName(getAgentLastName());
		personbpart.setEmailaddress(getAgentEmail());
		try {
			BeanUtil.initPersistentObject(personbpart, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CmPersonBpartner person = new CmPersonBpartner();
		if(supPersonBpartnerId!=null){
			person.setCmPersonBpartnerId(getSupPersonBpartnerId());
		}
		person.setCmBpartner(personbpart);
		person.setBirthCertificateNumber(WebUtil.formatStringNumbersToAnsi((String)getBirthCertificateNumber()));
		person.setBirthCertificateSerial(WebUtil.formatStringNumbersToAnsi((String)getBirthCertificateSerial()));
		if (getBirthDate() instanceof String){
			String birtdate = getBirthDate().toString().trim();
			if(DateUtil.useSolarDate(locale)){
				person.setBirthDate(birthDate!=null&&birthDate.trim().length()>0?DateUtil.solarToDate(birtdate):null);
			}else{
				person.setBirthDate(birthDate!=null&&birthDate.trim().length()>0?DateUtil.gerToDate(birtdate):null);
			}
		}
		person.setSex(getSex());
		person.setMaritalStatus(getMaritalStatus());
		person.setPhoneNo(getAgentPhoneNo());
		person.setMobile(getMobile());
		try {
			BeanUtil.initPersistentObject(person, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//
		
		CmSupplier supplier = (CmSupplier)super.convertToPersistent();
		supplier.setCmCompanyBpartner(company);
		supplier.setCmPersonBpartner(person);
		try {
			BeanUtil.initPersistentObject(supplier, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return supplier;
		
	}
	
	
	
	
	@Override
	public void importPersistent(UtopiaBasicPersistent persistent) {
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		Locale loc=new Locale(locale);
		super.importPersistent(persistent);
		if (persistent instanceof CmSupplier){
		//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//
			CmCompanyBpartner company = ((CmSupplier)persistent).getCmCompanyBpartner();
			setSupCompanyBpartnerId(company.getCmCompanyBpartnerId());
			setDescription(company.getDescription());
			setZipCode(company.getZipCode());
			setCompanyPhoneNo(company.getPhoneNo());
			setWebsite(company.getWebsite());
			if (company.getIranCodeDate()!=null){
				setIranCodeDate(DateUtil.getDateString(company.getIranCodeDate(), loc));
			}
			setIranCode(company.getIranCode());
			if (company.getEstablishedDate()!=null){
				setEstablishedDate(DateUtil.getDateString(company.getEstablishedDate(), loc));
			}
			setEstablishedCode(company.getEstablishedCode());
			
			CmBpartner compbpart = company.getCmBpartner();
			if (compbpart!=null){
				setSupCBpartnerId(compbpart.getCmBpartnerId());
				setLegalPersonNationalId(compbpart.getCode());
				setCompany(compbpart.getName());
				setCeo(compbpart.getSecoundName());
				setCompanyEmail(compbpart.getEmailaddress());
			}
		//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//**//
			CmPersonBpartner person = ((CmSupplier)persistent).getCmPersonBpartner();
			setSupPersonBpartnerId(person.getCmPersonBpartnerId());
			setMobile(person.getMobile());
			setAgentPhoneNo(person.getPhoneNo());
			setMaritalStatus(person.getMaritalStatus());
			setSex(person.getSex());
			if (person.getBirthDate()!=null){
				setBirthDate(DateUtil.getDateString(person.getBirthDate(), loc));
			}
			setBirthCertificateSerial(person.getBirthCertificateSerial());
			setBirthCertificateNumber(person.getBirthCertificateNumber());
			
			CmBpartner personbpart = person.getCmBpartner();
			if (personbpart!=null){
				setSupPBpartnerId(personbpart.getCmBpartnerId());
				setAgentCodeMelli(personbpart.getCode());
				setAgentName(personbpart.getName());
				setAgentLastName(personbpart.getSecoundName());
				setAgentEmail(personbpart.getEmailaddress());
			}
		}
		
	}
}
