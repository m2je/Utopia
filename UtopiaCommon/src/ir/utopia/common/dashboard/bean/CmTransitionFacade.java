package ir.utopia.common.dashboard.bean;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil;
import ir.utopia.common.basicinformation.fiscalyear.persistent.FiscalYearSupport;
import ir.utopia.common.dashboard.persistent.CmTransition;
import ir.utopia.common.dashboard.persistent.CmTransitionHistory;
import ir.utopia.common.dashboard.persistent.TransitionRecord;
import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.common.doctype.persistent.CmDoctype;
import ir.utopia.common.doctype.security.persistent.CmRoleDocstatusAccess;
import ir.utopia.common.doctype.security.persistent.CmUserDocstatusAccess;
import ir.utopia.common.doctype.util.DocTypeUtil;
import ir.utopia.common.doctype.util.UsecaseDocInfo;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.bean.OrderBy;
import ir.utopia.core.bean.OrganizationSupportFacade;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.constants.QueryComparsionType;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.util.EnumUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
@Stateless
public class CmTransitionFacade extends AbstractBasicUsecaseBean<CmTransition, CmTransition> implements CmTransitionFacadeRemote {
	private static final Logger logger=Logger.getLogger(CmTransitionFacade.class.getName());
	
	@EJB
	CoUserFacadeRemote userfacade;
	
