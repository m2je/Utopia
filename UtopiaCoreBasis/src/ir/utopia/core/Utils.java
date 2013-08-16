package ir.utopia.core;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Utils {
	/**
	 * 
	 * @param source
	 * @return
	 */
	public static Long[] convertCommaSepratedIdToArray(String source){
		if(source==null||source.trim().length()==0)return null;
		StringTokenizer tokener=new StringTokenizer(source,",");
		ArrayList<Long>result=new ArrayList<Long>();
		while(tokener.hasMoreTokens()){
			result.add(Long.parseLong(tokener.nextToken()));
		}
		return result.toArray(new Long[result.size()]);
	}
	
	public static String convertObjectArrayToCammaSeperted(Object ...input){
		if(input==null||input.length==0)return "";
		StringBuffer result=new StringBuffer();
		boolean first=true;
		for(Object cur:input){
			if(!first){
				result.append(",");
			}
			result.append(cur.toString());
			first=false;
		}
		return result.toString();
	} 
	
}
