package ir.utopia.core.struts;

import ir.utopia.core.constants.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActionUtil {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ActionUtil.class.getName());
	}
	public static final int SYSTEM=1;
	public static final int SUB_SYSTEM=2;
	public static final int USECASE=3;
	public static final int METHOD=4;
	/**
	 * returns a map contains 
	 * SystemName,subsystemName,usecaseName,actionName
	 * @param uri
	 * @return
	 */
	public static  Map<Integer,String> parseClassAndMethod(String uri){
		try {
			HashMap<Integer, String>result=new HashMap<Integer, String>();
			String actionName=uri.substring(uri.lastIndexOf("/")+1);
			if(actionName.startsWith(Constants.REDIREDT_TO_PAGE_PREFIX+
					Constants.RIDIRECT_TO_PAGE_SEPERATOR)){
				actionName=actionName.substring((Constants.REDIREDT_TO_PAGE_PREFIX+
						Constants.RIDIRECT_TO_PAGE_SEPERATOR).length(),actionName.length());
				
			}
				
			
			int index=actionName.indexOf(Constants.JSP_SEPERATOR);
			String methodName=actionName.substring(0,index);
			String usecaseName=actionName.substring(index+1);
			usecaseName=usecaseName.substring(0,usecaseName.indexOf(Constants.STRUTS_EXTENSION_NAME));
			index=usecaseName.indexOf(Constants.USECASE_SEPERATOR);
			String systemName=usecaseName.substring(0,index);
			int secoundIndex=usecaseName.indexOf(Constants.USECASE_SEPERATOR,index+1);
			String subSystemName=usecaseName.substring(index+1,secoundIndex);
			index=secoundIndex;
			usecaseName=usecaseName.substring(index+1);
			result.put(SYSTEM, systemName);
			result.put(SUB_SYSTEM, subSystemName);
			result.put(USECASE, usecaseName);
			result.put(METHOD, methodName);
			return result;
		} catch (Exception e) {
			logger.log(Level.ALL,"error",  e);
		}
		return null;
		}
}
