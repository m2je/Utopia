package ir.utopia.core.log.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.log.persistent.CoActionLog;

import javax.ejb.Remote;

/**
 * Remote interface for CoActionLogFacade.
 * 
 * @author 
 */
@Remote
public interface CoActionLogFacadeRemote extends UtopiaBasicUsecaseBean<CoActionLog, CoActionLog> {
	
}