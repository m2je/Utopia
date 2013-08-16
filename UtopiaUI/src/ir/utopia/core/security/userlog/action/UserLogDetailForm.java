package ir.utopia.core.security.userlog.action;

import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.UsecaseForm;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.security.userlogdtl.persistent.CoUserLogDtl;
import ir.utopia.core.security.userlogdtl.persistent.LogActionStatus;
import ir.utopia.core.struts.AbstractUtopiaBasicForm;

@PersistedMapForm
@UsecaseForm
public class UserLogDetailForm extends AbstractUtopiaBasicForm<CoUserLogDtl> {

	private Long userLogId;
	private Long userId;
	private Long userLogDtlId;
	private LogActionStatus status;
	private String systemName;
	private String subsystemName;
	private String usecaseName;
	private String actionName;
	private Long usecaseActionId;
	private String actionDate;
	
	@FormId
	public Long getUserLogId() {
		return userLogId;
	}
	public void setUserLogId(Long userLogId) {
		this.userLogId = userLogId;
	}
	@FormPersistentAttribute(method="getCoUserId")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@FormPersistentAttribute(method="getCoUserLogDtlId")
	public Long getUserLogDtlId() {
		return userLogDtlId;
	}
	public void setUserLogDtlId(Long userLogDtlId) {
		this.userLogDtlId = userLogDtlId;
	}
	@SearchItem(index=4)
	@FormPersistentAttribute(method="getStatus")
	public LogActionStatus getStatus() {
		return status;
	}
	public void setStatus(LogActionStatus status) {
		this.status = status;
	}
	@SearchItem(index=0)
	@FormPersistentAttribute(method="getSystemname")
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	@SearchItem(index=1)
	@FormPersistentAttribute(method="getSubsystemname")
	public String getSubsystemName() {
		return subsystemName;
	}
	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}
	@SearchItem(index=2)
	@FormPersistentAttribute(method="getUsecasename")
	public String getUsecaseName() {
		return usecaseName;
	}
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
	@SearchItem(index=3)
	@FormPersistentAttribute
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	@FormPersistentAttribute(method="getCoUsecaseActionId")
	public Long getUsecaseActionId() {
		return usecaseActionId;
	}
	public void setUsecaseActionId(Long usecaseActionId) {
		this.usecaseActionId = usecaseActionId;
	}
	@SearchItem(index=6)
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE_TIME)
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	
}
