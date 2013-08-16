package ir.utopia.core.security.userlogdtl.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.userlog.persistent.CoUserLog;
import ir.utopia.core.security.userlogdtl.persistent.CoUserLogDtl;
import ir.utopia.core.security.userlogdtl.persistent.CoVUserLogDtl;
import ir.utopia.core.security.userlogdtl.persistent.LogActionStatus;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.FlushModeType;

@Stateless
public class CoUserLogDtlFacade extends AbstractBasicUsecaseBean<CoUserLogDtl,CoVUserLogDtl> implements
		CoUserLogDtlFacadeRemote {

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void logUserAction(Long coUserLogId, Long coUsecaseActionId,String actionName,LogActionStatus status) {
		CoUserLog log=entityManager.find(CoUserLog.class, coUserLogId);
		if(log!=null){
			CoUserLogDtl dtl=new CoUserLogDtl();
			dtl.setCoUserLog(log);
			if(coUsecaseActionId!=null&&coUsecaseActionId>0){
				CoUsecaseAction action=entityManager.find(CoUsecaseAction.class, coUsecaseActionId);
				dtl.setCoUsecaseAction(action);
			}
			dtl.setStatus(status);
			dtl.setActionName(actionName);
			dtl.setActionDate(new Date());
			entityManager.setFlushMode(FlushModeType.COMMIT);
			entityManager.persist(dtl);
		}
	}

	

}
