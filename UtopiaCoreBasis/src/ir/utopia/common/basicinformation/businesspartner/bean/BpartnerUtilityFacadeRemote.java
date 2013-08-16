package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.Sex;

import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface BpartnerUtilityFacadeRemote extends UtopiaBean{

	public CmBpartner createPersonPartner(Long partnerId,String name,String family,String code,
			Constants.Sex sex,Long address,String email,Date birthdate,Long mobile,Long phoneNo,
			Constants.MaritalStatus maritalStatus,String birthCertificateNumber,String birthCertificateSerial
			)throws Exception;
	
	public CmBpartner updateBpartner(Long partnerId,String name,String family,String code,
			Constants.Sex sex,Long address,String email,Date birthdate,Long mobile,Long phoneNo,
			Constants.MaritalStatus maritalStatus,String birthCertificateNumber,String birthCertificateSerial
			)throws Exception;

	public CmBpartner createPersonPartner(String name, String family, String code,
			Sex sex, Long address, String email)
			throws Exception;
}
