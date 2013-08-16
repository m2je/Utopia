/**
 * 
 */
package ir.utopia.security.authentication.bean;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.persistent.CmSystem;
import ir.utopia.core.bean.AbstractUtopiaBean;
import ir.utopia.core.security.persistent.CoVUserAllValidAccess;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Salarkia
 *
 */
@Stateless
public class JAASSecurityFacade extends AbstractUtopiaBean implements JAASSecurityFacadeRemore {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<CoVUserAllValidAccess> loadUserValidAccesses(long coUserId,long coUsecaseId) {
		StringBuffer quesryString=new StringBuffer(" select distinct model from ").
		append(CoVUserAllValidAccess.class.getSimpleName()).append(" model where ");
		quesryString.append(" model.coUserId=:coUserId and model.coUsecaseId=:coUsecaseId");
		Query query = entityManager.createQuery(quesryString.toString());
		resetQueryCache(query);
		query.setParameter("coUserId",coUserId );
		query.setParameter("coUsecaseId",coUsecaseId );
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CmSystem> loadUserAvailableSystems(Long userId){
				StringBuffer quesryString=new StringBuffer(" select distinct model.cmSystemId from ").
				append(CoVUserAllValidAccess.class.getSimpleName()).append(" model where ");
				quesryString.append(" model.coUserId=:coUserId ");
				Query query = entityManager.createQuery(quesryString.toString());
				resetQueryCache(query);
				query.setParameter("coUserId",userId);
				List<Long>resultLong=(List<Long>)query.getResultList();
				if(resultLong!=null&&resultLong.size()>0){
					StringBuffer loadQuery=new StringBuffer("select model from ").append(CmSystem.class.getSimpleName()).append(" model where model.cmSystemId in (");
					for(int i=0;i<resultLong.size();i++){
						if(i>0){
							loadQuery.append(",");
						}
						loadQuery.append(":system"+i);
					}
					loadQuery.append(")");
					query=entityManager.createQuery(loadQuery.toString());
					for(int i=0;i<resultLong.size();i++){
						query.setParameter("system"+i, resultLong.get(i));
					}
					return query.getResultList();
				}
				return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CmSubsystem> loadUserAvailableSubsystems(Long userId){
		StringBuffer quesryString=new StringBuffer(" select distinct model.cmSystemId  from ").
		append(CoVUserAllValidAccess.class.getSimpleName()).append(" model where ");
		quesryString.append(" model.coUserId=:coUserId ");
		Query query = entityManager.createQuery(quesryString.toString());
		resetQueryCache(query);
		query.setParameter("coUserId",userId);
		List<Long>result= query.getResultList();
		if(result!=null&&result.size()>0){
			StringBuffer loadQuery=new StringBuffer("select model from ").append(CmSubsystem.class.getSimpleName()).append(" model where model.CmSubsystemId in (");
			for(int i=0;i<result.size();i++){
				if(i>0){
					loadQuery.append(",");
				}
				loadQuery.append(":cmSubsystemId"+i);
			}
			loadQuery.append(")");
			query=entityManager.createQuery(loadQuery.toString());
			for(int i=0;i<result.size();i++){
				query.setParameter("cmSubsystemId"+i, result.get(i));
			}
			return query.getResultList();
		}
		return null;
}

	
}
