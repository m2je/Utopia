package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmVBpartner;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmBpartner.
 * 
 * @see ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CmBpartnerFacade extends AbstractBasicUsecaseBean<CmBpartner,CmVBpartner> implements CmBpartnerFacadeRemote {
	// property constants
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String SECOUND_NAME = "secoundName";
	public static final String ADDERS = "adders";
	public static final String EMAILADDRESS = "emailaddress";
	
	public List<CmBpartner> findByCode(Object code,int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

	public List<CmBpartner> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmBpartner> findByAdders(Object adders,
			int... rowStartIdxAndCount) {
		return findByProperty(ADDERS, adders, rowStartIdxAndCount);
	}

	public List<CmBpartner> findByEmailaddress(Object emailaddress,
			int... rowStartIdxAndCount) {
		return findByProperty(EMAILADDRESS, emailaddress, rowStartIdxAndCount);
	}


	

	
	

}