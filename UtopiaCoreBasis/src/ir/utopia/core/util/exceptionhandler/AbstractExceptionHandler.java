package ir.utopia.core.util.exceptionhandler;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.exception.CoreException;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;

public abstract class AbstractExceptionHandler implements ExceptionHandler{

	@Override
	public ExceptionResult handel(Throwable e,Map<String,Object>context) {
		ExceptionResult result=new ExceptionResult();
		result.setSuccess(false);
		result= handel(e, context, result);
		ArrayList<MessageNamePair>pairs=new ArrayList<MessageNamePair>();
		MessageNamePair tPair= translateLocalizedMessage(e,context);
		if(tPair!=null){
			pairs.add(tPair);
		}
		if( result.getMessages()!=null){
		l1:	for(MessageNamePair message:result.getMessages()){
			String newMessage=message.getMessage();
			if(newMessage==null||newMessage.trim().length()==0)continue l1;
				for(MessageNamePair pair:pairs){
					String msg=pair.getMessage();
					if(msg==null||msg.trim().length()==0)continue;
					if(msg.equals(newMessage)){
						continue l1;
					}
				}
				pairs.add(message);
			}
		}
		
		result.setMessages(pairs);
		return result;
	}
//**************************************************************************************
	protected ExceptionResult handel(Throwable e,Map<String,Object>context,ExceptionResult result) {
		if(e ==null)return result;
		Throwable cause=EJBException.class.isAssignableFrom(e.getClass()) ?((EJBException)e).getCausedByException():e.getCause();
		if(e.equals(cause))return result;
		Message m=getExceptionMessage(e, context);
		Set<String>errorFields= result.getErrorFields();
		errorFields=errorFields==null?new HashSet<String>():errorFields;
		if(m.errorFields!=null){
			for(String fields:m.errorFields){
				errorFields.add(fields);
			}
		}
		result.addMessage(m.message,MessageType.error,m.type);
		result.setErrorFields(errorFields);
		return handel(cause, context,result);
	}
//**************************************************************************************
	public static Message getExceptionMessage(Throwable e,Map<String,Object>context){
		String language=ContextUtil.getLoginLanguage(context);
		if(CoreException.class.isAssignableFrom(e.getClass())){
			return	new Message(MessageNamePair.ExceptionType.applicationSpecified, MessageHandler.getExceptionMessage((CoreException)e,language ),null);
			}else if(SQLException.class.isAssignableFrom(e.getClass())){
				return	getSQLMessage((SQLException)e, language);
			} 
			else{
			return	new Message(MessageNamePair.ExceptionType.internalError,MessageHandler.getMessage("internalApplicationError", "ir.utopia.core.constants.Glossory", language),null);
			}
	}
//**************************************************************************************	
	public static class Message{
		MessageNamePair.ExceptionType type;
		String message;
		String []errorFields;
		public Message(MessageNamePair.ExceptionType exType, String message,String []errorFields) {
			super();
			this.type = exType;
			this.message = message;
			this.errorFields=errorFields;
		}
		public MessageNamePair.ExceptionType getType() {
			return type;
		}
		public void setType(MessageNamePair.ExceptionType type) {
			this.type = type;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String[] getErrorFields() {
			return errorFields;
		}
		public void setErrorFields(String[] errorFields) {
			this.errorFields = errorFields;
		}
		
	}
//**************************************************************************************
	protected static Message getSQLMessage(SQLException exception,String language){
		String message=MessageHandler.getDataBaseConstraintMessage(OracleExeptionParser.getConstraintName(exception), language);
		return new Message(MessageNamePair.ExceptionType.databaseError,message,null);
	}
//**************************************************************************************
	protected static MessageNamePair translateLocalizedMessage(Throwable e,Map<String,Object>context){
		String message=e.getLocalizedMessage();
		if(message==null||message.trim().length()==0)return null;
		Throwable cause=e;
		while(cause.getCause()!=null&&!cause.equals(cause.getCause())){
			cause=cause.getCause();
		}
		if(SQLException.class.isInstance(cause) ){
			String resMsg= OracleExeptionParser.transalteDetailMessage(message,  context);
			return resMsg!=null?new MessageNamePair(MessageType.error,resMsg):null;
		}
		return null;
	}
}
