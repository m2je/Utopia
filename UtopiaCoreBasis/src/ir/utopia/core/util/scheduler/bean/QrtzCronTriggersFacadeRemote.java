package ir.utopia.core.util.scheduler.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.util.scheduler.persistent.QrtzCronTriggers;

import javax.ejb.Remote;

/**
 * Remote interface for QrtzCronTriggersFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface QrtzCronTriggersFacadeRemote extends UtopiaBasicUsecaseBean<QrtzCronTriggers, QrtzCronTriggers>{
	
}