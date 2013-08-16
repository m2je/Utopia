package ir.utopia.core.security.userlogdtl.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.userlogdtl.persistent.CoUserLogDtl;
import ir.utopia.core.security.userlogdtl.persistent.CoVUserLogDtl;
import ir.utopia.core.security.userlogdtl.persistent.LogActionStatus;

import javax.ejb.Remote;

@Remote
public interface CoUserLogDtlFacadeRemote extends  UtopiaBasicUsecaseBean<CoUserLogDtl,CoVUserLogDtl > {

	public void logUserAction(Long coUserLogId,Long coUsecaseActionId,String actionName,LogActionStatus status);
}
