package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.bean.AbstractUtopiaBean;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants.MaritalStatus;
import ir.utopia.core.constants.Constants.Sex;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BpartnerUtilityFacade extends AbstractUtopiaBean implements BpartnerUtilityFacadeRemote {
	@PersistenceContext
	protected EntityManager entityManager; 
	
	
	@Override
	public CmBpartner createPersonPartner(Long partnerId,String name,String family,String code,
			Sex sex,Long address,String email,Date birthdate,Long mobile,Long phoneNo,
			MaritalStatus maritalStatus,String birthCertificateNumber,String birthCertificateSerial)
			throws Exception {
		
		CmBpartner partner=new CmBpartner();
		partner.setName(name);
		partner.setEmailaddress(email);
		partner.setCode(code);
		partner.setAdderss(address);
		partner.setSecoundName(family);
		CmPersonBpartner person=new CmPersonBpartner();
		person.setSex(sex);
		person.setBirthDate(birthdate);
		person.setMobile(mobile);
		person.setPhoneNo(phoneNo);
		person.setMaritalStatus(maritalStatus);
		person.setBirthCertificateNumber(birthCertificateNumber);
		person.setBirthCertificateSerial(birthCertificateSerial);
		person.setCmBpartner(partner);
		HashSet<CmPersonBpartner> persons= new HashSet<CmPersonBpartner>();
		persons.add(person);
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		BeanUtil.initPersistentObject(entityManager, person, false,context);
		partner.setCmPersonBpartner(persons);
		BeanUtil.initPersistentObject(entityManager, partner, false,context);
		return partner;
	}


	@Override
	public CmBpartner updateBpartner(Long partnerId,String name,String family,String code,
			Sex sex,Long address,String email,Date birthdate,Long mobile,Long phoneNo,
			MaritalStatus maritalStatus,String birthCertificateNumber,String birthCertificateSerial)
			throws Exception {
		
		if(partnerId>0){
			CmBpartner partner=entityManager.find(CmBpartner.class, partnerId);
			Set<CmPersonBpartner>person= partner.getCmPersonBpartner();
			partner.setName(name);
			partner.setAdderss(address);
			partner.setSecoundName(family);
			partner.setCode(code);
			partner.setEmailaddress(email);
			partner.setAdderss(address);
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
			for(CmPersonBpartner p: person){
				p.setSex(sex);
				p.setBirthDate(birthdate);
				p.setMobile(mobile);
				p.setPhoneNo(phoneNo);
				p.setMaritalStatus(maritalStatus);
				p.setBirthCertificateNumber(birthCertificateNumber);
				p.setBirthCertificateSerial(birthCertificateSerial);
				BeanUtil.initPersistentObject(entityManager, p, true,context);
			}
			BeanUtil.initPersistentObject(entityManager, partner, true, context);
			return partner;
		}
		return null;
	}

	@Override
	public CmBpartner createPersonPartner(String name, String family,
			String code, Sex sex, Long address, String email)
			throws Exception {
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		CmBpartner partner=new CmBpartner();
		partner.setName(name);
		partner.setEmailaddress(email);
		partner.setCode(code);
		partner.setAdderss(address);
		partner.setSecoundName(family);
		CmPersonBpartner person=new CmPersonBpartner();
		person.setSex(sex);
		person.setCmBpartner(partner);
		HashSet<CmPersonBpartner> persons= new HashSet<CmPersonBpartner>();
		persons.add(person);
		BeanUtil.initPersistentObject(entityManager, person, false,context);
		partner.setCmPersonBpartner(persons);
		BeanUtil.initPersistentObject(entityManager, partner, false, context);
		return partner;
	}
}
