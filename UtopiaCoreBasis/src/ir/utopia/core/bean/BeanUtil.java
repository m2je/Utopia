package ir.utopia.core.bean;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.persistent.annotation.LookupJoin;
import ir.utopia.core.persistent.annotation.LookupJoinColumn;
import ir.utopia.core.persistent.annotation.TranslatedEntity;
import ir.utopia.core.persistent.lookup.model.JoinItem;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.security.exception.NonAuthorizedActionException;
import ir.utopia.core.util.Cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;

public class BeanUtil {
	private static final Object [] o=new Object[]{};
	private static Cache<String,Set<EntityPair>>MAPED_ITEMS_CACHE=new Cache<String, Set<EntityPair>>();
	private static Cache<String, String>CLASS_PRIMARY_KEY_CACHE=new Cache<String, String>();
	private static Cache<String,Boolean>CLASS_HAS_ATTRIBUTE_ATTRIBUTE=new Cache<String, Boolean>();
	private static Cache<Class<?  extends UtopiaBasicPersistent>,String[]>SELF_RELATION_CLASS_CACHE=new Cache<Class<?  extends UtopiaBasicPersistent>, String[]>();
	private static final Class<?>[] classes= new Class<?>[]{};
	private static Cache<String, String>PERSITENT_LINK_CACHE=new Cache<String, String>();
	private static Cache<String, Boolean>MANDATORY_COLUMN_CACHE=new Cache<String, Boolean>();
	private static Cache<Class<?  extends UtopiaBasicPersistent>, ClassAndProperty>ENTITY_TRANSLATION_CACHE=new Cache<Class<?  extends UtopiaBasicPersistent>, ClassAndProperty>();
	private static Cache<Class<?  extends UtopiaBasicPersistent>, Class<?  extends UtopiaBasicPersistent>>UTOPIA_SEARCH_ENTITY_CACHE=new Cache<Class<?  extends UtopiaBasicPersistent>, Class<?  extends UtopiaBasicPersistent>>();
	private static Cache<Class<? extends UtopiaBasicPersistent> ,LookupConfigurationModel>PERSISTENT_LOOKUP_CONFIGURATION_CACHE=new Cache<Class<? extends UtopiaBasicPersistent>, LookupConfigurationModel>();
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(BeanUtil.class.getName());
	}
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends UtopiaBean> findRemoteClassFromPersistent(Class<? extends UtopiaBasicPersistent> clazz ){
		try {
			if(clazz!=null){
				String pckage= clazz.getPackage().getName(); 
				String className=clazz.getSimpleName();
				pckage=pckage.substring(0,pckage.lastIndexOf("."));
				String remoteClassName=pckage+"."+"bean"+"."+className+Constants.REMOTE_CLASS_POSTFIX;
				Class<?>result= Class.forName(remoteClassName);
				if(UtopiaBean.class.isAssignableFrom(result)){
					return (Class<? extends UtopiaBean>)result;
				}
			}
		} catch (ClassNotFoundException e) {
			logger.log(Level.ALL,"fail to load remoteInterface for class "+clazz,e);
		}
		return null;
	}
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static UtopiaBean lookupEntityFacade(Class<? extends UtopiaBasicPersistent> clazz){
		 try {
			Class<? extends UtopiaBean> beanClass= (Class<? extends UtopiaBean>)
			 	BeanUtil.findRemoteClassFromPersistent(clazz);
			if(beanClass!=null){
				UtopiaBean bean=ServiceFactory.lookupFacade(beanClass);
				return bean;
			 }
			 
		} catch (Exception e) {
			logger.log(Level.ALL,"fail to load remoteInterface for class "+clazz,e);
		}
		return null;
	}
