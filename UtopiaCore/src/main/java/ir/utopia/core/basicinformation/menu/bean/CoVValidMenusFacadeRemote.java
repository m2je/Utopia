package ir.utopia.core.basicinformation.menu.bean;

import ir.utopia.core.basicinformation.menu.persistent.CoVSystemMenu;
import ir.utopia.core.bean.UtopiaBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoVValidMenusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoVValidMenusFacadeRemote extends UtopiaBean{
	
	/**
	 * Find all CoVSystemMenu entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoVSystemMenu property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoVSystemMenu> found by query
	 */
	public List<CoVSystemMenu> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all CoVSystemMenu entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoVSystemMenu> all CoVValidMenus entities
	 */
	public List<CoVSystemMenu> findAll(int... rowStartIdxAndCount);
	
	/**
	 * Find all CoVSystemMenu entities with where this user has the access .
	 * 
	 * @param propertyName
	 *            the name of the CoVSystemMenu property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoVSystemMenu> found by query
	 */
	public List<CoVSystemMenu> findByUserId(long userId,int... rowStartIdxAndCount);
	/**
	 * 
	 * @param userId
	 * @param resetCashe
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<CoVSystemMenu> findByUserId(long userId,boolean resetCashe,int... rowStartIdxAndCount);
}