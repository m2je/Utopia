package ir.utopia.core.security;

import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;


public class WindowController {

	private static final String WINDOW_NUMBER_SESSION_ATTRIBUTE_NAME="windowNo"; 
	private static final String WINDOW_SESSION_MESSAGE="windowSessionMessage";
	private static final String WINDOW_SESSION_ERROR_FIELD="sessionErrorField";
	private static final String WINDOW_SESSION_ACTION_STATUS="sessionActionParam";
	private static final String CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME="__consumedWinNo";
	
	public static synchronized int getCurrentWindowNo(HttpSession session){
		if(session==null)return -1;
		int current=0;
		try {
			current=Integer.parseInt(session.getAttribute(WINDOW_NUMBER_SESSION_ATTRIBUTE_NAME).toString());
		} catch (Exception e) {
			current=0;
		}
		return current;
	}
//************************************************************************************************
	public static synchronized int getNextWindowNo(HttpSession session){
		if(session==null)return -1;
		int current=getCurrentWindowNo(session);
		session.setAttribute(WINDOW_NUMBER_SESSION_ATTRIBUTE_NAME, ++current);
		return current;
	}
//************************************************************************************************
	public static synchronized int getCurrentWindowNo(Map<String,Object> session){
		if(session==null)return -1;
		int current=0;
		try {
			current=Integer.parseInt(session.get(WINDOW_NUMBER_SESSION_ATTRIBUTE_NAME).toString());
		} catch (Exception e) {
			current=0;
		}
		return current;
	}
//************************************************************************************************
	public static synchronized int getNextWindowNo(Map<String,Object> session){
		if(session==null)return -1;
		int current=getCurrentWindowNo(session);
		session.put(WINDOW_NUMBER_SESSION_ATTRIBUTE_NAME, ++current);
		return current;
	}
//************************************************************************************************
	public static synchronized void consumeWindowNumber(Map<String,Object> session,int windowNumber){
		if(session==null)return;
		@SuppressWarnings("unchecked")
		Set<Integer> consumedWindows=(Set<Integer>)session.get(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME);
		if(consumedWindows==null){
			consumedWindows=new HashSet<Integer>();
			session.put(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME, consumedWindows);
		}
		consumedWindows.add(windowNumber);
	}
//************************************************************************************************
	public static synchronized void consumeWindowNumber(HttpSession session,int windowNumber){
		if(session==null)return;
		@SuppressWarnings("unchecked")
		Set<Integer> consumedWindows=(Set<Integer>)session.getAttribute(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME);
		if(consumedWindows==null){
			consumedWindows=new HashSet<Integer>();
			session.setAttribute(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME, consumedWindows);
		}
		consumedWindows.add(windowNumber);
	}
//************************************************************************************************
	@SuppressWarnings("unchecked")
	public static synchronized boolean isConsumed(Map<String,Object> session,int windowNumber){
		return session!=null&&session.get(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME)!=null&&
				((Set<Integer>)session.get(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME)).contains(windowNumber);
	}
//************************************************************************************************
	@SuppressWarnings("unchecked")
	public static synchronized boolean isConsumed(HttpSession session,int windowNumber){
		return session==null||session.getAttribute(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME)==null||
				!((Set<Integer>)session.getAttribute(CONSUMED_WINDOW_NEMBER_ATTRIBUTE_NAME)).contains(windowNumber);
	}
//************************************************************************************************
@SuppressWarnings("unchecked")
public static void markErrorField(HttpSession session,int windowNumber,String field){
	if(session!=null){
		String key=WINDOW_SESSION_ERROR_FIELD+"|"+windowNumber;
		Object o= session.getAttribute(key);
		List<String>errorFields;
		if(o instanceof List){
			errorFields=(List<String>)o;
		}else{
			errorFields=new ArrayList<String>();
		}
		session.setAttribute(key, errorFields);
	}	
	}
//************************************************************************************************
@SuppressWarnings("unchecked")
public static void markErrorField(Map<String,Object> session,int windowNumber,String field){
	if(session!=null){
		String key=WINDOW_SESSION_ERROR_FIELD+"|"+windowNumber;
		Object o= session.get(key);
		List<String>errorFields;
		if(o instanceof List){
			errorFields=(List<String>)o;
		}else{
			errorFields=new ArrayList<String>();
		}
		session.put(key, errorFields);
	}	
	}
//*************************************************************************************************
@SuppressWarnings("unchecked")
public static List<String> removeErrorFields(HttpSession session,int windowNumber){
	if(session!=null){
		String key=WINDOW_SESSION_ERROR_FIELD+"|"+windowNumber;
		Object o= session.getAttribute(key);
		session.removeAttribute(key);
		if(o instanceof List){
			return (List<String>)o;
		}
	}
	return null;
}
//*************************************************************************************************
@SuppressWarnings("unchecked")
public static List<String> removeErrorFields(Map<String,Object> session,int windowNumber){
	if(session!=null){
		String key=WINDOW_SESSION_ERROR_FIELD+"|"+windowNumber;
		if(!session.containsKey(key))return null;
		Object o= session.get(key);
		session.remove(key);
		if(o instanceof List){
			return (List<String>)o;
		}
	}
	return null;
}
//*************************************************************************************************
	public static void addWindowErrorMessage(HttpSession session,int windowNumber,String message){
		addWindowMessage(session, windowNumber, new MessageNamePair(MessageType.error,message));
	}
//*************************************************************************************************	
	public static void addWindowWarnMessage(HttpSession session,int windowNumber,String message){
		addWindowMessage(session, windowNumber, new MessageNamePair(MessageType.warning,message));
	}
//*************************************************************************************************	
	public static void addWindowInfoMessage(HttpSession session,int windowNumber,String message){
		addWindowMessage(session, windowNumber, new MessageNamePair(MessageType.info,message));
	}
//*************************************************************************************************	
	@SuppressWarnings("unchecked")
	public static void addWindowMessage(HttpSession session,int windowNumber,MessageNamePair pair){
		if(session!=null){
			String key=WINDOW_SESSION_MESSAGE+"|"+pair.getType().ordinal()+"|"+ windowNumber;
			Object o= session.getAttribute(key);
			List<MessageNamePair>pairs;
			if(o instanceof List){
				pairs=(List<MessageNamePair>)o;
			}else{
				pairs=new ArrayList<MessageNamePair>();
			}
			pairs.add(pair);
			session.setAttribute(key, pair);
		}
	}
//*************************************************************************************************	
	public static void addWindowErrorMessage(Map<String,Object> session,int windowNumber,String message){
		addWindowMessage(session, windowNumber, new MessageNamePair(MessageType.error,message));
	}
//*************************************************************************************************	
	public static void addWindowWarnMessage(Map<String,Object> session,int windowNumber,String message){
		addWindowMessage(session, windowNumber, new MessageNamePair(MessageType.warning,message));
	}
//*************************************************************************************************	
	public static void addWindowInfoMessage(Map<String,Object> session,int windowNumber,String message){
		addWindowMessage(session, windowNumber, new MessageNamePair(MessageType.info,message));
	}
//*************************************************************************************************
	@SuppressWarnings("unchecked")
	public static void addWindowMessage(Map<String,Object> session,int windowNumber,MessageNamePair pair){
		if(session!=null){
			String key=WINDOW_SESSION_MESSAGE+"|"+pair.getType().ordinal()+"|"+ windowNumber;
			Object o= session.get(key);
			List<MessageNamePair>pairs;
			if(o instanceof List){
				pairs=(List<MessageNamePair>)o;
			}else{
				pairs=new ArrayList<MessageNamePair>();
			}
			pairs.add(pair);
			session.put(key, pair);
		}
	}
//*************************************************************************************************
	public static List<MessageNamePair> removeWindowErrorMessages(HttpSession session,int windowNumber){
		return removeWindowMessages(session, windowNumber, MessageType.error);
	}
//*************************************************************************************************
	public static List<MessageNamePair> removeWindowWarningMessages(HttpSession session,int windowNumber){
		return removeWindowMessages(session, windowNumber, MessageType.warning);
	}
//*************************************************************************************************
	public static List<MessageNamePair> removeWindowInfoMessages(HttpSession session,int windowNumber){
		return removeWindowMessages(session, windowNumber, MessageType.info);
	}
//*************************************************************************************************
	public static List<MessageNamePair> removeWindowErrorMessages(Map<String,Object> session,int windowNumber){
		return removeWindowMessages(session, windowNumber, MessageType.error);
	}
//*************************************************************************************************
	public static List<MessageNamePair> removeWindowWarningMessages(Map<String,Object> session,int windowNumber){
		return removeWindowMessages(session, windowNumber, MessageType.warning);
	}
//*************************************************************************************************
	public static List<MessageNamePair> removeWindowInfoMessages(Map<String,Object> session,int windowNumber){
		return removeWindowMessages(session, windowNumber, MessageType.info);
	}
//*************************************************************************************************
	@SuppressWarnings("unchecked")
	public static List<MessageNamePair> removeWindowMessages(HttpSession session,int windowNumber,MessageType type){
		if(session!=null){
			String key=WINDOW_SESSION_MESSAGE+"|"+type.ordinal()+"|"+ windowNumber;
			Object o= session.getAttribute(key);
			if(o instanceof List){
				session.removeAttribute(key);
				return (List<MessageNamePair>)o;
			}
			
			}
		return null;
		

	}
//*************************************************************************************************
	@SuppressWarnings("unchecked")
	public static List<MessageNamePair> removeWindowMessages(Map<String,Object> session,int windowNumber,MessageType type){
		if(session!=null){
			String key=WINDOW_SESSION_MESSAGE+"|"+type.ordinal()+"|"+ windowNumber;
			Object o= session.get(key);
			if(o instanceof List){
				session.remove(key);
				return (List<MessageNamePair>)o;
			}
			
			}
		return null;
	}
//*************************************************************************************************	
	/**
	 * 
	 * @param session
	 * @param windowNumber
	 * @param actionName
	 * @param success
	 */
	public static void setActionStatus(HttpSession session,int windowNumber,String actionName,boolean success){
		if(session!=null){
			String key= WINDOW_SESSION_ACTION_STATUS+"|"+actionName+"|"+windowNumber;
			session.setAttribute(key, success);
		}
	}
//*************************************************************************************************	
	/**
	 * 
	 * @param session
	 * @param windowNumber
	 * @param actionName
	 * @param success
	 */
	public static void setActionStatus(Map<String,Object> session,int windowNumber,String actionName,boolean success){
		if(session!=null){
			String key= WINDOW_SESSION_ACTION_STATUS+"|"+actionName+"|"+windowNumber;
			session.put(key, success);
		}
	}
//*************************************************************************************************
	/**
	 * 
	 * @param session
	 * @param windowNumber
	 * @param actionName
	 * @return
	 */
	public static boolean removeActionStatus(HttpSession session,int windowNumber,String actionName){
		if(session!=null){
			String key= WINDOW_SESSION_ACTION_STATUS+"|"+actionName+"|"+windowNumber;
			Object o= session.getAttribute(key);
			session.removeAttribute(key);
			try {
				return Boolean.parseBoolean(o.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
//*************************************************************************************************
	/**
	 * 
	 * @param session
	 * @param windowNumber
	 * @param actionName
	 * @return
	 */
	public static boolean removeActionStatus(Map<String,Object> session,int windowNumber,String actionName){
		if(session!=null){
			String key= WINDOW_SESSION_ACTION_STATUS+"|"+actionName+"|"+windowNumber;
			if(session.containsKey(key)){
				Object o= session.get(key);
				session.remove(key);
				try {
					return Boolean.parseBoolean(o.toString());
				} catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}
}
