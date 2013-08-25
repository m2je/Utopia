package ir.utopia.common.basicinformation.location.bean;

import ir.utopia.common.basicinformation.location.persistent.CmLocation;
import ir.utopia.common.basicinformation.location.persistent.CmVLocation;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmLocationFacade.
 * 
 */
@Remote
public interface CmLocationFacadeRemote extends UtopiaBasicUsecaseBean<CmLocation,CmVLocation>{
	

	public List<CmLocation> findByName(Object name,
			int... rowStartIdxAndCount);

	public List<CmLocation> findByParentLocation(
			Object cmLocation, int... rowStartIdxAndCount);
	
	public List<CmLocation> findByParentName(
			Object parentName, int... rowStartIdxAndCount);
	
	public List<CmLocation> loadChildern(Long parentId);
	
	public List<CmLocation> findByOrganization(Object cmOrganization,
			int... rowStartIdxAndCount);
}