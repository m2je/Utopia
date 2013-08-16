package ir.utopia.core.util.scheduler.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.util.scheduler.persistent.JobGroup;
import ir.utopia.core.util.scheduler.persistent.QrtzJobDetails;
import ir.utopia.core.util.scheduler.persistent.QrtzMailRecepients;
import ir.utopia.core.util.scheduler.persistent.QrtzMailRecepientsId;
import ir.utopia.core.utilities.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity QrtzMailRecepients.
 * 
 * @see 
 * @author 
 */
@Stateless
public class QrtzMailRecepientsFacade extends  AbstractBasicUsecaseBean<QrtzMailRecepients, QrtzMailRecepients> implements QrtzMailRecepientsFacadeRemote {

	@Override
	public void saveMailRecepients(List<Long> partnerIds, String scheduleName,
			String jobName, JobGroup jobGroup,Map<String,Object>context) throws SaveRecordException{
		if(partnerIds==null||partnerIds.size()==0){
			deleteRecepients(scheduleName, jobName, jobGroup, null);
			
		}
		Query q=entityManager.createQuery("select model.id.cmBpartnerId from "+QrtzMailRecepients.class.getSimpleName()+
				" model where  model.id.schedName=:schedName and model.id.jobName=:jobName and model.id.jobGroup=:jobGroup ");
		q.setParameter("schedName", scheduleName);
		q.setParameter("jobName", jobName);
		q.setParameter("jobGroup", jobGroup);
		super.resetQueryCache(q);
		List<Long>currentRecepients= q.getResultList();
		ArrayList<Long>temp=new ArrayList<Long>(currentRecepients);
		if(currentRecepients!=null&&currentRecepients.size()>0){
			currentRecepients.removeAll(partnerIds);//Removed items
			if(currentRecepients.size()>0){
				Long[] deletingArray= currentRecepients.toArray(new Long[currentRecepients.size()]);
				List<Object[]>delArr=ArrayUtils.breakArray(deletingArray, 1000);
				deleteRecepients(scheduleName, jobName, jobGroup, delArr);
			}
			partnerIds.removeAll(temp);//new Items
		}
		saveNewRecors(partnerIds, scheduleName, jobName, jobGroup, context);
	}
//*****************************************************************************************************
	private void saveNewRecors(List<Long> partnerIds, String scheduleName,
			String jobName, JobGroup jobGroup,Map<String,Object>context)throws SaveRecordException{
		if(partnerIds.size()>0){
			Query q=entityManager.createQuery("select model from "+QrtzJobDetails.class.getSimpleName()+
			" model where  model.id.schedName=:schedName and model.id.jobName=:jobName and model.id.jobGroup=:jobGroup ");
			q.setParameter("schedName", scheduleName);
			q.setParameter("jobName", jobName);
			q.setParameter("jobGroup", jobGroup);
			super.resetQueryCache(q);
			List<QrtzJobDetails>jobDetails= q.getResultList();
			if(jobDetails!=null&&jobDetails.size()>0){
				QrtzJobDetails jobDetail=jobDetails.get(0);
				for(Long recordId:partnerIds){
					QrtzMailRecepientsId id=new QrtzMailRecepientsId(scheduleName, jobName, jobGroup, recordId);
					QrtzMailRecepients current=new QrtzMailRecepients(id, jobDetail);
					entityManager.persist(current);
				}
				entityManager.flush();
			}else{
				throw new SaveRecordException("unable to find quartz job detail with schedName="+scheduleName +" jobName="+jobName +" and jobGroup="+jobGroup);
			}
			
		}
		
	}
//*****************************************************************************************************
	private void deleteRecepients(String scheduleName,
			String jobName, JobGroup jobGroup,List<Object[]>delArr){
		StringBuffer deleteQuery=new StringBuffer("delete ").append(QrtzMailRecepients.class.getSimpleName())
		.append(" model where model.id.schedName=:schedName and model.id.jobName=:jobName and model.id.jobGroup=:jobGroup");
		if(delArr!=null){
			deleteQuery.append(" and model.id.cmBpartnerId in(");
			int arraySize=delArr.get(0).length;
			for(int index=0;index<arraySize;index++){
				if(index>0){
					deleteQuery.append(",");
				}
				deleteQuery.append(":recordId_"+index);
			}
			deleteQuery.append(")");
		}
		
		Query q= entityManager.createQuery(deleteQuery.toString());
		for(int i=0;i<1 ||(delArr!=null&& i<delArr.size());i++){
			q.setParameter("schedName", scheduleName);
			q.setParameter("jobName", jobName);
			q.setParameter("jobGroup", jobGroup);
			if(delArr!=null){
				for(Object recordId:delArr.get(i)){
					q.setParameter("recordId_"+i, recordId);
				}
				if(i>0&&delArr.get(i).length<delArr.get(0).length){
					//if array size was less than others set the remaining parameters with -1
					for(int k=delArr.get(i).length;k<delArr.get(0).length;k++){
						q.setParameter("recordId_"+i, -1l);
					}
				}
			}
			q.executeUpdate();
		}
		
		return;
	}
//*****************************************************************************************************
	@SuppressWarnings("unchecked")
	@Override
	public List<QrtzMailRecepients> findScheduledTaskRecepients(String scheduleName,
			String jobName, JobGroup jobGroup, Map<String, Object> context) {
		Query q=entityManager.createQuery("select model from "+QrtzMailRecepients.class.getSimpleName()+
		" model join fetch model.cmBpartner where  model.id.schedName=:schedName and model.id.jobName=:jobName and model.id.jobGroup=:jobGroup ");
		q.setParameter("schedName", scheduleName);
		q.setParameter("jobName", jobName);
		q.setParameter("jobGroup", jobGroup);
		super.resetQueryCache(q);
		return q.getResultList();
	}
	
}