package ir.utopia.core.bean;

import ir.utopia.core.Context;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.attachment.bean.CoAttachmentFacadeRemote;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.BooleanType;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.constants.QueryComparsionType;
import ir.utopia.core.customproperty.bean.CoCustomPropertyFacadeRemote;
import ir.utopia.core.exception.DeleteRecordExeption;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.MonitoredColumnInfo;
import ir.utopia.core.persistent.PersistentTranslationHelper;
import ir.utopia.core.persistent.SoftDeletePersistent;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.persistent.lookup.model.DetailPersistentValueInfo;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.persistent.parentchild.ParentChildPair;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.EventType;
import ir.utopia.core.revision.annotation.RevisionSupport;
import ir.utopia.core.revision.bean.CoRevisionFacadeRemote;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.language.LanguagePatch;
import ir.utopia.core.utilities.ArrayUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.MethodUtils;


	 
public abstract class AbstractBasicUsecaseBean<P extends UtopiaBasicPersistent,S extends UtopiaBasicPersistent> extends AbstractUtopiaBean implements UtopiaBasicUsecaseBean<P,S> {
	
	protected static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractBasicUsecaseBean.class.getName());
	}
	
	protected static Cache<String, String>CLASS_LOAD_QUERY_CACHE=new Cache<String, String>();
	
	
	
	protected static final Object[] O= new Object[]{};
	protected static final Class<?>[]C= new Class<?>[0];
    protected Class<P> persistentClass;
    protected Class<S> searchPersistentClass;
    @PersistenceContext
	protected EntityManager entityManager; 
    @EJB 
    protected CoAttachmentFacadeRemote attachmentFacade;
    @EJB
    protected CoCustomPropertyFacadeRemote customPropsFacade;
    @EJB 
    protected CoRevisionFacadeRemote revisionFacade;
    protected boolean supportRevisioning=false;
//**********************************************************************************************************
    @PostConstruct
    protected void initFacade(){
    	RevisionSupport revisionSupport= getPersistentClass().getAnnotation(RevisionSupport.class);
    	if(revisionSupport!=null){
    		supportRevisioning=true;
    	}
    } 
//**********************************************************************************************************
	public List<S> search(String []propertyName,
			final Object []value, final int... rowStartIdxAndCount) {
		return search(converToSearchItem(propertyName, value), null, rowStartIdxAndCount);
	}
//**********************************************************************************************************	
@Override
	public List<S> search(SearchConditionItem []items, OrderBy[] orderby,
			 int... rowStartIdxAndCount) {
		
	return search(items, orderby, null,  rowStartIdxAndCount);
	}
//**********************************************************************************************************
@SuppressWarnings("unchecked")
public List<S> search(SearchConditionItem []items, OrderBy[] orderby,Condition defaultCondition,
		 int... rowStartIdxAndCount) {
	if(defaultCondition!=null){
		defaultCondition.setCondtionString( defaultCondition.getCondition().replaceAll(getPersistentClass().getSimpleName()+".", getSearchModelClass().getSimpleName()+"."));
		if(defaultCondition.isLazyInitCondition()){
			Context context= ContextHolder.getContext();
			if(context!=null&&context.getContextMap()!=null){
				defaultCondition.initFromContext(context.getContextMap());
			}
		}
		
	}
	return (List<S>)searchEnity(entityManager,getSearchModelClass(),items,orderby,defaultCondition, rowStartIdxAndCount);
}
//**********************************************************************************************************
	@Override
	public int getResultCount(String[] propertyName, Object[] value
			) {
		return getResultCount(converToSearchItem(propertyName, value));
	}
//************************************************************************************************************
	@Override
	public int getResultCount(String query,
			Map<String, Object> queryParameters) {
		Query q=entityManager.createQuery(query);
		Set<String>params=queryParameters.keySet();
		for(String param:params){
			Object value=queryParameters.get(param);
			if(Boolean.class.isInstance(value)||boolean.class.isInstance(value)){
				value=Boolean.valueOf(value.toString())?BooleanType.True:BooleanType.False;
			}
			q.setParameter(param,value);
		}
		Object result=q.getSingleResult();
		return Integer.class.isInstance(result)?(Integer)result:((Long)result).intValue()  ;
	}
//************************************************************************************************************
	protected SearchConditionItem[] converToSearchItem(String[] propertyName, Object[] value){
		SearchConditionItem[] items=null;
		if(propertyName!=null){
			items=new SearchConditionItem[propertyName.length]; 
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
			for(int i=0;i<propertyName.length;i++){
				items[i]=new SearchConditionItem(getPersistentClass(),propertyName[i],value[i],QueryComparsionType.eqaul);
				context.put(propertyName[i], value[i]);
			}
		}
		return items;
	}
//************************************************************************************************************
	@Override
	public int getResultCount(SearchConditionItem []items) {
		return getResultCount(items, null);
	}
//************************************************************************************************************
	@Override
	public int getResultCount(SearchConditionItem []items,Condition defaultCondition) {
		try {
			if(defaultCondition!=null){
				defaultCondition.setCondtionString( defaultCondition.getCondition().replaceAll(getPersistentClass().getSimpleName()+".", getSearchModelClass().getSimpleName()+"."));
				if(defaultCondition.isLazyInitCondition())
					defaultCondition.initFromContext(ContextHolder.getContext().getContextMap());
			}
			Query query=getSearchQuery(items,null,getSearchModelClass(),defaultCondition,true);
			int result=((Long)query.getSingleResult()).intValue();
			return result;
		}catch(NoResultException e){
			return 0;
		} 
		catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find by property name failed", re);
			throw re;
		}
	}
	
//************************************************************************************************************	
	@SuppressWarnings("unchecked")
	public  List<? extends UtopiaBasicPersistent>searchEnity(EntityManager entityManager,Class<? extends UtopiaBasicPersistent>searchModel, SearchConditionItem []items,EntityPair []orderby, Condition defaultCondition,   final int... rowStartIdxAndCount) {
	try {
		Query query=getSearchQuery(items,orderby,searchModel,defaultCondition,false);
		setQueryStartEndIndex(query, rowStartIdxAndCount);
		
		List<? extends UtopiaBasicPersistent>result=query.getResultList();
		
		return result;
	} catch (RuntimeException re) {
		logger.log(Level.SEVERE,"find by property name failed", re);
		throw re;
	}
}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	protected Query getSearchQuery(SearchConditionItem []items,EntityPair []orderBy, Class<? extends UtopiaBasicPersistent>searchClass,Condition defaultCondition,boolean count){
		String entityName=searchClass.getSimpleName();
		final StringBuffer queryString =new StringBuffer("select ");
		if(count){
			queryString.append("count("). append(entityName).append(")");
		}else{
			queryString.append(entityName);
		}
		Set<SearchCondionWithIndex>joinItems=new HashSet<SearchCondionWithIndex>();
		queryString.append(" from ").append(entityName).append(" ").append(entityName).append(" @jointo@ ");
		ArrayList<Condition> conditions; 
		if(items!=null&&items.length>0){
			conditions=getConditions( items, searchClass, joinItems);
		}else{
			conditions=new ArrayList<Condition>();
		}
		if(defaultCondition!=null){
			conditions.add(defaultCondition);
		}
		StringBuffer joinStr=null;
		if(joinItems.size()>0){
			joinStr=new StringBuffer();
			
			boolean joinedTopersistent=false;
			for(SearchCondionWithIndex joinItem: joinItems){
					if(getPersistentClass().equals(joinItem.getEntityClass())){
						if(!joinedTopersistent)
							conditions.add(joinToPersistentClass(joinStr));
						joinedTopersistent=true;
					}else{
						String prop=BeanUtil.getPersistentLinkField(searchClass,
								(Class<? extends UtopiaPersistent>)joinItem.getEntityClass() , joinItem.getPropertyName());
						if(prop!=null){
							conditions.add(new Condition( entityName+"."+prop+"="+
									joinItem.getEntityName()+"."+joinItem.getPropertyName(),joinItem.isAndWithPreviousCondition(),null));
							joinStr.append(" ,").append(joinItem.getEntityClass().getSimpleName()).append(" ").append(joinItem.getEntityName());
						}else{
							 prop=BeanUtil.getPersistentLinkField(getPersistentClass(),
									(Class<? extends UtopiaPersistent>)joinItem.getEntityClass() , joinItem.getPropertyName());
							 if(prop==null){
								 if(logger.isLoggable(Level.FINE)){
									 logger.log(Level.WARNING,"invalid join item found "+joinItem.getEntityName());
									 }
								 continue;
							 }
							 if(!joinedTopersistent)
								 conditions.add(joinToPersistentClass(joinStr));
							 joinedTopersistent=true;
							 conditions.add(new Condition(getPersistentClass().getSimpleName()+"."+prop+"="+
										joinItem.getEntityName()+"."+joinItem.getPropertyName(),joinItem.isAndWithPreviousCondition(),null));
							 joinStr.append(" ,").append(joinItem.getEntityClass().getSimpleName()).append(" ").append(joinItem.getEntityName());
						}	
					}
				}
		
		} 
		
		if(conditions.size()>0){
				addConditionsString(queryString, conditions);
		}

		
		
		queryString.append(getOrderBy(orderBy));
		String jpql=queryString.toString();
		jpql=jpql.replaceAll("@jointo@",joinStr==null?"":joinStr.toString());
		Query query = entityManager.createQuery(jpql);
		resetQueryCache(query);
		if(conditions.size()>0){
			for(Condition condition: conditions){
				String []params =condition.getParameters();
				if(params!=null){
					for(String param:params){
						Object value=condition.getParameterValue(param);
						if(Boolean.class.isInstance(value)||boolean.class.isInstance(value)){
							value=Boolean.valueOf(value.toString())?BooleanType.True:BooleanType.False;
						}
						query.setParameter(param,value);
					}
				}
			}
		}
		return query;
	}
