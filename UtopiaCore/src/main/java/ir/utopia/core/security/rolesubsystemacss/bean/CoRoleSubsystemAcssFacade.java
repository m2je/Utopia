package ir.utopia.core.security.rolesubsystemacss.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.rolesubsystemacss.persistence.CoRoleSubsystemAcss;
import ir.utopia.core.security.rolesubsystemacss.persistence.CoVRoleSubsystemAcss;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoRoleSubsystemAcss.
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoRoleSubsystemAcssFacade extends AbstractBasicUsecaseBean<CoRoleSubsystemAcss,CoVRoleSubsystemAcss> implements
		CoRoleSubsystemAcssFacadeRemote {
	// property constants
	public static final String CM_SUBSYSTEM_ID = "cmSubsystemId";
	
	public List<CoRoleSubsystemAcss> findByCmSubsystemId(Object cmSubsystemId,
			int... rowStartIdxAndCount) {
		return findByProperty(CM_SUBSYSTEM_ID, cmSubsystemId,
				rowStartIdxAndCount);
	}
	

}