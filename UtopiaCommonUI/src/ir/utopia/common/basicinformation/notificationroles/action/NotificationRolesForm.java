package ir.utopia.common.basicinformation.notificationroles.action;

import ir.utopia.common.basicinformation.notificationroles.persistent.CmNotificationRoles;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.struts.AbstractUtopiaBasicForm;


@PersistedMapForm
@DataInputForm
public class NotificationRolesForm  extends AbstractUtopiaBasicForm<CmNotificationRoles> {
	
	private Long cmNotificationRolesId;
	private Long cmNotification;
	private Long coRole;
	
	@FormId
	@FormPersistentAttribute
	public Long getCmNotificationRolesId() {
		return cmNotificationRolesId;
	}
	public void setCmNotificationRolesId(Long cmNotificationRolesId) {
		this.cmNotificationRolesId = cmNotificationRolesId;
	}
	
	@FormPersistentAttribute(method = "getCmNotification")
	public Long getCmNotification() {
		return cmNotification;
	}
	public void setCmNotification(Long cmNotification) {
		this.cmNotification = cmNotification;
	}
	
	@FormPersistentAttribute(method = "getCoRole")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1,breakLineAfter=true)
	public Long getCoRole() {
		return coRole;
	}
	public void setCoRole(Long coRole) {
		this.coRole = coRole;
	}

}