//**********************************************************************************************************
	private void addConditionsString(final StringBuffer queryString,
			List<Condition> conditions) {
		StringBuffer ands=new StringBuffer();
		StringBuffer ors=new StringBuffer();
		for(Condition condition: conditions){
			String condStr=condition.getCondition();
		if(condStr!=null&& condStr.trim().length()>0){
			if(condition.isAndWithPreviousCondition()){
				if(ands.length()>0){
					ands.append(" AND ");
				}
				ands.append(" (").append(condStr.trim()).append(" )");
			}else{
				if(ors.length()>0){
					ors.append(" OR ");
				}
				ors.append(" (").append(condStr.trim()).append(" )");
			}
			
		}
		}
		if(ands.length()>0||ors.length()>0){
			queryString.append(" where ");
			if(ors.length()>0){
				queryString.append(" ( ").append(ors).append(" ) ");
				if(ands.length()>0){
					queryString.append(" AND ");
				}
			}
			if(ands.length()>0){
				queryString.append(" ( ").append(ands).append(" ) ");
			}
		}
	}

//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	@Override
	public List<S> search(String query, Map<String, Object> queryParameters,
			 int... rowStartIdxAndCount) {
		Query q= entityManager.createQuery(query);
		setQueryStartEndIndex(q,rowStartIdxAndCount);
		if(queryParameters!=null){
			for(String param:queryParameters.keySet()){
				q.setParameter(param,queryParameters.get(param));
			}
		}
		return q.getResultList();
	}	
//**********************************************************************************************************
	protected Condition  joinToPersistentClass(StringBuffer joinStr ){
		String searchpk= AnnotationUtil.getPrimaryKeyName(getSearchModelClass());
		String persistentPk=AnnotationUtil.getPrimaryKeyName(getPersistentClass());
		joinStr.append(" ,").append(getPersistentClass().getSimpleName()).append(" ").append(getPersistentClass().getSimpleName());
		return new Condition(getSearchModelClass().getSimpleName()+"."+searchpk+"="+getPersistentClass().getSimpleName()+"."+persistentPk,null);
	}
//**********************************************************************************************************
	protected ArrayList<Condition> getConditions(SearchConditionItem []items,
			Class<? extends UtopiaBasicPersistent>searchClass,Set<SearchCondionWithIndex>joinItems){
		
		String currentEnityOwnerParam;
		ArrayList<Condition>result=new ArrayList<Condition>();
		Map<Class<?>,Integer>joinClasscountMap=new HashMap<Class<?>, Integer>();
		for(int i = 0; i < items.length ; i++){
			Class<?>currentClass=items[i].getEntityClass();
			currentClass=currentClass==null?searchClass:currentClass;
			if(!searchClass.equals(currentClass)){
				int counter;
				if(joinClasscountMap.containsKey(currentClass)){
					counter=joinClasscountMap.get(currentClass);
					counter++;
				}else{
					counter=1;
				}
					SearchCondionWithIndex nitem=new SearchCondionWithIndex(items[i],counter);
					currentEnityOwnerParam=getPersistentClass().equals(nitem.getEntityClass())?getPersistentClass().getSimpleName():nitem.getEntityName();
					joinClasscountMap.put(currentClass,counter );
					joinItems.add(nitem);
			}else{
				currentEnityOwnerParam=findEntityClass(items[i], searchClass);
				if(currentEnityOwnerParam==null){
					if(logger.isLoggable(Level.FINE)){
						logger.log(Level.WARNING,"property -->"+items[i].getPropertyName()+" was not found in class -->"+searchClass.getSimpleName()+
							" or class-->"+persistentClass.getSimpleName()+" igoring condition");
					}
					continue;
				}
			}
			result.add(getJoinCondition(items[i],   currentEnityOwnerParam,i));
		}
		return result;
	}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	protected String findEntityClass(SearchConditionItem item,Class<? extends UtopiaBasicPersistent>searchClass){
		Class<? extends UtopiaBasicPersistent> curClass=(Class<? extends UtopiaPersistent>)item.getEntityClass();
		Class<? extends UtopiaBasicPersistent>persistentClass=getPersistentClass();
		if(curClass==null||curClass.equals(persistentClass)){
			Method method=MethodUtils.getAccessibleMethod(searchClass, AnnotationUtil.getGetterMethodName(item.getPropertyName()), C) ;
			if(method==null){
				method=MethodUtils.getAccessibleMethod(persistentClass, AnnotationUtil.getGetterMethodName(item.getPropertyName()), C) ;
				if(method!=null){
					return persistentClass.getSimpleName();
				}else{
					return null;
				}
				
			}else{
				return searchClass.getSimpleName();
			}
		}else{
			String prop=BeanUtil.getPersistentLinkField(searchClass,
					curClass, item.getPropertyName());
			if(prop==null){
				prop=BeanUtil.getPersistentLinkField(persistentClass,
						curClass , item.getPropertyName());
				if(prop!=null){
					return persistentClass.getSimpleName();
				}else{
					
					return null;
				}
			}else{
			return searchClass.getSimpleName();
			}
		}
	}
//**********************************************************************************************************
	protected Condition getJoinCondition(SearchConditionItem item,
			String currentEnityOwnerParam,int index){
		
		if(QueryComparsionType.like.equals(item.getEqualityType())){
			return createLikeCondition(item, currentEnityOwnerParam, index);
		}else if(QueryComparsionType.in.equals(item.getEqualityType())){
			StringBuffer queryString=new StringBuffer();
				queryString.append(currentEnityOwnerParam).append(".").append(item.getPropertyName()).
				append(item.getEqualityType().getComparisonSign()).
				append("(");
				StringBuffer valueStr=new StringBuffer();
				Condition cond=new Condition(queryString.toString(),item.isAndWithPreviousCondition(),null);
				if(item.getValue()!=null){
					Object [] val=(Object [])item.getValue();
					for(int i=0;i<val.length;i++){
						String key="P_in_"+index+"_i";
						if(i>0){
							valueStr.append(",");
						}
						cond.setParameterValue(key, val[i]);
						valueStr.append(":").append(key);
					}
				}else{
					valueStr.append("null");
				}
				queryString.append(valueStr).append(")");
				cond.setCondtionString(queryString.toString());
				return cond;
		}
		else{
			StringBuffer queryString=new StringBuffer();
			String key="P__"+index;
			queryString.append(currentEnityOwnerParam).append(".").append(item.getPropertyName()).
			append(item.getEqualityType().getComparisonSign()).
			append(":").append(key);
			Condition cond=new Condition(queryString.toString(),item.isAndWithPreviousCondition(),null);
			cond.setParameterValue(key, item.getValue());
			return cond;
		}
		
	}

