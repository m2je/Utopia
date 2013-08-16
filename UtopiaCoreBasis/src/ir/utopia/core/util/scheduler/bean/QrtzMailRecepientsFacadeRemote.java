package ir.utopia.core.util.scheduler.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.util.scheduler.persistent.JobGroup;
import ir.utopia.core.util.scheduler.persistent.QrtzMailRecepients;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Remote interface for QrtzMailRecepientsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface QrtzMailRecepientsFacadeRemote extends UtopiaBasicUsecaseBean<QrtzMailRecepients, QrtzMailRecepients> {
	
	public void saveMailRecepients(List <Long>partnerIds,String scheduleName, String jobName, JobGroup jobGroup,Map<String,Object>context) throws SaveRecordException;
	
	public List<QrtzMailRecepients> findScheduledTaskRecepients(String scheduleName, String jobName, JobGroup jobGroup,Map<String,Object>context);
}