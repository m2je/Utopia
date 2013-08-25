package ir.utopia.core.log.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.log.persistent.CoActionLogDtl;

import javax.ejb.Remote;

/**
 * Remote interface for CoActionLogDtlFacade.
 * 
 * @author 
 */
@Remote
public interface CoActionLogDtlFacadeRemote extends UtopiaBasicUsecaseBean<CoActionLogDtl, CoActionLogDtl>{
	
}