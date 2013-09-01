package ir.utopia.core.util.tags.controlpanel.server;

import ir.utopia.common.systems.subsystem.bean.CmSubsystemFacadeRemote;
import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.common.systems.system.bean.CmSystemFacadeRemote;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.basicinformation.action.bean.CoActionFacadeRemote;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.log.persistent.CoCpLogConfig;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.security.persistent.CoVRoleValidAccess;
import ir.utopia.core.security.persistent.CoVUserAllValidAccess;
import ir.utopia.core.security.role.bean.CoRoleFacadeRemote;
import ir.utopia.core.security.role.persistence.CoRole;
import ir.utopia.core.security.user.bean.CoUserFacadeRemote;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userrole.bean.CoUserRolesFacadeRemote;
import ir.utopia.core.security.userrole.persistence.CoUserRoles;
import ir.utopia.core.usecase.usecase.bean.CoUsecaseFacadeRemote;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.bean.CoUsecaseActionFacadeRemote;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;
import ir.utopia.core.util.tags.AbstractUtopiaGWTServiceAction;
import ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;

public class ControlPanelServlet extends AbstractUtopiaGWTServiceAction implements ControlPanelService{
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractBasicUsecaseBean.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -7795201982906705852L;

	@Override
	public UILookupInfo loadData(int rootType) {
		try {
			Class<?>beanClass;
			switch (rootType) {
			case LIST_TYPE_SYSTEM:
				beanClass=CmSystemFacadeRemote.class;
				break;
			case LIST_TYPE_SUB_SYSTEM:
				beanClass=CmSubsystemFacadeRemote.class;
				break;
			case LIST_TYPE_USECASE:
				beanClass=CoUsecaseFacadeRemote.class;
				break;
			case LIST_TYPE_ACTION:
				beanClass=CoActionFacadeRemote.class;
				break;
			case LIST_TYPE_ROLE:
				beanClass=CoRoleFacadeRemote.class;
				break;
			case LIST_TYPE_USER:
				beanClass=CoUserFacadeRemote.class;
				break;
			
			default:throw new IllegalArgumentException("invalid type --->"+rootType);
				
			}
			
			UtopiaBasicUsecaseBean<?, ?>bean=(UtopiaBasicUsecaseBean<?, ?>)ServiceFactory.lookupFacade(beanClass.getName());
			
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup( null) , getLanguage()) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//*****************************************************************************************
	@Override
	public UILookupInfo[] loadDetailData(Long []parentIds,int type,int rootType) {
		try {
	
			Map<String,Object>context=ContextUtil.createContext(getSession());
			
			UILookupInfo  systems=null, subSystems=null,usecases=null,action=null,roles=null,user=null;
			switch (type) {
			case LIST_TYPE_SYSTEM:
				context.put("cmSystemId", parentIds[LIST_TYPE_SYSTEM]);
				subSystems=loadLookup(getLookupConfiguration("name","cmSubsystemId",CmSubsystem.class, "CmSubsystem.cmSystem.cmSystemId=@cmSystemId@", context),
						CmSubsystemFacadeRemote.class,parentIds, type,context) ;
				usecases=loadLookup(getLookupConfiguration("name","coUsecaseId",CoUsecase.class, "CoUsecase.cmSubsystem.cmSystem.cmSystemId=@cmSystemId@", context), 
						 CoUsecaseFacadeRemote.class, parentIds,type,context);
				action=loadLookup(getLookupConfiguration("coAction.name","coAction.coActionId",CoUsecaseAction.class, "CoUsecaseAction.coUsecase.cmSubsystem.cmSystem.cmSystemId=@cmSystemId@", context), 
						CoUsecaseActionFacadeRemote.class, parentIds,type,context);
				user=loadLookup(getLookupConfiguration("username","coUserId",CoUser.class, "CoUser.coUserId in (select CoVUserAllValidAccess.coUserId from  "+
						CoVUserAllValidAccess.class.getSimpleName()+" CoVUserAllValidAccess where CoVUserAllValidAccess.cmSystemId=@cmSystemId@)", context), CoUserFacadeRemote.class, parentIds,type,context);
				roles=loadLookup(getLookupConfiguration("name","coRoleId",CoRole.class, "CoRole.coRoleId in (select CoVRoleValidAccess.id.coRoleId from  "+
						CoVRoleValidAccess.class.getSimpleName()+" CoVRoleValidAccess where CoVRoleValidAccess.id.cmSystemId=@cmSystemId@)"
						, context), CoRoleFacadeRemote.class, parentIds,type,context);
				break;

			case LIST_TYPE_SUB_SYSTEM:
				context.put("cmSubsystemId", parentIds[LIST_TYPE_SUB_SYSTEM]);
				usecases=loadLookup(getLookupConfiguration("name","coUsecaseId",CoUsecase.class,"CoUsecase.cmSubsystem.cmSubsystemId=@cmSubsystemId@", context), 
						 CoUsecaseFacadeRemote.class, parentIds,type,context);
				action=loadLookup(getLookupConfiguration("coAction.name","coAction.coActionId",CoUsecaseAction.class,"CoUsecaseAction.coUsecase.cmSubsystem.cmSubsystemId=@cmSubsystemId@", context), 
						CoUsecaseActionFacadeRemote.class, parentIds,type,context);
				user=loadLookup(getLookupConfiguration("username","coUserId",CoUser.class,"CoUser.coUserId in (select CoVUserAllValidAccess.coUserId from  "+
						CoVUserAllValidAccess.class.getSimpleName()+" CoVUserAllValidAccess where CoVUserAllValidAccess.cmSubsystemId=@cmSubsystemId@)", context), CoUserFacadeRemote.class, parentIds,type,context);
				roles=loadLookup(getLookupConfiguration("name","coRoleId",CoRole.class,"CoRole.coRoleId in (select CoVRoleValidAccess.id.coRoleId from  "+
						CoVRoleValidAccess.class.getSimpleName()+" CoVRoleValidAccess where CoVRoleValidAccess.id.cmSubsystemId=@cmSubsystemId@)"
						, context), CoRoleFacadeRemote.class,parentIds, type,context);
				break;
				
			case LIST_TYPE_USECASE:
				context.put("coUsecaseId", parentIds[LIST_TYPE_USECASE]);
				action=loadLookup(getLookupConfiguration("coAction.name","coAction.coActionId",CoUsecaseAction.class,"CoUsecaseAction.coUsecase.coUsecaseId=@coUsecaseId@", context), 
						CoUsecaseActionFacadeRemote.class, parentIds,type,context);
				user=loadLookup(getLookupConfiguration("username","coUserId",CoUser.class,"CoUser.coUserId in (select CoVUserAllValidAccess.coUserId from  "+
						CoVUserAllValidAccess.class.getSimpleName()+" CoVUserAllValidAccess where CoVUserAllValidAccess.coUsecaseId=@coUsecaseId@)", context), CoUserFacadeRemote.class,parentIds,type, context);
				roles=loadLookup(getLookupConfiguration("name","coRoleId",CoRole.class,"CoRole.coRoleId in (select CoVRoleValidAccess.id.coRoleId from  "+
						CoVRoleValidAccess.class.getSimpleName()+" CoVRoleValidAccess where CoVRoleValidAccess.id.coUsecaseId=@coUsecaseId@)"
						, context), CoRoleFacadeRemote.class,parentIds,type, context);
				break;
			
			case LIST_TYPE_ACTION:
				context.put("coActionId", parentIds[LIST_TYPE_ACTION]);
				StringBuffer roleCondition=new StringBuffer();
				StringBuffer userCondition=new StringBuffer();
				String roleDefaultCondition="CoRole.coRoleId in (select CoVRoleValidAccess.id.coRoleId from  "+
				CoVRoleValidAccess.class.getSimpleName()+" CoVRoleValidAccess where CoVRoleValidAccess.id.coActionId=@coActionId@ @extraCondition@)";
				String userDefaultCondition="CoUser.coUserId in (select CoVUserAllValidAccess.coUserId from  "+
				CoVUserAllValidAccess.class.getSimpleName()+" CoVUserAllValidAccess where CoVUserAllValidAccess.coActionId=@coActionId@ @extraCondition@)";
				for(int i=rootType;i<type;i++)
				switch (i) {
						case LIST_TYPE_USECASE:{
							if(parentIds[LIST_TYPE_USECASE]!=null&&parentIds[LIST_TYPE_USECASE]>0){
								context.put("coUsecaseId", parentIds[LIST_TYPE_USECASE]);
								roleCondition.append(" and CoVRoleValidAccess.id.coUsecaseId=@coUsecaseId@");
								userCondition.append(" and CoVUserAllValidAccess.coUsecaseId=@coUsecaseId@ ");
							}
						}	
						break;
						case LIST_TYPE_SUB_SYSTEM:{
							if(parentIds[LIST_TYPE_SUB_SYSTEM]!=null&&parentIds[LIST_TYPE_SUB_SYSTEM]>0){
								context.put("cmSubsystemId", parentIds[LIST_TYPE_SUB_SYSTEM]);
								roleCondition.append(" and CoVRoleValidAccess.id.cmSubsystemId=@cmSubsystemId@");
								userCondition.append(" and CoVUserAllValidAccess.cmSubsystemId=@cmSubsystemId@");
							}
						}
						break;
						case LIST_TYPE_SYSTEM:{
							if(parentIds[LIST_TYPE_SYSTEM]!=null&&parentIds[LIST_TYPE_SYSTEM]>0){
								context.put("cmSystemId", parentIds[LIST_TYPE_SYSTEM]);
								roleCondition.append(" and CoVRoleValidAccess.id.cmSystemId=@cmSystemId@");
								userCondition.append(" and CoVUserAllValidAccess.cmSystemId=@cmSystemId@");
							}
						}
						break;
						default :throw new IllegalArgumentException("invalid rootType-->"+i);
				}
				roleDefaultCondition=roleDefaultCondition.replaceAll("@extraCondition@", roleCondition.toString());
				userDefaultCondition=userDefaultCondition.replaceAll("@extraCondition@", userCondition.toString());
				roles=loadLookup(getLookupConfiguration("name","coRoleId",CoRole.class,roleDefaultCondition, context), CoRoleFacadeRemote.class,parentIds,type, context);
				user=loadLookup(getLookupConfiguration("username","coUserId",CoUser.class,userDefaultCondition, context), CoUserFacadeRemote.class,parentIds,type, context);
				break;
				
			case LIST_TYPE_ROLE:
				context.put("coRoleId", parentIds[LIST_TYPE_ROLE]);
				user=loadLookup(getLookupConfiguration("coUser.username","coUser.coUserId",CoUserRoles.class,"CoUserRoles.coRole.coRoleId=@coRoleId@", context), CoUserRolesFacadeRemote.class,parentIds,type, context);
				
				break;
			}
	
			return new UILookupInfo[]{systems,subSystems,usecases,action,roles,user};
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);

		}
		return null;
	}

 	
//*****************************************************************************************
	private LookupConfigurationModel getLookupConfiguration(String displayCoumn,String keyColumn,Class<? extends UtopiaPersistent>persistentClass, String condition,Map<String,Object>context){
		LookupConfigurationModel confLoadModel=new LookupConfigurationModel(persistentClass);
		confLoadModel.setOwnDisplayColumns(displayCoumn);
		confLoadModel.setPrimaryKeyColumn(keyColumn);
		confLoadModel.addCondition(condition,context);
		return confLoadModel;
	}
//*****************************************************************************************
	private UILookupInfo loadLookup(LookupConfigurationModel model,Class<?>beanClass,
			Long[] parentIds,int type, Map<String,Object>context)throws Exception{
		InitialContext ctx=new InitialContext();
		UtopiaBasicUsecaseBean< ?,?>bean=(UtopiaBasicUsecaseBean< ?,?>)ctx.lookup(beanClass.getName());
		
		
		if(type==LIST_TYPE_ACTION){
			if(parentIds[LIST_TYPE_ACTION]!=null&&parentIds[LIST_TYPE_ACTION]>0&&
					parentIds[LIST_TYPE_USECASE]!=null&&parentIds[LIST_TYPE_USECASE]>0){
				CoUsecaseActionFacadeRemote usact=(CoUsecaseActionFacadeRemote)ctx.lookup(CoUsecaseActionFacadeRemote.class.getName());
				
				
			List<CoUsecaseAction> resList=	usact.findByProperties(new String[]{"coUsecase.coUsecaseId","coAction.coActionId"}, 
						new Object[]{parentIds[LIST_TYPE_USECASE],parentIds[LIST_TYPE_ACTION]},  null);
			if(resList!=null&&resList.size()>0){
				context.put("coUsecaseAction",resList.get(0).getCoUsecaseActionId());		
			}
			}
		}
		ctx.close();
		StringBuffer logLoad=new StringBuffer(" select count(CoCpLogConfig.coCpLogConfigId) from CoCpLogConfig ").
		append(CoCpLogConfig.class.getSimpleName()).append(" where ");
		for(int i=0;i<=type;i++){
			switch (i) {
			case LIST_TYPE_SYSTEM:
				context.put("cmSystemId", parentIds[LIST_TYPE_SYSTEM]);
				break;
			case LIST_TYPE_SUB_SYSTEM:
				context.put("cmSubsystemId", parentIds[LIST_TYPE_SUB_SYSTEM]);
				break;
			case LIST_TYPE_USECASE:
				context.put("coUsecaseId", parentIds[LIST_TYPE_USECASE]);
				break;
			case LIST_TYPE_ACTION:
				context.put("coActionId", parentIds[LIST_TYPE_ACTION]);
				
				break;
			case LIST_TYPE_USER:
				context.put("coUserId", parentIds[LIST_TYPE_USER]);
				break;
			case LIST_TYPE_ROLE:
				context.put("coRoleId", parentIds[LIST_TYPE_ROLE]);
				break;
			default:
				break;
			}
		}
		return AbstractUtopiaUIHandler.convertNamePairTolookupInfo( bean.loadLookup(model,null),getLanguage());
	}
}