//**********************************************************************************************************
	protected Condition createLikeCondition(SearchConditionItem item,
			String currentEnityOwnerParam,int index){
		
		int internalIndex=0;
		String key="like_Cond_"+index+"_"+internalIndex;
		Condition condition=null;
		
		if(item.getValue()!=null){
			String language= ContextUtil.getLoginLanguage();
			LanguagePatch patch= ServiceFactory.getLanguagePatch(language);
			if(patch!=null){
				Object []alternateValues= patch.getAlternateValuesFor(item.getValue());
				if(alternateValues!=null&&alternateValues.length>0){
					Map<String,Object>parameterValueMap=new HashMap<String, Object>();
					StringBuffer queryString=new StringBuffer("(").append(getLikeCondtion(currentEnityOwnerParam, item, key));
					parameterValueMap.put(key, "%"+ item.getValue()+"%");
					for(Object alternateValue:alternateValues){
						internalIndex++;
						key="like_Cond_"+index+"_"+internalIndex;
						queryString.append(" OR ").append(getLikeCondtion(currentEnityOwnerParam, item, key));
						parameterValueMap.put(key,  "%"+alternateValue+ "%");
					}
					queryString.append(")");
					condition= new Condition(queryString.toString(),item.isAndWithPreviousCondition(),null);
					for(String k:parameterValueMap.keySet()){
						condition.setParameterValue(k, parameterValueMap.get(k));
					}
				}
			}
		}
		if(condition==null){
			condition= new Condition(getLikeCondtion(currentEnityOwnerParam, item, key).toString(),item.isAndWithPreviousCondition(),null);
			condition.setParameterValue(key,"%"+ (item.getValue()==null?"":item.getValue().toString().toLowerCase())+"%");
		}
		
		
		
		return condition;
	}
//**********************************************************************************************************
	protected StringBuffer getLikeCondtion(String currentEnityOwnerParam,SearchConditionItem item,String key){
		StringBuffer queryString=new StringBuffer();
		queryString.append("lower(").append(currentEnityOwnerParam).append(".").append(item.getPropertyName()).append(")").
		append(item.getEqualityType().getComparisonSign()).
		append(":").append(key);
		return queryString;
	}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	protected String getOrderBy(EntityPair []orderByColumns){
		if(orderByColumns==null){
			return getDefaultSearchOrderBy();
		}else{
			String []cols=new String [orderByColumns.length];
			String []classes=new String[cols.length];
			for(int i=0;i<cols.length;i++){
				cols[i]=orderByColumns[i].getPropertyName();
				Class<?>clazz=orderByColumns[i].getEntityClass();
				clazz=clazz==null?getSearchModelClass():clazz;
				try {
					Class<?>propClass=AnnotationUtil.getGetterMethodReturnType(clazz, cols[i]);
					if(UtopiaPersistent.class.isAssignableFrom(propClass)){
						cols[i]=cols[i]+"."+AnnotationUtil.getPrimaryKeyName(propClass);
					}
				} catch (Exception e) {
					logger.log(Level.WARNING,"", e);
				}
				if(SearchCondionWithIndex.class.isInstance(orderByColumns[i])){
					classes[i]=((SearchCondionWithIndex)orderByColumns[i]).getEntityName();	
				}else{
				classes[i]=clazz.getSimpleName();
				}
			}
			 return orderby(classes,cols);
		}
	}
//**********************************************************************************************************
	protected String getDefaultSearchOrderBy(){
		Class<?>clazz= getSearchModelClass();
		String []cols= AnnotationUtil.getPersitentColumns(clazz);
		String []classes=new String[cols.length];
		if(cols!=null&&cols.length>0){
			ArrayList<String>cole=new ArrayList<String>();
			int index=0;
			for(String col:cols){
				
				if("name".equalsIgnoreCase(col)||"code".equalsIgnoreCase(col)){
					cole.add(col);
					classes[index]=clazz.getSimpleName();
					index++;
					}
			}
			return orderby(classes, cole.toArray(new String[cole.size()]));
		}
		return null;
	}
//**********************************************************************************************************
	protected String orderby(String []className,String []cols){
		StringBuffer res=new StringBuffer();
		boolean first=true;
		int index=0;
		for(String col:cols){
				if(!first){
					res.append(",");
				}
				res.append(className[index]).append(".").append(col);
				first=false;
		}
		if(res.length()==0){
//			res.append(getSearchModelClass().getSimpleName()).append(".").append("created ");
			return "";
		}
		return " order by "+res.toString();
	}
//**********************************************************************************************************
	@Override
	public void refreshEntity(Long entityId) {
		entityManager.refresh(entityManager.find(getPersistentClass(), entityId));
		entityManager.refresh(entityManager.find(getSearchModelClass(), entityId));
	}	
//**********************************************************************************************************
	public List<NamePair> loadLookup(int ...rowStartEndIndex){
		Map<String,Object>context= ContextHolder.getContext().getContextMap();
		LookupConfigurationModel lookupConfigModel=BeanUtil.getLookupConfigurationModel(context,getPersistentClass());
		return loadLookup(lookupConfigModel, rowStartEndIndex);
	}
//**********************************************************************************************************
	public List<NamePair> loadLookup(LookupConfigurationModel lookupConfigModel, int ...rowStartEndIndex){
		return loadLookups( lookupConfigModel, rowStartEndIndex);
	}
//**********************************************************************************************************
	@Override
	public List<ParentChildPair> loadChildren(Long parendId,
			 int... rowStartEndIndex) {
		LookupConfigurationModel model=BeanUtil.getLookupConfigurationModel(ContextUtil.createContext( ContextHolder.getContext().getContextMap()), getPersistentClass());
		return loadChildren(model,parendId,  rowStartEndIndex);
	}

//**********************************************************************************************************
	@Override
	public List<ParentChildPair> loadChildren(LookupConfigurationModel model,Long parentId,
			 int... rowStartEndIndex) {
		if(parentId!=null){
			return loadTreeNodeChildren(model,parentId);
		}else{
			return loadTreeRoots(model);
		}
	}
