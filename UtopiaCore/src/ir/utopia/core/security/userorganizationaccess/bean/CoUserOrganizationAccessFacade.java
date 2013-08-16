package ir.utopia.core.security.userorganizationaccess.bean;

import ir.utopia.core.bean.AbstractUtopiaBean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@Stateless
public class CoUserOrganizationAccessFacade extends AbstractUtopiaBean
		implements CoUserOrganizationAccessFacadeRemote {
	@PersistenceContext
	protected EntityManager entityManager;
	@Override
	public Long[] getUserAccessibleOrganizations(Long userId) {
		Query q=entityManager.createQuery("select  model.id.cmOrganizationId from CoVValidOrgAccss model  where model.id.coUserId=:userId");
		q.setParameter("userId", userId);
		@SuppressWarnings("unchecked")
		List<Long>result= q.getResultList();
		if(result!=null){
			return result.toArray(new Long[result.size()]);
		}else{
			return null;
		}
		
	}

}
