/**
 * 
 */
package ir.utopia.core.scheduler;

import ir.utopia.core.scheduler.model.SchedulerModel;

import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

/**
 * @author Salarkia
 *
 */
@Stateless
public class DefaultScheduledBeanFacade extends AbstractScheduledBeanFacade {

	
	@Timeout
	public void doTimeout(Timer timer) {
		SchedulerModel model=(SchedulerModel) timer.getInfo();
		
	}
}
