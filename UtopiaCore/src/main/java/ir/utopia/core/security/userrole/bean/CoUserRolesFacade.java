package ir.utopia.core.security.userrole.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.security.userrole.persistence.CoUserRoles;
import ir.utopia.core.security.userrole.persistence.CoVUserRoles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

/**
 * Facade for entity CoUserRoles.
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoUserRolesFacade extends AbstractBasicUsecaseBean<CoUserRoles,CoVUserRoles> implements CoUserRolesFacadeRemote {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Long> getUserRoles(Long userId) {
		StringBuffer query=new StringBuffer(" select me.coRole.coRoleId from ").append(CoUserRoles.class.getSimpleName()).
		append(" me where me.coUser.coUserId=:coUserId and me.isactive=:isactive and me.coUser.isactive=:isactive and me.coRole.isactive=:isactive");
		List<Long>res=entityManager.createQuery(query.toString()).setParameter("coUserId", userId).setParameter("isactive", Constants.IsActive.active).getResultList();
		return new HashSet<Long>(res);
		
	}
	// property constants
	
	
}