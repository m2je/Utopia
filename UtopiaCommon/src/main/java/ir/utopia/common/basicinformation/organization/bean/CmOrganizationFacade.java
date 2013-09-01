package ir.utopia.common.basicinformation.organization.bean;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.common.basicinformation.organization.persistent.CmVOrganization;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;


@Stateless
public class CmOrganizationFacade extends AbstractBasicUsecaseBean<CmOrganization,CmVOrganization> implements CmOrganizationFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String PARENT_NAME = "parentName";
	
	private static final Logger logger;

	static {
		logger = Logger.getLogger(CmOrganizationFacade.class.getName());
	}
	
	public List<CmOrganization> findByName(Object name,
			int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmOrganization> findByParentName(
			Object parentName, int... rowStartIdxAndCount) {
		return findByProperty(PARENT_NAME, parentName,
				rowStartIdxAndCount);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CmOrganization> loadChildern(Long parentId) {

		logger.log(Level.INFO, "finding all Children instances");
		try {
			if(parentId !=0){
			String q1 ="select co from CmOrganization co  where co.cmOrganizationId = :id";
			Query q2 = entityManager.createQuery(q1);
			q2.setParameter("id", parentId);
			List<CmOrganization> list =q2.getResultList();
			CmOrganization cm=new CmOrganization();
			if(list!=null && list.size()>0)
			 cm =list.get(0);
			final String queryString = "select co from CmOrganization co  where co.parentId = :parent ";// TODO
			Query query = entityManager.createQuery(queryString);
			query.setParameter("parent", cm);
			List<CmOrganization> result = query.getResultList();
			return (result == null || result.size() == 0) ? null : result;
			}else {
				final String queryString = "select co from CmOrganization co  where co.parentId is null ";// TODO
				Query query = entityManager.createQuery(queryString);
				List<CmOrganization> result = query.getResultList();
				return (result == null || result.size() == 0) ? null : result;
			}
			
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE, "find all failed", re);
			throw re;
		}
	}
	
}