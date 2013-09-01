package ir.utopia.common.basicinformation.employee.action;


import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.common.basicinformation.employee.persistent.CmEmployee;
import ir.utopia.common.locality.province.bean.CmProvinceFacadeRemote;
import ir.utopia.common.locality.province.persistent.CmProvince;
import ir.utopia.common.locality.state.bean.CmStateFacadeRemote;
import ir.utopia.common.locality.state.persistent.CmState;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.NativeEvent;
import ir.utopia.core.form.annotation.NativeEventType;
import ir.utopia.core.form.annotation.NativeScript;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.struts.AbstractUtopiaForm;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;

import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;


@PersistedMapForm
@DataInputForm(nativeScript=@NativeScript(script="" +
		"function nationalCodeCheck(){" +(char)13+
		"var nationCode = document.getElementById('bpartnerCode').value;"+(char)13+
        "Ext.Ajax.request({"+
		   "url: 'nationalCodeCheck_Cm_Bi_Employee.json'," +(char)13+
		   "success: function(response, opts) {" +(char)13+
		    "var jsonObj = Ext.decode(response.responseText);" +(char)13+
		    "document.getElementById('bpartnerName').value = jsonObj.bpartnerName"+(char)13+
		    "document.getElementById('secoundName').value = jsonObj.secoundName"+(char)13+
		    "document.getElementById('emailAddress').value = jsonObj.emailAddress"+(char)13+
		    "document.getElementById('empBpartnerId').value = jsonObj.empBpartnerId"+(char)13+
		    "document.getElementById('fatherName').value = jsonObj.fatherName"+(char)13+
		    "document.getElementById('lodgingAddress').value = jsonObj.lodgingAddress"+(char)13+
		    "document.getElementById('birthCertificateNumber').value = jsonObj.birthCertificateNumber"+(char)13+
		    "document.getElementById('birthCertificateSerial').value = jsonObj.birthCertificateSerial"+(char)13+
		    "document.getElementById('phoneNo').value = jsonObj.phoneNo"+(char)13+
		    "document.getElementById('mobile').value = jsonObj.mobile"+(char)13+
		    "document.getElementById('empPersonBpartnerId').value = jsonObj.empPersonBpartnerId"+(char)13+
		    "document.getElementById('birthdate').value = jsonObj.birthdate"+(char)13+
		    "var maritalStatusId = getGWTComponentId('maritalStatus');"+(char)13+
            "var maritalStatus = Ext.getCmp(maritalStatusId);" +(char)13+
            "maritalStatus.setValue(jsonObj.maritalStatus);"+(char)13+
            "var sexId = getGWTComponentId('sex');"+(char)13+
            "var sex = Ext.getCmp(sexId);" +(char)13+
            "sex.setValue(jsonObj.sex);"+(char)13+
            "var cmProvinceId = getGWTComponentId('cmProvince');"+(char)13+
            "var cmProvince = Ext.getCmp(cmProvinceId);" +(char)13+
            "cmProvince.setValue(jsonObj.cmProvince);"+(char)13+
            "var cmStateId = getGWTComponentId('cmState');"+(char)13+
            "var cmState = Ext.getCmp(cmStateId);" +(char)13+
            "cmState.setValue(jsonObj.cmState);"+(char)13+
		   "}," +(char)13+
		   "failure: function(response, opts) {" +(char)13+
		     " console.log('server-side failure with status code ' + response.status);" +(char)13+
		   "}," +(char)13+
		   "params:{'bpartnerCode':nationCode}" +(char)13+
		"});"+(char)13+
		"}"))
public class EmployeeForm  extends AbstractUtopiaForm<CmEmployee> {
	
	
	
	private Long cmEmployeeId;
	private Long cmPersonBpartner;
	private String bpartnerCode;
	private String bpartnerName;
	private String secoundName;
	private String fatherName;
	private String emailAddress;
	private Constants.Sex sex;
	private Long phoneNo;
	private Long mobile;
	private String birthdate;
	private Long cmState;
	private Long cmProvince;
	private Constants.MaritalStatus maritalStatus;
	private String birthCertificateNumber;
	private String birthCertificateSerial;
	private String org;
	private Long cmJobTitle;
	private String jobName;
	private Long cmEmployee;
	private String mngr;
	private String code;
	private String hireDate;
	private Long salary;
	private Long bonus;
	private String lodgingAddress;
	private Long empBpartnerId;
	private Long empPersonBpartnerId;
	
	
	public EmployeeForm(){
		
	}


