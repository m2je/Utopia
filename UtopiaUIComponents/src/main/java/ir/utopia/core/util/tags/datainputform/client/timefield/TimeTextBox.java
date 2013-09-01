package ir.utopia.core.util.tags.datainputform.client.timefield;

import com.google.gwt.user.client.ui.TextBox;

public class TimeTextBox extends TextBox {

	public TimeTextBox(){
		
	}
	public boolean validate(){
		String value=getValue();
		return value==null||value.trim().length()==0||
		(value.matches("[0-9]{1,2}:[0-9]{1,2}")&&
		 Integer.parseInt(value.substring(0,value.indexOf(":")))<24 &&
		 Integer.parseInt(value.substring(value.indexOf(":")+1))<60
				);
	}
	
	
}
