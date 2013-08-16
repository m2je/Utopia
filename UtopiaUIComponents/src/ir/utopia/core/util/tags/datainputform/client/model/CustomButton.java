package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CustomButton extends InputItem implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5429550344481586004L;
	String cssClass;
	
	String onclick;

	
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	
	
	
}
