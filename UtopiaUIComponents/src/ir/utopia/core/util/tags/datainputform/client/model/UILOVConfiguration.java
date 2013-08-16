package ir.utopia.core.util.tags.datainputform.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UILOVConfiguration implements Serializable,IsSerializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5800125458442767674L;
	String searchFormClass;
	String usecaseName;
	String  condiotions;
	boolean multiSelect;
	String lovSearchTitle;
	String []displayBoxColumns;
	String displaItemSeperator;
	public String getSearchFormClass() {
		return searchFormClass;
	}
	public void setSearchFormClass(String searchFormClass) {
		this.searchFormClass = searchFormClass;
	}
	public String getCondiotions() {
		return condiotions;
	}
	public void setCondiotions(String condiotions) {
		this.condiotions = condiotions;
	}
	public boolean isMultiSelect() {
		return multiSelect;
	}
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	public String getUsecaseName() {
		return usecaseName;
	}
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
	public String getLovSearchTitle() {
		return lovSearchTitle;
	}
	public void setLovSearchTitle(String lovSearchTitle) {
		this.lovSearchTitle = lovSearchTitle;
	}
	public String[] getDisplayBoxColumns() {
		return displayBoxColumns;
	}
	public void setDisplayBoxColumns(String... displayBoxColumns) {
		this.displayBoxColumns = displayBoxColumns;
	}
	public String getDisplaItemSeperator() {
		return displaItemSeperator;
	}
	public void setDisplaItemSeperator(String displaItemSeperator) {
		this.displaItemSeperator = displaItemSeperator;
	}
	
}
