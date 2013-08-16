package ir.utopia.core.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

public class GWTUtil {
	/**
	 * converts display type to UtopiaCoreUIComponents display types
	 * @param displayType
	 * @return
	 */
	public static int convertDisplayType(int displayType){
		if (displayType==DisplayTypes.Integer.ordinal()) {
			return InputItem.DISPLAY_TYPE_NUMERIC;
		}else if(displayType==DisplayTypes.Date.ordinal()){
			return InputItem.DISPLAY_TYPE_DATE;
		}else if(displayType==DisplayTypes.checkBox.ordinal()){
			return InputItem.DISPLAY_TYPE_CHECK_BOX;
		}else if(displayType==DisplayTypes.lookup.ordinal()){
			return InputItem.DISPLAY_TYPE_COMBOBOX;
		}else if(displayType==DisplayTypes.list.ordinal()){
			return InputItem.DISPLAY_TYPE_LIST;
		}else if(displayType==DisplayTypes.currencyField.ordinal()){
			return InputItem.DISPLAY_TYPE_CURRENCY;
		}else if(displayType==DisplayTypes.password.ordinal()){
			return InputItem.DISPLAY_TYPE_PASSWORD;
		}else if(displayType==DisplayTypes.RadioButton.ordinal()){
			return InputItem.DISPLAY_TYPE_RADIO_BUTTON;
		}else if(displayType==DisplayTypes.LargeString.ordinal()){
			return InputItem.DISPLAY_TYPE_LARGE_STRING;
		}else if(displayType==DisplayTypes.Hidden.ordinal()){
			return InputItem.DISPLAY_TYPE_HIDDEN;
		}else if(displayType==DisplayTypes.LOV.ordinal()){
			return InputItem.DISPLAY_TYPE_LOV;
		}else if(displayType==DisplayTypes.upload.ordinal()){
			return InputItem.DISPLAY_TYPE_FILE;
		}
		return InputItem.DISPLAY_TYPE_STRING;
	}
	public static int convertDisplayType(DisplayTypes type){
		if(type==null){
			return InputItem.DISPLAY_TYPE_STRING;
		}
		return convertDisplayType(type.ordinal());
	}
	public static Class<?> getPreferedTypeForDisplayType(int displayType){
		if (displayType==InputItem.DISPLAY_TYPE_NUMERIC) {
			return int.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_DATE){
			return Date.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_CHECK_BOX){
			return boolean.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_COMBOBOX){
			return long.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_LIST){
			return Enum.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_CURRENCY){
			return String.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_PASSWORD){
			return String.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_RADIO_BUTTON){
			return Enum.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_LARGE_STRING){
			return String.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_HIDDEN){
			return String.class;
		}else if(displayType==InputItem.DISPLAY_TYPE_LOV){
			return long.class;
		}
		return String.class;
	}
	
	public static ExecutionResult convertExectionResult(ir.utopia.core.process.ExecutionResult executionResult){
		if(executionResult==null)return null;
		ExecutionResult result=new ExecutionResult(executionResult.isSuccess());
	    List<MessageNamePair>messages= executionResult.getMessages();
	    if(messages!=null&&messages.size()>0){
	    	ArrayList<String> errors=new ArrayList<String>();
	    	ArrayList<String> warnings=new ArrayList<String>();
	    	ArrayList<String> infos=new ArrayList<String>();
	    	for(MessageNamePair message:messages){
	    		if(MessageType.error.equals(message.getType())){
	    			errors.add(message.getMessage());
	    		}else if(MessageType.warning.equals(message.getType())){
	    			warnings.add(message.getMessage());
	    		}else if(MessageType.error.equals(message.getType())){
	    			infos.add(message.getMessage());
	    		}
	    		
	    	}
	    	result.setErrorMessages(errors.toArray(new String[errors.size()]));
	    	result.setInfoMessages(infos.toArray(new String[infos.size()]));
	    	result.setWarningMessages(warnings.toArray(new String[warnings.size()]));
	    }
		return result;
	}
}
