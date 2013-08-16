package ir.utopia.core.utilities.serialization;

import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Set;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class DefaultUtopiaSerializer implements UtopiaSerializer {
	private static final Logger logger=Logger.getLogger(DefaultUtopiaSerializer.class.getName());
	private static final Object[] O=new Object[0]; 
	@Override
	public String serializedToJSON(UtopiaBasicPersistent persistent) {
		if(persistent!=null){
			try {
				ObjectMapper mapper = new ObjectMapper();
				AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
				mapper.setAnnotationIntrospector(introspector);
				UtopiaBasicPersistent cloned=preparePersistentItemForSerialization(persistent,0);
				return mapper.writeValueAsString(cloned);
			} catch (Exception e) {
				logger.log(Level.WARNING,"fail to serialize persistent Object ",e);
			}
		}
		
		return null;
	}
	
	protected UtopiaBasicPersistent preparePersistentItemForSerialization(UtopiaBasicPersistent source,int level){
		UtopiaBasicPersistent cloned=null;
		try {
			Class<?>clazz= source.getClass();
			cloned=(UtopiaBasicPersistent)clazz.newInstance();
			Method []methods= clazz.getMethods();
			for(Method method:methods){
				String getterMethodName=null,setterMethodName=null;
				if(method.getName().startsWith("get")&&UtopiaBasicPersistent.class.isAssignableFrom( method.getDeclaringClass())){
					getterMethodName=method.getName();
					setterMethodName="set"+method.getName().substring(3);
				}else if (method.getName().startsWith("is")&&UtopiaBasicPersistent.class.isAssignableFrom( method.getDeclaringClass())){
					getterMethodName=method.getName();
					setterMethodName="set"+method.getName().substring(2);
				}
				if(getterMethodName!=null){
					Class<?>C= method.getReturnType();
					Object invokationResult=null;
					try {
						invokationResult = method.invoke(source, O);
					} catch (Exception e1) {
						if(logger.isLoggable(Level.FINEST)){
							logger.log(Level.FINEST,"invokation of getter method failed",e1);
						}
					}
					if(invokationResult!=null){
						if((Collection.class.isAssignableFrom(C)&&C.getGenericInterfaces()!=null&&UtopiaBasicPersistent.class.isAssignableFrom(C.getGenericInterfaces()[0].getClass()))
								||Collection.class.isAssignableFrom(invokationResult.getClass())
								||UtopiaBasicPersistent.class.isAssignableFrom(C)){
							if(level==0){
								try {
										if(Collection.class.isAssignableFrom(invokationResult.getClass())){
											Collection<UtopiaBasicPersistent>newCollection;
											if(Set.class.isAssignableFrom(C)){
												newCollection=new HashSet<UtopiaBasicPersistent>();
											}else /*if(List.class.isInstance(invokationResult))*/{
												newCollection=new ArrayList<UtopiaBasicPersistent>();
											}
											try {
												for(Object o :(Collection<?>)invokationResult){//check for lazy loaded Objects
													if(UtopiaBasicPersistent.class.isInstance(o) ){
														UtopiaBasicPersistent p= preparePersistentItemForSerialization((UtopiaBasicPersistent)o, level+1);
														newCollection.add(p);
													}
												}
											} catch (Exception e) {
												if(logger.isLoggable(Level.FINEST)){
													logger.log(Level.FINEST,"invokation of Collection object method failed",e);
													e.printStackTrace();
												}
											}
											updateToValue(cloned, C, newCollection, setterMethodName);
										}else{
											UtopiaBasicPersistent p=preparePersistentItemForSerialization((UtopiaBasicPersistent)invokationResult, level+1);
											updateToValue(cloned, C, p, setterMethodName);	
										}
									
										
								} catch (Exception e) {
									if(logger.isLoggable(Level.FINEST)){
										logger.log(Level.FINEST,"invokation of clone object method failed",e);
									}
									
							   }
							}else{
								updateToValue(cloned, C, null,setterMethodName);
							}
						}else{
							
								updateToValue(cloned, C, invokationResult,setterMethodName);
						}
					}
					
				}
				}
		} catch (Exception e) {
			logger.log(Level.FINEST,"invokation of setter method failed",e);
		}
		return cloned;

	}

	
	protected void updateToValue(Object obj,Class<?>C,Object newValue,String setterMethodName){
		if(setterMethodName!=null){
			try {
				obj.getClass().getMethod(setterMethodName, C).invoke(obj,new Object[]{newValue});
			} catch (Exception e1) {
				if(logger.isLoggable(Level.FINEST)){
					logger.log(Level.WARNING,"invokation of setter method failed",e1);
				}

			}
		}
		
	}
}
