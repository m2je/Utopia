package ir.utopia.common.basicinformation.businesspartner.bean;

import java.util.List;
import java.util.Map;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmCompanyBpartner;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

/**
 * Facade for entity CmCompanyBpartner.
 * 
 * @see ir.utopia.common.basicinformation.businesspartner.persistent.CmCompanyBpartner
 * @author Arsalani 
 */
@Stateless
public class CmCompanyBpartnerFacade extends AbstractBasicUsecaseBean<CmCompanyBpartner,CmCompanyBpartner> implements CmCompanyBpartnerFacadeRemote {

	@Override
	public List<CmCompanyBpartner> findByCmBpartner_1(Object cmBpartner_1,
			Map<String, Object> context, int... rowStartIdxAndCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CmCompanyBpartner> findByAddress(Object address,
			Map<String, Object> context, int... rowStartIdxAndCount) {
		// TODO Auto-generated method stub
		return null;
	}

}