//**********************************************************************************************************************	
	/**
	 * 
	 * @param facade
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public static Class<? extends UtopiaBean> getRemoteClass(Class<? extends UtopiaBean> facade){
		if(facade.getAnnotation(Stateless.class)==null&&facade.getAnnotation(Stateful.class)==null){
			return null; 
		}
		Class<?>[]interfaces= facade.getInterfaces();
		for(Class<?> clazz:interfaces){
			if(clazz.getAnnotation(Remote.class)!=null&& UtopiaBean.class.isAssignableFrom(clazz) ){
				return (Class<? extends UtopiaBean>)clazz;
			}
		}
		return null;
	}
//**********************************************************************************************************************	
	/**
	 * 
	 * @param source
	 * @param cloneDetails
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static UtopiaPersistent clonePersistent(UtopiaPersistent source,boolean cloneDetails){
		try {
			UtopiaPersistent result= (UtopiaPersistent)ConstructorUtils.invokeConstructor(source.getClass(), o);
			BeanUtils.copyProperties(result, source);
			result.setCreated(null);
			result.setUpdated(null);
			result.setRecordId(0l);
			result.setCreatedby(null);
			result.setUpdatedby(0l);
			ArrayList<LookupInfo>lookupInfos=new ArrayList<LookupInfo>();
			if(source.getLookupInfo()!=null){
				for(LookupInfo info: source.getLookupInfo()){
					lookupInfos.add(info.clone());
				}
			}
			result.setLookupInfos(lookupInfos);
			
			Set<String>linkedFields= getLinkedFields(source.getClass(),OneToOne.class,OneToMany.class);
			for(String field:linkedFields){
				Object invokationResult= MethodUtils.invokeExactMethod(source, AnnotationUtil.getGetterMethodName(field), o);
				if(invokationResult instanceof UtopiaPersistent){
					lookupInfos.add(new LookupInfo(invokationResult.getClass(),((UtopiaPersistent)invokationResult).getRecordId()));
				}else if(cloneDetails&&invokationResult!=null&&
						Collection.class.isAssignableFrom(invokationResult.getClass())){
					Set<UtopiaPersistent>detailList=new HashSet<UtopiaPersistent>();
						for(Object o :(Collection)invokationResult){
							if(o instanceof UtopiaPersistent){
								detailList.add(clonePersistent((UtopiaPersistent)o,true));
							}
						}
						MethodUtils.invokeExactMethod(source, AnnotationUtil.getSetterMethodName(field), detailList);
				}
			}
			return result;
		} catch (Exception e) {
			logger.log(Level.ALL,"",e);
		}
		return null;
	}
//**********************************************************************************************************************
	@SuppressWarnings("unchecked")
	public static List<NamePair> loadLookup(LookupConfiguration confModel,Map<String,Object>context){
		if(confModel!=null&&(Class<? extends UtopiaPersistent>) confModel.persistentClass()!=null&&!Object.class.equals(confModel.getClass())){
			Class<? extends UtopiaPersistent> persistentClass= (Class<UtopiaPersistent>)confModel.persistentClass();
			LookupConfigurationModel model=BeanUtil.getLookupConfigurationModel(context,persistentClass,confModel);
			if(confModel.displayColumns()!=null&&confModel.displayColumns().length>0){
				model.setOwnDisplayColumns(confModel.displayColumns());
			}
			if(confModel.primaryKeyColumnName()!=null&&confModel.primaryKeyColumnName().trim().length()>0){
				model.setPrimaryKeyColumn(confModel.primaryKeyColumnName());
			}
			return loadLookup(model);
		}
		return null;
	}
//**********************************************************************************************************************
	public static List<NamePair> loadLookup(LookupConfigurationModel model){
		Class<? extends UtopiaBean> beanClass= BeanUtil.findRemoteClassFromPersistent(model.getOwnPersitentClass());
		if(LookUpLoaderBean.class.isAssignableFrom(beanClass)){
			try {
				LookUpLoaderBean bean=(LookUpLoaderBean)ServiceFactory.lookupFacade(beanClass);
			return	bean.loadLookup(model, null); 
			} catch (NamingException e) {
				logger.log(Level.WARNING, "",e);
			}
		}
		return null;
	}
//**********************************************************************************************************************	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static UtopiaPersistent clonePersistent(UtopiaPersistent source){
		return clonePersistent(source,false);
	}
//**********************************************************************************************************************
	/**
	 * 
	 * @param source
	 * @param cloneDatails
	 * @param count
	 * @return
	 */
	public static UtopiaPersistent[] clonePersistent(UtopiaPersistent source,boolean cloneDatails,int count){
		UtopiaPersistent[] result=new UtopiaPersistent[count];
		for(int i=0;i<count;i++){
			result[i]=clonePersistent(source,cloneDatails);
			if(result[i]==null)break;//case when exception occured
		}
		return result;
	}
