package ir.utopia.common.basicinformation.location.bean;

import ir.utopia.common.basicinformation.location.persistent.CmLocation;
import ir.utopia.common.basicinformation.location.persistent.CmVLocation;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CmLocation.
 * 
 * @see ir.utopia.common.basicinformation.location.persistent.CmLocation
 * 
 */
@Stateless
public class CmLocationFacade extends AbstractBasicUsecaseBean<CmLocation,CmVLocation> implements CmLocationFacadeRemote {
	// property constants
	public static final String NAME = "name";
	public static final String PARENT_NAME = "parentName";
	public static final String PARENT_LOCATION = "cmLocation";
	public static final String ORGANIZATION = "cmOrganization";
	
	private static final Logger logger;

	static {
		logger = Logger.getLogger(CmLocationFacade.class.getName());
	}
	
	public List<CmLocation> findByName(Object name,
			int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<CmLocation> findByParentName(
			Object parentName, int... rowStartIdxAndCount) {
		return findByProperty(PARENT_NAME, parentName,
				rowStartIdxAndCount);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CmLocation> loadChildern(Long parentId) {

		logger.log(Level.INFO, "finding all Children instances");
		try {
			if(parentId !=0){
			String q1 ="select cl from CmLocation cl  where cl.cmLocationId = :id";
			Query q2 = entityManager.createQuery(q1);
			q2.setParameter("id", parentId);
			List<CmLocation> list =q2.getResultList();
			CmLocation cl=new CmLocation();
			if(list!=null && list.size()>0)
			 cl =list.get(0);
			final String queryString = "select cl from CmLocation cl  where cl.parentId = :parent ";// TODO
			Query query = entityManager.createQuery(queryString);
			query.setParameter("parent", cl);
			List<CmLocation> result = query.getResultList();
			return (result == null || result.size() == 0) ? null : result;
			}else {
				final String queryString = "select cl from CmLocation cl  where cl.parentId is null ";// TODO
				Query query = entityManager.createQuery(queryString);
				List<CmLocation> result = query.getResultList();
				return (result == null || result.size() == 0) ? null : result;
			}
			
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE, "find all failed", re);
			throw re;
		}
	}

	public List<CmLocation> findByParentLocation(
			Object cmLocation, int... rowStartIdxAndCount) {
		return findByProperty(PARENT_LOCATION, cmLocation,
				rowStartIdxAndCount);
	}
	
	public List<CmLocation> findByOrganization(Object cmOrganization,
			int... rowStartIdxAndCount) {
		return findByProperty(ORGANIZATION,cmOrganization,
				rowStartIdxAndCount);
	}
	
	
}