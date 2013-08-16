package ir.utopia.core.basicinformation.menu.bean;

import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.bean.AbstractUtopiaBean;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



/**
 * Facade for entity CoVValidMenus.
 * 
 * @see
 * @author 
 */
@Stateless
public class CoVValidMenusFacade extends AbstractUtopiaBean implements CoVValidMenusFacadeRemote {
	// property constants
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoVValidMenusFacade.class.getName());
	}
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Find all CoVValidMenus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoVValidMenus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<CoVValidMenus> found by query
	 */
	public List<CoVSystemMenu> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		return findByProperty(propertyName, value, false, rowStartIdxAndCount);
	}
	@SuppressWarnings("unchecked")
	public List<CoVSystemMenu> findByProperty(String propertyName,
			 Object value,boolean resetCache ,  int... rowStartIdxAndCount) {
		logger.log(Level.INFO,"finding CoVSystemMenu instance with property: "
				+ propertyName + ", value: " + value );
		try {
			final String queryString = "select model from CoVSystemMenu model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);	
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			if(resetCache){
				resetQueryCache(query);
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find by property name failed",  re);
			throw re;
		}
	}
	/**
	 * Find all CoVValidMenus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoVValidMenus> all CoVValidMenus entities
	 */
	@SuppressWarnings("unchecked")
	public List<CoVSystemMenu> findAll(final int... rowStartIdxAndCount) {
		logger.log(Level.INFO,"finding all CoVSystemMenu instances");
		try {
			final String queryString = "select model from CoVSystemMenu model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find all failed",  re);
			throw re;
		}
	}

	public List<CoVSystemMenu> findByUserId(long userId, int... rowStartIdxAndCount) {
		return findByUserId(userId, false, rowStartIdxAndCount);
	}

	@Override
	public List<CoVSystemMenu> findByUserId(long userId, boolean resetCashe,
			int... rowStartIdxAndCount) {
		return findByProperty("coUserId", userId,resetCashe, rowStartIdxAndCount);
	}

}