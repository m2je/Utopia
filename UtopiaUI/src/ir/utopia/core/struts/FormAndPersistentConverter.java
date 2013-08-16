package ir.utopia.core.struts;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.persistent.UtopiaAttachmentInfo;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.DetailPersistentValueInfo;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.WebUtil;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;

public class FormAndPersistentConverter {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(FormAndPersistentConverter.class.getName());
	}
	private static final Class<?> []c=new Class<?>[]{};
	private static final Object []o=new Object[]{};
	private static final Cache<String,MethodAndParams>CLASS_METHOD_CACHE=new Cache<String, MethodAndParams>();

//********************************************************************************************************************	
	public static UtopiaBasicPersistent converFormToPersistent(UtopiaFormMetaData meta,Map<UtopiaFormMethodMetaData,Object>valueModel,File []attactments,String []fileNames,String []removedAttachments,Long recordId,String locale){
		UtopiaBasicPersistent result=converFormToPersistent(meta, valueModel, recordId, locale);
		ArrayList<UtopiaAttachmentInfo>infos=null;
		if(attactments!=null&&attactments.length>0){
			infos=new ArrayList<UtopiaAttachmentInfo>();
			int index=-1;
			for(File attachment: attactments){
				index++;
				if(attachment!=null&&attachment.exists()&&attachment.length()>0){
					infos.add(new UtopiaAttachmentInfo(attachment,fileNames[index]));
				}else{
					logger.log(Level.WARNING,"AttachFile is null or empty skipping...");
				}
			}
			}
			if(removedAttachments!=null&&removedAttachments.length>0){
				if(infos==null){
					infos=new ArrayList<UtopiaAttachmentInfo>();
				}
				SecurityProvider securityProvider= ServiceFactory.getSecurityProvider();
				for(String cur:removedAttachments){
					try {
						UtopiaAttachmentInfo info=new UtopiaAttachmentInfo();
						if(cur!=null){
							info.setAttachmentId(Long.parseLong(securityProvider.decrypt(cur)));
						}
						info.setRemoved(true);
						infos.add(info);
					} catch (Exception e) {
						logger.log(Level.WARNING,"",e);
						logger.log(Level.WARNING,"fail to set attachment recordId");
					}
				}
			}
			result.setAttachmentInfos(infos);
		
		return result;
	}
