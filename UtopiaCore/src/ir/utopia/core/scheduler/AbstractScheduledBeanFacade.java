package ir.utopia.core.scheduler;

import ir.utopia.core.bean.AbstractUtopiaBean;
import ir.utopia.core.scheduler.model.SchedulerModel;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.TimerService;

public abstract class AbstractScheduledBeanFacade extends AbstractUtopiaBean  implements ScheduledBeanFacadeRemote{

	@Resource TimerService timerService;
	
	@Override
	public void schedule(Date startDate, Long nextInterVal,
			SchedulerModel model) {
		timerService.createTimer(startDate,nextInterVal, model);
	}
	
	
}
