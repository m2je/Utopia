package ir.utopia.common.systems.system.bean;
	
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

/**
 * Remote interface for CmSystemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CmSystemFacadeRemote extends UtopiaBasicUsecaseBean<CmSystem,CmSystem> {
	
	public CmSystem findByName(Object name, int... rowStartIdxAndCount);

	
	/**
	 * loads the system and all subSystems
	 * @return
	 */
	public CmSystem loadSystem(String systemName);
}