//********************************************************************************************************************	
	@SuppressWarnings("unchecked")
	public static UtopiaBasicPersistent converFormToPersistent(UtopiaFormMetaData meta,Map<UtopiaFormMethodMetaData,Object>valueModel,Long recordId,String locale){
		try {
			Class<? extends UtopiaBasicPersistent>clazz= meta.getPersistentClass();
			Class<? extends UtopiaBasicPersistent>trlClazz=BeanUtil.getTranslationEntity((Class<UtopiaBasicPersistent>)clazz);
			UtopiaPersistent result=(UtopiaPersistent)ConstructorUtils.invokeConstructor(clazz, o);
			String translationAttribute=null;
			if(trlClazz!=null){
				try {
					UtopiaBasicPersistent translationObject=(UtopiaBasicPersistent)ConstructorUtils.invokeConstructor(trlClazz, o);
					translationAttribute= BeanUtil.getTranslationObjectAttribute(trlClazz);
					MethodUtils.invokeMethod(result, AnnotationUtil.getSetterMethodName(translationAttribute), translationObject);
				} catch (Exception e) {
					logger.log(Level.WARNING,"fail to set object translation attribute",e);
				}
			}
			
			
			for(UtopiaFormMethodMetaData methodMeta:valueModel.keySet()){
				 if(methodMeta.isPersistentMethod()){
					 FormPersistentAttribute.FormToEntityMapType mapType=methodMeta.getMapType();
					 if(FormPersistentAttribute.FormToEntityMapType.real.equals(mapType)){
						 String methodName =methodMeta.getPersistentMethodName();
						 Object value=valueModel.get(methodMeta);
						 if(value==null)continue;
						 MethodAndParams method=findMethod(meta.getClazz(), methodMeta.getMethodName(), null);
						 methodName=AnnotationUtil.getSetterMethodName(methodName);
						 Object[] res=getPersistentParamClass(result, meta,methodMeta, methodName, value, method.returnType,locale);
						 if(res==null)continue;
						 Class<?> returnType=(Class<?>)res[0];
						 value=res[1];
						 invokeObjectMethod(result, methodName,translationAttribute, value,returnType,locale);
						 }
					 }
			}
			
			result.setRecordId(recordId);
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//*******************************************************************************************
	private static Object[] getPersistentParamClass(UtopiaPersistent persistentObject,UtopiaFormMetaData metaData,UtopiaFormMethodMetaData meta,String methodName
			,Object value,Class<?>returnType,String locale){
		
		DisplayTypes displayType=meta.getDisplayType();
		FormPersistentAttribute.FormToEntityDataTypeMap typeMap=meta.getFormToEntityMapType();
		if(FormPersistentAttribute.FormToEntityDataTypeMap.LONG_TO_LOOKUP.equals(typeMap)||
				  (displayType!=null&&displayType.equals(DisplayTypes.lookup))){
				 List<LookupInfo>lookupInfos=persistentObject.getLookupInfo()==null? new ArrayList<LookupInfo>():persistentObject.getLookupInfo();	
				 Class<? extends UtopiaPersistent> lookupClass=metaData.getLookupPersistentClass(meta);
				 if(meta.getPersistentMethodClass()!=null){
					 lookupInfos.add(new LookupInfo(AnnotationUtil.getPropertyName(meta.getPersistentMethodName()) ,lookupClass,value)); 
				 }else{
					 lookupInfos.add(new LookupInfo(lookupClass,value));
				 }
				 persistentObject.setLookupInfos(lookupInfos);
				 return null;  
		  }
		else if(FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE.equals(typeMap)||
				FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE_TIME.equals(typeMap)){
			returnType=Date.class;
			if(value instanceof String){
				
					 String strValue=value.toString().trim();
						if(DateUtil.useSolarDate(locale) ){
							value=strValue!=null&&strValue.trim().length()>0?DateUtil.solarToDate(strValue):null;
						}else{
							value=strValue!=null&&strValue.trim().length()>0?DateUtil.gerToDate(strValue):null;
						}
				}
		}else if(FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_TIME.equals(typeMap)){
			returnType=Date.class;
			if(value instanceof String){
				value=DateUtil.getTime(value.toString());
			}
		}
		else if(FormPersistentAttribute.FormToEntityDataTypeMap.ISACTIVE.equals(typeMap)){
			returnType=Constants.IsActive.class;
			if(value instanceof Boolean)
			value=((Boolean)value)?Constants.IsActive.active:Constants.IsActive.disActive;
		 }else if(value instanceof String){
				 value=WebUtil.formatStringNumbersToAnsi((String)value);
		 }else if(FormPersistentAttribute.FormToEntityDataTypeMap.BOOLEAN_TO_ENUM.equals(typeMap)){
			 returnType=Constants.BooleanType.class;
			 if(value instanceof Boolean)
			 value =((Boolean)value)? Constants.BooleanType.True:Constants.BooleanType.False;
		 }else if(Constants.DisplayTypes.upload.equals( displayType)){
			 if(File.class.isInstance(value)){
				 byte []b=readFile((File)value);
				 returnType=(b).getClass();
				 value=b;
			 }
			 
		 }
		 else if(meta.isIncludedLoadMethod()){
			 try {
				 Collection<DetailPersistentValueInfo>includedPersistents=
							new ArrayList<DetailPersistentValueInfo>();
						for(Object current: (Collection<?>) value){
							if(current instanceof UtopiaBasicForm<?>){
								((UtopiaBasicForm<?>) current).setLocale(locale);
								UtopiaBasicPersistent curentPersistent= ((UtopiaBasicForm<?>) current).convertToPersistent();
								includedPersistents.add(new DetailPersistentValueInfo(curentPersistent, ((UtopiaBasicForm<?>) current).isDeleted()));	
							}
						}
						persistentObject.setIncludedPersistentValue(meta.getPersistentFieldName(), includedPersistents);
						return null;
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
			 
			
			
		 }
		
		return new Object[]{returnType,value};
	}
//**************************************************************************************************
	public static class MethodAndParams{
		Method method;
		Class<?>[] parameterTypes;
		Class<?>returnType;
		Class<?> methodCollectionClass;
		MethodAndParams (Method method){
			this.method=method;
			if(method!=null){
				this.parameterTypes=method.getParameterTypes();
				this.returnType=method.getReturnType();
				if(parameterTypes!=null&&parameterTypes.length>0){
				if(Collection.class.isAssignableFrom(parameterTypes[0])){
					this.methodCollectionClass=(Class<?>)(((ParameterizedType)method.
								getGenericParameterTypes()[0])).getActualTypeArguments()[0];
					}
				}
			}
		}
	}

//**************************************************************************************************
		public static Method findMethodByName(Class<?>clazz,String methodName,Class<?> preferedClass){
			for(Method method:clazz.getMethods()){
				 if(method.getName().equals(methodName)){
					 if(preferedClass==null){
						 return  method;
					 }else{
						 Class<?>[]methodParamsClass= method.getParameterTypes();
						 if(methodParamsClass!=null&&methodParamsClass.length>0
								 &&Collection.class.isAssignableFrom(methodParamsClass[0])){
							 return method;
						 }
					 }
				}
				 }
			return null;
		}
//********************************************************************************************
		public static MethodAndParams findObjectMethod(Object o,String methodName,Class<?> methodParamClass)throws Exception{
			Class<?>clazz=o.getClass();
			return findMethod(clazz, methodName, methodParamClass);
	}
//**************************************************************************************************
		public static MethodAndParams findMethod(Class<?>clazz,String methodName,Class<?> methodParamClass)throws Exception{
			
			String key=clazz.getName()+"|"+methodName;
			if(!CLASS_METHOD_CACHE.containsKey(key)){//CLASS_METHOD_CACHE.clear()
				Method result=null;
				if(methodParamClass!=null){
					try {
						if(Collection.class.isAssignableFrom(methodParamClass)){
							result=findMethodByName(clazz, methodName,Collection.class);
						}else{
							result= clazz.getMethod(methodName,new Class<?>[]{methodParamClass} );
						}
					} catch (Exception e) {
						if(isPrimitive(methodParamClass)){
							try {
								result= clazz.getMethod(methodName,new Class<?>[]{togglePrimitive(methodParamClass)} );
							} catch (Exception e2) {
							}
						}
					}
				}
				else{
					result= findMethodByName(clazz, methodName,null);
				}
				if(result==null){
					logger.log(Level.WARNING,"fail to find method "+methodName+" for class "+o.getClass(), Level.WARNING);
				}
				CLASS_METHOD_CACHE.put(key,new MethodAndParams(result));
			}
			
			return CLASS_METHOD_CACHE.get(key);
	}
//*******************************************************************************************
		public static Object invokeObjectMethod(Object invokingObject,String methodName,Object inValue,Class<?>methodParamClass,String locale){
			return invokeObjectMethod(invokingObject, methodName, null, inValue, methodParamClass, locale);
		}
//*******************************************************************************************		
		public static Object invokeObjectMethod(Object invokingObject,String methodName,String translationAttribute,Object inValue,Class<?>methodParamClass,String locale){
			try {
				if(methodName.indexOf(".")>=0){
				 	Object[]linkResult= findLinkedObjectMethod(invokingObject, methodName);
				 	if(linkResult==null||linkResult[0]==null||linkResult[1]==null)
				 		return null;
				 	methodName=(String)linkResult[1];
				 	invokingObject=linkResult[0];
				}
				MethodAndParams method=findObjectMethod(invokingObject, methodName, methodParamClass);
				Object translationObject=null;
				if(translationAttribute!=null){
					try {
						translationObject=MethodUtils.invokeMethod(invokingObject, AnnotationUtil.getGetterMethodName(translationAttribute), o);
						method=findObjectMethod(translationObject, methodName, methodParamClass);
					} catch (Exception e) {
						logger.log(Level.WARNING, "fail to initiate translation object of "+invokingObject.getClass(),e);
					}
				}
				
				if(method.method!=null){
					if(inValue instanceof String){
						inValue=((String)inValue).trim();
					}
					Object [] value=inValue==null?o:new Object[]{inValue};
					Class<?>[]parametrs= method.parameterTypes;
					if(method.methodCollectionClass!=null&&Collection.class.isInstance(inValue))
								value=convertAsIncludedObject(parametrs[0],method.methodCollectionClass, inValue,locale);
					return method.method.invoke(translationObject==null?invokingObject:translationObject, value);
				}
			} catch (Exception e) {
				logger.log( Level.WARNING,"fail to invoke method "+methodName+" for class "+(invokingObject==null?"null":invokingObject.getClass()));
			}
			return null;
		}
//*****************************************************************************************************
		public static Object[] findLinkedObjectMethod(Object invokingObject,String methodName){
			String targetMethodName=methodName;
			Object linkObject=invokingObject;
			if(methodName.indexOf(".")>=0){
				try {
					String linkMethodName=methodName.substring(0,methodName.indexOf("."));
					MethodAndParams linkMethod=findObjectMethod(invokingObject, linkMethodName, null);
					if(linkMethod!=null&&linkMethod.method!=null){ 
						 linkObject=linkMethod.method.invoke(invokingObject,o);
						if(linkObject!=null){
							targetMethodName = methodName.substring(methodName.indexOf(".")+1);
							return findLinkedObjectMethod(linkObject, targetMethodName);
						}
					}
					
				} catch (Exception e) {
					logger.log(Level.WARNING,"",e);
				}
			}
			return new Object[]{invokingObject,targetMethodName};
		}
//*****************************************************************************************************		
		public static Object[] getFormToPersistentMethodParamClass(UtopiaFormMethodMetaData methodMeta,Object input,String locale){
			FormPersistentAttribute.FormToEntityDataTypeMap map=methodMeta.getFormToEntityMapType();
			if(FormPersistentAttribute.FormToEntityDataTypeMap.ISACTIVE.equals(map)){
				return new Object[]{IsActive.active.equals(input)?Boolean.valueOf(true).booleanValue():Boolean.valueOf(false).booleanValue(),boolean.class};
			}else if(FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE.equals(map)||
					FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE_TIME.equals(map) ){
				String result=null;
				if(input!=null){
					Date d= (Date)input;
					Locale l=new Locale(locale);
					if(FormPersistentAttribute.FormToEntityDataTypeMap.STRING_TO_DATE_TIME.equals(map)){
						result= DateUtil.getDateAndTimeString(d, l);
					}else{
						result= DateUtil.getDateString(d, l);
					}
					}
				return new Object[]{result,String.class};
			}else if(FormPersistentAttribute.FormToEntityDataTypeMap.BOOLEAN_TO_ENUM.equals(map)||(input instanceof Constants.BooleanType)){
				return new Object []{Constants.BooleanType.True.equals(input)?true:false ,Boolean.class};
			}
			else{
				 InputItem inp=(InputItem)methodMeta.getAnnotation(InputItem.class);
				  if(FormPersistentAttribute.FormToEntityDataTypeMap.LONG_TO_LOOKUP.equals(map)||
						  (inp!=null&&(inp.displayType().equals(DisplayTypes.lookup) ||inp.displayType().equals(DisplayTypes.LOV) ))
						  ){
					  if(input instanceof UtopiaBasicPersistent){
						 return new Object[]{((UtopiaBasicPersistent)input).getRecordId(),  long.class};
					  }else if(input instanceof Long){
						  return new Object[]{(Long)input,  long.class};
					  }
					  else{
						  return null;
					  }
				  }
			
				}
			return new Object[]{input,input!=null?input.getClass():null};
		}
//********************************************************************************************
		private static Object[] convertAsIncludedObject(Class<?> collectionClass, Class<?> methodCollectionClass ,Object inValue,String locale)throws Exception{
			
			if(UtopiaBasicForm.class.isAssignableFrom(methodCollectionClass)){
				Collection<UtopiaBasicForm<?>>includedForms=
					List.class.isAssignableFrom(collectionClass)?new ArrayList<UtopiaBasicForm<?>>():new HashSet<UtopiaBasicForm<?>>();
				for(Object current: (Collection<?>) inValue){
					if(current instanceof UtopiaBasicPersistent){
						UtopiaBasicForm<?>form=(UtopiaBasicForm<?>) methodCollectionClass.getConstructor(c).newInstance(o);
						form.setLocale(locale);
						form.importPersistent((UtopiaBasicPersistent)current);
						includedForms.add(form);
					}
				}
					return new Object[]{includedForms};
			}else if(UtopiaBasicPersistent.class.isAssignableFrom(methodCollectionClass)){
				return null;//we handle this values by DetailPersistentValueInfo
			}
			return new Object[]{inValue};
		}
//**************************************************************************************************
		private static boolean isPrimitive(Class<?> clazz){
			return clazz.isPrimitive()||
			Boolean.class.equals(clazz)||
			Integer.class.equals(clazz)||
			Long.class.equals(clazz)||
			Byte.class.equals(clazz)||
			Float.class.equals(clazz);
		}
//**************************************************************************************************
		private static Class<?> togglePrimitive(Class<?> clazz){
			if(Integer.class.equals(clazz)){
				return int.class;
			}
			else if(int.class.equals(clazz)){
				return Integer.class;
			}
			else if(Long.class.equals(clazz)){
				return long.class;
			}
			else if(long.class.equals(clazz)){
				return Long.class;
			}
			else if(Byte.class.equals(clazz)){
				return byte.class;
			}
			else if(byte.class.equals(clazz)){
				return Byte.class;
			}
			else if(Boolean.class.equals(clazz)){
				return boolean.class;
			}
			else if(boolean.class.equals(clazz)){
				return Boolean.class;
			}
			else if(Float.class.equals(clazz)){
				return float.class;
			}
			else if(float.class.equals(clazz)){
				return Float.class;
			}
			
			return clazz;
		}
//*********************************************************************************************************
		public static byte[] readFile(File file){
			try {
				byte []lob =new byte[(int)file.length()] ;
				FileInputStream fin=new FileInputStream(file);
				fin.read(lob);
				fin.close();
				return lob;
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.WARNING,"",e);
			}
			return null;
		}
}
