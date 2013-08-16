package ir.utopia.core.security.user.bean;

import ir.utopia.common.basicinformation.businesspartner.bean.BpartnerUtilityFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.AbstractUtopiaBean;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants.Sex;
import ir.utopia.core.persistent.lookup.model.DetailPersistentValueInfo;
import ir.utopia.core.process.BeanProcess;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.EventType;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userrole.persistence.CoUserRoles;
import ir.utopia.core.security.usruscsactnaccs.persistence.CoUsrUscsActnAccs;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.security.auth.Subject;
import javax.transaction.UserTransaction;
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserUtilityFacade extends AbstractUtopiaBean implements UserUtilityFacadeRemote {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UserUtilityFacade.class.getName());
	}
	@EJB
	private CoUserFacadeRemote userBean;
	@EJB
	private BpartnerUtilityFacadeRemote bpartnerBean; 
	@PersistenceContext
	protected EntityManager entityManager; 
	@Resource
	private UserTransaction userTransaction;
//***************************************************************************************************	
	@Override
	public void creatUser(CoUser user, String name, String family, String code,
			Sex sex, String address, String email) throws Exception {
		try {
		userTransaction.begin();
		CmBpartner partner= bpartnerBean.createPersonPartner(name, family, code, sex, null, email);
		doCreateUser(user, partner);
		userTransaction.commit();
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				logger.log(Level.WARNING,"", e1);
			}
			throw e;
		}
	}
//***************************************************************************************************
	@Override
	public void createUserForBpartner(CoUser user, CmBpartner bpartner)throws Exception {
		try {
		userTransaction.begin();
		bpartner= entityManager.find(CmBpartner.class, bpartner.getCmBpartnerId());
		doCreateUser(user, bpartner);
		userTransaction.commit();
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				logger.log(Level.WARNING,"", e1);
			}
			throw e;
		}
	}
//***************************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected void doCreateUser(CoUser user,CmBpartner partner)throws Exception{
		try {
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
			user.setCmBpartner(partner);
			Collection<DetailPersistentValueInfo>roles= user.getIncludedPersistentValue("coUserRoleses");
			if(roles!=null){
				for(DetailPersistentValueInfo roleO:roles){
					CoUserRoles role=(CoUserRoles)roleO.getValue();
					BeanUtil.initPersistentObject(entityManager, role, false,context) ;
					BeanUtil.initLookupInfos(entityManager, role);
					role.setCoUser(user);
				}
			}
			BeanUtil.initLookupInfos(entityManager, user);
			BeanUtil.initPersistentObject(entityManager, user, false, context);
			entityManager.persist(user);
			addDefaultAccesses(user.getCoUserId(),context);
			entityManager.flush();
			userBean.refreshEntity(user.getCoUserId());
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			try {
			} catch (Exception e1) {
				logger.log(Level.WARNING,"", e1);
			}
			throw e;
		}
	}
//***************************************************************************************************
	@Override
	public void updateUserForBpartner(CoUser user, CmBpartner bpartner)throws Exception {
		try{
		userTransaction.begin();
		CoUser currentUser=entityManager.find(CoUser.class,user.getCoUserId());
		entityManager.find(CmBpartner.class, bpartner.getCmBpartnerId());
		doUpdateUser(user, currentUser, bpartner);
		userTransaction.commit();
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				logger.log(Level.WARNING,"", e1);
			}
			throw e;
		}
	}
//***************************************************************************************************
	@Override
	public void updateUser(CoUser user, String name, String family,
			String code, Sex sex, String address, String email)
			throws Exception {
		try{
			userTransaction.begin();
			CoUser currentUser=entityManager.find(CoUser.class,user.getCoUserId());
			CmBpartner partner= bpartnerBean.updateBpartner(currentUser.getCmBpartner().getCmBpartnerId(), 
					name, family, code, sex, null, email, null, null, null,null,null,null);
			doUpdateUser(user, currentUser, partner);
			userTransaction.commit();
			} catch (Exception e) {
				try {
					userTransaction.rollback();
				} catch (Exception e1) {
					logger.log(Level.WARNING,"", e1);
				}
				throw e;
			}
		
	}