	@FormId
	@FormPersistentAttribute
	public Long getCmEmployeeId() {
		return cmEmployeeId;
	}
	public void setCmEmployeeId(Long cmEmployeeId) {
		this.cmEmployeeId = cmEmployeeId;
	}


//	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
	@FormPersistentAttribute(method="getCmPersonBpartner")
//	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3)
	public Long getCmPersonBpartner() {
		return cmPersonBpartner;
	}
	public void setCmPersonBpartner(Long cmPersonBpartner) {
		this.cmPersonBpartner = cmPersonBpartner;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=3)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=3,breakLineAfter=false
	           ,nativeEvents=@NativeEvent(eventType=NativeEventType.onValueChanged,function="nationalCodeCheck")
	           ,readonlyLogic="groovy:\"update\".equals(@"+ContextUtil.CURRENT_ACTION_PARAMETER+"@)")
	public String getBpartnerCode() {
		return bpartnerCode;
	}
	public void setBpartnerCode(String bpartnerCode) {
		this.bpartnerCode = bpartnerCode;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=4)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=4,breakLineAfter=false)
	public String getBpartnerName() {
		return bpartnerName;
	}
	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=5)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=5,breakLineAfter=true)
	public String getSecoundName() {
		return secoundName;
	}
	public void setSecoundName(String secoundName) {
		this.secoundName = secoundName;
	}

	
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=6,breakLineAfter=false)
	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}


	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=14,breakLineAfter=true)
	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=6)
	@FormPersistentAttribute
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@InputItem(index=12,isManadatory=true,displayType=DisplayTypes.list,breakLineAfter=false)
	public Constants.Sex getSex() {
		return sex;
	}
	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}

	
	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=15,breakLineAfter=false)
	public Long getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(Long phoneNo) {
		this.phoneNo = phoneNo;
	}


	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=16,breakLineAfter=false)
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}


	
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Date,index=9,breakLineAfter=false)
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
	@FormPersistentAttribute(method = "getCmState")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=10,breakLineAfter=false)
	public Long getCmState() {
		return cmState;
	}

	public void setCmState(Long cmState) {
		this.cmState = cmState;
	}

	@FormPersistentAttribute(method = "getCmProvince")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=11,breakLineAfter=true)
	@LookupConfiguration(condition="CmProvince.cmState.cmStateId=@cmState@")
	public Long getCmProvince() {
		return cmProvince;
	}

	public void setCmProvince(Long cmProvince) {
		this.cmProvince = cmProvince;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=6)
	@FormPersistentAttribute
	@TypeConversion(converter="ir.utopia.core.struts.UtopiaTypeConverter")
	@InputItem(index=13,isManadatory=true,displayType=DisplayTypes.list,breakLineAfter=false)
	public Constants.MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(Constants.MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}


	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=7,breakLineAfter=false)
	public String getBirthCertificateNumber() {
		return birthCertificateNumber;
	}
	public void setBirthCertificateNumber(String birthCertificateNumber) {
		this.birthCertificateNumber = birthCertificateNumber;
	}


	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=8,breakLineAfter=true)
	public String getBirthCertificateSerial() {
		return birthCertificateSerial;
	}
	public void setBirthCertificateSerial(String birthCertificateSerial) {
		this.birthCertificateSerial = birthCertificateSerial;
	}
	

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=9)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM,index=7)
	@FormPersistentAttribute(method = "getCmJobTitle")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=17,breakLineAfter=true)
	public Long getCmJobTitle() {
		return cmJobTitle;
	}
	public void setCmJobTitle(Long cmJobTitle) {
		this.cmJobTitle = cmJobTitle;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=7)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM,index=8)
	@FormPersistentAttribute(method = "getCmEmployee",formToEntityMap=FormToEntityDataTypeMap.LONG_TO_LOOKUP)
	@InputItem(isManadatory=false,displayType=DisplayTypes.lookup,index=18,breakLineAfter=false)
	@LookupConfiguration(displayColumns={"code","cmPersonBpartner.cmBpartner.name","cmPersonBpartner.cmBpartner.secoundName"},displayItemSeperator="-")
	public Long getCmEmployee() {
		return cmEmployee;
	}
	public void setCmEmployee(Long cmEmployee) {
		this.cmEmployee = cmEmployee;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=8)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getMngr() {
		return mngr;
	}
	public void setMngr(String mngr) {
		this.mngr = mngr;
	}


	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=1)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1,breakLineAfter=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=false,displayType=DisplayTypes.Date,index=2,breakLineAfter=true)
	public String getHireDate() {
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}


	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=19,breakLineAfter=false)
	public Long getSalary() {
		return salary;
	}
	public void setSalary(Long salary) {
		this.salary = salary;
	}


	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.Integer,index=20,breakLineAfter=true)
	public Long getBonus() {
		return bonus;
	}
	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}
	
	
	@FormPersistentAttribute
	@InputItem(isManadatory=false,displayType=DisplayTypes.LargeString,index=23,breakLineAfter=true)
	public String getLodgingAddress() {
		return lodgingAddress;
	}

	public void setLodgingAddress(String lodgingAddress) {
		this.lodgingAddress = lodgingAddress;
	}

	@InputItem(isManadatory=false,index=21,breakLineAfter=true,displayType=DisplayTypes.Hidden)
	public Long getEmpBpartnerId() {
		return empBpartnerId;
	}


	public void setEmpBpartnerId(Long empBpartnerId) {
		this.empBpartnerId = empBpartnerId;
	}

	@InputItem(isManadatory=false,index=22,breakLineAfter=true,displayType=DisplayTypes.Hidden)
	public Long getEmpPersonBpartnerId() {
		return empPersonBpartnerId;
	}


	public void setEmpPersonBpartnerId(Long empPersonBpartnerId) {
		this.empPersonBpartnerId = empPersonBpartnerId;
	}


	@Override
	public UtopiaPersistent convertToPersistent() {
		
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		
		CmBpartner bpartner = new CmBpartner();
		if(empBpartnerId!=null){
			bpartner.setCmBpartnerId(empBpartnerId);
		}
		bpartner.setCode(WebUtil.formatStringNumbersToAnsi((String)getBpartnerCode()));
		bpartner.setName(getBpartnerName());
		bpartner.setSecoundName(getSecoundName());
		bpartner.setEmailaddress(getEmailAddress());
		try {
			BeanUtil.initPersistentObject(bpartner, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CmPersonBpartner person = new CmPersonBpartner();
		if(empPersonBpartnerId!=null){
			person.setCmPersonBpartnerId(empPersonBpartnerId);
		}
		person.setCmBpartner(bpartner);
		person.setPhoneNo(getPhoneNo());
		person.setMobile(getMobile());
		person.setSex(getSex());
		if(getBirthdate() instanceof String){
			
			 String strValue=getBirthdate().toString().trim();
				if(DateUtil.useSolarDate(locale) ){
					person.setBirthDate(strValue!=null&&strValue.trim().length()>0?DateUtil.solarToDate(strValue):null);
//					value=strValue!=null&&strValue.trim().length()>0?DateUtil.solarToDate(strValue):null;
				}else{
					person.setBirthDate(strValue!=null&&strValue.trim().length()>0?DateUtil.gerToDate(strValue):null);
//					value=strValue!=null&&strValue.trim().length()>0?DateUtil.gerToDate(strValue):null;
				}
		}
		if(getCmProvince()!=null){
			CmProvinceFacadeRemote provinceBean = null;
			try {
				provinceBean = (CmProvinceFacadeRemote)ServiceFactory.lookupFacade(CmProvinceFacadeRemote.class.getName());
			} catch (NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			CmProvince province = provinceBean.loadById(getCmProvince());
			person.setCmProvince(province);
		}
		if(getCmState()!=null){
			CmStateFacadeRemote stateBean = null;
			try {
				stateBean = (CmStateFacadeRemote)ServiceFactory.lookupFacade(CmStateFacadeRemote.class.getName());
			} catch (NamingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			CmState state = stateBean.loadById(getCmState());
			person.setCmState(state);
		}
		
		person.setFatherName(getFatherName());
		person.setLodgingAddress(getLodgingAddress());
		person.setMaritalStatus(getMaritalStatus());
		person.setBirthCertificateNumber(WebUtil.formatStringNumbersToAnsi((String)getBirthCertificateNumber()));
		person.setBirthCertificateSerial(WebUtil.formatStringNumbersToAnsi((String)getBirthCertificateSerial()));
		try {
			BeanUtil.initPersistentObject(person, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		CmEmployee employee = (CmEmployee)super.convertToPersistent();
		employee.setCmPersonBpartner(person);
		try {
			BeanUtil.initPersistentObject(employee, false, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return employee;
	}
	
	
	
	
	@Override
	public void importPersistent(UtopiaBasicPersistent persistent) {
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		Locale l=new Locale(locale);
		super.importPersistent(persistent);
		if(persistent instanceof CmEmployee){
			CmPersonBpartner person=((CmEmployee)persistent).getCmPersonBpartner();
			setSex(person.getSex());
			setPhoneNo(person.getPhoneNo());
			setMobile(person.getMobile());
			if(person.getBirthDate()!=null){
				
				setBirthdate(DateUtil.getDateString(person.getBirthDate(), l));
			}
			if(person.getCmProvince()!=null){
				setCmProvince(person.getCmProvince().getCmProvinceId());
			}
			if(person.getCmState()!=null){
				setCmState(person.getCmState().getCmStateId());
			}
			setFatherName(person.getFatherName());
			setLodgingAddress(person.getLodgingAddress());
			setMaritalStatus(person.getMaritalStatus());
			setBirthCertificateNumber(person.getBirthCertificateNumber());
			setBirthCertificateSerial(person.getBirthCertificateSerial());
			setEmpPersonBpartnerId(person.getCmPersonBpartnerId());
			    
			CmBpartner partners = person.getCmBpartner();
			if(partners!=null){
				setBpartnerCode(partners.getCode());
				setBpartnerName(partners.getName());
				setSecoundName(partners.getSecoundName());
				setEmailAddress(partners.getEmailaddress());
				setEmpBpartnerId(partners.getCmBpartnerId());
			}	
		}
	}

}
