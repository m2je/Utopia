package ir.utopia.core.util;

import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.persistent.lookup.model.NamePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class EnumUtil {
	/**
	 * 
	 * @param enume
	 * @param ordinal
	 * @return
	 */
	
	public static <T extends Enum<T>> T getEnumValue(Class<T> enume,int ordinal){
		if(enume==null||ordinal<0)throw new IllegalArgumentException("invalid value null");
		Field[]fields= enume.getFields();
		
		for(int i=0;i<fields.length;i++){
			if(i==ordinal){
				return (T)Enum.valueOf((Class<T>)enume, fields[i].getName());
			}
		}
		return null;
	}
	/**
	 * 
	 * @param enume
	 * @param name
	 * @return
	 */
	public static <T extends Enum<T>> T  getEnumValue(Class<T>enume,String name){
		if(enume==null||name==null||name.trim().length()==0 )throw new IllegalArgumentException("invalid value null");
		Field[]fields= enume.getFields();
		
		for(int i=0;i<fields.length;i++){
			if(fields[i].getName().equals(name)){
				return (T)Enum.valueOf((Class<T>)enume, fields[i].getName());
			}
		}
		return null;
	} 
	
	public static List<NamePair> getEnumLookups(Class<?>enume,String language ){
		List<NamePair> result = new ArrayList<NamePair>();
		Field[] fields = enume.getFields();
		for (int i = 0; i < fields.length; i++) {
			result.add(new NamePair(getEnumString(enume, language,i),i));
		}
		return result;
		
	}
	public static String getEnumString(Class<?>enume,String language,int ordinal){
		if (enume != null && Enum.class.isAssignableFrom(enume)) {
			Field[] fields = enume.getFields();
			Class<?> declaringClass = enume.getDeclaringClass();
			boolean isInnerClass = (declaringClass!=null)&&(!declaringClass.equals(enume));
			String bundel = isInnerClass ? declaringClass.getName()
					: enume.getName();
			return MessageHandler
			.getMessage(isInnerClass ? enume
					.getSimpleName()
					+ "." + fields[ordinal].getName() : fields[ordinal]
					.getName(), bundel, language);
		}
		return "";
	}
}
