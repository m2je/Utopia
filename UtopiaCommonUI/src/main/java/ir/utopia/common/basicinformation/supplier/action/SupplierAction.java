package ir.utopia.common.basicinformation.supplier.action;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import com.opensymphony.xwork2.ActionContext;

import ir.utopia.common.basicinformation.businesspartner.bean.CmBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.bean.CmCompanyBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.bean.CmPersonBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmCompanyBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.struts.AbstractUtopiaAction;
import ir.utopia.core.util.DateUtil;

public class SupplierAction extends AbstractUtopiaAction<SupplierForm> {
	
	
	private static final long serialVersionUID = -5494844081966569117L;
    SupplierForm model;
//********************************************************************************************
	@Override
	public String execute() throws Exception {
		
		return super.execute();
	}
//********************************************************************************************
	protected SupplierForm createModelInstance() {
		model = new SupplierForm();
		return model;
		
	}
//*********************************************************************************************
	public String personNationalCodeCheck() throws NamingException{
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		Locale l=new Locale(locale);

		CmBpartnerFacadeRemote bean = (CmBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmBpartnerFacadeRemote.class.getName());
		List<CmBpartner> bpartners = bean.findByProperty("code", model.getAgentCodeMelli(),  null);
		if(bpartners.size()>0){
			for(CmBpartner bpartner:bpartners){
				model.setAgentName(bpartner.getName());
				model.setAgentLastName(bpartner.getSecoundName());
				model.setAgentEmail(bpartner.getEmailaddress());
				model.setSupPBpartnerId(bpartner.getCmBpartnerId());
				CmPersonBpartnerFacadeRemote pBean = (CmPersonBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmPersonBpartnerFacadeRemote.class.getName());
				List<CmPersonBpartner> persons = pBean.findByProperty("cmBpartner.cmBpartnerId", bpartner.getCmBpartnerId(),  null);
				if(persons.size()>0){
					CmPersonBpartner person = persons.get(0);
					model.setMaritalStatus(person.getMaritalStatus());
					model.setBirthCertificateNumber(person.getBirthCertificateNumber());
					model.setBirthCertificateSerial(person.getBirthCertificateSerial());
					model.setSex(person.getSex());
					model.setAgentPhoneNo(person.getPhoneNo());
					model.setMobile(person.getMobile());
					model.setSupPersonBpartnerId(person.getCmPersonBpartnerId());
					if(person.getBirthDate()!=null){
						
						model.setBirthDate(DateUtil.getDateString(person.getBirthDate(), l));
					}

    			}
					
			}
		}

		return SUCCESS;
	}
//*********************************************************************************************
	public String companyNationalCodeCheck() throws NamingException{
		Map<String,Object>session= ActionContext.getContext().getSession();
		Map<String,Object>context= ContextUtil.createContext(session);
		String locale = ContextUtil.getLoginLanguage(context);
		Locale l=new Locale(locale);

		CmBpartnerFacadeRemote bean = (CmBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmBpartnerFacadeRemote.class.getName());
		List<CmBpartner> bpartners = bean.findByProperty("code", model.getLegalPersonNationalId(),  null);
		if(bpartners.size()>0){
			for(CmBpartner bpartner:bpartners){
				model.setCompany(bpartner.getName());
				model.setCeo(bpartner.getSecoundName());
				model.setCompanyEmail(bpartner.getEmailaddress());
				model.setSupCBpartnerId(bpartner.getCmBpartnerId());
				CmCompanyBpartnerFacadeRemote cBean = (CmCompanyBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmCompanyBpartnerFacadeRemote.class.getName());
				List<CmCompanyBpartner> comanies = cBean.findByProperty("cmBpartner.cmBpartnerId", bpartner.getCmBpartnerId(),  null);
				if(comanies.size()>0){
					CmCompanyBpartner comany = comanies.get(0);
					model.setWebsite(comany.getWebsite());
					model.setCompanyPhoneNo(comany.getPhoneNo());
					model.setEstablishedCode(comany.getEstablishedCode());
					model.setIranCode(comany.getIranCode());
					model.setZipCode(comany.getZipCode());
					model.setDescription(comany.getDescription());
					model.setSupCompanyBpartnerId(comany.getCmCompanyBpartnerId());
					if(comany.getEstablishedDate()!=null){
						
						model.setEstablishedDate(DateUtil.getDateString(comany.getEstablishedDate(), l));
					}
                    if(comany.getIranCodeDate()!=null){
						
						model.setIranCodeDate(DateUtil.getDateString(comany.getIranCodeDate(), l));
					}
    			}
					
			}
		}

		return SUCCESS;
	}
}