//**********************************************************************************************************
	protected List<ParentChildPair> loadTreeRoots(LookupConfigurationModel model){
		Class<P> persistentClass =getPersistentClass();
		String persistentName= persistentClass.getSimpleName();
		String parentLinkField=BeanUtil.getSelfParentLinkFieldName(persistentClass);
		String childrencColumnName=BeanUtil.getSelfChildrenLinkFieldName(persistentClass);
		StringBuffer noChildRootsQuery=new StringBuffer(" select    distinct  ");
		StringBuffer hasChildRootsQuery=new StringBuffer(" select   ");
		StringBuffer loadingColumnNames=new StringBuffer(persistentName).append(".").append(model.getPrimaryKeyColumn());
		StringBuffer dispalyableFields = getDisplayFields(model,
				persistentClass,persistentName);
		loadingColumnNames.append(",").append(dispalyableFields);
		if(model.getDescriptionColumn()!=null){
			loadingColumnNames.append(",").append(persistentName).append(".").append(model.getDescriptionColumn());
		}
		noChildRootsQuery.append(loadingColumnNames).append(" from ").append(persistentName).append(" ").append(persistentName).append(" where ");
		hasChildRootsQuery.append(loadingColumnNames).append(" ,count(").append(persistentName).append(".").append(childrencColumnName).append(")  ");
		hasChildRootsQuery.append(" from ").append(persistentName).append(" ").append(persistentName).append(" where ");
		List<Condition>conditions= model.getConditions();
		StringBuffer conditionStr=convertConditionsToString(persistentClass,persistentName, conditions,false);
		if(conditionStr.length()>0){
			noChildRootsQuery.append(conditionStr).append(" and ");
			hasChildRootsQuery.append(conditionStr).append(" and ");
		}
		noChildRootsQuery.append(" ").append(persistentName).append(".").append(parentLinkField).append(" is null ");
		hasChildRootsQuery.append(" ").append(persistentName).append(".").append(parentLinkField).append(" is null ");
		noChildRootsQuery.append(" and ").append(persistentName).append(".").append(childrencColumnName).append(" is empty ");
		boolean shouldCheckIsActive=SoftDeletePersistent.class.isAssignableFrom( persistentClass);
		if(shouldCheckIsActive){
			noChildRootsQuery.append(" AND ").append(persistentName).append(".").append("isactive").append("=:isactive");
			hasChildRootsQuery.append(" AND ").append(persistentName).append(".").append("isactive").append("=:isactive");
		}
		hasChildRootsQuery.append(" group by ").append(loadingColumnNames);
		hasChildRootsQuery.append(" order by ").append(dispalyableFields);
		noChildRootsQuery.append(" order by ").append(dispalyableFields);
		Query noChildRoots=entityManager.createQuery(noChildRootsQuery.toString());
		Query hasChildRoots=entityManager.createQuery(hasChildRootsQuery.toString());
		initQueryConditionValues(noChildRoots,persistentClass, conditions,shouldCheckIsActive);
		initQueryConditionValues(hasChildRoots,persistentClass, conditions,shouldCheckIsActive);
		List<ParentChildPair> result= convertTreeQueryToPair(model,noChildRoots,false);
		result.addAll(convertTreeQueryToPair(model,hasChildRoots,true));
		return result; 
	}
//**********************************************************************************************************	
	protected List<ParentChildPair> loadTreeNodeChildren(LookupConfigurationModel model,Long parentId){
		Class<P> persistentClass =getPersistentClass();
		String persistentName= persistentClass.getSimpleName();
		String childColumnName=BeanUtil.getSelfChildrenLinkFieldName(persistentClass);
		String parentColumnName=BeanUtil.getSelfParentLinkFieldName(persistentClass);
		StringBuffer hasChildQuery=new StringBuffer("select ");
		StringBuffer noChildQuery=new StringBuffer("select distinct ").append(persistentName).append(".").append(model.getPrimaryKeyColumn());
		StringBuffer loadingColumnNames=new StringBuffer("children.").append(model.getPrimaryKeyColumn());
		StringBuffer dispalyableFields = getDisplayFields(model,
				persistentClass,"children");
		StringBuffer noChildDispalyableFields = getDisplayFields(model,
				persistentClass,persistentName);
		loadingColumnNames.append(",").append(dispalyableFields);
		if(model.getDescriptionColumn()!=null){
			loadingColumnNames.append(",").append("children.").append(model.getDescriptionColumn());
			noChildDispalyableFields.append(",").append(persistentName).append(".").append(model.getDescriptionColumn());
		}
		noChildQuery.append(",").append(noChildDispalyableFields);
		hasChildQuery.append(loadingColumnNames).append(",").append("count(children.").append(childColumnName).append(")").append(" from ").append(persistentName).append(" ").append(persistentName).
		append(" inner join ").append(persistentName).append(".").append(childColumnName).append(" children where ");
		noChildQuery.append(" from ").append(persistentName).append(" ").append(persistentName).append(" where ");
		noChildQuery.append(persistentName).append(".").append(childColumnName).append(" is empty and ");
		List<Condition>conditions= model.getConditions();
		StringBuffer conditionStr=convertConditionsToString(persistentClass,"children", conditions,false);
		StringBuffer noChildconditionStr=convertConditionsToString(persistentClass,persistentName, conditions,false);
		if(conditionStr.length()>0){
			hasChildQuery.append(conditionStr).append(" and ");
			noChildQuery.append(noChildconditionStr).append(" and ");
		}
		hasChildQuery.append(persistentName).append(".").append(model.getPrimaryKeyColumn()).append("=:").append(model.getPrimaryKeyColumn());
		noChildQuery.append(persistentName).append(".").append(parentColumnName).append(".").append(model.getPrimaryKeyColumn()).append("=:").
		append(model.getPrimaryKeyColumn());
		boolean shouldCheckIsActive=SoftDeletePersistent.class.isAssignableFrom( persistentClass);
		if(shouldCheckIsActive){
			hasChildQuery.append(" AND ").append(persistentName).append(".").append("isactive").append("=:isactive");
			noChildQuery.append(" AND ").append(persistentName).append(".").append("isactive").append("=:isactive");
		}
		hasChildQuery.append(" group by ").append(loadingColumnNames).append(" order by ").append(dispalyableFields);
		noChildQuery.append(" order by ").append(noChildDispalyableFields);
		Query hasChildQueryCommand= entityManager.createQuery(hasChildQuery.toString());
		Query noChildQueryCommand= entityManager.createQuery(noChildQuery.toString());
		hasChildQueryCommand.setParameter(model.getPrimaryKeyColumn(), parentId);
		noChildQueryCommand.setParameter(model.getPrimaryKeyColumn(), parentId);
		initQueryConditionValues(hasChildQueryCommand,persistentClass, conditions,shouldCheckIsActive);
		initQueryConditionValues(noChildQueryCommand,persistentClass, conditions,shouldCheckIsActive);
		List<ParentChildPair>result= convertTreeQueryToPair(model,hasChildQueryCommand,true);
		result.addAll(convertTreeQueryToPair(model,noChildQueryCommand,false));
		return result;
	}
//**********************************************************************************************************
	protected List<ParentChildPair>convertTreeQueryToPair(LookupConfigurationModel model,Query q,boolean hasCountColumn){
		List<ParentChildPair> result=new  ArrayList<ParentChildPair>();
		@SuppressWarnings("unchecked")
		List<Object[]>items= q.getResultList();
		List<EntityPair>pairsList=new ArrayList<EntityPair>();
		pairsList.add(new EntityPair(model.getOwnPersitentClass(), model.getPrimaryKeyColumn()));
		pairsList.addAll(model.getDisplayColumns());
		int descriptionColumnIndex=-1;
		if(model.getDescriptionColumn()!=null){
			pairsList.add(new EntityPair(model.getOwnPersitentClass(), model.getDescriptionColumn()));
			descriptionColumnIndex=pairsList.size()-1;
		}
		EntityPair []pairs=pairsList.toArray(new EntityPair[pairsList.size()]);
		
		for(Object []row:items){
//			StringBuffer name=new StringBuffer();
//			for(int j=1;j<row.length-(hasCountColumn?1:0);j++){
//				name.append(row[j]==null?"":row[j]);
//				if(j>1){
//					name.append(itemSeperator);
//				}
//			}
//			ParentChildPair pair=new ParentChildPair(name.toString(), (Long)row[0], hasCountColumn?(Long)row[row.length-1]:0l);
			ParentChildPair pair=new ParentChildPair(pairs,
													row,
													model.getSeperator(),
													0,
													descriptionColumnIndex,
													hasCountColumn?(Long)row[row.length-1]:0l);
			result.add(pair);
		}
		return result;//entityManager.createQuery("select children.cmOrganizationId,children.code,children.name,CmOrganization.description,count(children.children) from CmOrganization CmOrganization inner join CmOrganization.children children where CmOrganization.cmOrganizationId=1 group by children.cmOrganizationId,children.code,children.name,CmOrganization.description order by children.code,children.name").getResultList()
	}
