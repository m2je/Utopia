package ir.utopia.core.process.model;

import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.persistent.annotation.LOVConfiguration;
import ir.utopia.core.persistent.annotation.LookupConfiguration;

public class ProcessParameter {
	String name;
	ActionParameterTypes type;
	int displayIndex;
	DisplayTypes displayType;
	LookupConfiguration lookupModel;
	LOVConfiguration lovConfiguration;
	Long minValue;
	Long maxValue;
	String defaultValue; 
	boolean mandatory;
	boolean confirmRequired=false;
	String readOnlyLogic;
	String displayLogic;
	public boolean isConfirmRequired() {
		return confirmRequired;
	}
	public void setConfirmRequired(boolean confirmRequired) {
		this.confirmRequired = confirmRequired;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public ActionParameterTypes getType() {
		return type;
	}
	public void setType(ActionParameterTypes type) {
		this.type = type;
	}
	public int getDisplayIndex() {
		return displayIndex;
	}
	public void setDisplayIndex(int index) {
		this.displayIndex = index;
	}
	public DisplayTypes getDisplayType() {
		return displayType;
	}
	public void setDisplayType(DisplayTypes displayType) {
		this.displayType = displayType;
	}
	
	public Long getMinValue() {
		return minValue;
	}
	public void setMinValue(Long minValue) {
		this.minValue = minValue;
	}
	public Long getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Long maxValue) {
		this.maxValue = maxValue;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public LookupConfiguration getLookupModel() {
		return lookupModel;
	}
	public void setLookupModel(LookupConfiguration lookupModel) {
		this.lookupModel = lookupModel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReadOnlyLogic() {
		return readOnlyLogic;
	}
	public void setReadOnlyLogic(String readOnlyLogic) {
		this.readOnlyLogic = readOnlyLogic;
	}
	public String getDisplayLogic() {
		return displayLogic;
	}
	public void setDisplayLogic(String displayLogic) {
		this.displayLogic = displayLogic;
	}
	public LOVConfiguration getLovConfiguration() {
		return lovConfiguration;
	}
	public void setLovConfiguration(LOVConfiguration lovConfiguration) {
		this.lovConfiguration = lovConfiguration;
	}
	
	
}
