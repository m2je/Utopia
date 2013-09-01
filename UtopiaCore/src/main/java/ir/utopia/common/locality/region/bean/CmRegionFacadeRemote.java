package ir.utopia.common.locality.region.bean;

import ir.utopia.common.locality.region.persistent.CmRegion;
import ir.utopia.common.locality.region.persistent.CmVRegion;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmRegionFacade.
 * 
 * @author Jahani
 */
@Remote
public interface CmRegionFacadeRemote extends UtopiaBasicUsecaseBean<CmRegion,CmVRegion>{

	public List<CmRegion> findByName(Object name, int... rowStartIdxAndCount);

	public List<CmRegion> findByDescription(Object description,
			int... rowStartIdxAndCount);

	public List<CmRegion> findByCode(Object code,int... rowStartIdxAndCount);
}