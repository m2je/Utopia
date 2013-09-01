package ir.utopia.common.basicinformation.notificationroles.bean;

import ir.utopia.common.basicinformation.notificationroles.persistent.CmNotificationRoles;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

@Remote
public interface CmNotificationRolesFacadeRemote extends UtopiaBasicUsecaseBean<CmNotificationRoles,CmNotificationRoles>{

}