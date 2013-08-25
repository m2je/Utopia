/**
 * 
 */
package ir.utopia.core.messagehandler;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.exception.CoreException;
import ir.utopia.core.logic.util.LogicParser;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author salarkia
 *
 */
public abstract class MessageHandler {
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(MessageHandler.class.getName());
	}
	public static final String DEFAULT_GLOSSOARY_BUNDLE="ir.utopia.core.constants.Glossory";
	public static final String IMPORTER_GLOSSOARY_BUNDLE="ir.utopia.core.constants.Importer";
	public static synchronized String getMessage(String messageName,String messagePath,String alternatePath,String language){
		String result;
		try {
			result=getMessageBundle(messageName, messagePath, language);
		} catch (RuntimeException e) {
			logger.log(Level.WARNING,"fail to load bundel --->"+messageName+" in bundle --->"+messagePath);
			return getMessage(messageName, alternatePath, language);
		}
		return result==null?messageName:result.trim();
	}
//*************************************************************************************************	 
 /**
 * 
 * @param messageName
 * @param messagePath
 * @param language
 * @return
 */	 
	public static synchronized String getMessage(String messageName,String messagePath,String language){
		String result;
		try {
			result=getMessageBundle(messageName, messagePath, language);
		} catch (RuntimeException e) {
			
			try {
				Set<String>globalGlossories=ServiceFactory.getGlobalGlossory();
				for(String bundel:globalGlossories){
					try {
						return getMessageBundle(messageName, bundel, language);
					} catch (Exception e1) {
						continue;
					}
				}
				if(!DEFAULT_GLOSSOARY_BUNDLE.equals(messagePath))
						return getMessageBundle(messageName, DEFAULT_GLOSSOARY_BUNDLE, language);
				else {
					logger.log(Level.WARNING,"fail to load bundel --->"+messageName+" in bundle --->"+DEFAULT_GLOSSOARY_BUNDLE);
					result=messageName;
				}
			} catch (Exception e1) {
				logger.log(Level.WARNING,"fail to load bundel --->"+messageName+" in bundle --->"+messagePath);
				result=messageName;
			}
		}
		return result==null?messageName:result.trim();
	}
//*************************************************************************************************
	private static synchronized String getMessageBundle(String messageName,String messagePath,String language){
		if(messageName==null ||messageName.trim().length()==0){
			return "";
		}
		if(messagePath==null||messagePath.trim().length()==0){
			return "";
		}
		ResourceBundle myMessages = ResourceBundle.getBundle(messagePath,new Locale( language));
		return myMessages.getString( messageName);
			}
//*************************************************************************************************
/**
 * 
 * @param messageName
 * @param messagePath
 * @param language
 * @param params
 * @return
 */	
	public static synchronized String getMessage(String messageName,String messagePath,String language,Map<String,String>params){
		String result=getMessage(messageName, messagePath, language);
		return LogicParser.getFlatExpression(result, params);
	}
//*************************************************************************************************
	/**
	 * 
	 * @param exception
	 * @param language
	 * @return
	 */
	public static synchronized String getExceptionMessage(CoreException exception,String language){
		return getExceptionMessage(exception, language,null);
	}
//*************************************************************************************************
	/**
	 * 
	 * @param exception
	 * @param language
	 * @param params
	 * @return
	 */
	public static synchronized String getExceptionMessage(CoreException exception,String language,Map<String,Object>context){
		String result=getMessage("unKnownException", "ir.utopia.core.constants.CoreException",language ) ;
		Set<String>exceptionBundels= ServiceFactory.getExceptionBundelPaths();
		if(exceptionBundels!=null){
			for(String bundel:exceptionBundels){
				 try {
					result= getMessageBundle(exception.getClass().getName(), bundel, language);
					if(result!=null&&result.trim().length()>0){
						Map<String,Object>myContext ;
						if(context==null){
							myContext=exception.getContext();
						}else {
							myContext=context;
							if(exception.getContext()!=null){
								myContext.putAll(exception.getContext());
							}
						}
						if(myContext==null){
							return result;
						}
						return LogicParser.getFlatExpression(result, myContext);
					}
				} catch (Exception e) {
					continue;
				}
			}
			}
		return result;
	}
//*************************************************************************************************
	public static synchronized String getDataBaseConstraintMessage(String constraintName,String language){
		return getMessages(constraintName, language,ServiceFactory.getDataBaseConstraintBundels());
	}
//*************************************************************************************************
	public static synchronized String getDataBaseColumnName(String column,String language){
		return getMessages(column, language,ServiceFactory.getDataBaseColumnBundel());
	}
//*************************************************************************************************
	public static synchronized String getMessages(String name,String language,Set<String>bundels){
		if(name==null)return null;
		if(bundels!=null){
			for(String bundel:bundels){
				 try {
					String result= getMessageBundle(name, bundel, language);
					return result;
					}
				 catch (Exception e) {
					continue;
				}
			}
			}
		try {
			return getMessageBundle(name, DEFAULT_GLOSSOARY_BUNDLE, language);
		} catch (Exception e) {
			return name;
		}
	}
}
