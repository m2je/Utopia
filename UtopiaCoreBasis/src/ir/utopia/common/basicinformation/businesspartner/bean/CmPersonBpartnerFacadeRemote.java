package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmPersonBpartnerFacade.
 * 
 * @author 
 */
@Remote
public interface CmPersonBpartnerFacadeRemote extends UtopiaBasicUsecaseBean<CmPersonBpartner,CmPersonBpartner> {
	

	public List<CmPersonBpartner> findBySex(Object sex,
			int... rowStartIdxAndCount);

	}