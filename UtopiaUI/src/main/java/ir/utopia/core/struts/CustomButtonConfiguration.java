package ir.utopia.core.struts;

import ir.utopia.core.constants.Constants.predefindedActions;

public class CustomButtonConfiguration {

	String cssClass;
	String text;
	String onClick;
	String displayLogic;
	String readOnlyLogic;
	predefindedActions[] actions;
	String id;
	public CustomButtonConfiguration(predefindedActions[] actions){
		this.actions=actions;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOnClick() {
		return onClick;
	}
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}
	public String getDisplayLogic() {
		return displayLogic;
	}
	public void setDisplayLogic(String displayLogic) {
		this.displayLogic = displayLogic;
	}
	public String getReadOnlyLogic() {
		return readOnlyLogic;
	}
	public void setReadOnlyLogic(String readOnlyLogic) {
		this.readOnlyLogic = readOnlyLogic;
	}
	public predefindedActions[] getActions() {
		return actions;
	}
	public void setActions(predefindedActions[] actions) {
		this.actions = actions;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
