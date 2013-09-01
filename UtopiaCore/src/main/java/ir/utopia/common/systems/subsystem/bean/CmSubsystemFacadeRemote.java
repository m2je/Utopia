package ir.utopia.common.systems.subsystem.bean;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.subsystem.persistent.CmVSubsystem;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmSubsystemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CmSubsystemFacadeRemote extends UtopiaBasicUsecaseBean<CmSubsystem,CmVSubsystem> {
	

	
	
	public List<CmSubsystem> findByName(Object name, int... rowStartIdxAndCount);

	/**
	 * find subsystems with subSystemAbbrivation which are childern of system with abbrevation name
	 * systemAbbrevation
	 * @param systemAbbrevation
	 * @param subSystemAbbrivation
	 * @return
	 */
	public CmSubsystem getSubSystemId(String systemAbbrevation,String subSystemAbbrivation);
}