//**********************************************************************************************************************	
	/**
	 * 
	 * @param source
	 * @param count
	 * @return
	 */
	public static UtopiaPersistent[] clonePersistent(UtopiaPersistent source,int count){
		return clonePersistent(source,false, count);
	}
	
//**********************************************************************************************************************	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getSelfParentLinkFieldName(Class<? extends UtopiaBasicPersistent>clazz){
		if(!SELF_RELATION_CLASS_CACHE.containsKey(clazz)){
			initClassRelationCache(clazz);
		}
			return SELF_RELATION_CLASS_CACHE.get(clazz)[1];
	}
//**********************************************************************************************************************	
		/**
		 * 
		 * @param clazz
		 * @return
		 */
		public static String getSelfChildrenLinkFieldName(Class<? extends UtopiaBasicPersistent>clazz){
			if(!SELF_RELATION_CLASS_CACHE.containsKey(clazz)){
				initClassRelationCache(clazz);
			}
				return SELF_RELATION_CLASS_CACHE.get(clazz)[0];
		}
	
//**********************************************************************************************************************
	@SuppressWarnings("unchecked")
	private static void initClassRelationCache(Class<? extends UtopiaBasicPersistent>clazz){
		String childField=null,parentField=null;
		try {
			Set<String> childfields=getLinkedFields(clazz,ManyToOne.class,OneToOne.class);
			childField = extractField(clazz, childfields);
			Set<String> parentfields=getLinkedFields(clazz,OneToMany.class);
			parentField= extractField(clazz, parentfields);
		} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
		}
		SELF_RELATION_CLASS_CACHE.put(clazz, new String[]{parentField,childField});
	}
//**********************************************************************************************************************	
	private static String extractField(
			Class<? extends UtopiaBasicPersistent> clazz, 
			Set<String> childfields) {
		if(childfields!=null){
			for(String field:childfields){
				String methodName= AnnotationUtil.getGetterMethodName(field);
				Class<?> refClass= AnnotationUtil.extractGenericClass(clazz, methodName);
				if(clazz.isAssignableFrom(refClass)){
					return field;
				}
			}
		}
		return null;
	}
//**********************************************************************************************************************
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<EntityPair>getLinkFieldWithClass(Class<? extends UtopiaBasicPersistent>clazz ){
		return getLinkedFieldsWithClass(clazz,OneToOne.class,ManyToMany.class,OneToMany.class,ManyToOne.class );
	}
//**********************************************************************************************************************
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getLinkedFields(Class<? extends UtopiaBasicPersistent>clazz ){
		return getLinkedFields(clazz,OneToOne.class,ManyToMany.class,OneToMany.class,ManyToOne.class );
	} 
//**********************************************************************************************************************

	/**
	 * 
	 * @param clazz
	 * @param annotaionTypes
	 * @return
	 */
	public static Set<String> getLinkedFields(Class<? extends UtopiaBasicPersistent>clazz,Class<? extends Annotation> ...annotaionTypes ){
		 Set<EntityPair>clzzProps= getLinkedFieldsWithClass(clazz, annotaionTypes);
		 HashSet<String>result=new HashSet<String>();
		 if(clzzProps!=null){
			 for(EntityPair prop:clzzProps){
				 result.add(prop.getPropertyName());
			 }
		 }
		 return result;
	}
