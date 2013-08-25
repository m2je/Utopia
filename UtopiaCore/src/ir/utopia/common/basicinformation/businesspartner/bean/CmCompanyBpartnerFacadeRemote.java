package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmCompanyBpartner;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.persistent.UtopiaPersistent;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Remote interface for CmCompanyBpartnerFacade.
 * 
 * @author Arsalani
 */
@Remote
//public interface CmCompanyBpartnerFacadeRemote<P extends UtopiaPersistent,S extends UtopiaPersistent> extends UtopiaBasicUsecaseBean<P,S> {
public interface CmCompanyBpartnerFacadeRemote extends UtopiaBasicUsecaseBean<CmCompanyBpartner,CmCompanyBpartner> {
	

	
	public List<CmCompanyBpartner> findByCmBpartner_1(Object cmBpartner_1,Map<String,Object>context,
			int... rowStartIdxAndCount);

	public List<CmCompanyBpartner> findByAddress(Object address,Map<String,Object>context,
			int... rowStartIdxAndCount);

	
}