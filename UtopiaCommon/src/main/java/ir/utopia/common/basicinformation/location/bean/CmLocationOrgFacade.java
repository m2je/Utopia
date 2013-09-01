package ir.utopia.common.basicinformation.location.bean;

import ir.utopia.common.basicinformation.location.persistent.CmLocationOrg;
import ir.utopia.core.bean.AbstractOrganizationSupportFacade;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;


/**
 * Facade for entity CmLocationOrg.
 * 
 * @see ir.utopia.common.basicinformation.location.persistent.CmLocationOrg
 * 
 */
@Stateless
public class CmLocationOrgFacade extends AbstractOrganizationSupportFacade<CmLocationOrg,CmLocationOrg> implements CmLocationOrgFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String PARENT_NAME = "parentName";
	public static final String PARENT_LOCATION = "cmLocation";
	public static final String ORGANIZATION = "cmOrganization";
	
	private static final Logger logger;

	static {
		logger = Logger.getLogger(CmLocationFacade.class.getName());
	}
	
	public List<CmLocationOrg> findByName(Object name,
			int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmLocationOrg> findByParentName(
			Object parentName, int... rowStartIdxAndCount) {
		return findByProperty(PARENT_NAME, parentName,
				rowStartIdxAndCount);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CmLocationOrg> loadChildern(Long parentId) {

		logger.log(Level.INFO, "finding all Children instances");
		try {
			if(parentId !=0){
			String q1 ="select cl from CmLocationOrg cl  where cl.cmLocationId = :id";
			Query q2 = entityManager.createQuery(q1);
			q2.setParameter("id", parentId);
			List<CmLocationOrg> list =q2.getResultList();
			CmLocationOrg cl=new CmLocationOrg();
			if(list!=null && list.size()>0)
			 cl =list.get(0);
			final String queryString = "select cl from CmLocationOrg cl  where cl.parentId = :parent ";// TODO
			Query query = entityManager.createQuery(queryString);
			query.setParameter("parent", cl);
			List<CmLocationOrg> result = query.getResultList();
			return (result == null || result.size() == 0) ? null : result;
			}else {
				final String queryString = "select cl from CmLocationOrg cl  where cl.parentId is null ";// TODO
				Query query = entityManager.createQuery(queryString);
				List<CmLocationOrg> result = query.getResultList();
				return (result == null || result.size() == 0) ? null : result;
			}
			
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE, "find all failed", re);
			throw re;
		}
	}

	public List<CmLocationOrg> findByParentLocation(
			Object cmLocationOrg, int... rowStartIdxAndCount) {
		return findByProperty(PARENT_LOCATION, cmLocationOrg,
				rowStartIdxAndCount);
	}
	
	public List<CmLocationOrg> findByOrganization(Object cmOrganization,
			int... rowStartIdxAndCount) {
		return findByProperty(ORGANIZATION,cmOrganization,
				rowStartIdxAndCount);
	}
}
