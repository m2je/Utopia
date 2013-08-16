package ir.utopia.core.bean;

import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.constants.QueryComparsionType;
import ir.utopia.core.exception.DeleteRecordExeption;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.process.annotation.ActionParameter;
import ir.utopia.core.process.annotation.UsecaseAction;
import ir.utopia.core.process.annotation.UsecaseActions;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
@UsecaseActions(
		actions={
				@UsecaseAction(name="save",parameters={@ActionParameter(name="persistentObject", type= ActionParameterTypes.PesistentObject,index=0)}),
				@UsecaseAction(name="update",parameters={@ActionParameter(name="persistentObject",type= ActionParameterTypes.PesistentObject,index=0)}),
				@UsecaseAction(name="delete",parameters={@ActionParameter(name="persistentObject",type= ActionParameterTypes.PesistentObject,index=0)}),
				@UsecaseAction(name="search",parameters={
												  @ActionParameter(name="propsArray",type= ActionParameterTypes.PropertyArray,index=0),
												  @ActionParameter(name="propsValue",type= ActionParameterTypes.PropertyValueArray,index=1),
												  @ActionParameter(name="pagingArray",type= ActionParameterTypes.PagingArray,index=2)
												  })
				}
		)
public interface UtopiaBasicUsecaseBean<P extends UtopiaBasicPersistent,S extends UtopiaBasicPersistent> extends LookUpLoaderBean,TreeLoaderBean {
	public static final String CREATEDBY = "createdby";
	public static final String UPDATEDBY = "updatedby";
	public static final String CREATED = "created";
	public static final String UPDATED = "updated";
	public static final String ISACTIVE = "isactive";
	
	public static final int SAVE_BATCH_SIZE=10000;
	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @param rowStartIdxAndCount
	 * @return
	 */
	
	public List<S> search(String []propertyName,
			 Object []value,  int... rowStartIdxAndCount);
	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @param comparisontype
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<S> search(SearchConditionItem []items,OrderBy []orderby,  int... rowStartIdxAndCount);
	/**
	 * 
	 * @param items
	 * @param orderby
	 * @param defaultCondition
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<S> search(SearchConditionItem []items,OrderBy []orderby,Condition defaultCondition,  int... rowStartIdxAndCount);
	/**
	 * 
	 * @param query
	 * @param queryParameters
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<S> search(String query,Map<String,Object> queryParameters,  int... rowStartIdxAndCount);
	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public int getResultCount(String []propertyName, Object []value);
	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @param comparisontype
	 * @return
	 */
	public int getResultCount(SearchConditionItem []items);
	/**
	 * 
	 * @param items
	 * @param defaultCondition
	 * @return
	 */
	public int getResultCount(SearchConditionItem []items,Condition defaultCondition);
	/**
	 * 
	 * @param query
	 * @param queryParameters
	 * @return
	 */
	public int getResultCount(String query,Map<String,Object> queryParameters);
	/**
	 * Perform an initial save of a previously unsaved <P> entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            <P> entity to persist
	 * @throws SaveRecordException
	 *             when the operation fails
	 */
	public P save(P persitentObject) throws SaveRecordException;
	/**
	 * acts just like save method but save all detail attribute
	 * note: in case of one detail primary key value is less than zero it will call entityManager to delete it
	 * @param persitentObject
	 * @param handelDetails
	 * @throws SaveRecordException
	 */
	public P save(P persitentObject,boolean handelDetails) throws SaveRecordException;
	/**
	 * saves an array of persistent Objects and handles the transaction it self  
	 * @param persitentObject
	 * @param handelDetails
	 * @throws SaveRecordException
	 */
	public void saveAll(P[] persitentObject,boolean handelDetails,ProcessListener listener) throws SaveRecordException;
	/**
	 * @deprecated
	 * saves an array of persistent Objects and let the invoker to handle the transaction
	 * @param persitentObject
	 * @param entityManager
	 * @param handelDetails
	 * @throws SaveRecordException
	 */
	public void saveAll(P[] persitentObject,EntityManager entityManager,boolean handelDetails,ProcessListener listener) throws SaveRecordException;
	/**
	 * Delete a persistent P entity.
	 * 
	 * @param entity
	 *            P entity to delete
	 * @throws DeleteRecordExeption
	 *             when the operation fails
	 */
	public void delete(P persitentObject)throws DeleteRecordExeption;
	
