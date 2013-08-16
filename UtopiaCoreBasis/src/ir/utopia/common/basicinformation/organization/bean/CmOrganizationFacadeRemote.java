package ir.utopia.common.basicinformation.organization.bean;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.common.basicinformation.organization.persistent.CmVOrganization;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface CmOrganizationFacadeRemote extends UtopiaBasicUsecaseBean<CmOrganization,CmVOrganization>{
	

	public List<CmOrganization> findByName(Object name,
			int... rowStartIdxAndCount);

	public List<CmOrganization> findByParentName(
			Object parentName, int... rowStartIdxAndCount);
	
	public List<CmOrganization> loadChildern(Long parentId);
}