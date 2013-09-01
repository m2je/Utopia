package ir.utopia.core.security.userlog.action;

import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.IncludedForm;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.security.userlog.persistent.CoUserLog;
import ir.utopia.core.struts.AbstractUtopiaBasicForm;

import java.util.Collection;

@PersistedMapForm
@DataInputForm(includedForms={
		@IncludedForm(formClass=UserLogDetailForm.class,name="userLogs")})
public class UserLogForm extends AbstractUtopiaBasicForm<CoUserLog> {

	private Long userLogId;
	private Long userId;
	private String loginDate;
	private String logOutDate;
	private String clientAddress;
	private String username;
	private String name;
	private Collection<UserLogDetailForm> userLogs;
	
	@FormId
	@FormPersistentAttribute(method="getCoUserLogId")
	public Long getUserLogId() {
		return userLogId;
	}
	public void setUserLogId(Long userLogId) {
		this.userLogId = userLogId;
	}
	@FormPersistentAttribute(method="getCoUserLogId")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@SearchItem(index=0)
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE_TIME)
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	@SearchItem(index=1)
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE_TIME)
	public String getLogOutDate() {
		return logOutDate;
	}
	public void setLogOutDate(String logOutDate) {
		this.logOutDate = logOutDate;
	}
	@SearchItem(index=2)
	@FormPersistentAttribute
	public String getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	@SearchItem(index=3)
	@FormPersistentAttribute
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@SearchItem(index=4)
	@FormPersistentAttribute()
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@FormPersistentAttribute(method="getCoUserLogDtls")
	public Collection<UserLogDetailForm> getUserLogs() {
		return userLogs;
	}
	public void setUserLogs(Collection<UserLogDetailForm> userLogs) {
		this.userLogs = userLogs;
	}
	
	
}