//**********************************************************************************************************************
	public static Set<EntityPair> getLinkedFieldsWithClass(Class<? extends UtopiaBasicPersistent>clazz,Class<? extends Annotation> ...annotaionTypes ){
		String key=clazz.getName();
		for(Class<?> annot: annotaionTypes){
			key+="|"+annot.getName();
		}
		Set<EntityPair>linkedFields;
		if(!MAPED_ITEMS_CACHE.containsKey(key)){//MAPED_ITEMS_CACHE.clear()
				linkedFields=new HashSet<EntityPair>();
				for(Method method: clazz.getMethods()){
					boolean linked=AnnotationUtil.hasAnnotaions(method, annotaionTypes);
					
					if(linked){
						Class<?>returnType= method.getReturnType();
						String property;
						String getterMethodName=method.getName();
						if(returnType!=null){//getter method
							property= AnnotationUtil.getPropertyName(method.getName());
						}else{//setter method
							property= AnnotationUtil.getPropertyNameFromSetterMethod(method.getName());
							getterMethodName= AnnotationUtil.getGetterMethodName(property); 
						}
							Class<?> relatedFieldClass=Collection.class.isAssignableFrom(returnType)? AnnotationUtil.extractGenericClass(clazz, getterMethodName):returnType;
							linkedFields.add(new EntityPair(relatedFieldClass, property) );
						}
					
					}
				
				for(Field field:clazz.getFields()){
					boolean linked=AnnotationUtil.hasAnnotaions(field, annotaionTypes);
					if(linked){
						String getterMethodName=AnnotationUtil.getGetterMethodName(field.getName());
						Class<?> relatedFieldClass=Collection.class.isInstance(field.getClass())? AnnotationUtil.extractGenericClass(field.getClass(), getterMethodName):field.getClass();
						linkedFields.add(new EntityPair(relatedFieldClass, field.getName()));
					}
				}
				MAPED_ITEMS_CACHE.put(key, linkedFields);
		}else{
			linkedFields=MAPED_ITEMS_CACHE.get(key);
		}
		return linkedFields;
	}
//**********************************************************************************************************************	
	/**
	 * 
	 * @param context
	 * @param persitentClass
	 * @return
	 */
	public static LookupConfigurationModel getLookupConfigurationModel(Map<String,Object> context,Class<? extends UtopiaBasicPersistent> persitentClass){
		if(!PERSISTENT_LOOKUP_CONFIGURATION_CACHE.containsKey(persitentClass)){//PERSISTENT_LOOKUP_CONFIGURATION_CACHE.clear();
			PERSISTENT_LOOKUP_CONFIGURATION_CACHE.put(persitentClass, getLookupConfigurationModel(context, persitentClass, null));
		}
		return PERSISTENT_LOOKUP_CONFIGURATION_CACHE.get(persitentClass); 
	}
//**********************************************************************************************************	
	/**
	 * 	
	 * @param context
	 * @param persitentClass
	 * @param conf
	 * @return
	 */
	public static LookupConfigurationModel getLookupConfigurationModel(Map<String,Object> context,Class<? extends UtopiaBasicPersistent> persitentClass,LookupConfiguration conf){
		LookupConfigurationModel result=new LookupConfigurationModel(persitentClass);
		LookupJoinColumn joinConf=persitentClass.getAnnotation(LookupJoinColumn.class);
		if(conf!=null){
			initwithLookupCofiguration(conf, result,context,persitentClass);
		}else if(joinConf!=null){
			initWithJoinConfiguration(joinConf, result,context,persitentClass);
		}else {
			 conf=persitentClass.getAnnotation(LookupConfiguration.class);
			if(conf!=null)return getLookupConfigurationModel(context, persitentClass, conf);
			if(hasNameAttribute(persitentClass)){
				result.setPrimaryKeyColumn(persitentClass, getPrimaryKeyColumn(persitentClass));
				result.setOwnDisplayColumns("name");
				if(hasDescriptionAttribute(persitentClass)){
					result.setDescriptionColumn("description");
				}
			}else{
				logger.log(Level.WARNING," entitity "+persitentClass+" does not have name column!! you have to specify the name column for this enitity ");
				return null;
			}
		}
		return result;		
	}

//**********************************************************************************************************
	/**
	 * 
	 * @param joinConf
	 * @param result
	 * @param context
	 * @param persitentClass
	 */
	private static void initWithJoinConfiguration(LookupJoinColumn joinConf,LookupConfigurationModel result,Map<String,Object> context,Class<? extends UtopiaBasicPersistent> persitentClass){
		LookupJoin[] joins= joinConf.joins();
		if(joins!=null){
			for(LookupJoin join:joins){
				Class<? extends UtopiaBasicPersistent>joinToClass= join.joinToClass();
				joinToClass=joinToClass==null?persitentClass:joinToClass;
				LookupConfiguration conf= join.lookupConfiguration();
				Class<? extends UtopiaBasicPersistent>primaryKeyClass= joinConf.primaryKeyClass();
				result.setPrimaryKeyColumn(primaryKeyClass,getPrimaryKeyColumn(primaryKeyClass));
				ArrayList<String>dispCols=new ArrayList<String>();
				result.addCondition(join.condition(), context);
				for(String col:conf.displayColumns()){
					dispCols.add(col);
				}
				JoinItem item=new JoinItem(dispCols,joinToClass,persitentClass);
				result.join(item);
			}
			result.addCondition(joinConf.condition(), context);
		}
	}
