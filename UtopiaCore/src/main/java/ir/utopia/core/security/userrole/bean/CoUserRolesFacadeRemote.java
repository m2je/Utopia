package ir.utopia.core.security.userrole.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.userrole.persistence.CoUserRoles;
import ir.utopia.core.security.userrole.persistence.CoVUserRoles;

import java.util.Set;

import javax.ejb.Remote;

/**
 * Remote interface for CoUserRolesFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoUserRolesFacadeRemote extends UtopiaBasicUsecaseBean<CoUserRoles,CoVUserRoles> {
	
	public Set<Long> getUserRoles(Long userId);
}