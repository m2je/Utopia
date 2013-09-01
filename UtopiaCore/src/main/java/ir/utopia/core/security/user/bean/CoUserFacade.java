package ir.utopia.core.security.user.bean;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.user.persistence.CoVUser;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CoUser.
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoUserFacade extends AbstractBasicUsecaseBean<CoUser,CoVUser> implements CoUserFacadeRemote {
	// property constants
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String USER_IMAGE = "userImage";
	public static final String CM_BPARTNER_ID = "cmBpartnerId";
	
	
	public CoUser findByUsername(Object username
			) {
		List<CoUser>result=findByProperty(USERNAME, username, 0);
		return result.size()>=1?result.get(0):null;
	}

	public List<CoUser> findByUserImage(Object userImage,
			int... rowStartIdxAndCount) {
		return findByProperty(USER_IMAGE, userImage, rowStartIdxAndCount);
	}

	public List<CoUser> findByCmBpartnerId(Object cmBpartnerId,
			int... rowStartIdxAndCount) {
		return findByProperty(CM_BPARTNER_ID, cmBpartnerId, rowStartIdxAndCount);
	}
	
	public CoVUser getById(Long id){
		try{
			String q = "select vuser from " + CoVUser.class.getSimpleName() + " vuser where vuser.coUserId = :id" ;
			Query query = entityManager.createQuery(q);
			query.setParameter("id", id);
			return (CoVUser)query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setDefaultOrganization(Long userId,Long organizationId) {
		CmOrganization organization= entityManager.find(CmOrganization.class, organizationId);
		CoUser user= entityManager.find(CoUser.class,userId);
		user.setCmOrganization(organization);
		entityManager.merge(user);
	}

	
	

	
	
	
	
}