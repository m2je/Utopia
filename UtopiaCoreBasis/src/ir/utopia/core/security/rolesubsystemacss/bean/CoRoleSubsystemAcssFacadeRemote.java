package ir.utopia.core.security.rolesubsystemacss.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.rolesubsystemacss.persistence.CoRoleSubsystemAcss;
import ir.utopia.core.security.rolesubsystemacss.persistence.CoVRoleSubsystemAcss;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoRoleSubsystemAcssFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoRoleSubsystemAcssFacadeRemote extends UtopiaBasicUsecaseBean<CoRoleSubsystemAcss,CoVRoleSubsystemAcss>{


	public List<CoRoleSubsystemAcss> findByCmSubsystemId(Object cmSubsystemId,
			int... rowStartIdxAndCount);
	
}