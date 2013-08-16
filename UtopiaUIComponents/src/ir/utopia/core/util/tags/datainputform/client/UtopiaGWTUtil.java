package ir.utopia.core.util.tags.datainputform.client;

public class UtopiaGWTUtil {

	//**************************************************************************	
	public static String getLocaleNumber(String number,String language){
		if(number==null||number.trim().length()==0)return "";
		if("fa".equals(language)||"ar".equals(language)){
			char [] input=number.toCharArray();
			StringBuffer result=number.startsWith("-")?//if negative number
					new StringBuffer("\u200E"):new StringBuffer();
			for(int i=0;i<input.length;i++){
				char c=input[i];
				if(Character.isDigit(c)){
						result.append(toRigthToLeftNumber(Integer.parseInt(c+"")));
				}else{
					result.append(c);
				}
			}
			return result.toString().trim();
		}
		return number;
	}
//**************************************************************************	
	public static String toRigthToLeftNumber(int c){
		switch (c) {
		case 0:return "\u06f0";
		case 1:return "\u06f1";
		case 2:return "\u06f2";
		case 3:return "\u06f3";
		case 4:return "\u06f4";
		case 5:return "\u06f5";
		case 6:return "\u06f6";
		case 7:return "\u06f7";
		case 8:return "\u06f8";
		case 9:return "\u06f9";
		}
		return "" ;
	} 

}
