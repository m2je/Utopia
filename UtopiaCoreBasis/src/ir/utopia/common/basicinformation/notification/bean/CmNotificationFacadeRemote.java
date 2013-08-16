package ir.utopia.common.basicinformation.notification.bean;

import ir.utopia.common.basicinformation.notification.persistent.CmNotification;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

@Remote
public interface CmNotificationFacadeRemote extends UtopiaBasicUsecaseBean<CmNotification,CmNotification>{

}