//**********************************************************************************************************
	protected StringBuffer getDisplayFields(LookupConfigurationModel model,
			Class<P> persistentClass,String defaultPrefix) {
		StringBuffer dispalyableFields=new StringBuffer();
		int index=0;
		for(EntityPair column:model.getDisplayColumns()){
			if(index++>0){
				dispalyableFields.append(",");
			}
			if(column.getEntityClass()==null|| column.getEntityClass().equals(persistentClass)){
				dispalyableFields.append(defaultPrefix).append(".").append(column.getPropertyName());
			}else{
				dispalyableFields.append(defaultPrefix).append(".").append(column.getEntityClass().getSimpleName()).append(".").append(column.getPropertyName());
			}
		}
		return dispalyableFields;
	}
//**********************************************************************************************************
	protected List<NamePair> loadLookups(LookupConfigurationModel lookupConfigModel, int ...rowStartEndIndex){
		String loadQuery=getLookUpLoadQueryLookup(lookupConfigModel);
		return executeLookupQuery(loadQuery, lookupConfigModel,rowStartEndIndex);
	}
//************************************************************************************************************	
	protected String getLookUpLoadQueryLookup(LookupConfigurationModel lookupConfigModel){
		Class<P> persistenClass =getPersistentClass();
		 String persistentName= persistenClass.getSimpleName();
		 StringBuffer queryString=new StringBuffer(" select   ");
		 		queryString.append(" distinct ");
			StringBuffer selectingColumns=new StringBuffer();
			selectingColumns.append( lookupConfigModel.getDisplayColumnsCommaSeperated())
			.append("  ,")
			.append(lookupConfigModel.getOwnPersitentClass().getSimpleName()).append(".")
			.append(lookupConfigModel.getPrimaryKeyColumn());
			queryString.append(selectingColumns);
			queryString.append(" from ").append(persistentName).append(" ").append( persistentName );
			String join=lookupConfigModel.getJoinclause();
			if(join!=null&&join.trim().length()>0){
				queryString.append(join);
			}
			
			List<Condition>conditions= lookupConfigModel.getConditions();
			StringBuffer conditionStr=convertConditionsToString(persistenClass,persistentName, conditions,true);
			if(conditionStr.length()>0){
				queryString.append(" where ").append(conditionStr);
			}
			queryString.append(" order by ");
			StringBuffer orderby=new StringBuffer();
			if(lookupConfigModel.getOrderby()!=null&&lookupConfigModel.getOrderby().length>0){
				int index=0;
				for(String cOrderby :lookupConfigModel.getOrderby()){
					if(cOrderby.trim().length()>0){
						if(index>0){
							orderby.append(" , ");
						}
						orderby.append(cOrderby.trim());
					}
				}
			}
			if(orderby.length()==0){
				orderby=new StringBuffer( lookupConfigModel.getDisplayColumnsCommaSeperated() );
			}
			queryString.append(orderby);
			return queryString.toString();
	}
//************************************************************************************************************
	private StringBuffer convertConditionsToString(Class<P> persistenClass, String persistentName,List<Condition>conditions,boolean considerSoftDelete) {
		StringBuffer conditionsStr=new StringBuffer();
		if(considerSoftDelete&&SoftDeletePersistent.class.isAssignableFrom(persistenClass)){
			Condition isactive=new Condition(persistentName+".isactive=:isactive ", true,null);
			conditions.add(isactive);
		}
		if(conditions!=null){
			addConditionsString(conditionsStr, conditions);
		}
		return conditionsStr;
	}
//************************************************************************************************************	
	protected List<NamePair> executeLookupQuery(String loadQuery,LookupConfigurationModel lookupConfigModel,int ...rowStartEndIndex){
		Query query=entityManager.createQuery(loadQuery);
		
		List<Condition>conditions= lookupConfigModel.getConditions();
		initQueryConditionValues(query,lookupConfigModel.getOwnPersitentClass(), conditions,true);
		List<EntityPair>cols= lookupConfigModel.getDisplayColumns();
		
		String language=ContextUtil.getLoginLanguage();
		
		setQueryStartEndIndex(query, rowStartEndIndex);
		@SuppressWarnings("unchecked")
		List<Object[]>result=(List<Object[]>) query.getResultList();
		ArrayList<NamePair> resultValue = convertQueryDataToNamePair(
				lookupConfigModel, cols, language, result);
		return resultValue;
	}
//************************************************************************************************************	
	protected ArrayList<NamePair> convertQueryDataToNamePair(
			LookupConfigurationModel lookupConfigModel, List<EntityPair> cols,
			String language, List<Object[]> result) {
		ArrayList<NamePair>resultValue=new ArrayList<NamePair>();
		int displayColumnCount=lookupConfigModel.getDisplayColumns().size();
		EntityPair[]colsArray= cols.toArray(new EntityPair[cols.size()]);
		if(result!=null&&result.size()>0){
			for(Object[] c:result){
			if(displayColumnCount==1&&String.class.isInstance(c[0])){
				EntityPair currentPair=cols.get(0);
				String name=currentPair.isTranslated(language)?
						PersistentTranslationHelper.transalateRecord(currentPair.getEntityClass(), currentPair.getPropertyName(), (String)c[0], language) 
						: (String)c[0];
				resultValue.add(new NamePair(name, (Long)c[1]));
			}else{
				resultValue.add(new NamePair(colsArray, c, lookupConfigModel.getSeperator(),displayColumnCount));
			}
		}
			}
		return resultValue;
	}
//**********************************************************************************************************	
	protected void initQueryConditionValues(Query query,Class<?>persistentClass, List<Condition>conditions,boolean considerSoftDelete){
		if(considerSoftDelete&&SoftDeletePersistent.class.isAssignableFrom(persistentClass)){
			query.setParameter("isactive", Constants.IsActive.active);
		}
		if(conditions!=null){
			for(Condition condition: conditions){
				for(String param: condition.getParameters()){
					Object value= condition.getParameterValue(param);
					query.setParameter(param, value);
				}
			}
		}
	}
//**********************************************************************************************************	
	protected static void setQueryStartEndIndex(Query query,int ...rowStartIdxAndCount){
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
	} 

//**********************************************************************************************************
@SuppressWarnings("unchecked")
public Class<P> getPersistentClass(){
	if(persistentClass==null)
	 this.persistentClass =(Class<P>) (((ParameterizedType)getClass().getGenericSuperclass())).getActualTypeArguments()[0];
	return this.persistentClass;
}	
//**********************************************************************************************************	
@SuppressWarnings("unchecked")
public Class<S> getSearchModelClass(){
	if(searchPersistentClass==null)
	 this.searchPersistentClass =(Class<S>) (((ParameterizedType)getClass().getGenericSuperclass())).getActualTypeArguments()[1];
	return this.searchPersistentClass;
}	

