package ir.utopia.core.usecase.usecase.bean;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecase.persistence.CoVUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CoUsecase.
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoUsecaseFacade extends AbstractBasicUsecaseBean<CoUsecase,CoVUsecase> implements CoUsecaseFacadeRemote {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoUsecaseFacade.class.getName());
	}
	// property constants
	public static final String NAME = "name";
	
	public static final String USCS_REMOTE_CLASS = "uscsRemoteClass";
	
	public List<CoUsecase> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public CoUsecase findByUscsRemoteClass(String uscsRemoteClass
			) {
		List<CoUsecase> result =findByProperty(USCS_REMOTE_CLASS, uscsRemoteClass,
				null);
		return (result!=null&&result.size()>0)?result.get(0):null;
	}
	
	

	public CoUsecase loadUsecase(String uscaseName, String systemAbbreviation,
			String subSystemAbbreviation) {
		try {
			 final StringBuffer queryString =new StringBuffer(" select a1 from CoUsecase a1 inner join a1.cmSubsystem a2 inner join a2.cmSystem a3 ");
			 queryString.append(" where ").append("a1.name=:name and a2.abbreviation=:ABBREVIATION1 and a3.abbreviation=:ABBREVIATION2 and a1.isactive=a2.isactive ");
			 queryString.append(" and a3.isactive=a1.isactive and a1.isactive=:isactive ");
			Query query = entityManager.createQuery(queryString.toString()) ;
			query.setParameter("name",uscaseName);
			query.setParameter("ABBREVIATION1", subSystemAbbreviation);
			query.setParameter("ABBREVIATION2", systemAbbreviation);
			query.setParameter("isactive", Constants.IsActive.active);
			return loadUsecase(query);
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find by property name failed", re);
			throw re;
		}
	}

	
	public CoUsecase loadUsecase(String uscsRemoteClass) {
		try {
			 final StringBuffer queryString =new StringBuffer(" select a1 from CoUsecase a1 inner join a1.cmSubsystem a2 inner join a2.cmSystem a3 ");
			 queryString.append(" where ").append("a1.uscsRemoteClass=:uscsRemoteClass  and a1.isactive=a2.isactive ");
			 queryString.append(" and a3.isactive=a1.isactive and a1.isactive=:isactive ");
			Query query = entityManager.createQuery(queryString.toString()) ;
			query.setParameter("uscsRemoteClass",uscsRemoteClass);
			query.setParameter("isactive", Constants.IsActive.active);
			return loadUsecase(query);
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find by property name failed",  re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	private CoUsecase loadUsecase(Query query){
		List<CoUsecase>resultList=query.getResultList();
		if(resultList==null||resultList.size()==0)return null;
		CoUsecase result=resultList.get(0);
		for(CoUsecaseAction action: result.getCoUsecaseActions()){//load usecase actions
			action.getCoAction();
		}
		CmSubsystem subSystem= result.getCmSubsystem();
		subSystem.getCmSystem();//load system and subSystem
		
		return result;
	}

	@Override
	public CoUsecase loadUsecase(Long usecaseId) {
		try {
			 final StringBuffer queryString =new StringBuffer(" select a1 from CoUsecase a1 inner join a1.cmSubsystem a2 inner join a2.cmSystem a3 ");
			 queryString.append(" where ").append("a1.coUsecaseId=:coUsecaseId  and a1.isactive=a2.isactive ");
			 queryString.append(" and a3.isactive=a1.isactive and a1.isactive=:isactive ");
			Query query = entityManager.createQuery(queryString.toString()) ;
			query.setParameter("coUsecaseId",usecaseId);
			query.setParameter("isactive", Constants.IsActive.active);
			return loadUsecase(query);
		} catch (RuntimeException re) {
			logger.log(Level.SEVERE,"find by property name failed",  re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoUsecase> loadUsecases(List<String> uscsRemoteClasses) {
			if(uscsRemoteClasses!=null&&uscsRemoteClasses.size()>0){
			  StringBuffer queryString =new StringBuffer(" select DISTINCT a1 from CoUsecase a1  join FETCH a1.cmSubsystem join Fetch a1.coUsecaseActions ");
			 queryString.append(" where a1.isactive=:isactive and a1.cmSubsystem.isactive=:isactive and a1.cmSubsystem.cmSystem.isactive=:isactive  ");
			 queryString.append("  AND (");
			 for(int i=0;i<uscsRemoteClasses.size();i++){
				 if(i>0){
					 queryString.append(" OR ");
				 }
				 queryString.append(" a1.uscsRemoteClass=:facadeClass").append(i);
			 }
			 queryString.append(" ) ");
			Query query = entityManager.createQuery(queryString.toString()) ;
			for(int i=0;i<uscsRemoteClasses.size();i++){
				query.setParameter("facadeClass"+i,uscsRemoteClasses.get(i));
			}
			query.setParameter("isactive", Constants.IsActive.active);
			List<CoUsecase> result=query.getResultList();
/*			if(result!=null&&result.size()>0){
				queryString=new StringBuffer(" SELECT  CoUsecaseAction FROM CoUsecaseAction CoUsecaseAction join fetch CoUsecaseAction.coAction   ");
				queryString.append(" where CoUsecaseAction.coAction.isactive=:isactive AND (1=1  ");
				int index=0;
				Map<Integer,Long>paramMap=new HashMap<Integer,Long>();
				for(CoUsecase usecase:result){
					queryString.append(" OR CoUsecaseAction.coUsecase.coUsecaseId=:coUsecaseId").append(index);
					paramMap.put(index, usecase.getCoUsecaseId());
					index++;
				}
				queryString.append(" ) "); 
				query = entityManager.createQuery(queryString.toString()) ;
				query.setParameter("isactive", Constants.IsActive.active);
				for(int i:paramMap.keySet()){
					query.setParameter("coUsecaseId"+i, paramMap.get(i));
				}
				List<CoUsecaseAction>validActions= query.getResultList();
				for(CoUsecase usecase:result){
					Set<CoUsecaseAction>srcActions= usecase.getCoUsecaseActions();
					Set<CoUsecaseAction>actionList=new HashSet<CoUsecaseAction>();
					L1: for(CoUsecaseAction actionSrc:srcActions){
						 for(CoUsecaseAction action:validActions){
								if(action.getCoUsecaseActionId().equals(actionSrc.getCoUsecaseActionId())){
									actionList.add(action);
									continue L1;
								}
							}
					}
					usecase.setCoUsecaseActions(actionList);
				}
				
			}*/
			return result;
			}
		return null;
	}
	
	
	
	
}