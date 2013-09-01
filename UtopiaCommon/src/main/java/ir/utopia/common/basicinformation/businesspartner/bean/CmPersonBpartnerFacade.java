package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmPersonBpartner.
 * 
 * @see ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner
 * @author MyEclipse Persistence Tools
 */
@Stateless
public  class CmPersonBpartnerFacade extends AbstractBasicUsecaseBean<CmPersonBpartner,CmPersonBpartner> implements CmPersonBpartnerFacadeRemote {
	// property constants
	public static final String SEX = "sex";

	

	




	@Override
	public List<CmPersonBpartner> findBySex(Object sex,
			int... rowStartIdxAndCount) {
		// TODO Auto-generated method stub
		return null;
	}

}