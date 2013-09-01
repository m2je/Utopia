package ir.utopia.core.util.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class PersianLanguagePatch extends AbstractLanguagePatch implements LanguagePatch {

	@Override
	public Object[] getAlternateValuesFor(Object value) {
		if(String.class.isInstance(value)){
			String textValue=(String)value;
			ArrayList<Object>result=getPersianYAlternate(textValue);
			if(result.size()>0){
				ArrayList<Object>newResult=new ArrayList<Object>();
				for(Object o:result){
					ArrayList<Object> k= getPersianKAlternate((String)o);
					if(k!=null){
						newResult.addAll(k);
					}
				}
				result.addAll(newResult);
				return result.toArray(new String[result.size()]);
			}else{
				ArrayList<Object> k=getPersianKAlternate(textValue);
				if(k!=null)
					result.addAll(k);
			}
			return result.toArray(new String[result.size()]);
		}else{
			return null;
		}
		
	}
	protected ArrayList<Object> getPersianKAlternate(String textValue){
		
		if(textValue.indexOf("\u0643")>=0|| 
		   textValue.indexOf("\u06A9")>=0){
			ArrayList<Object>result=new ArrayList<Object>();
			result.addAll(getAlternateFor(textValue, "\u0643", "\u06A9"));
			result.addAll(getAlternateFor(textValue, "\u06A9", "\u0643"));
			return result;
			
		}
		return null;
	}
	protected ArrayList<Object> getPersianYAlternate(String textValue){
		ArrayList<Object>result=new ArrayList<Object>();
		if(textValue.indexOf("\uFEF1")>=0|| 
		   textValue.indexOf("\uFBFC")>=0||	
		   textValue.indexOf("\u06CC")>=0||
		   textValue.indexOf("\u064A")>=0){
			result.addAll(getAlternateFor(textValue, "\uFEF1", "\uFBFC"));
			result.addAll(getAlternateFor(textValue, "\uFBFC", "\uFEF1"));
			result.addAll(getAlternateFor(textValue, "\u06CC", "\u064A"));
			result.addAll(getAlternateFor(textValue, "\u064A", "\u06CC"));
			
		}
		return result;
	}
	
	public static void main (String []args){
		PersianLanguagePatch patch=new PersianLanguagePatch();
//		Set<String>alternates= patch.getAlternateFor("\u064A"+"AAAA"+"\u064A"+"B"+"\u064A", "\u064A", "\u06CC");
		Set<String>alternates= patch.getAlternateFor("B"+"\u064A", "\u064A", "\u06CC");
		String [] r=alternates.toArray(new String[alternates.size()]);
		Arrays.sort(r);
		System.out.println() ;
//		System.out.println(patch.getAlternateFor("\u064A"+"AAAA"+"\u064A", "\u064A", "\u06CC"));
	}
}
