package ir.utopia.core.bean;

import ir.utopia.core.persistent.PersistentTranslationHelper;

import java.io.Serializable;
import java.util.HashMap;

public class EntityPair implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1526533000814289734L;
	String propertyName;
	Class<?> entityClass;
	HashMap<String,Boolean> translationMap;
	public EntityPair( Class<?> entityClass,String propertyName) {
		super();
		this.propertyName = propertyName;
		this.entityClass = entityClass;
	}
	public Class<?> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public boolean isTranslated(String language){
		if(translationMap==null){
			translationMap=new HashMap<String, Boolean>();
		}
		if(!translationMap.containsKey(language)){
			translationMap.put(language, PersistentTranslationHelper.isTranslatedTable(entityClass, language));
		}
		return translationMap.get(language); 
	}
}
