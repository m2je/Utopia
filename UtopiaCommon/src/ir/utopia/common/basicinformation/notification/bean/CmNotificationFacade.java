package ir.utopia.common.basicinformation.notification.bean;

import ir.utopia.common.basicinformation.notification.persistent.CmNotification;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

@Stateless
public class CmNotificationFacade extends AbstractBasicUsecaseBean<CmNotification,CmNotification> implements CmNotificationFacadeRemote {

}