package ir.utopia.core.util;

import ir.utopia.core.importer.FieldSetup.Types;

public abstract class ImportSettingUtils {
	
//*************************************************************************************************	
	public static Class<?> togglePrimitive(Class<?> clazz){
		if(int.class.equals(clazz)){
			return Integer.class;
		}
		else if(long.class.equals(clazz)){
			return Long.class;
		}
		else if(byte.class.equals(clazz)){
			return Byte.class;
		}
		else if(boolean.class.equals(clazz)){
			return Boolean.class;
		}
		else if(float.class.equals(clazz)){
			return Float.class;
		}
		
		return clazz;
	}
//*************************************************************************************************
	public static String getFieldFormula(String formula, String fieldName){
		
		if(formula!=null&&formula.trim().length()>0){
		 int index=formula.indexOf(fieldName + "Excel");
		 int nextComma=formula.indexOf(",",index);
		 int equalSign=formula.indexOf("=",index);
		 if(nextComma>0&&equalSign>0&&nextComma>equalSign+1)
			 return formula.substring(equalSign+1,nextComma);
		}
		return null;
	}
	
//*************************************************************************************************
	public static Types getType(String formula,String fieldName){
		if(formula!=null&&formula.trim().length()>0){
		 int index=formula.indexOf(fieldName + "Type");
		 int nextComma=formula.indexOf(",",index);
		 int equalSign=formula.indexOf("=",index);
		 if(nextComma>0&&equalSign>0&&nextComma>equalSign+1)
			return Types.getType(Integer.parseInt(formula.substring(equalSign+1,nextComma)));
		}
		return null;
	}
}
