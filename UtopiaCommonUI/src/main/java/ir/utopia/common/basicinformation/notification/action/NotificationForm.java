package ir.utopia.common.basicinformation.notification.action;

import java.util.Collection;
import ir.utopia.common.basicinformation.notification.persistent.CmNotification;
import ir.utopia.common.basicinformation.notificationroles.action.NotificationRolesForm;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.IncludedForm;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.struts.AbstractUtopiaForm;


@PersistedMapForm
@DataInputForm(includedForms={@IncludedForm(formClass=NotificationRolesForm.class,name="cmNotificationRoleses",preferedWidth=350)})
public class NotificationForm  extends AbstractUtopiaForm<CmNotification> {
	
	private Long cmNotificationId;
	private String startDate;
	private String endDate;
	private String notification;
	private Collection<NotificationRolesForm> cmNotificationRoleses;
	
	@FormId
	@FormPersistentAttribute
	public Long getCmNotificationId() {
		return cmNotificationId;
	}
	public void setCmNotificationId(Long cmNotificationId) {
		this.cmNotificationId = cmNotificationId;
	}
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=1)
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=true,index=1,breakLineAfter=false,displayType=DisplayTypes.Date,defaultValue="@currentDate@")
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=2)
	@FormPersistentAttribute(formToEntityMap=FormToEntityDataTypeMap.STRING_TO_DATE)
	@InputItem(isManadatory=true,index=2,breakLineAfter=true,displayType=DisplayTypes.Date,defaultValue="@currentDate@")
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.BOTH,index=3)
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=3,breakLineAfter=true,displayType=DisplayTypes.LargeString)
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	
	@FormPersistentAttribute(method="getCmNotificationRoleses")
	public Collection<NotificationRolesForm> getCmNotificationRoleses() {
		return cmNotificationRoleses;
	}
	public void setCmNotificationRoleses(Collection<NotificationRolesForm> cmNotificationRoleses) {
		this.cmNotificationRoleses = cmNotificationRoleses;
	}
}
