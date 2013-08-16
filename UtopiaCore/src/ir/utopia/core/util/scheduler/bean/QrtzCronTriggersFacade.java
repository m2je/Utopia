package ir.utopia.core.util.scheduler.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.util.scheduler.persistent.QrtzCronTriggers;

import javax.ejb.Stateless;

/**
 * Facade for entity QrtzCronTriggers.
 * 
 * @see test.QrtzCronTriggers
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class QrtzCronTriggersFacade extends AbstractBasicUsecaseBean<QrtzCronTriggers, QrtzCronTriggers> implements QrtzCronTriggersFacadeRemote {
	// property constants
	public static final String CRON_EXPRESSION = "cronExpression";
	public static final String TIME_ZONE_ID = "timeZoneId";

	

}