	private Map<Class<?>, String>fiscalYearColumnMap=new HashMap<Class<?>, String>();
	@SuppressWarnings("unchecked")
	public List<CmTransition> getAccessibleTransitions(Long userId,Long fiscalYearId){
		
		 Query q=	entityManager.createQuery("SELECT CmTransition from CmTransition CmTransition ,CoVUserAllValidAccess AllValidAccess" +
					" WHERE AllValidAccess.coUsecaseActionId=CmTransition.coUsecaseAction.coUsecaseActionId AND AllValidAccess.coUserId=:userId  " +
					"AND CmTransition.cmDocStatusByCmDocStatusTo.cmFiscalyear.cmFiscalyearId=:cmFiscalyearId AND  CmTransition.cmDocStatusByCmDocStatusFrom.cmFiscalyear.cmFiscalyearId=:cmFiscalyearId ");
		 q.setParameter("userId", userId);
		 q.setParameter("cmFiscalyearId", fiscalYearId);
		 return q.getResultList();
	}
//*************************************************************************************************************************************************
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<TransitionRecord> getTransitionRecords(Long userId,
			Long usecaseId,Long fiscalYearId,Long docStatusFromId,OrderBy []orderBy,int... rowStartIdxAndCount) { 
		
		try {
				UseCase usecase= UsecaseUtil.getUsecase(usecaseId);
				UtopiaBasicUsecaseBean<?, ?>facade=(UtopiaBasicUsecaseBean<?, ?>) ServiceFactory.lookupFacade( usecase.getRemoteClassName());
				Class<?>persitentClass= facade.getPersistentClass();
				HashMap<String, Object> queryParams=new HashMap<String, Object>() ;
				String persistenObjectName=persitentClass.getSimpleName();
				StringBuffer usecaseQuery=getSearchQuery(userId, usecaseId, fiscalYearId, docStatusFromId,facade,queryParams,false);
				if(usecaseQuery!=null){
					usecaseQuery.append("  ORDER BY ");
					if(orderBy!=null&&orderBy.length>0){
						int index=0;
						for(OrderBy corderby:orderBy){
							if(index>0){
								usecaseQuery.append(",");
							}
							usecaseQuery.append(corderby.getEntityClass().getSimpleName()).append(".").append(corderby.getPropertyName()).append(" ").append(corderby.getDirection().toString());
							index++;
						}
						
					}else{
						usecaseQuery.append(persistenObjectName).append(".updated DESC  ");
					}
					
					List<? extends UtopiaPersistent>searchResult=(List<? extends UtopiaPersistent>) facade.search(usecaseQuery.toString(),queryParams,  rowStartIdxAndCount);
					if(searchResult!=null&&searchResult.size()>0){
						return getChronologicalChanges(userId, usecaseId, usecase,
							searchResult);
					}
				}
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		
		return null;
	}
//*************************************************************************************************************************************************	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected StringBuffer getSearchQuery(Long userId,
			Long usecaseId,Long fiscalYearId,Long docStatusFromId,UtopiaBasicUsecaseBean<?, ?>facade,HashMap<String, Object> queryParams,boolean count){
		UsecaseDocInfo docInfo= DocTypeUtil.getUsecaseDocStatusInfo(usecaseId,fiscalYearId);
				if(docInfo!=null){
				Set<Integer>accessibleDocStatuses= getAccessibleDocStatuses(userId, usecaseId,
						docStatusFromId,fiscalYearId);
					if(accessibleDocStatuses.size()>0){
						queryParams.put("isActive", Constants.IsActive.active);
						Class<?>persitentClass= facade.getPersistentClass();
						String persistenObjectName=persitentClass.getSimpleName();
						StringBuffer usecaseQuery=new StringBuffer("SELECT ");
						if(count){
							usecaseQuery.append(" COUNT(").append(persistenObjectName).append(") ");
						}else{
							usecaseQuery.append(persistenObjectName);
						}
						usecaseQuery.append(" FROM  ").append(persistenObjectName).append(" ").
								append(persistenObjectName).append(" WHERE ").append(persistenObjectName).append(".isactive=:isActive AND (");
						int index=0;
						for(int i:accessibleDocStatuses){
							if(index>0){
								usecaseQuery.append(" OR ");
							}
							usecaseQuery.append(persistenObjectName).append(".").append(docInfo.getColumnName()).append("=:status").append(index);
							queryParams.put("status"+index, EnumUtil.getEnumValue((Class<? extends Enum>)docInfo.getEnumClass(), i));
							index++;
						}
						usecaseQuery.append(" ) ");
						if(OrganizationSupportFacade.class.isInstance( facade)){
							Condition condition=((OrganizationSupportFacade)facade).getOrganizationCondition(persitentClass, null);
							if(condition!=null&&condition.toString().length()>0){
								usecaseQuery.append(" AND (").append( condition.toString()).append(" ) ");
								for(String conditionName:condition.getParameters()){
									queryParams.put(conditionName,condition.getParameterValue(conditionName));
								}
							}
						}else {
							String fiscalColumnName= getFiscalYearColumnName(facade.getPersistentClass());
							if(fiscalColumnName!=null){
								usecaseQuery.append(" AND ").append(persistenObjectName).append(".").append(fiscalColumnName).append("=:__fiscalYear");
								Map<String,Object>context=ContextHolder.getContext().getContextMap();
								queryParams.put("__fiscalYear", FiscalYearUtil.getFiscalYearId(context));
							}
						}
						return usecaseQuery;
					}
					
				}
				return null;
		}
//*************************************************************************************************************************************************	
	@SuppressWarnings("unchecked")
	private Set<Integer> getAccessibleDocStatuses(Long userId, Long usecaseId,
			Long docStatusFromId,Long fiscalYearId) {
		StringBuffer fromStatusQuery=new StringBuffer("SELECT DISTINCT CmTransition.cmDocStatusByCmDocStatusFrom.status FROM CmTransition CmTransition , CoVUserAllValidAccess ");
		fromStatusQuery.append("AllValidAccess WHERE AllValidAccess.coUsecaseActionId=CmTransition.coUsecaseAction.coUsecaseActionId AND AllValidAccess.coUserId=:userId AND AllValidAccess.coUsecaseId=:coUsecaseId ");
		StringBuffer toStatusQuery=new StringBuffer(" SELECT DISTINCT CmTransition.cmDocStatusByCmDocStatusTo.status FROM CmTransition CmTransition , CoVUserAllValidAccess ");
		toStatusQuery.append("AllValidAccess WHERE AllValidAccess.coUsecaseActionId=CmTransition.coUsecaseAction.coUsecaseActionId AND AllValidAccess.coUserId=:userId AND AllValidAccess.coUsecaseId=:coUsecaseId ");
		toStatusQuery.append(" AND  NOT EXISTS(SELECT CmTransition2.cmDocStatusByCmDocStatusFrom FROM CmTransition CmTransition2 WHERE CmTransition2.cmDocStatusByCmDocStatusFrom.cmDocStatusId=CmTransition.cmDocStatusByCmDocStatusTo.cmDocStatusId ) ");
		
		
		if(docStatusFromId!=null){
			fromStatusQuery.append(" AND CmTransition.cmDocStatusByCmDocStatusFrom.cmDocStatusId=:cmDocStatusId ");
			toStatusQuery.append( " AND CmTransition.cmDocStatusByCmDocStatusTo.cmDocStatusId=:cmDocStatusId ");
		}else{
			fromStatusQuery.append(" AND CmTransition.cmDocStatusByCmDocStatusFrom.cmFiscalyear.cmFiscalyearId=:fiscalYearId ");
			toStatusQuery.append(" AND CmTransition.cmDocStatusByCmDocStatusTo.cmFiscalyear.cmFiscalyearId=:fiscalYearId ");
		}
		 Query q=	entityManager.createQuery( fromStatusQuery.toString());
					
		q.setParameter("userId", userId);
		q.setParameter("coUsecaseId", usecaseId);
		if(docStatusFromId!=null){
			q.setParameter("cmDocStatusId", docStatusFromId);
		}else{
			q.setParameter("fiscalYearId", fiscalYearId);
		}
		HashSet<Integer>result=new HashSet<Integer>();
		result.addAll(q.getResultList());
		
		q=	entityManager.createQuery( toStatusQuery.toString());
		q.setParameter("userId", userId);
		q.setParameter("coUsecaseId", usecaseId);
		if(docStatusFromId!=null){	
			q.setParameter("cmDocStatusId", docStatusFromId);
		}else{
			q.setParameter("fiscalYearId", fiscalYearId);
		}	
		result.addAll(q.getResultList());
		StringBuffer userDocStatusQuery=new StringBuffer("SELECT CmUserDocstatusAccess.cmDocStatus.status FROM CmUserDocstatusAccess CmUserDocstatusAccess WHERE CmUserDocstatusAccess.coUser.coUserId=:coUserId AND  CmUserDocstatusAccess.isactive=:active AND CmUserDocstatusAccess.cmDocStatus.cmDoctype.coUsecase.coUsecaseId=:coUsecaseId ");
		StringBuffer roleDocStatusQuery=new StringBuffer("SELECT CmRoleDocstatusAccess.cmDocStatus.status FROM CmRoleDocstatusAccess CmRoleDocstatusAccess ,CoUserRoles CoUserRoles ");
		roleDocStatusQuery.append("WHERE ").append(" CoUserRoles.coUser.coUserId=:coUserId AND CmRoleDocstatusAccess.coRole.coRoleId=CoUserRoles.coRole.coRoleId AND  CmRoleDocstatusAccess.isactive=:active AND CmRoleDocstatusAccess.cmDocStatus.cmDoctype.coUsecase.coUsecaseId=:coUsecaseId  ");
		
		if(docStatusFromId!=null){
			userDocStatusQuery.append(" AND CmUserDocstatusAccess.cmDocStatus.cmDocStatusId=:cmDocStatusId  ");
			roleDocStatusQuery.append(" AND CmRoleDocstatusAccess.cmDocStatus.cmDocStatusId=:cmDocStatusId  ");
		}else{
			userDocStatusQuery.append(" AND CmUserDocstatusAccess.cmDocStatus.cmFiscalyear.cmFiscalyearId=:fiscalYearId");
			roleDocStatusQuery.append(" AND CmRoleDocstatusAccess.cmDocStatus.cmFiscalyear.cmFiscalyearId=:fiscalYearId");
		}
		q=entityManager.createQuery(userDocStatusQuery.toString());
		q.setParameter("coUserId", userId);
		q.setParameter("active", Constants.IsActive.active);
		q.setParameter("coUsecaseId", usecaseId);
		if(docStatusFromId!=null){
			q.setParameter("cmDocStatusId", docStatusFromId);
		}else{
			q.setParameter("fiscalYearId", fiscalYearId);
		}
		result.addAll(q.getResultList());
		
		q=entityManager.createQuery(roleDocStatusQuery.toString());
		q.setParameter("coUserId", userId);
		q.setParameter("active", Constants.IsActive.active);
		q.setParameter("coUsecaseId", usecaseId);
		if(docStatusFromId!=null){
			q.setParameter("cmDocStatusId", docStatusFromId);
		}else{
			q.setParameter("fiscalYearId", fiscalYearId);
		}
		result.addAll(q.getResultList());
		
		return result;
		
	}
//*************************************************************************************************************************************************
	@SuppressWarnings("unchecked")
	private List<TransitionRecord> getChronologicalChanges(Long userId, Long usecaseId,
			UseCase usecase,
			List<? extends UtopiaPersistent> searchResult) {
		int index;
		UsecaseAction viewAction= usecase.getUsecaseAction(predefindedActions.view.name());
		UsecaseAction lockAction= usecase.getUsecaseAction(predefindedActions.lock.name());
		UsecaseAction unLockAction= usecase.getUsecaseAction(predefindedActions.unlock.name());
		if(viewAction==null){
			logger.log(Level.WARNING,"unable to find action \"view\" for usecase "+usecase.getName()+" so all the record will be shown as unread always");
		}
		List<TransitionRecord> result=new ArrayList<TransitionRecord>(searchResult.size());
		List<CmTransitionHistory>historyList=null;
		StringBuffer modifiers=new StringBuffer();
		StringBuffer historyQuery=new StringBuffer("SELECT MODEL FROM CmTransitionHistory MODEL WHERE MODEL.recordId IN (");
		index=0;
		List<Long>recordIds=new ArrayList<Long>();
		for(UtopiaPersistent persistent :searchResult){
			if(index>0){
				historyQuery.append(",");
				modifiers.append(",");
			}
			recordIds.add(persistent.getRecordId());
			historyQuery.append(":__recordId"+index);
			result.add(new TransitionRecord(persistent));
			modifiers.append(persistent.getUpdatedby());
			index++;
		}
		Map<Long,String>modifierMap=new HashMap<Long, String>();
		if(modifiers.length()>0){
			List<CoUser>users= userfacade.findByProperties(new String[]{"coUserId"}, new String[]{modifiers.toString()}, false, QueryComparsionType.in,  null);
			for(CoUser user:users){
				CmBpartner bparner= user.getCmBpartner();
				modifierMap.put(user.getCoUserId(),bparner.getName()+" "+bparner.getSecoundName() );
			}
		}
		if(viewAction!=null||lockAction!=null){	
				historyQuery.append(" ) AND MODEL.coUsecase.coUsecaseId =:coUsecaseId  AND (");
				if(viewAction!=null){
					historyQuery.append("  (MODEL.coUser.coUserId=:userId AND MODEL.coUsecaseAction.coUsecaseActionId=:viewActionId) ");
					if(lockAction!=null){
						historyQuery.append(" OR MODEL.coUsecaseAction.coUsecaseActionId=:lockActionId");
					}
					if(unLockAction!=null){
						historyQuery.append(" OR MODEL.coUsecaseAction.coUsecaseActionId=:unlockActionId");
					}
				}else{
					historyQuery.append("MODEL.coUsecaseAction.coUsecaseActionId=:lockActionId");
					if(unLockAction!=null){
						historyQuery.append(" OR MODEL.coUsecaseAction.coUsecaseActionId=:unlockActionId");
					}
				}
				historyQuery.append(") ORDER BY MODEL.actionDate DESC ");
				Query historyQ=entityManager.createQuery(historyQuery.toString());
				index=0;
				for(Long recordId:recordIds){
					historyQ.setParameter("__recordId"+index, recordId);
					index++;
				}
				if(viewAction!=null){
					historyQ.setParameter("viewActionId", viewAction.getActionId());
					historyQ.setParameter("userId", userId);
				}
				if(lockAction!=null){
					historyQ.setParameter("lockActionId", lockAction.getActionId());
				}
				if(unLockAction!=null){
					historyQ.setParameter("unlockActionId", unLockAction.getActionId());
				}
				historyQ.setParameter("coUsecaseId", usecaseId);
				
				historyList= historyQ.getResultList();
				

			}
		
			for(TransitionRecord record :result){
				UtopiaPersistent persistent=(UtopiaPersistent) record.getRecord();
				Boolean read=null;
				Boolean locked=null;
				if(historyList!=null){
					for(CmTransitionHistory history:historyList){
						if(history.getRecordId().equals(persistent.getRecordId())){
							Long currentAction=history.getCoUsecaseAction().getCoUsecaseActionId();
							if(read==null&&currentAction.equals(viewAction.getActionId())&&history.getCoUser().getCoUserId().equals(userId) ){
								read=true;
							}
							if(locked==null&&lockAction!=null&&currentAction.equals(lockAction.getActionId())){
								locked=true;
							}else if(locked==null&&unLockAction!=null&&currentAction.equals(unLockAction.getActionId())){
								locked=false;
							}
							
						}
					}
			}
				record.setViewed(read!=null&&read);
				record.setLocked(locked!=null&&locked);
				if(modifierMap.containsKey(persistent.getUpdatedby())){
					record.setLastModifiedBy(modifierMap.get(persistent.getUpdatedby()));
				}
		}
		
		return result;
	}
//*************************************************************************************************************************************************
	public Map<Integer,Set<Integer>>getUserAccesisbleStatusChanges(Long userId,Long usecaseId){
		 Query q=	entityManager.createQuery("SELECT DISTINCT CmTransition.cmDocStatusByCmDocStatusFrom.status,CmTransition.cmDocStatusByCmDocStatusTo.status FROM CmTransition CmTransition , CoVUserAllValidAccess " +
					"AllValidAccess WHERE AllValidAccess.coUsecaseActionId=CmTransition.coUsecaseAction.coUsecaseActionId AND AllValidAccess.coUserId=:userId AND AllValidAccess.coUsecaseId=:coUsecaseId "
				 );
		q.setParameter("userId", userId);
		q.setParameter("coUsecaseId", usecaseId);
		Map<Integer,Set<Integer>>result=new HashMap<Integer, Set<Integer>>();
		@SuppressWarnings("unchecked")
		List<Integer[]>queryResult= q.getResultList();
		for(Integer[] row:queryResult){
			Set<Integer> avaiableStatus;
			if(result.containsKey(row[0])){
				avaiableStatus=result.get(row[0]);
			}else{
				avaiableStatus=new HashSet<Integer>();
				result.put(row[0], avaiableStatus);
			}
			avaiableStatus.add(row[1]);
		}
		return result;
	}
//*************************************************************************************************************************************************
	@SuppressWarnings("unchecked")
	public List<CmDocStatus> getUsecaseDocStatues(Long usecaseId,Long fiscalYearId){
		StringBuffer query=new StringBuffer("select DISTINCT CmDocStatus from CmDocStatus CmDocStatus , CmTransition CmTransition where (CmDocStatus.cmDocStatusId = CmTransition.cmDocStatusByCmDocStatusTo.cmDocStatusId ");
		query.append(" OR CmDocStatus.cmDocStatusId = CmTransition.cmDocStatusByCmDocStatusFrom.cmDocStatusId ) AND CmTransition.coUsecaseAction.coUsecase.coUsecaseId=:coUsecaseId AND CmDocStatus.cmFiscalyear.cmFiscalyearId=:cmFiscalyearId ");
		Query q=entityManager.createQuery(query.toString());
		q.setParameter("coUsecaseId", usecaseId);
		q.setParameter("cmFiscalyearId", fiscalYearId);
		return q.getResultList();
	}
//*************************************************************************************************************************************************
	protected String getFiscalYearColumnName(Class<?>p){
		if(fiscalYearColumnMap.containsKey(p)){
			return fiscalYearColumnMap.get(p);
		}else{
			FiscalYearSupport fiscalYearSupport=p.getAnnotation(FiscalYearSupport.class);
			if(fiscalYearSupport!=null){
				fiscalYearColumnMap.put(p, fiscalYearSupport.fiscalYearField());
				return fiscalYearSupport.fiscalYearField();
			}else{
				fiscalYearColumnMap.put(p, null);
				return null;
			}
		}
	}
//*************************************************************************************************************************************************
	@SuppressWarnings("unchecked")
	public Set<Long> getDashboardAccessibleUsecases(Long userId,Long fiscalYearId){
		List<Long>avaiableUsecases=entityManager.createNamedQuery(CmDoctype.GET_USER_ACCESSIBLE_USECASES).setParameter("coUserId", userId).setParameter("cmFiscalyearId", fiscalYearId).getResultList();
		List<Long>roleAccessibleDocTypes= entityManager.createNamedQuery(CmRoleDocstatusAccess.GET_ACCESSIBLE_USECASES).setParameter("userId", userId).
		setParameter("isactive", IsActive.active).setParameter("cmFiscalyearId", fiscalYearId).getResultList();
		List<Long>userDocStatus=entityManager.createNamedQuery(CmUserDocstatusAccess.USER_VALID_DOCSTATUS_ACCESS).setParameter("coUserId", userId).
				setParameter("isactive", IsActive.active).setParameter("cmFiscalyearId", fiscalYearId).getResultList();
		Set<Long>result=new HashSet<Long>();
		result.addAll(avaiableUsecases);
		result.addAll(roleAccessibleDocTypes);
		result.addAll(userDocStatus);
		return result;
	}
//*************************************************************************************************************************************************
	@Override
	public int getTranistionalRecordCount(Long userId, Long usecaseId,
			Long fiscalYearId, Long docStausFromId) {
		try{
		UseCase usecase= UsecaseUtil.getUsecase(usecaseId);
		UtopiaBasicUsecaseBean<?, ?>facade=(UtopiaBasicUsecaseBean<?, ?>) ServiceFactory.lookupFacade( usecase.getRemoteClassName());
		HashMap<String, Object> queryParams=new HashMap<String, Object>() ;
		StringBuffer usecaseQuery=getSearchQuery(userId, usecaseId, fiscalYearId, docStausFromId,facade,queryParams,true);
		if(usecaseQuery!=null){
			return  facade.getResultCount(usecaseQuery.toString(),queryParams);
		}
		}catch(Exception e){
			logger.log(Level.WARNING,"",e);
		}
		
		return 0;
	}
//*************************************************************************************************************************************************
	@Override
	public void markAs(Long userId, Long usecaseId, Long recordId,
			boolean read) {
		
		UseCase usecase= UsecaseUtil.getUsecase(usecaseId);
		
		UsecaseAction viewAction= usecase.getUsecaseAction(predefindedActions.view.name());
		if(viewAction!=null){
			if(read){
				Query q=entityManager.createQuery("SELECT count(CmTransitionHistory) FROM CmTransitionHistory CmTransitionHistory WHERE " +
						"CmTransitionHistory.coUsecase.coUsecaseId=:coUsecaseId AND CmTransitionHistory.coUsecaseAction.coUsecaseActionId=:coUsecaseActionId"+
						" AND CmTransitionHistory.coUser.coUserId=:coUserId AND CmTransitionHistory.recordId=:recordId AND CmTransitionHistory.isactive=:isActive");
				q.setParameter("isActive", Constants.IsActive.active);
				q.setParameter("coUserId", userId);
				q.setParameter("coUsecaseId", usecase.getUsecaseId());
				q.setParameter("coUsecaseActionId", viewAction.getActionId());
				q.setParameter("recordId", recordId);
				Long count=(Long) q.getSingleResult();
				if(count!=null&&count.intValue()==0){
					CmTransitionHistory history=new CmTransitionHistory();
					history.setCoUsecase(entityManager.find(CoUsecase.class, usecaseId));
					history.setCoUsecaseAction(entityManager.find(CoUsecaseAction.class, viewAction.getActionId()));
					history.setCoUser(entityManager.find(CoUser.class, userId));
					history.setRecordId(recordId);
					history.setActionDate(new Date());
					entityManager.persist(history);
				}
			}else{
				Query q= entityManager.createQuery("UPDATE CmTransitionHistory CmTransitionHistory SET CmTransitionHistory.isactive=:isActive WHERE "+
			"CmTransitionHistory.coUser.coUserId=:coUserId AND CmTransitionHistory.coUsecaseAction.coUsecaseActiondId=:viewActionId " +
			" AND CmTransitionHistory.coUsecase.coUsecaseId=:coUsecaseId AND CmTransitionHistory.recordId=:recordId");
				q.setParameter("isActive", Constants.IsActive.disActive);
				q.setParameter("coUserId", userId);
				q.setParameter("coUsecaseId", usecase.getUsecaseId());
				q.setParameter("viewActionId", viewAction.getActionId());
				q.setParameter("recordId", recordId);
				q.executeUpdate();
			}
		}else{
			logger.log(Level.WARNING,"action view for usecase "+usecaseId+" not found can not mark as read/unread");
		}
		
	}
//*************************************************************************************************************************************************
	public CmTransitionHistory saveTransitionHistory(Long transitionId,Long userId,Long recordId){
		CmTransition transition=entityManager.find(CmTransition.class, transitionId);
		CoUser user=entityManager.find(CoUser.class, recordId);
		CmTransitionHistory history=new CmTransitionHistory();
		history.setCoUsecaseAction(transition.getCoUsecaseAction());
		history.setCoUsecase(transition.getCoUsecaseAction().getCoUsecase());
		history.setActionDate(new Date());
		history.setCoUser(user);
		entityManager.persist(history);
		return history;
	}
//*************************************************************************************************************************************************
	@Override
	public CoUser getRecordLockedBy(Long usecaseId, Long recordId,
			Long userId) {
		try {
			UseCase usecase= UsecaseUtil.getUsecase(usecaseId);
			UsecaseAction lockAction= usecase.getUsecaseAction(predefindedActions.lock.toString());
			if(lockAction!=null){
				 Query q=   entityManager.createQuery("SELECT model.coUser from "+CmTransitionHistory.class.getSimpleName()+
						" model where model.recordId=:recordId and model.coUser.coUserId<>:userId and model.isactive=:iactive " +
						" and model.coUsecase.coUsecaseId=:coUsecaseId and model.coUsecaseAction.coUsecaseActionId=:usActionId order by actionDate desc");
				 q.setParameter("recordId", recordId);
				 q.setParameter("userId", userId);
				 q.setParameter("iactive", Constants.IsActive.active);
				 q.setParameter("usActionId", lockAction.getActionId());
				 q.setParameter("coUsecaseId", usecaseId);
				 q.setMaxResults(1);
				 return ((CoUser)q.getSingleResult());
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
				}
		return null;
	}
	
	}
