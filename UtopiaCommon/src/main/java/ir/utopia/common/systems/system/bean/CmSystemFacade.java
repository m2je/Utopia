package ir.utopia.common.systems.system.bean;

import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CmSystem.
 * 
 * @see ir.utopia.common.systems.system.persistent.CmSystem
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CmSystemFacade extends AbstractBasicUsecaseBean<CmSystem,CmSystem> implements CmSystemFacadeRemote {
	// property constants
	
	public static final String NAME = "name";
	public CmSystem findByName(Object name, int... rowStartIdxAndCount) {
		List<CmSystem> result=findByProperty(NAME, name, rowStartIdxAndCount);
		return (result!=null&&result.size()>0)?result.get(0):null;
	}

	@Override
	public CmSystem loadSystem(String systemName) {
		CmSystem system=findByName(systemName, null);
		if(system!=null){
			 system.getCmSubsystems().contains(null);//load all subSystems
		}
		return system;
	}

}