package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SchedulerConfigurationModel implements Serializable,IsSerializable{
	public static final int SCHEDULE_TASK_TYPE_MAIL=1;
	public static final int SCHEDULE_TASK_TYPE_PROCESS=1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7101342741198579151L;
	int type;
	
	Long configurationId;
	String configurationName;
	String configurationNewName;
	String Description;
	UILookupInfo systems,subsystems,usecases,usecaseActions;
	String[] customPropertyNames;
	String[] customPropertyValues;
	String startDate;
	String enddate;
	ScheduleInfo scheduleInfo;
	RecepientInfo recepients;
	Long usecaseActionId;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getConfigurationId() {
		return configurationId;
	}
	public void setConfigurationId(Long configurationId) {
		this.configurationId = configurationId;
	}
	public String getConfigurationName() {
		return configurationName;
	}
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
	
	public String getConfigurationNewName() {
		return configurationNewName;
	}
	public void setConfigurationNewName(String configurationNewName) {
		this.configurationNewName = configurationNewName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	public String[] getCustomPropertyNames() {
		return customPropertyNames;
	}
	public void setCustomPropertyNames(String[] customPropertyNames) {
		this.customPropertyNames = customPropertyNames;
	}
	public String[] getCustomPropertyValues() {
		return customPropertyValues;
	}
	public void setCustomPropertyValues(String[] customPropertyValues) {
		this.customPropertyValues = customPropertyValues;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public ScheduleInfo getScheduleInfo() {
		return scheduleInfo;
	}
	public void setScheduleInfo(ScheduleInfo scheduleInfo) {
		this.scheduleInfo = scheduleInfo;
	}
	public RecepientInfo getRecepients() {
		return recepients;
	}
	public void setRecepients(RecepientInfo recepients) {
		this.recepients = recepients;
	}
	
	public UILookupInfo getSystems() {
		return systems;
	}
	public void setSystems(UILookupInfo systems) {
		this.systems = systems;
	}
	public UILookupInfo getSubsystems() {
		return subsystems;
	}
	public void setSubsystems(UILookupInfo subsystems) {
		this.subsystems = subsystems;
	}
	public UILookupInfo getUsecases() {
		return usecases;
	}
	public void setUsecases(UILookupInfo usecases) {
		this.usecases = usecases;
	}
	public UILookupInfo getUsecaseActions() {
		return usecaseActions;
	}
	public void setUsecaseActions(UILookupInfo usecaseActions) {
		this.usecaseActions = usecaseActions;
	}
	public Long getUsecaseActionId() {
		return usecaseActionId;
	}
	public void setUsecaseActionId(Long usecaseActionId) {
		this.usecaseActionId = usecaseActionId;
	}
	
	
}
