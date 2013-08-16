package ir.utopia.security.authentication.bean;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.security.persistent.CoVUserAllValidAccess;

import java.util.List;

import javax.ejb.Remote;
@Remote
public interface JAASSecurityFacadeRemore extends UtopiaBean {
	/**
	 * returns a list of user valid usecase action access 
	 * @param coUserId
	 * @return
	 */
	public List<CoVUserAllValidAccess> loadUserValidAccesses(long coUserId,long coUsecaseId);

	public abstract List<CmSubsystem> loadUserAvailableSubsystems(Long userId);

	public abstract List<CmSystem> loadUserAvailableSystems(Long userId);
}
