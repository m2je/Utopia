package ir.utopia.common.basicinformation.employee.action;


import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import com.opensymphony.xwork2.ActionContext;

import ir.utopia.common.basicinformation.businesspartner.bean.CmBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.bean.CmPersonBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.struts.AbstractUtopiaAction;
import ir.utopia.core.util.DateUtil;

public class EmployeeAction extends AbstractUtopiaAction<EmployeeForm> {
	
	private static final long serialVersionUID = -7556222531847466746L;
	
	EmployeeForm model;
//*********************************************************************************************
	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
//*********************************************************************************************
	protected EmployeeForm createModelInstance() {
		
		model = new EmployeeForm();
		return model;
		
	}
//*********************************************************************************************
	public String nationalCodeCheck() throws NamingException{
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		Locale l=new Locale(locale);

		CmBpartnerFacadeRemote bean = (CmBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmBpartnerFacadeRemote.class.getName());
		List<CmBpartner> bpartners = bean.findByProperty("code", model.getBpartnerCode(), null);
		if(bpartners.size()>0){
			for(CmBpartner bpartner:bpartners){
				model.setBpartnerName(bpartner.getName());
				model.setSecoundName(bpartner.getSecoundName());
				model.setEmailAddress(bpartner.getEmailaddress());
				model.setEmpBpartnerId(bpartner.getCmBpartnerId());
				CmPersonBpartnerFacadeRemote pBean = (CmPersonBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmPersonBpartnerFacadeRemote.class.getName());
				List<CmPersonBpartner> persons = pBean.findByProperty("cmBpartner.cmBpartnerId", bpartner.getCmBpartnerId(),  null);
				if(persons.size()>0){
					CmPersonBpartner person = persons.get(0);
					model.setFatherName(person.getFatherName());
					model.setLodgingAddress(person.getLodgingAddress());
					model.setMaritalStatus(person.getMaritalStatus());
					model.setBirthCertificateNumber(person.getBirthCertificateNumber());
					model.setBirthCertificateSerial(person.getBirthCertificateSerial());
					model.setSex(person.getSex());
					model.setPhoneNo(person.getPhoneNo());
					model.setMobile(person.getMobile());
					model.setEmpPersonBpartnerId(person.getCmPersonBpartnerId());
					if(person.getBirthDate()!=null){
					
						model.setBirthdate(DateUtil.getDateString(person.getBirthDate(), l));
					}
					if(person.getCmProvince()!=null){
						model.setCmProvince(person.getCmProvince().getCmProvinceId());
					}
					if(person.getCmState()!=null){
						model.setCmState(person.getCmState().getCmStateId());
					}
					
				}
					
			}
		}

		return SUCCESS;
	}	
	
}
