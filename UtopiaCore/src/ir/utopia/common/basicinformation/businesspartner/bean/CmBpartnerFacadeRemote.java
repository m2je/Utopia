package ir.utopia.common.basicinformation.businesspartner.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmVBpartner;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmBpartnerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CmBpartnerFacadeRemote extends UtopiaBasicUsecaseBean<CmBpartner,CmVBpartner>{
	
	public CmBpartner findById(Long id);

	

	public List<CmBpartner> findByCode(Object code, int... rowStartIdxAndCount);

	public List<CmBpartner> findByName(Object name, int... rowStartIdxAndCount);

	public List<CmBpartner> findByAdders(Object adders,
			int... rowStartIdxAndCount);

	public List<CmBpartner> findByEmailaddress(Object emailaddress,
			int... rowStartIdxAndCount);
	
	
}