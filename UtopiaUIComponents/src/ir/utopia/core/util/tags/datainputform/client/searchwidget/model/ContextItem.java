package ir.utopia.core.util.tags.datainputform.client.searchwidget.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContextItem implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7543194606832671977L;
	private String formClass;
	private String fieldName;
	private String value;
	public String getFormClass() {
		return formClass;
	}
	public void setFormClass(String formClass) {
		this.formClass = formClass;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
