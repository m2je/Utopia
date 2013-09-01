package ir.utopia.common.basicinformation.location.bean;

import ir.utopia.common.basicinformation.location.persistent.CmLocationOrg;
import ir.utopia.core.bean.OrganizationSupportFacade;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmLocationOrgFacade.
 * 
 */
@Remote
public interface CmLocationOrgFacadeRemote extends OrganizationSupportFacade<CmLocationOrg,CmLocationOrg> {

	public List<CmLocationOrg> findByName(Object name,
			int... rowStartIdxAndCount);

	public List<CmLocationOrg> findByParentLocation(
			Object cmLocationOrg, int... rowStartIdxAndCount);
	
	public List<CmLocationOrg> findByParentName(
			Object parentName, int... rowStartIdxAndCount);
	
	public List<CmLocationOrg> loadChildern(Long parentId);
	
	public List<CmLocationOrg> findByOrganization(Object cmOrganization,
			int... rowStartIdxAndCount);
	
}