//**********************************************************************************************************	
	/**
	 * 
	 * @param conf
	 * @param result
	 * @param persitentClass
	 */
	@SuppressWarnings("unchecked")
	private static void initwithLookupCofiguration(LookupConfiguration conf,LookupConfigurationModel result,Map<String,Object> context,Class<? extends UtopiaBasicPersistent> persitentClass){
		Class<? extends UtopiaBasicPersistent> currentPersistent=UtopiaBasicPersistent.class.isInstance(conf.persistentClass()) ?
				(Class<? extends UtopiaBasicPersistent>)conf.persistentClass():
				(Class<? extends UtopiaBasicPersistent>)persitentClass;
			
				if(conf.primaryKeyColumnName()!=null&&conf.primaryKeyColumnName().trim().length()>0){
					result.setPrimaryKeyColumn(currentPersistent, conf.primaryKeyColumnName());
					}else{
						result.setPrimaryKeyColumn(currentPersistent,AnnotationUtil.getPrimaryKeyName(currentPersistent));
					}
			if(conf.condition()!=null&&conf.condition().trim().length()>0){
				result.addCondition(conf.condition(), context);
			}
			if(conf.descriptionColumnName()!=null&&conf.descriptionColumnName().trim().length()>0){
				result.setDescriptionColumn(conf.descriptionColumnName());
			}
			result.setSeperator(conf.displayItemSeperator());
			result.setOwnDisplayColumns(conf.displayColumns());
			if(conf.orderby()!=null){
				result.setOrderby(conf.orderby());
			}
		
	}
//**********************************************************************************************************
	public static String getPrimaryKeyColumn(Class<?> clazz){
		String key=clazz.getName();
		if(!CLASS_PRIMARY_KEY_CACHE.containsKey(key)){
			if(!searchMethods(clazz, key)){
				searchFields(clazz, key);
			}
		}
		return CLASS_PRIMARY_KEY_CACHE.get(key);
	}
//**********************************************************************************************************
		private static boolean searchMethods(Class<?> clazz,String key){
			Method []methods =clazz.getMethods();
			for(Method method:methods ){
				if(method.getAnnotation(Id.class)!=null){
					CLASS_PRIMARY_KEY_CACHE.put(key,AnnotationUtil.getPropertyName(method.getName()));
					return true;
				}
			}
			return false;
		} 
//**********************************************************************************************************
		private static boolean searchFields(Class<?> clazz,String key){
			Field []fields =clazz.getFields();
			for(Field field:fields){
				if(field.getAnnotation(Id.class)!=null){
					CLASS_PRIMARY_KEY_CACHE.put(key,AnnotationUtil.getPropertyName(field.getName()));
					return true;
				}
			}
			return false;
		} 
//**********************************************************************************************************
	private static boolean hasNameAttribute(Class<?> persitentClass){
		return classHasAttribute(persitentClass, "getName");
	}
//**********************************************************************************************************
	private static boolean hasDescriptionAttribute(Class<?> persitentClass){
		return classHasAttribute(persitentClass, "getDescription");
	}
//**********************************************************************************************************
	private static boolean classHasAttribute(Class<?> persitentClass,String getterName){
		String key=persitentClass.getName()+"|"+getterName;
		Method method=null;
		if(!CLASS_HAS_ATTRIBUTE_ATTRIBUTE.containsKey(key)){
			try {
				method=persitentClass.getMethod(getterName, classes);
			} catch (Exception e) {
			} 
			CLASS_HAS_ATTRIBUTE_ATTRIBUTE.put(key, method!=null);
		}
		return CLASS_HAS_ATTRIBUTE_ATTRIBUTE.get(key);
	}	
