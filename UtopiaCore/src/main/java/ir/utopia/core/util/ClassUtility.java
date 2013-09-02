package ir.utopia.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassUtility {
	private static final Logger logger=Logger.getLogger(ClassUtility.class.getName());
	private static final Class<?>[] C=new Class[0];
	private static final Object[] O=new Object[0];
	
	public static BeanMethodInfo[] getIncludedDomains(Class<?> domainClass){
		return getIncludedDomains(domainClass, false, false);
	}
	public static BeanMethodInfo[] getIncludedDomains(Class<?> domainClass,boolean persistentTransients,boolean xmlTransient){
		return getMethodInfos(domainClass, persistentTransients, xmlTransient, Object.class);
	}
	public static BeanMethodInfo[] getMethodInfos(Class<?> domainClass, boolean persistentTransients,boolean xmlTransient,Class<?>baseClass){
		return getBeanInfo(domainClass, persistentTransients, xmlTransient, baseClass).getMethodInfos();
	}
	public static BeanInfo getBeanInfo(Class<?> domainClass, boolean persistentTransients,boolean xmlTransient,Class<?>baseClass){
		BeanInfo result=new BeanInfo();
		Class<?>currentClass=domainClass;
		List<BeanMethodInfo>methodsInfo=new ArrayList<BeanMethodInfo>();
		Set<String>checkedMethods=new HashSet<String>();
		java.lang.reflect.Type genericType= domainClass.getGenericSuperclass();
		boolean isGenericClass= ParameterizedType.class.isInstance(genericType) && ( ((ParameterizedType)genericType).getActualTypeArguments().length>0 );
		while(true){
			Method[] methods= currentClass.getMethods();
			if(methods!=null&&methods.length>0){
				for(Method method: methods){
					if(currentClass.equals(method.getDeclaringClass())&&!checkedMethods.contains(method.getName())){
						String methodName=method.getName();
						String getterMethodName=null;
						String setterMethodName=null;
						String fieldName=null;
						String JSONName=null;
						if(!(void.class.equals(method.getReturnType()))){
							if(methodName.startsWith("get")){
								getterMethodName=methodName;
								setterMethodName="s"+methodName.substring(1);
								fieldName=getPropertyName(getterMethodName);
								
							}else if(methodName.startsWith("is")){
								getterMethodName=methodName;
								setterMethodName="set"+methodName.substring(2);
								fieldName=getPropertyName(getterMethodName);
							}
						}else if(methodName.startsWith("set")){
							setterMethodName=methodName;
							getterMethodName="g"+methodName.substring(1);
							fieldName=getPropertyName(getterMethodName);
						}
						if(getterMethodName!=null&&setterMethodName!=null&&fieldName!=null){
							checkedMethods.add(getterMethodName);
							checkedMethods.add(setterMethodName);
							Method getter=null,setter=null;
							try {
								getter=currentClass.getMethod(getterMethodName, C);
							} catch (Exception e) {
								if(logger.isLoggable(Level.FINEST)){
									logger.log(Level.FINEST,"fail to check setter method for field ["+fieldName+"] -->"+e.toString());
									}
							}
							if(getter!=null&&!isTransient(getter,persistentTransients,xmlTransient)&&
									(baseClass==null||baseClass.isAssignableFrom(getter.getReturnType()))){
								try {
									setter=currentClass.getMethod(setterMethodName, getter.getReturnType());
								} catch (Exception e) {
									
									if(logger.isLoggable(Level.FINEST)){
										logger.log(Level.FINEST,"fail to check setter method for field ["+fieldName+"] --> "+e.toString());
										}
								}
									
									JSONName=fieldName;
									JsonProperty jsonProperty=getter.getAnnotation(JsonProperty.class);
									if(jsonProperty!=null&&jsonProperty.value()!=null&&jsonProperty.value().trim().length()>0){
										JSONName=jsonProperty.value();
									}
									Class<?>clazz=getter.getReturnType();
									if(getter.getGenericReturnType()!=null &&isGenericClass &&TypeVariable.class.isInstance(getter.getGenericReturnType())){
										clazz=(Class<?>)((ParameterizedType)genericType).getActualTypeArguments()[0];
									}
									methodsInfo.add(new BeanMethodInfo(clazz,fieldName, getter, setter,JSONName));
							}
							
						}else{
							methodsInfo.add(new BeanMethodInfo(method));
						}
					}
				}
			}
			if(Object.class.equals(currentClass))break;
			currentClass=currentClass.getSuperclass();
		}
		result.setMethodInfos(methodsInfo.toArray(new BeanMethodInfo[methodsInfo.size()]));
		return result;
	}
//*****************************************************************************
	private static boolean isTransient(Method getter,boolean persistentTransient,boolean xmlTransient){
		if(persistentTransient){
			Transient transient1= getter.getAnnotation(Transient.class);
			if(transient1!=null){
				return true;
			}
			XmlTransient t2=getter.getAnnotation(XmlTransient.class);
			if(t2!=null)
				return true;
			
		}else if(xmlTransient){
			XmlTransient t2=getter.getAnnotation(XmlTransient.class);
			if(t2!=null)
				return true;
			
		}
		
		return false;
	}
//*****************************************************************************
		public static  String getPropertyName(String getterMethodName){
			String result;
			int index;
			if(getterMethodName.startsWith("get")){
				index=4;
			 }else if(getterMethodName.startsWith("is")){
				 index=3;
			 }else{
				 if(logger.isLoggable(Level.FINEST))
					 logger.log(Level.FINEST,"invalid getter method name"+getterMethodName);
				 return null;
			 }
			result =getterMethodName.substring(index);
			char c= Character.toLowerCase(getterMethodName.charAt(index-1));
			return  c+result ;
		}
//**********************************************************************************
	public static class BeanMethodInfo{
		private String fieldName;
		private String JSONName;
		private Method getter;
		private Method setter;
		private Class<?>clazz;
		private Method method;
		public BeanMethodInfo(Class<?>clazz, String fieldName, Method getter, Method setter,String JSONName) {
			super();
			this.clazz=clazz;
			this.fieldName = fieldName;
			this.getter = getter;
			this.setter = setter;
			this.JSONName=JSONName;
		}
		public BeanMethodInfo(Method method) {
			this.method=method;
		}
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public Method getGetter() {
			return getter;
		}
		public void setGetter(Method getter) {
			this.getter = getter;
		}
		public Method getSetter() {
			return setter;
		}
		public void setSetter(Method setter) {
			this.setter = setter;
		}
		public Class<?> getClazz() {
			return clazz;
		}
		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}
		public String getJSONName() {
			return JSONName;
		}
		public void setJSONName(String jSONName) {
			JSONName = jSONName;
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		
		
	}
//**********************************************************************************
	public static class BeanInfo{
		BeanMethodInfo [] methodInfos;
		
		public BeanMethodInfo[] getMethodInfos() {
			return methodInfos;
		}
		public void setMethodInfos(BeanMethodInfo[] methodInfos) {
			this.methodInfos = methodInfos;
		}
		
	}
}
