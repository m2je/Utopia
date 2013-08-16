package ir.utopia.core.bean;

import ir.utopia.common.basicinformation.fiscalyear.persistent.FiscalYearSupport;
import ir.utopia.common.basicinformation.organization.bean.CmOrganizationFacadeRemote;
import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.persistent.OrganizationData;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.persistent.parentchild.ParentChildPair;
import ir.utopia.core.security.principal.UserIdPrincipal;
import ir.utopia.core.utilities.ArrayUtils;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.security.auth.Subject;

/**
 * 
 * @author Salarkia
 *
 * @param <P>
 * @param <S>
 */
public class AbstractOrganizationSupportFacade<P extends OrganizationData,S extends OrganizationData> 
										extends AbstractBasicUsecaseBean<P,S>
										implements OrganizationSupportFacade<P,S>{
	public static final String CM_ORGANIZATION_ID = "cmOrganization.cmOrganizationId";
	@EJB
	private CmOrganizationFacadeRemote organizationFacade;
	protected String fiscalYearColumn=null;
	protected String fiscalYearViewColumn=null;
	protected Boolean isFiscalYearSupport;
	//*****************************************************************************************************	
		@Override
		public List<NamePair> loadLookup(
				LookupConfigurationModel lookupConfigModel, int... rowStartEndIndex) {
			Condition condition=getOrganizationCondition(getPersistentClass(), null);
			if(condition!=null)
				lookupConfigModel.addCondition(condition);
			return super.loadLookup( lookupConfigModel, rowStartEndIndex);
		}
	//*****************************************************************************************************
		public Condition getOrganizationCondition(Class<?> persistenObject,Condition condition){
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
			Subject subject= ContextUtil.getUser(context);
			if(((java.util.Set<UserIdPrincipal>)subject.getPrincipals(UserIdPrincipal.class)).iterator().next().getUserId()==1l){
				return getAdminCondition(persistenObject, condition, context);
			}
			String persistentName=persistenObject.getSimpleName();
			StringBuffer cond;
			if(condition==null || condition.getCondition()==null|| condition.getCondition().trim().length()==0){
				cond=new StringBuffer();
				condition=new Condition(null, context);
			}else{
				cond=new StringBuffer();
			}
			 	boolean allAccessibleOrganization= ContextUtil.isShowAllOrganizationData(context);
			 	Long [] organizations;
			 	if(allAccessibleOrganization){
			 		organizations=ServiceFactory.getSecurityProvider().getAvialableOrganizations(subject) ;
			 	}
			 	else{
			 		Long organizationId= ServiceFactory.getSecurityProvider().getOrganization(subject,context);
			 		organizations=organizationId!=null&&organizationId.longValue()>0?new Long[]{organizationId}:null;
			 		}
			 	
				if(organizations!=null&&organizations.length>0){
					List<Object[]>breakedOrganizations= ArrayUtils.breakArray(organizations, 1000);
					
					for(int i=0;i<breakedOrganizations.size();i++){
						Object[] cur=breakedOrganizations.get(i);
						cond.append(persistentName).append(".").append(CM_ORGANIZATION_ID). append(" IN (");
						for(int j=0;j<cur.length;j++){
							cond.append(":org"+i+""+j);
							if(j+1<cur.length){
								cond.append(",");
							}
							condition.setParameterValue("org"+i+""+j, cur[j]);
						}
						cond.append(")");
						if(i+1<breakedOrganizations.size()){
							cond.append(" OR ");
						}
					}
				}else{
					cond.append(persistentName).append(".").append(CM_ORGANIZATION_ID). append(" IN (:noOrg) ");
					condition.setCondtionString(cond.toString());
					condition.setParameterValue("noOrg", -1l);
				}
				
				if(isFiscalYearSupport()){
					Long fiscalyearId= ContextUtil.getCurrentFiscalYear(context);
					if(fiscalyearId!=null){
						cond.append(" AND ").append(persistentName).append(".").append(getFiscalYearColumn(persistenObject)).append("=:__cmFiscalyearId");
						condition.setParameterValue("__cmFiscalyearId",fiscalyearId );
					}
				}
					if(condition.getCondition()==null||condition.getCondition().trim().length()==0){
						condition.setCondtionString(" ("+cond+")");
					}else{
						condition.setCondtionString(condition.getCondition()+" and ("+cond+")");
					}
				
					
				
				
				return condition;	
			
			
		}
//*****************************************************************************************************
		private Condition getAdminCondition(Class<?> persistenObject,
				Condition condition, Map<String, Object> context) {
			if(isFiscalYearSupport()){
				Long fiscalyearId= ContextUtil.getCurrentFiscalYear(context);
				if(fiscalyearId!=null){
					StringBuffer cond=new StringBuffer(persistenObject.getSimpleName()).append(".").append(getFiscalYearColumn(persistenObject)).append("=:__cmFiscalyearId");
					if(condition==null || condition.getCondition()==null|| condition.getCondition().trim().length()==0){
						condition=new Condition(cond.toString(), context);
					}else{
						condition.setCondtionString(condition.getCondition()+" and ("+cond+")");
					}
					condition.setParameterValue("__cmFiscalyearId",fiscalyearId );
					return condition;
				}
			}
			return null;
		}
//*****************************************************************************************************
		@Override
		protected Query getSearchQuery(SearchConditionItem []items,EntityPair []orderBy, Class<? extends UtopiaBasicPersistent>searchClass,Condition defaultCondition,boolean count){
			Map<String,Object>context= ContextHolder.getContext().getContextMap();
			Subject subject= ContextUtil.getUser(context);
			if(((java.util.Set<UserIdPrincipal>)subject.getPrincipals(UserIdPrincipal.class)).iterator().next().getUserId()==1l){//for admin user
				return super.getSearchQuery(items, orderBy, searchClass, defaultCondition, count);
			}
			Condition cond= getOrganizationCondition(getSearchModelClass(),defaultCondition);
			return super.getSearchQuery(items,orderBy, searchClass, cond,
					count);
		}
	//*****************************************************************************************************		
		@Override
		public List<P> findByProperty(String propertyName,
				Object value, 
				int... rowStartIdxAndCount) {
			Long currentUserId= ContextUtil.getCurrentUserId();
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
			Subject subject= ContextUtil.getUser(context);
			if(currentUserId==1l){
				return super.findByProperty(propertyName, value,  rowStartIdxAndCount);
			}else{
				return super.findByProperties(new String[]{propertyName, CM_ORGANIZATION_ID}, new Object[]{value,ServiceFactory.getSecurityProvider().getOrganization(subject,context) },
						 rowStartIdxAndCount);	
			}
			
		}
	//*****************************************************************************************************	
		@Override
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		protected P doSaveNoLog(P persitentObject,EntityManager entityManager, boolean handelDetails,boolean autoFlush,boolean referesh)throws SaveRecordException{
			Map<String,Object>context= ContextHolder.getContext().getContextMap();
			Subject subject= ContextUtil.getUser(context);
			Long organiztionId=ServiceFactory.getSecurityProvider().getOrganization(subject,context);
			CmOrganization cmOrganization= organizationFacade.findById(organiztionId);
			((OrganizationData)persitentObject).setCmOrganization(cmOrganization);
			return super.doSaveNoLog(persitentObject,  entityManager, handelDetails, autoFlush, referesh);
		}
		//*****************************************************************************************************
		@Override
		protected P doUpdateNoLong(UtopiaBasicPersistent persitentObject,
				EntityManager entityManager,
				boolean handelDetails) throws SaveRecordException {
			@SuppressWarnings("unchecked")
			P oldP= (P)entityManager.find(persitentObject.getClass(), persitentObject.getRecordId());
			((OrganizationData)persitentObject).setCmOrganization(((OrganizationData)oldP).getCmOrganization());//Organization data movement controller
			return super.doUpdateNoLong(persitentObject,  entityManager,
					handelDetails);
		}
//*****************************************************************************************************	
		public List<ParentChildPair> loadChildren(LookupConfigurationModel model,Long parentId,
				int... rowStartEndIndex) {
			Condition condition=getOrganizationCondition(getPersistentClass(), null);
			if(condition!=null)
				model.addCondition(condition);
			return super.loadChildren(model, parentId,  rowStartEndIndex);
		}
//*****************************************************************************************************	
	protected boolean isFiscalYearSupport(){
		if(isFiscalYearSupport==null){
			String  fiscalYearField= getFiscalYearColumn(getPersistentClass());
			isFiscalYearSupport=fiscalYearField!=null;
		}
		return isFiscalYearSupport;
	}	
//*****************************************************************************************************
	protected String getFiscalYearColumn(Class<?> persistentClass){
		if(fiscalYearColumn==null){
			FiscalYearSupport fiscalYearSupport=getPersistentClass().getAnnotation(FiscalYearSupport.class);
			if(fiscalYearSupport!=null){
				fiscalYearViewColumn=fiscalYearSupport.viewFiscalYearField();
				fiscalYearColumn=fiscalYearSupport.fiscalYearField();
			}
	    }	
		if(getSearchModelClass().isAssignableFrom(persistentClass)){
			return fiscalYearViewColumn;
		}
		return fiscalYearColumn;
		}
//*****************************************************************************************************
}
