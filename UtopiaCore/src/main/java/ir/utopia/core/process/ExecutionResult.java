package ir.utopia.core.process;

import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExecutionResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1520155944652729070L;
	private List<MessageNamePair>messages;
	private boolean success=true;
	public ExecutionResult(boolean success){
		this.success=success;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public ExecutionResult(){
		
	}
	public ExecutionResult(List<MessageNamePair> messages) {
		this.messages = messages;
	}
	
	public List<MessageNamePair> getMessages() {
		if(messages!=null){
			Collections.sort(messages,new Comparator<MessageNamePair>(){

				@Override
				public int compare(MessageNamePair o1, MessageNamePair o2) {
					return o1.getType().ordinal()-o2.getType().ordinal();
				}});
		}
		return messages;
	}
	public void setMessages(List<MessageNamePair> messages) {
		this.messages = messages;
	}
	
	public void addMessage(String message,MessageType type){
		addMessage(new MessageNamePair(type,message));
	}
	public void addMessage(String message,MessageType type,MessageNamePair.ExceptionType exType){
		addMessage(new MessageNamePair(type,message,exType));
	}
	
	public void addMessage(MessageNamePair pair){
		if(messages==null){
			messages=new ArrayList<MessageNamePair>();
		}
		messages.add(pair);
	}
	
	public void addMessages(MessageNamePair[] pairs){
		if(pairs!=null){
			for(MessageNamePair pair:pairs){
				messages.add(pair);
			}
		}
		
	}
	
	public void addMessages(Collection<MessageNamePair> pairs){
		if(pairs!=null){
			addMessages(pairs.toArray(new MessageNamePair[pairs.size()]));
		}
	}
}
