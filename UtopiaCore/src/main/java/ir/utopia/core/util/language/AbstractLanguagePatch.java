package ir.utopia.core.util.language;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLanguagePatch implements LanguagePatch {

	public Set<String> getAlternateFor(String value,String character,String alternateChar){
		int index=value.indexOf(character);
		String prefix,postfix;
		HashSet<String>result=new HashSet<String>();
		while(index>=0){
			prefix="";
			postfix="";
			if(index>0&&index<value.length()){
				prefix=value.substring(0,index);
			}
			if(index<value.length()-1){
				postfix=value.substring(index+1,value.length());
			}
			String alternate=new StringBuffer(prefix).append(alternateChar).append(postfix).toString();
			result.add(alternate);
			result.addAll(getAlternateFor(alternate, character, alternateChar));
			index=value.indexOf(character,index+1);
		}
		return result;
	}

}