//*******************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected void doUpdateUser(CoUser user,CoUser currentUser,CmBpartner partner)throws Exception{
		try {
			Map<String,Object>context= ContextHolder.getContext().getContextMap();
			currentUser.setUsername(user.getUsername());
			currentUser.setPassword(user.getPassword());
			BeanUtil.initLookupInfos(entityManager, currentUser);
			BeanUtil.initPersistentObject(entityManager, currentUser, true, context);
			Collection<DetailPersistentValueInfo> roles=user.getIncludedPersistentValue("coUserRoleses");
			Set<CoUserRoles>curretntRoles= currentUser.getCoUserRoleses();
			List<CoUserRoles>removedRoles=new ArrayList<CoUserRoles>(); 
			if(roles!=null){
				for(DetailPersistentValueInfo roleO:roles){
					CoUserRoles role=(CoUserRoles)roleO.getValue();
					if(roleO.isDeleted()){
						for(CoUserRoles crole:curretntRoles){
							if(crole.getRecordId().equals(role.getRecordId())){
								removedRoles.add(crole);
								break;
							}
						}
						
					}else{
						Long roleId=role.getCoUserRolesId();
						if(roleId==null||roleId.equals(0l)){
							BeanUtil.initPersistentObject(entityManager, role, false, context) ;
							BeanUtil.initLookupInfos(entityManager, role);
							role.setCoUser(currentUser);
							curretntRoles.add(role);
						}else{
							if(curretntRoles==null)continue;
							for(CoUserRoles crole:curretntRoles){
								if(roleId.equals(crole.getCoUserRolesId())){
									BeanUtil.initLookupInfos(entityManager, role);
									crole.setCoRole(role.getCoRole());
									BeanUtil.initPersistentObject(entityManager, crole, true, context) ;
								}
								
							}
						}
					}
					
				}
			}
			for(CoUserRoles role:removedRoles){
				entityManager.remove(role);
			}
			currentUser.setCmBpartner(partner);
			entityManager.merge(currentUser);
			entityManager.flush();
			userBean.refreshEntity(user.getCoUserId());
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				logger.log(Level.WARNING,"", e1);
			}
			
			throw e;
		}
	}
//*******************************************************************************************	
	@Override
	public int getTotalStepCount(BeanProcess processBean,Map<String,Object>context) {
		if(UserUtilityFacadeRemote.UTILITY_PROCESS_CHANGE_PASSWORD.equals(processBean.getProcessName())){
			return 1;
		}
		return 0;
	}
//*******************************************************************************************	
	@Override
	public BeanProcessExcutionResult startProcess(BeanProcess processBean,
			Map<String, Object> context, ProcessListener listener)
			throws Exception {
		if(UserUtilityFacadeRemote.UTILITY_PROCESS_CHANGE_PASSWORD.equals(processBean.getProcessName())){
			String newPassword=null;
			Long userId=null;
			for(BeanProcessParameter param: processBean.getParameters()){
				if(UTILITY_PROCESS_CHANGE_PASSWORD_NEW_PASSWORD_PARAMETER.equals(param.getName())){
					newPassword=String.valueOf(param.getValue());
				}else if(USERID_PARAMETER_NAME.equals(param.getName())){
					userId=(Long)param.getValue();
				}
			}
			if(newPassword==null||newPassword.trim().length()==0){
				throw new IllegalArgumentException("new password can not be null");
			}
			changePassword(userId, newPassword, context);
			if(listener!=null){
				listener.notifyStatusChanged(new ProcessStatusChangeEvent(EventType.processFinished));
			}
			
		}else if(CoUserFacadeRemote.CHANGE_ORGANIZATION_METHODNAME.equals(processBean.getProcessName())){
			Subject user= ContextUtil.getUser(context);
			Long userId=ServiceFactory.getSecurityProvider().getUserId(user);
			Long organizationId=-1l;
			boolean changeDefaultOrganization=false;
			for(BeanProcessParameter param: processBean.getParameters()){
				if("organizationId".equals(param.getName()) ){
					organizationId=(Long)param.getValue();
				}
				if("setAsDefaultOrganization".equals(param.getName()) ){
					changeDefaultOrganization=((Boolean)param.getValue()).booleanValue();
					
				}
			}
			if(changeDefaultOrganization){
				changeDefaultOrganization(userId, organizationId, context);
			}
			if(listener!=null){
				listener.notifyStatusChanged(new ProcessStatusChangeEvent(EventType.processFinished));
			}
		}
		return null;
	}
