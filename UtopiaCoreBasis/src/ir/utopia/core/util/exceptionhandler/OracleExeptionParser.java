package ir.utopia.core.util.exceptionhandler;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.messagehandler.MessageHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class OracleExeptionParser {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(OracleExeptionParser.class.getName());
	}
	private static final int CHECK_CONSTRAINT_ERROR_CODE_KEY=2290;
	private static final int UNIQUE_CONSTRAINT_ERROR_CODE_KEY=1;
	public  static String getConstraintName(SQLException exception){
		String constaintName=parseConstraint(exception.getMessage());
//		switch (exception.getErrorCode()) {
//		case CHECK_CONSTRAINT_ERROR_CODE_KEY:
//			constaintName=parseCheckConstraint(exception.getMessage());
//			break;
//			exception.getMessage()
//		default:constaintName=null;
//			break;
//		}
		return constaintName;
	} 
	
	private static String parseConstraint(String constraint){
		try {
			int beginIndex=constraint.indexOf(".")+1;
			int endIndex=constraint.lastIndexOf(")");
			return constraint.substring(beginIndex,endIndex).trim();
		} catch (Exception e) {
			logger.log(Level.WARNING, "",e);
			return null;
		}
	}
//*******************************************************************************	
	public static String transalteDetailMessage(String detailMessage,Map<String,Object>context){
		int callIndex=detailMessage.indexOf("Call:");
		if(callIndex>=0){
			int insertIndex=detailMessage.indexOf("INSERT INTO");
			if(insertIndex>callIndex){
				return translateParameterMessages(insertIndex, detailMessage,context);
			}else{
				//TODO update and other sql statements
			}
		}
		return null;
	}
//*******************************************************************************
	private static String translateParameterMessages(int startIndex ,String detailMessage,Map<String,Object>context){
		Map<String,Object>bindMap=new HashMap<String, Object>();
		int columnIndex=detailMessage.indexOf("(",startIndex);
		int endIndex=detailMessage.indexOf(")",columnIndex);
		String columnStr=detailMessage.substring(columnIndex+1,endIndex);
		int bindIndex=detailMessage.indexOf("bind =>");
		bindIndex=detailMessage.indexOf("[",bindIndex);
		int endBindIndex=detailMessage.indexOf("]", bindIndex);
		String bindString=detailMessage.substring(bindIndex+1,endBindIndex);
		StringTokenizer propertyTokenizer=new StringTokenizer(columnStr,",");
		StringTokenizer valueTokenizer=new StringTokenizer(bindString,",");
		while(propertyTokenizer.hasMoreElements()){
			try {
				String property=propertyTokenizer.nextToken();
				String value=valueTokenizer.nextToken();
				bindMap.put(property.trim(), value.trim());
			} catch (Exception e) {
				continue;
			}
		}
		String language=ContextUtil.getLoginLanguage(context);
		String message=MessageHandler.getMessage("invalidArgumentsValues", "ir.utopia.core.util.exceptionhandler.SQLParameters",language );
		StringBuffer bindMessage=new StringBuffer();
		boolean first=true;
		for(String bind:bindMap.keySet()){
			if(!first){
				bindMessage.append(",");
			}
			bindMessage.append(MessageHandler.getDataBaseColumnName(bind,language ));
			bindMessage.append("-->")
			.append(bindMap.get(bind)).append((char)13);
			first=false;
		}
		message=message.replaceAll("@arguments@", bindMessage.toString());
		return message;
	}
	

}
