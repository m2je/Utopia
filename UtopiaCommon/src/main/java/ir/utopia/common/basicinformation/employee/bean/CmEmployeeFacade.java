package ir.utopia.common.basicinformation.employee.bean;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ir.utopia.common.basicinformation.employee.persistent.CmEmployee;
import ir.utopia.common.basicinformation.employee.persistent.CmVEmployee;
import ir.utopia.common.basicinformation.fiscalyear.bean.CmFiscalyearFacade;
import ir.utopia.core.bean.AbstractOrganizationSupportFacade;
import javax.ejb.Stateless;
import javax.persistence.Query;


/**
 * Facade for entity CmEmployee.
 * 
 * @author Arsalani 
 */
@Stateless
public class CmEmployeeFacade extends AbstractOrganizationSupportFacade<CmEmployee,CmVEmployee> implements CmEmployeeFacadeRemote {
	
private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CmFiscalyearFacade.class.getName());
	}
	
	public Long findEmployeeId(Long userId,Map<String, Object> context){
		try {
			String q ="select E.cmEmployeeId from CoUser as U , CmPersonBpartner as P , CmEmployee as E where U.cmBpartner.cmBpartnerId = P.cmBpartner.cmBpartnerId and P.cmPersonBpartnerId=E.cmPersonBpartner.cmPersonBpartnerId and U.coUserId=:userId";
			Query query =entityManager.createQuery(q);
			query.setParameter("userId", userId);
			return (Long)query.getSingleResult();
		} catch (Exception e) {
			logger.log(Level.WARNING,"unable to find employee for current user: "+userId.toString(),e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CmEmployee> findByPersonBpartnerForDuplicate(Long personId,Map<String, Object> context){
		try {
			String q ="select model from CmEmployee model where model.cmPersonBpartner.cmPersonBpartnerId=:personId";
			Query query =entityManager.createQuery(q);
			query.setParameter("personId", personId);
			return query.getResultList();
		} catch (Exception e) {
			logger.log(Level.WARNING,"unable to find employee for this personBpartner: "+personId.toString(),e);
		}
		return null;
	}

}