	/**
	 * Delete all persistent object
	 * @param persitentObjecs
	 * @throws DeleteRecordExeption
	 */
	public void deleteAll(P[] persitentObjecs)throws DeleteRecordExeption;
	/**
	 * 
	 * @param persitentObjecs
	 * @param entityManager
	 * @throws DeleteRecordExeption
	 */
	public void deleteAll(P[] persitentObjecs, EntityManager entityManager)throws DeleteRecordExeption;
		/**
	 * Persist a previously saved P entity and return it or a
	 * copy of it to the sender. A copy of the CmCompanyBpartner entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            P entity to update
	 * @returns P the persisted P entity
	 *          instance, may not be the same
	 * @throws SaveRecordException
	 *             if the operation fails
	 */
	public P update(P persitentObject)throws SaveRecordException;
	/**
	 * acts as update
	 * note: in case of one detail primary key value is less than zero it will call entityManager to delete it
	 * @param persitentObject
	 * @param handelDetais
	 * @return
	 * @throws SaveRecordException
	 */
	public P update(P persitentObject,boolean handelDetais)throws SaveRecordException;
	/**
	 * updates an array of persistent Object on a same transaction and handles the transaction
	 * @param persitentObjects
	 * @param handelDetails
	 * @return
	 * @throws SaveRecordException
	 */
	public P[] updateAll(P[] persitentObjects, boolean handelDetails)throws SaveRecordException;
	/**
	 * updates an array of persistent Object on a same transaction and let the invoker to handles the transaction
	 * @param persitentObjects
	 * @param entityManager
	 * @param handelDetails
	 * @return
	 * @throws SaveRecordException
	 */
	public P[] updateAll(P[] persitentObjects,EntityManager entityManager, boolean handelDetails)throws SaveRecordException;
	
	/**
	 * 
	 * @param entityId
	 * @return
	 */
	public P findById(Long entityId);

	
	/**
	 * Load all lookup fields(for @OneToMany, @OneToOne, @OneToMany and @ManyToMany annotations)
	 *
	 * @param entityId
	 * @return
	 */
	public P loadByIdComplete(Long entityId);
	
	/**
	 * Load an object to view 
	 * @param entityId
	 * @return
	 */
	public S loadSearchObjectById(Long entityId);

	/**
	 * loads record and also records mapped by @OneToMany and @OneToOne annotation
	 * @param entityId
	 * @return
	 */
	public P loadById(Long entityId);
	/**
	 * Loads record and specified fields as its child records (for @OneToMany,@ManyToMany fields)
	 * @param entityId
	 * @param fieldsToLoad
	 * @return
	 */
	public P loadById(Long entityId ,String ...childrenFieldsToLoad);
	/**
	 * Find all P entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the P property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<P> found by query
	 */
	public List<P> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);
	
	public List<P> findByProperties(String[] propertyName, Object[] value,
			int... rowStartIdxAndCount);
	
	public List<P> findByProperties(String[] propertyName, Object[] value,boolean and,QueryComparsionType eqaulityType,
			int... rowStartIdxAndCount);
	
	public List<P> findByCreatedby(Long createdby,
			int... rowStartIdxAndCount);
	
	
	public List<P> findByUpdatedby(Long updatedby,
			int... rowStartIdxAndCount);

	public List<P> findByCreated(Date created,
			int... rowStartIdxAndCount);
	
	
	public List<P> findByUpdated(Date updated,
			int... rowStartIdxAndCount);

	public List<P> findByIsactive(IsActive isactive,
			int... rowStartIdxAndCount);
	
	/**
	 * Find all P entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<P> all P entities
	 */
	public List<P> findAll(int... rowStartIdxAndCount);
	/**
	 * find all records
	 * @param activeRecords
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<P> findAll(Boolean activeRecords,int... rowStartIdxAndCount);
	/**
	 * loads all record witch are currently active records
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<P> findAllActives(int... rowStartIdxAndCount);
	
	/**
	 * 
	 * @return
	 */
	public Class<P> getPersistentClass();	
		
	/**
	 * 
	 * @return
	 */
	public Class<S> getSearchModelClass();
	/**
	 * 	
	 * @param entityId
	 */
	public void refreshEntity(Long entityId);
	/**
	 * 
	 * @param persistents
	 * @return
	 */
	public BeanProcessExcutionResult<? extends P> validatePersistent(P ... persistents); 
	/**
	 * 
	 * @param model
	 * @return
	 */
	public int getReportResultCount(BeanReportModel model);
	/**
	 * 
	 * @param model
	 * @return
	 */
	public List<Object[]>getReportResult(BeanReportModel model,int from,int to);
}
