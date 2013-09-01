package ir.utopia.core.log.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.log.persistent.CoActionLog;

import javax.ejb.Stateless;

/**
 * Facade for entity CoActionLog.
 * 
 * @see test.CoActionLog
 * @author
 */
@Stateless
public class CoActionLogFacade extends AbstractBasicUsecaseBean<CoActionLog,CoActionLog> implements CoActionLogFacadeRemote {

	

}