//**********************************************************************************************************
	/**
	 * 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public P save(P persitentObject)throws SaveRecordException{
		return save(persitentObject, true);
	}
//**********************************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public P save(P persitentObject, boolean handelDetails)	throws SaveRecordException {
		P res=(P)doSave(persitentObject,entityManager, handelDetails);
		entityManager.flush();
		return res;
}
//**********************************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveAll(P []persitentObject,boolean handelDetails,ProcessListener listener)throws SaveRecordException{
		saveAll(persitentObject, entityManager, handelDetails,listener);
		
	}
//**********************************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveAll(P []persitentObject, EntityManager entityManager,boolean handelDetails,ProcessListener listener)throws SaveRecordException{
		if(persitentObject==null)return;
		Long t1=System.currentTimeMillis();
		entityManager.joinTransaction();
		entityManager.setFlushMode(FlushModeType.COMMIT);
		int totalSize=persitentObject.length;
		String poName=getPersistentClass().getSimpleName();
		logger.log(Level.INFO,"saving  "+totalSize+" of "+poName+" instance ");
		List<Object[]>dividedPersistents=ArrayUtils.breakArray(persitentObject, SAVE_BATCH_SIZE);
		int currentPage=0;
		for(Object []o:dividedPersistents){
			int curLenght=o.length;
			int offset=currentPage*SAVE_BATCH_SIZE;
			logger.log(Level.INFO," saving "+(offset+curLenght)+"/"+totalSize+" of "+poName);
			UtopiaPersistent []p=new UtopiaPersistent[curLenght];
			System.arraycopy(o, 0, p, 0, curLenght);
			doSaveAll(p,  entityManager, handelDetails, listener,offset);
			logger.log(Level.INFO," saving "+(offset+curLenght)+"/"+totalSize+" of "+poName+" was successful");
			currentPage++;
		}
		logger.log( Level.INFO,"saving  "+totalSize+" of "+poName+" instance was successfull in "+(System.currentTimeMillis()-t1)+" ms ");
	}

//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	protected void doSaveAll(UtopiaBasicPersistent []persitentObject,EntityManager entityManager,boolean handelDetails,ProcessListener listener,int offset)throws SaveRecordException{
		int currentStep=0;
		try {
			for (;currentStep < persitentObject.length; currentStep++) {
				if (persitentObject[currentStep] == null)
					continue;
				doSaveNoLog((P)persitentObject[currentStep],  entityManager, handelDetails, false,false);
				if (listener != null) {
					ProcessStatusChangeEvent e = new ProcessStatusChangeEvent(EventType.processStatusChanged);
					e.setProcessStatus(offset+currentStep + 1);
					listener.notifyStatusChanged(e);
				}
			}
			entityManager.flush();
			if (listener != null) {
				ProcessStatusChangeEvent e = new ProcessStatusChangeEvent(EventType.processFinished);
				e.setProcessStatus(offset+persitentObject.length);
				listener.notifyStatusChanged(e);
			}
			entityManager.clear();
		} catch (Exception ex) {
			logger.log(Level.WARNING,"", ex);
			if (listener != null) {
				ProcessStatusChangeEvent e = new ProcessStatusChangeEvent(EventType.processFailed);
				e.setException(ex);
				e.setProcessStatus(offset+currentStep + 1);
				listener.notifyStatusChanged(e);
			}
			throw new SaveRecordException(ex);
		}
	}
//**********************************************************************************************************
	protected P doSave(P persitentObject,EntityManager entityManager, boolean handelDetails)throws SaveRecordException{
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"saving  "+persitentObject.getClass()+" instance" );
		}
		P p= doSaveNoLog(persitentObject, entityManager, handelDetails, true,true);
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"save successful");
		}
		return p;
	}
//**********************************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected P doSaveNoLog(P persitentObject,EntityManager entityManager, boolean handelDetails,boolean autoFlush,boolean referesh)throws SaveRecordException{		
		if(persitentObject==null){
			throw new SaveRecordException("cannot save null Entity !!!!!");
		}
		try {
			initPersitentObject(persitentObject, false);
			initLookupInfos(persitentObject);
			String revisinDescription=persitentObject.getRevisionDescription();
			entityManager.persist(persitentObject);
			if(handelDetails){
				handelDetails(persitentObject);
			}
			if(autoFlush)
			{	
				entityManager.flush();
			}
			if(supportRevisioning){
				fixToplinkDetailCacheIssue(entityManager, persitentObject);
				revisionFacade.createRevision(persitentObject,revisinDescription);
			}
			if(referesh)
				entityManager.refresh(persitentObject);
			
			attachmentFacade.handleAttachments(persitentObject,persitentObject.getAttachmentInfos(), predefindedActions.save);
			customPropsFacade.handleProperties(persitentObject, persitentObject.getCustomPropertyList() , predefindedActions.save);
			return persitentObject;
		} catch (Exception re) {
			
			logger.log(Level.SEVERE,"save failed", re);
		
			throw new SaveRecordException(re);
		}
	}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	protected void handelDetails(UtopiaBasicPersistent persitentObject)throws Exception{
		Class<? extends UtopiaBasicPersistent>clazz=persitentObject.getClass();
		Set<EntityPair>detailFields= BeanUtil.getLinkedFieldsWithClass(clazz, OneToMany.class);
		for(EntityPair column:detailFields){
			Collection<DetailPersistentValueInfo> details= persitentObject.getIncludedPersistentValue(column.getPropertyName());
			if(details!=null&&details.size()>0){
				StringBuffer deleteDetailStatement=new StringBuffer("delete from  ").append(column.getEntityClass().getSimpleName()).append(" model where model.").
						append(BeanUtil.getPrimaryKeyColumn(column.getEntityClass())).append(" in ( ");
				ArrayList<Long>deletedIds=new ArrayList<Long>();
				for(DetailPersistentValueInfo detailInfo:details){
					UtopiaBasicPersistent detailP=detailInfo.getValue();
					Long detailRecordId=detailP.getRecordId();
					if(detailInfo.isDeleted()){
						if(detailRecordId!=null&&detailRecordId!=0){
							if(deletedIds.size()>0){
								deleteDetailStatement.append(",");
							}
							deleteDetailStatement.append(":deleted").append(deletedIds.size());
							deletedIds.add(detailRecordId);
						}
						
					}else{
						initLookupInfos(detailP);
	    				setParentRecordOf(detailP,persitentObject);
	    				
	    				if(detailRecordId!=null&&detailRecordId!=0){
	    					initPersitentObject(detailP,  true);
	    					entityManager.merge(detailP);
	    				}else{
	    					initPersitentObject(detailP,  false);
	    					entityManager.persist(detailP);
	    				}
					}
				}
				deleteDetailStatement.append(")");
				if(deletedIds.size()>0){
					Query deleteQuery=entityManager.createQuery(deleteDetailStatement.toString());
					int index=0;
					for(Long deletedId:deletedIds){
						deleteQuery.setParameter("deleted"+index++, deletedId);
					}
					deleteQuery.executeUpdate();
				}
			}
		}

	}
//**********************************************************************************************************
	protected void setParentRecordOf(UtopiaBasicPersistent p,UtopiaBasicPersistent parent)throws Exception{
		Set<String>propsArray=AnnotationUtil.getParentRelations(p.getClass(), parent.getClass());
		if(propsArray.size()==1){
			String props= propsArray.iterator().next();
			MethodUtils.invokeExactMethod(p,AnnotationUtil.getSetterMethodOfField(props) , parent);
		}else{
			
		}
	}

//**********************************************************************************************************
	/**
	 * assign lookup values to persistentObject 
	 * @param persitentObject
	 */
protected void initLookupInfos(UtopiaBasicPersistent persitentObject)   {
	BeanUtil.initLookupInfos(entityManager, persitentObject);
}
//**********************************************************************************************************	
	@SuppressWarnings("unchecked")
	protected void doDelete(UtopiaBasicPersistent persitentObject, EntityManager entityManager) throws DeleteRecordExeption {
		if(persitentObject==null){
			throw new DeleteRecordExeption("cannot delete null Entity !!!!!");
		}
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"deleting  "+persitentObject.getClass()+" instance");
		}
		
		try {
			
			persitentObject =(P) entityManager.getReference(persitentObject.getClass(), persitentObject
					.getRecordId());
			
			entityManager.remove(persitentObject);
			if(logger.isLoggable(Level.FINE)){
				logger.log(Level.INFO, "delete successful" );
			}
		} catch (Exception re) {
			logger.log(Level.SEVERE,"delete failed", re);
			throw new DeleteRecordExeption(re);
		}
	}
//**********************************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(P persitentObject) throws DeleteRecordExeption{
		doDelete(persitentObject,entityManager);
		entityManager.flush();
	}
//**********************************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAll(P[] persistentObjects) throws DeleteRecordExeption{
		deleteAll(persistentObjects, entityManager);
		entityManager.flush();
	}	
//**********************************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAll(P[] persistentObjects, EntityManager entityManager) throws DeleteRecordExeption{
		for(UtopiaBasicPersistent persist:persistentObjects){
			doDelete(persist,entityManager);
		}
	}
//**********************************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public P update(P persitentObject) throws SaveRecordException {
		return update(persitentObject,true);
	}
