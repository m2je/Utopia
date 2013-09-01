package ir.utopia.core.scheduler;

import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.scheduler.model.SchedulerModel;

import java.util.Date;

public interface ScheduledBeanFacadeRemote extends UtopiaBean {

	public void schedule(Date startDate,Long nextInterVal,SchedulerModel model);
}
