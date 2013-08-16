package ir.utopia.common.basicinformation.notificationroles.bean;

import ir.utopia.common.basicinformation.notificationroles.persistent.CmNotificationRoles;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

@Stateless
public class CmNotificationRolesFacade extends AbstractBasicUsecaseBean<CmNotificationRoles,CmNotificationRoles> implements
		CmNotificationRolesFacadeRemote {

}