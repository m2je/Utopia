package ir.utopia.common.doctype.security.bean;

import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.common.doctype.security.persistent.CmUserDocstatusAccess;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CmUserDocstatusAccess.
 * 
 * @see pref.CmUserDocstatusAccess
 * @author 
 */
@Stateless
public class CmUserDocstatusAccessFacade extends AbstractBasicUsecaseBean<CmUserDocstatusAccess, CmUserDocstatusAccess> implements CmUserDocstatusAccessFacadeRemote{
	@SuppressWarnings("unchecked")
	@Override
	public List<CmDocStatus> getUserAccessibleDocStatuses(Long userId,
			Map<String, Object> context) {
		Query q= entityManager.createQuery(" SELECT NEW ir.utopia.common.doctype.persistent.CmDocStatus(CmUserDocstatusAccess.cmDocStatus,CmUserDocstatusAccess.cmDocStatus.cmDoctype.coUsecase.coUsecaseId) FROM CmUserDocstatusAccess CmUserDocstatusAccess WHERE CmUserDocstatusAccess.coUser.coUserId=:coUserId AND  CmUserDocstatusAccess.isactive=:active");
		q.setParameter("coUserId", userId);
		q.setParameter("active", Constants.IsActive.active);
		List<CmDocStatus> transitions= q.getResultList();
		q= entityManager.createQuery(" SELECT NEW ir.utopia.common.doctype.persistent.CmDocStatus(CmRoleDocstatusAccess.cmDocStatus,CmRoleDocstatusAccess.cmDocStatus.cmDoctype.coUsecase.coUsecaseId) FROM CmRoleDocstatusAccess CmRoleDocstatusAccess , CoUserRoles  CoUserRoles WHERE CoUserRoles.coUser.coUserId=:coUserId AND  CmRoleDocstatusAccess.isactive=:active AND CoUserRoles.coRole.coRoleId=CmRoleDocstatusAccess.coRole.coRoleId ");
		q.setParameter("coUserId", userId);
		q.setParameter("active", Constants.IsActive.active);
		transitions.addAll(q.getResultList());
		return transitions;
	}

}