//**********************************************************************************************************	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public P update(P persitentObject, boolean handelDetails)	throws SaveRecordException {
		List<MonitoredColumnInfo>monitoredColumns=null;
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		if(context.containsKey(MonitoredColumnInfo.MONITORED_COLUMNS_CONTEXT_KEYS)){
				 monitoredColumns=(List<MonitoredColumnInfo>) context.get(MonitoredColumnInfo.MONITORED_COLUMNS_CONTEXT_KEYS);
				 if(monitoredColumns!=null&&monitoredColumns.size()>0){
					 StringBuffer q=new StringBuffer("SELECT ");
					 int index=0;
					 for(MonitoredColumnInfo column:monitoredColumns ){
						 if(index>0){
							 q.append(",");
						 }
						 q.append(" model.").append(column.getColumn());
						 index++;
					 }
					 q.append(" FROM ").append(persitentObject.getClass().getSimpleName()).append(" model WHERE model.").
					 append(BeanUtil.getPrimaryKeyColumn(persitentObject.getClass())).append("=:Id");
					 Query query= entityManager.createQuery(q.toString());
					 query.setParameter("id", persitentObject.getRecordId());
					 Object[] old=(Object[])query.getSingleResult();
					 for(int i=0;i<old.length;i++){
						 MonitoredColumnInfo monitoredColumn=monitoredColumns.get(i);
						 monitoredColumn.setOldValue(old[i]);
						 monitoredColumn.setNewValue(BeanUtil.getFieldValue(persitentObject, monitoredColumn.getColumn()));
						 try {
							monitoredColumn.verify();
						} catch (Exception e) {
							throw new SaveRecordException("fail to verify values for update ",e);
						}
					 }
				 }
			}
		P result= doUpdate(persitentObject,entityManager, handelDetails);
		entityManager.flush();
		return result;
	}
//**********************************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public P[] updateAll(P[] persitentObjects, boolean handelDetails)throws SaveRecordException{
		P []result =updateAll(persitentObjects,entityManager, handelDetails);
		entityManager.flush();
		return result;
	}
//**********************************************************************************************************	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public P[] updateAll(P[] persitentObjects,EntityManager entityManager, boolean handelDetails)throws SaveRecordException{
		for(P pers:persitentObjects){
		     pers=doUpdate(pers,entityManager, handelDetails);	
		}
		return persitentObjects;
	}