//**********************************************************************************************************
	public static void initPersistentObject(UtopiaPersistent persistent,boolean update, Map<String,Object>context)throws Exception{
		initPersistentObject(null, persistent, update, context);
	}
//**********************************************************************************************************
	public static void initPersistentObject(EntityManager entityManager, UtopiaPersistent persistent,boolean update, Map<String,Object>context)throws Exception{
		if(persistent==null)return ;
		Long userId=ContextUtil.getUserId(context);
		if(userId==null||userId<=0){
			throw new NonAuthorizedActionException("acting subject cannot be null ");
		}
		
		Date currentDate=new Date(System.currentTimeMillis());
		if(!update){
			persistent.setCreated(currentDate);
			persistent.setCreatedby(userId);
		}else{
			if(entityManager!=null){
				Date created=persistent.getCreated();
				Long createdBy=persistent.getCreatedby();
				if((created==null||createdBy==null||createdBy.intValue()<=0)&&persistent.getRecordId()>0){
					String persitentName=persistent.getClass().getSimpleName();
					String primaryField= AnnotationUtil.getPrimaryKeyName(persistent.getClass());
					Query q=entityManager.createQuery("select model.created,model.createdby from "+
							persitentName+" model where model."+primaryField+"=:recordId" );
					q.setParameter("recordId", persistent.getRecordId());
					Object[]result=(Object[]) q.getSingleResult();
					persistent.setCreated((Date)result[0]);
					persistent.setCreatedby((Long)result[1]);
					}
			}
		}
		persistent.setUpdated(currentDate);
		persistent.setUpdatedby(userId);
	} 
//**********************************************************************************************************	
	/**
	 * assign lookup values to persistentObject 
	 * @param persitentObject
	 */
public static void initLookupInfos(EntityManager entityManager, UtopiaBasicPersistent persitentObject)   {
	try {
		List<LookupInfo>infos= persitentObject.getLookupInfo();
		if(infos!=null&&infos.size()>0){
			for(LookupInfo info: infos){
				Object value=info.getLookupValue();
				String methodName=AnnotationUtil.getSetterMethodOfField(info.getColumnName());
				if(value!=null){
					if(UtopiaPersistent.class.isAssignableFrom(info.getLookupClass())){
						value=entityManager.find(info.getLookupClass(), value);
					}else{
						value=getCollectionLookupInfos(persitentObject,info);
					}
				}
				persitentObject.getClass().getMethod(methodName,info.getLookupClass() ).
				 invoke(persitentObject, value); 
			}
		}
	} catch (Exception e) {
		logger.log(Level.WARNING,"fail to init lookup object ",  e);
	} 
}
//*****************************************************************************
private static Object getCollectionLookupInfos(UtopiaBasicPersistent persitentObject,LookupInfo info){
	Method getter=MethodUtils.getAccessibleMethod(persitentObject.getClass(), AnnotationUtil.getGetterMethodName(info.getColumnName()), o.getClass()) ;
	Class<?>methodReturnType= getter.getReturnType();
	Object value= info.getLookupValue();
	Collection<Object> resultValue=null;
	if(Set.class.isAssignableFrom(methodReturnType)){
		resultValue=new HashSet<Object>();
	}else if(List.class.isAssignableFrom(methodReturnType)){
		resultValue=new ArrayList<Object>();
	}
	if(Collection.class.isInstance(value)){
		for(Object obj:(Collection<?>)value){
			resultValue.add(obj);
		}
	}else if(value.getClass().isArray()){
		for(Object obj:(Object[])value){
			resultValue.add(obj);
		}
	}
	
	return resultValue;
}
//*****************************************************************************
@SuppressWarnings("unchecked")
public static String getPersistentLinkField(Class<? extends UtopiaBasicPersistent>source,Class<? extends UtopiaBasicPersistent>destination,String propertyName){
		String key=source.getSimpleName()+"|"+destination.getSimpleName()+(propertyName==null?"":"|"+propertyName);
		if(PERSITENT_LINK_CACHE.containsKey(key)){
			return PERSITENT_LINK_CACHE.get(key);
		}
	   Set<String>simple= getLinkedFields(source, OneToOne.class,OneToMany.class);
	   String []c1=getPersistentLinkField(source, destination, propertyName, simple);
	   if(c1[0]!=null){
		   PERSITENT_LINK_CACHE.put(key, c1[0]);
		   return c1[0];
	   }
	   
	   Set<String>multiple=getLinkedFields(source, ManyToOne.class,ManyToMany.class);
	   String []c2=getPersistentLinkField(source, destination, propertyName, multiple);
	   if(c2[0]!=null){
		   PERSITENT_LINK_CACHE.put(key, c2[0]);
		   return c2[0];
	   }
	   if(c1[1]!=null){
		   PERSITENT_LINK_CACHE.put(key, c1[1]);
		   return c1[1];
	   }
	   if(c2[1]!=null){
		   PERSITENT_LINK_CACHE.put(key, c2[1]);
		   return c2[1];
	   }
	   PERSITENT_LINK_CACHE.put(key, null);
	return PERSITENT_LINK_CACHE.get(key);
}
//*********************************************************************************
private static String[]  getPersistentLinkField(Class<? extends UtopiaBasicPersistent>source,
		Class<? extends UtopiaBasicPersistent>destination,String propertyName,
		Set<String>fields){
	String condidate=null; 
	if(fields!=null){
		   for(String f:fields){
			    Method method=MethodUtils.getAccessibleMethod(source, AnnotationUtil.getGetterMethodName(f), classes) ;
			    if(method!=null){
			    	Class<?>clazz= method.getReturnType();
			    	if(Collection.class.isAssignableFrom(clazz)){
			    		clazz=(Class<?>)(((ParameterizedType)method.
			    				getGenericReturnType())).getActualTypeArguments()[0];
			    	}
			    	if(clazz.equals(destination)){
			    		condidate=f;
			    		if(propertyName==null||propertyName.equals(f)){
			    			return new String[]{f,condidate};
			    		}
			    	}
			    }
		   }
	   }	
	return new String[]{null,condidate};
}
//*********************************************************************************
/**
 * checks even column is nullable or not
 * @param clazz
 * @param column
 * @return
 */