//*******************************************************************************************
	protected void changeDefaultOrganization(Long userId,Long newOrgId,Map<String, Object> context)throws Exception{
		userTransaction.begin();
		CoUser user= entityManager.find(CoUser.class, userId);
		if(user==null){
			throw new IllegalArgumentException("invalid userId.user with id-->"+userId+" does not exists");
		}
		user.setCmOrganization(entityManager.find(CmOrganization.class, newOrgId));
		BeanUtil.initPersistentObject(entityManager, user, true, context);
		entityManager.merge(user);
		userTransaction.commit();

	}
//*******************************************************************************************
	protected void changePassword(Long userId,String newPassword,Map<String, Object> context)throws Exception{
			userTransaction.begin();
			CoUser user= entityManager.find(CoUser.class, userId);
			if(user==null){
				throw new IllegalArgumentException("invalid userId.user with id-->"+userId+" does not exists");
			}
			user.setPassword(ServiceFactory.getSecurityProvider().encrypt(newPassword));
			BeanUtil.initPersistentObject(entityManager, user, true, context);
			entityManager.merge(user);
			userTransaction.commit();
	}
//*******************************************************************************************
	@Override
	public void encyptNonEncryptedUsers() {
		try {
			Subject subject=ServiceFactory.getSecurityProvider().loginUser(1l);
			HashMap<String, Object> context=new  HashMap<String, Object>();
			context.put(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME, subject);
			List<CoUser>users= userBean.findAllActives( null);
			for(CoUser user:users){
				String sourcePassword=user.getPassword();
				if(sourcePassword==null){
					continue;
				}
				try {
					 ServiceFactory.getSecurityProvider().decrypt( sourcePassword);
					
				} catch (Exception e) {
					logger.log(Level.INFO,"un encrypted password for user:"+user.getUsername()+" encypting");
					changePassword(user.getCoUserId(),   sourcePassword, context);
				}
				
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
		}
		
	}
//***************************************************************************************************
		private void addDefaultAccesses(Long userId,Map<String,Object>context)throws Exception{
			addChangePassword(userId,context);
		}
//***************************************************************************************************
		private void addChangePassword(Long userId,Map<String,Object>context)throws Exception{
			try {
				StringBuffer usecaseActionOfChangePassword=new StringBuffer("select CoUsecaseAction from ").
				append(CoUsecaseAction.class.getSimpleName()).append(" CoUsecaseAction join CoUsecaseAction.coAction coAction join CoUsecaseAction.coUsecase coUsecase ").
				append(" where coAction.name=:actionName and coUsecase.name=:usecaseName");
				CoUsecaseAction usaction=(CoUsecaseAction)entityManager.createQuery(usecaseActionOfChangePassword.toString()).
				setParameter("actionName",CoUserFacadeRemote.CHANGE_PASSWORD_METHODNAME ).
				setParameter("usecaseName", CoUserFacadeRemote.USER_MANAGMENT_USECASE_NAME).getSingleResult();
				CoUsrUscsActnAccs actionaccs=new CoUsrUscsActnAccs();
				actionaccs.setCoUsecaseAction(usaction);
				BeanUtil.initPersistentObject(entityManager, actionaccs, false, context);
				actionaccs.setCoUser(entityManager.find(CoUser.class, userId));
				entityManager.persist(actionaccs);
			} catch (NoResultException e) {
				logger.log(Level.WARNING,"", e);
			}
		}
	
}