//**********************************************************************************************************
	protected P doUpdate(UtopiaBasicPersistent persitentObject,EntityManager entityManager, boolean handelDetails)	throws SaveRecordException {
		if(persitentObject==null){
			throw new SaveRecordException("cannot update null Entity !!!!!");
		}
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"updated  "+persitentObject.getClass()+" instance");
		}
		P res=doUpdateNoLong(persitentObject,  entityManager, handelDetails);
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"update successful" );
		}
		return res;
	}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected P doUpdateNoLong(UtopiaBasicPersistent persitentObject,EntityManager entityManager, boolean handelDetails)throws SaveRecordException{
		try {
			initPersitentObject(persitentObject, true);
			initLookupInfos(persitentObject);
			if(handelDetails){
				handelDetails(persitentObject);
				}
			String revisionDescription=persitentObject.getRevisionDescription();
			P result =(P) entityManager.merge(persitentObject);
			entityManager.flush();
			attachmentFacade.handleAttachments(persitentObject,persitentObject.getAttachmentInfos(), predefindedActions.update);
			customPropsFacade.handleProperties(persitentObject, persitentObject.getCustomPropertyList() , predefindedActions.update);
			if(supportRevisioning){
				result.setRevisionDescription(revisionDescription);
				fixToplinkDetailCacheIssue(entityManager, result);//FIX for Toplink OneToMany cache issue
				revisionFacade.createRevision(result,revisionDescription);
			}
			return result;
		} catch (Exception re) {
			logger.log(Level.SEVERE, "update failed", re);
			throw new SaveRecordException(re);
		}
	}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	protected void fixToplinkDetailCacheIssue(EntityManager entityManager,
			P result) {
		Set<String>detailFields= BeanUtil.getLinkedFields(persistentClass,OneToMany.class);
		if(detailFields!=null){
			for(String field:detailFields){
				Query q= entityManager.createQuery("Select model."+field+" from "+persistentClass.getSimpleName()+" model where model."+BeanUtil.getPrimaryKeyColumn(persistentClass)+"=:p1");
				q.setParameter("p1", result.getRecordId());
				List<?>details= q.getResultList();
				try {
					Collection<Object> c=(Collection<Object>)MethodUtils.invokeExactMethod(result, AnnotationUtil.getGetterMethodName(field), C);
					for(Object detail:details){
						if(!c.contains(detail)){
							c.add(detail);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		}
	}
//**********************************************************************************************************
	@SuppressWarnings("unchecked")
	public P findById(Long entityId) {
		Class<?>clazz=getPersistentClass();
		if(logger.isLoggable(Level.FINE)){
			logger.log( Level.INFO,"finding "+clazz+" instance with id: " + entityId);
		}
		try {
			P instance =(P) entityManager.find(clazz, entityId);
			return instance;
		} catch (RuntimeException re) {
			logger.log( Level.SEVERE,"find failed", re);
			throw re;
		}
	
	}
//************************************************************************************************************	
	public P loadById(Long entityId) {
		return loadById(entityId,new String[0]);
	}
//************************************************************************************************************
	public P loadByIdComplete(Long entityId){
		return loadByIdComplete(getPersistentClass(), entityId);
	}
//************************************************************************************************************
@Override
	public S loadSearchObjectById(Long entityId){
		return loadByIdComplete(getSearchModelClass(), entityId);
	}
//************************************************************************************************************
protected <Q>Q loadByIdComplete(Class<Q> clazz,Long entityId){
	@SuppressWarnings("unchecked")
	Set<Method> methodsSet = AnnotationUtil.getMethodAnnotations(clazz, 
			OneToOne.class, ManyToOne.class, OneToMany.class, ManyToMany.class);
	String[] fields = new String[methodsSet.size()]; 
	Iterator<Method> iter = methodsSet.iterator();
	int i = 0;
	while (iter.hasNext()) {
		Method method = iter.next();
		fields[i++] = AnnotationUtil.getPropertyName(method.getName());
	}
	return loadById(clazz,entityId, fields);
}
//************************************************************************************************************
	public P loadById(Long entityId,String ...childrenFieldsToLoad) {
		return loadById(getPersistentClass(), entityId, childrenFieldsToLoad);
	}
//************************************************************************************************************	
	@SuppressWarnings("unchecked")
	protected <Q>Q loadById(Class<Q> clazz,Long entityId,String ...childrenFieldsToLoad) {
		try {
			Query query= entityManager.createQuery(getLoadQuery(clazz,childrenFieldsToLoad));
			resetQueryCache(query);
			query.setFlushMode(FlushModeType.COMMIT);
			query.setParameter("recordId", entityId);
			Q result= (Q)query.getSingleResult();
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING, "fail to load record", e);
		}
		return null;
	}
//************************************************************************************************************
	@SuppressWarnings("unchecked")
	private String getLoadQuery(Class<?>clazz,String ...childrenFieldsToLoad){
		String key=clazz.getName();
		if(childrenFieldsToLoad!=null&&childrenFieldsToLoad.length>0){
			for(String s:childrenFieldsToLoad){
				key+="|"+s;
			}
		}
		if(!CLASS_LOAD_QUERY_CACHE.containsKey(key)){//CLASS_LOAD_QUERY_CACHE.clear()
			CLASS_LOAD_QUERY_CACHE.put(key,getClassLoadQuery((Class<? extends UtopiaPersistent>)clazz,childrenFieldsToLoad));
		}
		return CLASS_LOAD_QUERY_CACHE.get(key);
	} 
//************************************************************************************************************
	@SuppressWarnings("unchecked")
	protected String getClassLoadQuery(Class<? extends UtopiaPersistent>clazz,String ...childrenFieldsToLoad){
		String className=clazz.getSimpleName();
		StringBuffer result=new StringBuffer("select distinct model from ");
		result.append(className).append(" model ");
		Set<String>linkedFields= BeanUtil.getLinkedFields(clazz,OneToOne.class,ManyToOne.class,OneToMany.class);
		if(childrenFieldsToLoad!=null&&childrenFieldsToLoad.length>0){
			for(String s:childrenFieldsToLoad){
				if(linkedFields.contains(s)){
					result.append(" left join fetch model.").append(s);
				}
			}
		}
		else{
		for(String field: linkedFields){
			if(field!=null){
				result.append(" left join fetch model.").append(field);
			}
		}
		}
		
		result.append(" where model.").append(BeanUtil.getPrimaryKeyColumn(clazz)).append("=:recordId");
		return result.toString();
	}
//************************************************************************************************************
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
	 *            number of results to return.
	 * @return List<P> found by query
	 */
	
	public List<P> findByProperty(String propertyName,
			final Object value,final int... rowStartIdxAndCount) {
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"finding  instance with property: " + propertyName
					+ ", value: " + value);
		}
		return findByProperties(new String[]{propertyName}, new Object[]{value}, rowStartIdxAndCount);
	}
//************************************************************************************************************
	@Override
	public List<P> findByProperties(String[] propertyName, Object[] value,
			int... rowStartIdxAndCount) {
		return findByProperties(propertyName, value, true,QueryComparsionType.eqaul,  rowStartIdxAndCount);
	}
//************************************************************************************************************	
	@Override
	@SuppressWarnings("unchecked")
	public List<P> findByProperties(String[] propertyName, Object[] value,boolean and,QueryComparsionType eqaulityType,
			int... rowStartIdxAndCount) {
		Class<?>clazz=getPersistentClass();
		
		try {
			StringBuffer queryString=new StringBuffer("select model from ").append(clazz.getSimpleName()).append(" model ");
			if(propertyName!=null&&propertyName.length>0){
				queryString.append(" where ");
				for(int i=0;i<propertyName.length;i++){
					if(i > 0)
						queryString.append(and?" and ":" or ");
					if(value[i]==null){
						queryString.append(" model.").append(propertyName[i]).append(" is null ");
					}else if(QueryComparsionType.in.equals(eqaulityType)){
						queryString.append(" model.").append(propertyName[i]).append(" in (");
						for(int j=0;j<value.length;j++){
							if(j>0){
								queryString.append(",");
							}
							queryString.append(value[j]==null?"null":value[j].toString());
						}
						queryString.append(")");
					}else{
						queryString.append(" model.").append(propertyName[i]).append(" ").append(eqaulityType.getComparisonSign()).append("  :propertyValue").append(i);
					}	
					
					}
				}
				//entityManager.createQuery("select model from CoUser model  where  model.coUserId  in   (1)").getResultList()
			Query query = entityManager.createQuery(queryString.toString());
			if((propertyName!=null&&propertyName.length>0)&&!QueryComparsionType.in.equals(eqaulityType)){
				for(int i=0;i<propertyName.length;i++){
					if(value[i]!=null)
						query.setParameter("propertyValue"+i,QueryComparsionType.like.equals(eqaulityType)?"%"+value[i]+"%":value[i]);
				}
			}
		
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
			logger.log(Level.SEVERE,"find by property name failed", re);
			throw re;
		}
		
	}
	
//************************************************************************************************************	
	public List<P> findByCreatedby(Long createdby,
			int... rowStartIdxAndCount) {
		return findByProperty(CREATEDBY, createdby, rowStartIdxAndCount);
	}
//************************************************************************************************************
	public List<P> findByUpdatedby(Long updatedby,
			int... rowStartIdxAndCount) {
		return findByProperty(UPDATEDBY, updatedby, rowStartIdxAndCount);
	}
//************************************************************************************************************	
	public List<P> findByCreated(Date created,
			int... rowStartIdxAndCount) {
		return findByProperty(CREATED, created, rowStartIdxAndCount);
	}
//************************************************************************************************************
	public List<P> findByUpdated(Date updated,
			int... rowStartIdxAndCount) {
		return findByProperty(UPDATED, updated,rowStartIdxAndCount);
	}
//************************************************************************************************************
	public List<P> findByIsactive(IsActive isactive,
			int... rowStartIdxAndCount) {
		return findByProperty(ISACTIVE, isactive,rowStartIdxAndCount);
	}
//************************************************************************************************************	
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
	
	public List<P> findAll(final int... rowStartIdxAndCount) {
		return findAllRecords(false, rowStartIdxAndCount);
	}
//************************************************************************************************************
	public List<P> findAll(Boolean activeRecords,final int... rowStartIdxAndCount) {
		return findAllRecords(activeRecords.booleanValue(), rowStartIdxAndCount);
	}
//************************************************************************************************************		
	public List<P> findAllActives(int... rowStartIdxAndCount) {
		return findAllRecords(true, rowStartIdxAndCount);
	}
//************************************************************************************************************	
	/**
	 * 
	 * @param handleActiveRecords
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<P> findAllRecords(boolean handleActiveRecords,int... rowStartIdxAndCount){
		Class<?>clazz=getPersistentClass();
		if(logger.isLoggable(Level.FINE)){
			logger.log(Level.INFO,"finding all "+clazz.getSimpleName()+" instances");
		}
		try {
			 StringBuffer queryString = new StringBuffer("select model from ").append(clazz.getSimpleName()).append(" model ");
			if(handleActiveRecords){
				queryString.append(" where model.isactive=:isactive ");
			}
			Query query = entityManager.createQuery(queryString.toString());
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
			if(handleActiveRecords){
				query.setParameter("isactive", Constants.IsActive.active);
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find all failed", re);
			throw re;
		}
	}
//************************************************************************************************************	
	/**
	 * 
	 * @param persistent
	 * @param update
	 */
	protected void initPersitentObject(UtopiaBasicPersistent persistent,boolean update)throws Exception{
		if(UtopiaPersistent.class.isInstance(persistent)){
			Map<String,Object>context= ContextHolder.getContext().getContextMap();
			BeanUtil.initPersistentObject(entityManager, (UtopiaPersistent)persistent, update, context);
		}
		
	}
//************************************************************************************************************
	public BeanProcessExcutionResult<P> validatePersistent(P ... persistents){
		return new BeanProcessExcutionResult<P>(persistents,null);
	}
//************************************************************************************************************	
@SuppressWarnings("unchecked")
@Override
	public List<Object[]> getReportResult(BeanReportModel model,int from,int to
			) {
		try {
			Query q= entityManager.createQuery(model.getQueryString());
			String []params=model.getQueryParameters();
			setQueryStartEndIndex(q, from,to-from);
			for(String param:params){
				q.setParameter(param, model.getParameterValue(param));
			}
			List<Object[]> result= q.getResultList();
			entityManager.flush();
			entityManager.clear();
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//************************************************************************************************************
	@Override
	public int getReportResultCount(BeanReportModel model
			) {
		try {
			Query q= entityManager.createQuery(model.getCountQuery());
			String []params=model.getQueryParameters();
			
			for(String param:params){
				q.setParameter(param, model.getParameterValue(param));
			}
			
			return ((Long)q.getSingleResult()).intValue();
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return -1;
	}
//************************************************************************************************************
	protected class SearchCondionWithIndex extends SearchConditionItem{
		/**
		 * 
		 */
		protected static final long serialVersionUID = 9190195099057094525L;
		protected int index;
		public SearchCondionWithIndex(SearchConditionItem item,int index){
			this(item.getEntityClass()==null?getSearchModelClass():item.getEntityClass()
					,item.getPropertyName(), item.getValue(),item.getEqualityType(),index);
			setAndWithPreviousCondition(item.isAndWithPreviousCondition());
		}
		public SearchCondionWithIndex(Class<?> entityClass,
				String propertyName, Object value,
				QueryComparsionType equalityType,int index) {
			super(entityClass, propertyName, value, equalityType);
			this.index=index;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getEntityName(){
			return getEntityClass().getSimpleName()+index;
		}
	}
//************************************************************************************************************
	
	
}