public static boolean isNullable(Class<? extends UtopiaBasicPersistent>clazz,String column){
	if(clazz==null||column==null||column.trim().length()==0){
		throw new IllegalArgumentException("invalid class or column for class-->"+clazz+" and column "+column);
	}
	String key=clazz.getName()+"|"+column;
	if(!MANDATORY_COLUMN_CACHE.containsKey(key)){
		boolean nullable=true;
		try {
			Field filed= clazz.getField(column);
			nullable &=isNullable(filed.getAnnotations());
		} catch (Exception e) {
			logger.log(Level.WARNING,"unable to find column--> "+column+" in class-->"+clazz,e);
		}
		try {
			Method method= MethodUtils.getAccessibleMethod(clazz, AnnotationUtil.getGetterMethodName(column), classes);
			nullable &=isNullable(method.getAnnotations());
		} catch (Exception e) {
			logger.log(Level.WARNING,"unable to find getter method for column--> "+column+" in class-->"+clazz, e);
		}
	}
	return MANDATORY_COLUMN_CACHE.get(key);
} 
//*********************************************************************************
/**
 * check annotation of column or method 
 * @param annotations
 * @return
 */
private static boolean isNullable(Annotation []annotations){
	if(annotations==null||annotations.length==0){
		return false;
	}
	boolean nullable=true;
	for(Annotation annot: annotations){
		if(Column.class.isInstance(annot)){
			nullable&=((Column)annot).nullable();
		}else if(JoinColumn.class.isInstance(annot)){
			nullable&=((JoinColumn)annot).nullable();
		} 
	}
	return nullable;
}
//*********************************************************************************
public static boolean isTranslatedEntity(Class<? extends UtopiaBasicPersistent>entityClass){
	return getTranslationEntity(entityClass)!=null;
}
//*********************************************************************************
public static Class<? extends UtopiaBasicPersistent> getTranslationEntity(Class<? extends UtopiaBasicPersistent>entityClass){
	initTranslationCache(entityClass);
	ClassAndProperty res=ENTITY_TRANSLATION_CACHE.get(entityClass);
	return res==null?null:res.clazz;
}
//*********************************************************************************
@SuppressWarnings("unchecked")
private static void initTranslationCache(
		Class<? extends UtopiaBasicPersistent> entityClass) {
	if(!ENTITY_TRANSLATION_CACHE.containsKey(entityClass)){//ENTITY_TRANSLATION_CACHE.clear()
		TranslatedEntity annotation=entityClass.getAnnotation(TranslatedEntity.class);
		Class<? extends UtopiaBasicPersistent>translationEntity=null;
		if(annotation!=null){
			translationEntity=annotation.translationEntity();
			
			if(translationEntity==null||UtopiaBasicPersistent.class.equals(translationEntity)){
				try{
					translationEntity=(Class<? extends UtopiaBasicPersistent>)Class.forName(entityClass.getName()+"Lang");
				}catch (Exception e) {
					logger.log(Level.WARNING,"", e);
				}
			}else{
				translationEntity=annotation.translationEntity();
			}
		}
		ClassAndProperty classAndMethod=new ClassAndProperty(translationEntity, getTranslationAttribute(entityClass, translationEntity));
		ENTITY_TRANSLATION_CACHE.put(entityClass, classAndMethod);
	}
} 
//*********************************************************************************
public static String getTranslationObjectAttribute(Class<? extends UtopiaBasicPersistent>entityClass){
	 initTranslationCache(entityClass);
	 ClassAndProperty res=ENTITY_TRANSLATION_CACHE.get(entityClass);
	 return res==null?null:res.attributeName;
}
//*********************************************************************************
private static String getTranslationAttribute(Class<? extends UtopiaBasicPersistent>clazz,Class<?>translationClass){
	Set<String>connectedFields= BeanUtil.getLinkedFields(clazz);
	if(connectedFields!=null){
		for(String field:connectedFields){
			try{
			  if(translationClass.equals( clazz.getDeclaredField(field).getClass())){
				 return field;
			  }
			  
			}catch(Exception e){
				if(logger.isLoggable(Level.FINEST))
					logger.log(Level.WARNING,"", e);
			}
		}
		
	}
	return null;
}
//*********************************************************************************
@SuppressWarnings("unchecked")
public static Class<? extends UtopiaBasicPersistent>  getSearchEntityClass(Class<? extends UtopiaBasicPersistent>p){
	if(!UTOPIA_SEARCH_ENTITY_CACHE.containsKey(p)){
		Class<? extends UtopiaBasicPersistent> res=null;
		try {
			res= ((UtopiaBasicUsecaseBean<?,?>)ServiceFactory.lookupFacade( BeanUtil.findRemoteClassFromPersistent(p))).getSearchModelClass();
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		if(res==null){
			String pclass=p.getSimpleName();
			try {
				res=(Class<? extends UtopiaBasicPersistent>)Class.forName(pclass.substring(0, 2)+"V"+pclass.substring( 2));
			} catch (ClassNotFoundException e) {
				logger.log(Level.WARNING,"", e);
			}
		}
		UTOPIA_SEARCH_ENTITY_CACHE.put(p, res);
	}
	
	return UTOPIA_SEARCH_ENTITY_CACHE.get(p);
}
//*********************************************************************************
public static Object getFieldValue(Object p,String fieldName){
		try {
			Field field=null;
			try {
				field = p.getClass().getField(fieldName);
			} catch (Exception e) {
			}

			String method= AnnotationUtil.getGetterMethodName(fieldName,field!=null?field.getClass():null);
			return MethodUtils.invokeExactMethod(p, method, o);
		} catch (Exception e) {
			if(logger.isLoggable(Level.FINE)){
				logger.log(Level.FINE,"fail to invoke method to get value of :"+fieldName+" of object "+p,e);
				e.printStackTrace();
			}
			
		}
		
	return null;
	
}
//*********************************************************************************
public static class ClassAndProperty{
	String attributeName;
	Class<? extends UtopiaBasicPersistent>clazz;
	
	public ClassAndProperty(Class<? extends UtopiaBasicPersistent>clazz,String attributeName) {
		this.attributeName=attributeName;
		this.clazz=clazz;
	}
	
}
}
