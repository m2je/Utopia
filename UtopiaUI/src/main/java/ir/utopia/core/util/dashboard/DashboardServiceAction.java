package ir.utopia.core.util.dashboard;

import ir.utopia.common.basicinformation.fiscalyear.FiscalYearInfo;
import ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil;
import ir.utopia.common.dashboard.bean.CmTransitionFacadeRemote;
import ir.utopia.common.dashboard.persistent.CmTransition;
import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.common.doctype.security.bean.CmUserDocstatusAccessFacadeRemote;
import ir.utopia.common.doctype.util.DocTypeUtil;
import ir.utopia.common.doctype.util.UsecaseDocInfo;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UIServiceFactory;
import ir.utopia.core.constants.Constants.BooleanType;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.model.UsecaseUIInfo;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.util.EnumUtil;
import ir.utopia.core.util.tags.dashboard.client.DashboardClientService;
import ir.utopia.core.util.tags.dashboard.client.model.DocStatusInfo;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

public class DashboardServiceAction extends UtopiaBasicAction implements DashboardClientService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2574988233972037174L;
	Logger logger=Logger.getLogger(DashboardServiceAction.class.getName());
	@Override
	public List<TransitionalUsecaseInfo> getTransitionalUsecases() {
		Map<Long,TransitionalUsecaseInfo>resultMap=new HashMap<Long,TransitionalUsecaseInfo>();
		Map<Long,Class<?>>uscaseValidStatusesMap=new HashMap<Long, Class<?>>();
		Map<String,Object>context= createContext();
		String language=ContextUtil.getLoginLanguage(context);
		FiscalYearInfo fiscalYear= FiscalYearUtil.getFiscalYear(context);
		if(fiscalYear!=null){
		Long fiscalYearId=fiscalYear.getId();
		Map<Long,List<CmDocStatus>>avaiableDocStatueMap=new HashMap<Long,List<CmDocStatus>>();
		try {
			CmTransitionFacadeRemote transitionFacade=(CmTransitionFacadeRemote)ServiceFactory.lookupFacade(CmTransitionFacadeRemote.class);
			Subject user= ContextUtil.getUser(context);
			Long userId=ServiceFactory.getSecurityProvider().getUserId(user);
			List<CmTransition>transitions= transitionFacade.getAccessibleTransitions(userId,fiscalYearId);
			L1 :for(CmTransition transition:transitions){
				UseCase usecase= UsecaseUtil.getUsecaseFromUsecaseAtion(transition.getCoUsecaseAction().getCoUsecaseActionId());
				Long usecaseId=usecase.getUsecaseId();
				UsecaseDocInfo docInfo= DocTypeUtil.getUsecaseDocStatusInfo(usecaseId,fiscalYearId);
				initUsecaseAvaiableDocStatuses(usecaseId, transition.getCmDocStatusByCmDocStatusTo(), avaiableDocStatueMap);
				initUsecaseAvaiableDocStatuses(usecaseId, transition.getCmDocStatusByCmDocStatusFrom(), avaiableDocStatueMap);
				TransitionalUsecaseInfo usinfo;
				if(resultMap.containsKey(usecaseId)){
					usinfo=resultMap.get(usecaseId);
				}else{
					usinfo=new TransitionalUsecaseInfo();
					usinfo.setUsecaseId(usecaseId);
					usinfo.setUseCaseName(usecase.getName());
					UsecaseUIInfo uiInfo= UIServiceFactory.getUsecase(usecaseId);
					String bundelPath=ServiceFactory.getSubsystemConfiguration(UsecaseUtil.getUsecaseWithName(usecase.getFullName()).getSubSystemId()).getUsecaseBundelName();
					 if(bundelPath==null){
						 bundelPath= uiInfo.getMeta().getClazz().getName();
					 }
					 String usecaseHeader= MessageHandler.getMessage(usecase.getFullName(),bundelPath, language);
					 usinfo.setUseCaseHeader(usecaseHeader);
					usinfo.setFullUsecaseName(usecase.getFullName());
					if(uiInfo.getMeta().isCustomDataInput()){
						usinfo.setUsecaseViewAddress(AbstractUtopiaUIHandler.getActionUrl(predefindedActions.updateFromDashboard, predefindedActions.update.name(), usecase.getFullName()));
					}
					usinfo.setFormClass(ServiceFactory.getSecurityProvider().encrypt(uiInfo.getMeta().getClazz().getName()));
					usinfo.setSaveActionName(AbstractUtopiaUIHandler.getActionUrl(predefindedActions.update, predefindedActions.update.toString(), usecase.getFullName()));
					UtopiaFormMethodMetaData[] methods= uiInfo.getMeta().getMethodMetaData();
					boolean found=false;
					for(UtopiaFormMethodMetaData method:methods){
						if(docInfo.getColumnName().equals(method.getPersistentFieldName())){
							usinfo.setStatusFieldName(method.getFieldName());
							uscaseValidStatusesMap.put(usecaseId,  docInfo.getEnumClass() );
							found=true;
							break;
						}
					}
					if(!found){
						logger.log(Level.WARNING,"fail to find status field on class  "+uiInfo.getMeta().getClazz()+" , status change will not be available for this usecase");
					}
				}
				CmDocStatus fromStatus=transition.getCmDocStatusByCmDocStatusFrom();
				CmDocStatus toStatus=transition.getCmDocStatusByCmDocStatusTo();
				String actionName=null;
				for(UsecaseAction cAction: usecase.getUseCaseActions()){
					if(cAction.getUsecaseActionId()==transition.getCoUsecaseAction().getCoUsecaseActionId().longValue()){
						actionName=cAction.getActionName();
						break;
					}
				}
				if(usinfo.getDocStatusInfo()!=null&&usinfo.getDocStatusInfo().size()>0){
					for(DocStatusInfo cInfo:usinfo.getDocStatusInfo()){//if currently there is a transition info update it
						if(cInfo.getCurrentDocStatus().equals(fromStatus.getCmDocStatusId())){
							cInfo.addToDoctype(toStatus.getCmDocStatusId(), transition.getCoUsecaseAction().getCoUsecaseActionId(),
									actionName, 
									EnumUtil.getEnumString(docInfo.getEnumClass(), language, toStatus.getStatus()), toStatus.getStatus(),
									BooleanType.True.equals(toStatus.getSkipAble()),BooleanType.True.equals(toStatus.getLockAble()));
							continue L1;
						}
					}
				}
				DocStatusInfo info=new DocStatusInfo();
				info.setCurrentDocStatus(fromStatus.getCmDocStatusId());
				info.setCurrentDocStatusName(EnumUtil.getEnumString(docInfo.getEnumClass(), language, fromStatus.getStatus()));
				info.setDocStatusCode(fromStatus.getStatus());
				info.setName(EnumUtil.getEnumString(docInfo.getEnumClass(), language, fromStatus.getStatus())  );
				info.setDesciption(transition.getDescription());
				
				info.addToDoctype(toStatus.getCmDocStatusId(), transition.getCoUsecaseAction().getCoUsecaseActionId(),actionName,
						EnumUtil.getEnumString(docInfo.getEnumClass(), 
						language, toStatus.getStatus()), toStatus.getStatus(),
						BooleanType.True.equals(toStatus.getSkipAble()),BooleanType.True.equals(toStatus.getLockAble()));
				
				usinfo.addDocStatusInfo(info);
				
				resultMap.put(usecaseId, usinfo);
			}
			CmUserDocstatusAccessFacadeRemote docStatAccssBean=(CmUserDocstatusAccessFacadeRemote)ServiceFactory.lookupFacade(CmUserDocstatusAccessFacadeRemote.class);
			List<CmDocStatus>userAccessibleDocStatuses= docStatAccssBean.getUserAccessibleDocStatuses(userId, context);
			if(userAccessibleDocStatuses!=null){
				for(CmDocStatus docStatus:userAccessibleDocStatuses){
					initUsecaseAvaiableDocStatuses(docStatus.getCoUseCaseId(), docStatus, avaiableDocStatueMap);
				}
			}
			addMissingDocStatuses(resultMap, uscaseValidStatusesMap,
					avaiableDocStatueMap, language);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		}
		ArrayList<TransitionalUsecaseInfo> result=new ArrayList<TransitionalUsecaseInfo>(resultMap.values());
		for(TransitionalUsecaseInfo info:result){
			List<DocStatusInfo>docInfo= info.getDocStatusInfo();
			Collections.sort(docInfo, new Comparator<DocStatusInfo>() {

				@Override
				public int compare(DocStatusInfo o1, DocStatusInfo o2) {
					
					return o1.getDocStatusCode()-o2.getDocStatusCode();
				}
			});
		}
		
		return  result;
	}
//***********************************************************************************************************************	
	/**
	 * for adding missing doc statuses that user has access to but there is no transition for it
	 * @param resultMap
	 * @param uscaseValidStatusesMap
	 * @param avaiableDocStatueMap
	 * @param language
	 */
	private void addMissingDocStatuses(
			Map<Long, TransitionalUsecaseInfo> resultMap,
			Map<Long, Class<?>> uscaseValidStatusesMap,
			Map<Long, List<CmDocStatus>> avaiableDocStatueMap, String language) {
		for(Long usecaseId:uscaseValidStatusesMap.keySet()){
			if(resultMap.containsKey(usecaseId)&&avaiableDocStatueMap.containsKey(usecaseId)){
				TransitionalUsecaseInfo info=resultMap.get(usecaseId);
				List<CmDocStatus>accessibleDocStatuses= avaiableDocStatueMap.get(usecaseId);
				List<DocStatusInfo>infos= info.getDocStatusInfo();
				Class<?>enumClass= uscaseValidStatusesMap.get(usecaseId);
				List<NamePair>allStatusPairs= EnumUtil.getEnumLookups(enumClass, language);
				for(NamePair pair:allStatusPairs){
					boolean found=false;
					for(DocStatusInfo docInfo:infos){
						if((int)pair.getKey()== docInfo.getDocStatusCode()){
							found=true;
							break;
						}	
					}
					if(!found){
						CmDocStatus docStatus= findDocStatusByCode((int)pair.getKey(), accessibleDocStatuses);
						if(docStatus!=null){
							DocStatusInfo missingDocStatus=new DocStatusInfo();
							missingDocStatus.setDocStatusCode((int)pair.getKey());
							missingDocStatus.setName(pair.getName());
							missingDocStatus.setDesciption(pair.getDescription());
							missingDocStatus.setCurrentDocStatus(docStatus.getCmDocStatusId());
							missingDocStatus.setCurrentDocStatusName(pair.getName());
							infos.add(missingDocStatus);
						}
					}
				}
			}
		}
	}
//*************************************************************************************************************************************************
protected void initUsecaseAvaiableDocStatuses(Long usecaseId,CmDocStatus status, Map<Long,List<CmDocStatus>>avaiableDocStatues){
	List<CmDocStatus>docList=(avaiableDocStatues.containsKey(usecaseId))?avaiableDocStatues.get(usecaseId):new ArrayList<CmDocStatus>();
	boolean exists=false;
	for(CmDocStatus docStatus:docList){
		if(status.getCmDocStatusId().equals(docStatus.getCmDocStatusId())){
			exists=true;
			break;
		}
	}
	if(!exists){
		docList.add(status);
	}
	avaiableDocStatues.put(usecaseId, docList);
}
//*************************************************************************************************************************************************
protected CmDocStatus findDocStatusByCode(int code,List<CmDocStatus>docStatusList){
	for(CmDocStatus status:docStatusList){
		if(status.getStatus()==code){
			return status;
		}
	}
	return null;
}
//*************************************************************************************************************************************************
	@Override
	public SearchPageData getDashBoardRecords(
			UsecaseSearchCriteria criteria) {
		Long usecaseId= criteria.getUsecaseId();
		UsecaseUIInfo UIInfo= UIServiceFactory.getUsecase(usecaseId);
		UtopiaFormMetaData metaData= UIInfo.getMeta();
		Map<String,Object>context= createContext();
		return metaData.getHandler().getDashBoardRecords(criteria); 
	}
//*************************************************************************************************************************************************
@Override
public ExecutionResult markAs(Long usecaseId, Long recordId,
		 boolean read) {
	ExecutionResult result=new ExecutionResult();
	try {
		CmTransitionFacadeRemote facade=(CmTransitionFacadeRemote)ServiceFactory.lookupFacade(CmTransitionFacadeRemote.class);
		Map<String,Object>context= createContext();
		Long userId= ContextUtil.getUserId(context);
		facade.markAs(userId, usecaseId, recordId, read);
	} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
		result.setSuccess(false);
	}
	
	return result;
}
	

}
