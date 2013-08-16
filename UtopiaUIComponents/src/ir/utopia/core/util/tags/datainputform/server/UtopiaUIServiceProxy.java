package ir.utopia.core.util.tags.datainputform.server;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UIServiceFactory;
import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.importer.setting.bean.CoImporterSettingFacadeRemote;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;
import ir.utopia.core.logic.util.Evaluator;
import ir.utopia.core.logic.util.LogicParser;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.model.SubSystem;
import ir.utopia.core.model.UsecaseUIInfo;
import ir.utopia.core.persistent.annotation.LOVConfiguration;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.process.model.ProcessModel;
import ir.utopia.core.process.model.ProcessParameter;
import ir.utopia.core.security.SecurityProvider;
import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.struts.UtopiaBasicForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.struts.UtopiaTypeConverter;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.util.EnumUtil;
import ir.utopia.core.util.GWTUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.logic.LogicEvaluator;
import ir.utopia.core.util.tags.AbstractUtopiaGWTServiceAction;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputService;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILOVConfiguration;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.ContextItem;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageService;
import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;
import ir.utopia.core.util.tags.importpage.client.model.ImportDataServer;
import ir.utopia.core.util.tags.process.client.model.ProcessConfigurationModel;
import ir.utopia.core.util.tags.process.client.model.ProcessHandlerServer;
import ir.utopia.core.util.tags.treeview.client.model.TreeViewDataHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionContext;

public class UtopiaUIServiceProxy extends AbstractUtopiaGWTServiceAction implements
		DataInputService,SearchPageService,ImportDataServer,ProcessHandlerServer,TreeViewDataHandler {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UtopiaUIServiceProxy.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1669333567768022031L;
	public static final String SEARCH_REPORT_UID_PARAM_NAME="searchReportUID_";
	public static final int REPORT_PAGE_SIZE=10000;
	public DataInputFormModel getDataInputForm(String formClass,String usecaseName,String actionName){
			DataInputFormModel model=getDataInputFormModel(formClass, usecaseName, actionName,true);
		return model;
	}
//***********************************************************************************
	private DataInputFormModel getDataInputFormModel(String formClass,String usecaseName,String actionName,boolean loadLookups){
		try {
			String language=super.getLanguage();
			String usecaseDecryptedName=ServiceFactory.getSecurityProvider().decrypt(usecaseName);
			String actionDecryptedName=ServiceFactory.getSecurityProvider().decrypt(actionName);
			UtopiaFormMetaData meta=getFormMetaData(formClass);
			super.initContextByRequestParameters(meta);
			UseCase uscase=  UsecaseUtil.getUsecaseWithName(usecaseDecryptedName);
			UsecaseAction action =uscase.getUsecaseAction(actionDecryptedName);
			ContextHolder.getContext().setUsecase(uscase);
			ContextHolder.getContext().setActionName(actionDecryptedName);
			return	meta.getHandler().getDataInputFormModel( uscase, action.getPredefindAction(), language,loadLookups);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//***********************************************************************************
	private UtopiaFormMetaData getFormMetaData(String formClass){
		String parentConfigurationClass=
			ServiceFactory.getSecurityProvider().decrypt(formClass);
		UtopiaFormMetaData metaData= FormUtil.getMetaData(parentConfigurationClass);
		return metaData;
	}

//***********************************************************************************
	 
	public DataInputDataModel getFormData(String formClass,String usecaseName,String actionName,String recordId,boolean reload,String token) {
		Long crecordId =0l;
		try {
			 crecordId = Long.parseLong(recordId);
		} catch (Exception e) {
			try {
				crecordId = Long.parseLong(ServiceFactory.getSecurityProvider().decrypt(recordId));
			} catch (Exception e1) {
				logger.log( Level.ALL,"invalid record id "+recordId,e);	
			}
		}
		try {
			UtopiaBasicForm<?> formData=token==null?null:(UtopiaBasicForm<?>)getSession().get(token);	
			usecaseName=ServiceFactory.getSecurityProvider().decrypt(usecaseName);
			actionName=ServiceFactory.getSecurityProvider().decrypt(actionName);
			UtopiaFormMetaData meta=getFormMetaData(formClass);
			super.initContextByRequestParameters(meta);
			UseCase uscase=  UsecaseUtil.getUsecaseWithName(usecaseName);
			ContextHolder.getContext().setUsecase(uscase);
			ContextHolder.getContext().setActionName(actionName);
			return meta.getHandler().getFormData(uscase, uscase.getUsecaseAction(actionName).getPredefindAction(), formData, crecordId, getLanguage(), reload);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//***********************************************************************************
	@Override
	public SearchPageModel getSearchItems(String usecase,String formClass) {
		try {
			usecase= ServiceFactory.getSecurityProvider().decrypt(usecase);
			formClass=ServiceFactory.getSecurityProvider().decrypt(formClass);
			
			UseCase uscase=  UsecaseUtil.getUsecaseWithName(usecase);
			ContextHolder.getContext().setUsecase(uscase );
			ContextHolder.getContext().setActionName(predefindedActions.search.name());
			UtopiaFormMetaData meta= FormUtil.getMetaData(formClass);
			super.initContextByRequestParameters(meta);
			return meta.getHandler().getSearchPageModel(getLanguage(),uscase,
					(Subject)getSession().get(SecurityProvider.USER_SESSION_ATTRIBUTE_NAME));
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//***********************************************************************************
	@Override
	public SearchPageData getSearchResult(String usecase, String formClass,
			String[][] conditions,String defaultCondition,String []orderBy, int from, int fetchSize,List<ContextItem>contextItems) {
		try {
			usecase= ServiceFactory.getSecurityProvider().decrypt(usecase);
			formClass=ServiceFactory.getSecurityProvider().decrypt(formClass);
			UtopiaFormMetaData meta= FormUtil.getMetaData(formClass);
			super.initContextByRequestParameters(meta);
			ArrayList<String>props=new ArrayList<String>();
			ArrayList<String>values=new ArrayList<String>();
			if(conditions!=null){
				for(String []cond:conditions){
					if(cond==null||cond.length!=2){
						logger.log(Level.WARNING,"bad data for search");
						continue;
						}
					if(cond[0]!=null&&cond[0].trim().length()>0&&cond[1]!=null&&cond[1].trim().length()>0){
						props.add(cond[0].trim());
						values.add(cond[1].trim());
					}
				}
				}
			if(contextItems!=null){
				Map<String,Object>context=ContextHolder.getContext().getContextMap();
				for(ContextItem item:contextItems){
					putContextItemInContext(item,context);
				}
			}
			UseCase usc= UsecaseUtil.getUsecaseWithName(usecase);
			ContextHolder.getContext().setUsecase(usc);
			ContextHolder.getContext().setActionName(predefindedActions.search.name());
			return meta.getHandler().getSearchResult(props.toArray(new String[props.size()]),
					values.toArray(new String[values.size()]),defaultCondition,orderBy, from, fetchSize,  getLanguage());
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//***********************************************************************************
	protected void putContextItemInContext(ContextItem item,Map<String,Object>context){
		try {
			String formClassName= ServiceFactory.getSecurityProvider().decrypt(item.getFormClass());
			
			UtopiaFormMetaData metaData= FormUtil.getMetaData(formClassName);
			if(metaData!=null){
				UtopiaFormMethodMetaData methodMeta= metaData.getMethodMetaData(item.getFieldName());
				Object value= UtopiaTypeConverter.convertFromString(methodMeta.getReturnType(), WebUtil.getLanguage(context), item.getValue());
				context.put(item.getFieldName(), value);
			}
			 
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			
		}
	}
//***********************************************************************************
	public UILookupInfo getImportConfigurations(String usecaseName){
		try {
			CoImporterSettingFacadeRemote facade=(CoImporterSettingFacadeRemote)ServiceFactory.
								lookupFacade(CoImporterSettingFacadeRemote.class.getName());
			usecaseName= ServiceFactory.getSecurityProvider().decrypt(usecaseName);
			
			Long usecaseId;
			if(usecaseName.matches("[0-9]+")){
				usecaseId=Long.parseLong(usecaseName);
			}else{
				UseCase usecase= UsecaseUtil.getUsecaseWithName(usecaseName);
				ContextHolder.getContext().setUsecase(usecase);
				
				usecaseId=usecase.getUsecaseId();
			}
			ContextHolder.getContext().setActionName(predefindedActions.importData.toString());
			List<CoImporterSetting> res=facade.findByProperties(new String[]{"coUsecase.coUsecaseId"}
			, new Object[]{usecaseId}
			,  null);
			UILookupInfo result=new UILookupInfo();
			result.setColumns("key","value");
			result.setDisplayColumnIndex(1);
			if(res!=null){
				String [][]data=new String[res.size()][10];
				int index=0;
				for(CoImporterSetting imset:res){
					data[index][0]=String.valueOf(imset.getCoImporterSettingId());
					data[index][1]=WebUtil.write(imset.getName());
					data[index][3]=WebUtil.write(imset.getComment());
					data[index++][4]=imset.getSetting()==null?"":imset.getSetting();
				}
				result.setData(data);
			}
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//***********************************************************************************
	@Override
	public UILookupInfo getImportFileFormatConfiguration(int fileFormat,String usecase) {
		try {//TODO read data from another table
//			ImportFormat format=(ImportFormat)EnumUtil.getEnumValue(ImportFormat.class, fileFormat);
			CoImporterSettingFacadeRemote facade=(CoImporterSettingFacadeRemote)ServiceFactory.
								lookupFacade(CoImporterSettingFacadeRemote.class.getName());
			usecase= ServiceFactory.getSecurityProvider().decrypt(usecase);
			Long usecaseId;
			if(usecase.matches("[0-9]+")){
				usecaseId=Long.parseLong(usecase);
			}else{
				usecaseId=UsecaseUtil.getUsecaseWithName(usecase).getUsecaseId();
			}
			List<CoImporterSetting> res=facade.findByProperties(new String[]{"coUsecase.coUsecaseId"}
			, new Object[]{usecaseId}
			,  null);
			UILookupInfo result=new UILookupInfo();
			result.setColumns("key","value");
			if(res!=null){
				String [][]data=new String[res.size()][10];
				int index=0;
				for(CoImporterSetting imset:res){
					data[index][0]=WebUtil.write(imset.getCoImporterSettingId());
					data[index][1]=WebUtil.write(imset.getName());
//					data[index][2]=WebUtil.write(imset.getRegexp());
					data[index][3]=WebUtil.write(imset.getComment());
					data[index][4]=WebUtil.write(imset.getSetting());
//					data[index][5]=WebUtil.write(imset.getSql());
//					data[index][6]=WebUtil.write(imset.getResourceName());
//					data[index][7]=WebUtil.write(imset.getScdStartDate()!=null?DateUtil.getDateString(imset.getScdStartDate(), language):"");
//					data[index][8]=WebUtil.write(imset.getScdEndDate()!=null?DateUtil.getDateString(imset.getScdEndDate(), language):"");
//					if(imset.getScdStartDate()!=null){
//						Calendar c=Calendar.getInstance();
//						c.setTime(imset.getScdStartDate());
//						int hour=c.get(Calendar.HOUR);
//						int min=c.get(Calendar.MINUTE);
//						data[index++][9]=(hour<10?"0"+hour:hour)+":"+(min<10?"0"+min:min)+" "+(c.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM");
//					}else{
//						data[index++][9]="";
//					}
				}
				result.setData(data);
			}
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//***********************************************************************************
	@Override
	public UILookupInfo getImportTypes() {
		UILookupInfo result= AbstractUtopiaUIHandler.convertNamePairTolookupInfo(
				EnumUtil.getEnumLookups(Constants.ImportFormat.class, 
						getLanguage()),getLanguage());
		result.setSelectedId(String.valueOf( Constants.ImportFormat.excel.ordinal()));
		return result;
	}
//***********************************************************************************
	@Override
	public Long prepareReport(String usecaseName, String formClass,
			Long[] recordIds, String orderByCol) {
		try {
			Long currentUID= getCurrentReportUID();
			usecaseName= ServiceFactory.getSecurityProvider().decrypt(usecaseName);
			formClass=ServiceFactory.getSecurityProvider().decrypt(formClass);
			UtopiaFormMetaData meta= FormUtil.getMetaData(formClass);
			
			UseCase usecase= UsecaseUtil.getUsecaseWithName(usecaseName);
			ActionContext.getContext().put(SEARCH_REPORT_UID_PARAM_NAME+currentUID, new ReportModel(usecase,meta,recordIds,orderByCol));
			return currentUID;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return -1l;
	}
//***********************************************************************************	
	private Long getCurrentReportUID() {
		Map<String,Object> session=getSession();
		Long currentId=0l;
		for(Long i=0l;i<Long.MAX_VALUE;i++){
			Object o= session.get(SEARCH_REPORT_UID_PARAM_NAME+i.longValue());
			if(o==null){
				currentId=i;
				break;
			}
		}
		
		return currentId;
	}
	
//*******************************************************************************************
	public static int getReportRecordCount(long reportUID,HttpSession session){
		ReportModel model=(ReportModel)session.getAttribute(SEARCH_REPORT_UID_PARAM_NAME+reportUID);
		if(model!=null){
			return model.getMeta().getHandler().getReportRecordCount(model);
		}
		return -1;
	}
//*******************************************************************************************
	public static int getReportPageCount(long reportUID,HttpSession session){
		int recordCount=getReportRecordCount(reportUID, session);
		int lessThan=recordCount%REPORT_PAGE_SIZE;
		return lessThan==0?recordCount/REPORT_PAGE_SIZE:recordCount/REPORT_PAGE_SIZE+1;
		
	}
//*******************************************************************************************
	public static Vector<Map<String,Object>> getReportData(long reportUID,int from,int to,HttpSession session){
		
		ReportModel model=(ReportModel)session.getAttribute(SEARCH_REPORT_UID_PARAM_NAME+reportUID);
		if(model!=null){
			from=from<=1?0:from-1;
			to=to<0?REPORT_PAGE_SIZE:to;
			
			return model.getMeta().getHandler().getReportData(model, from, to);
		}
		return null;
	}
//*******************************************************************************************
	public static String getReportName(long reportUID,HttpSession session) {
		ReportModel model=(ReportModel)session.getAttribute(SEARCH_REPORT_UID_PARAM_NAME+reportUID);
		if(model!=null){
			 SubSystem sbSys=ServiceFactory.getSubsystemConfiguration(model.getUsecase().getSubSystemId());
			 String bundelPath=sbSys==null?null:sbSys.getUsecaseBundelName();
			 bundelPath=bundelPath==null? model.getMeta().getClazz().getName():bundelPath;
			 String header= MessageHandler.getMessage(model.getUsecase().getFullName(),bundelPath,WebUtil.getLanguage(session));
			 String actionName=MessageHandler.getMessage(predefindedActions.report.toString(),"ir.utopia.core.basicinformation.action.action" , WebUtil.getLanguage(session));
			 header= WebUtil.isRigthToLeft(session)?(actionName+" "+header):(header+" "+actionName);
			 return header;
		}
		return null;
	}
//*******************************************************************************************	
	@Override
	public ProcessConfigurationModel getProcessConfiguration(Long usecaseActionId) {
		ProcessModel model= ServiceFactory.getProcessHandler().getProcesses(usecaseActionId);//set the name of action even import excel ,or import data
		if(model!=null){
			Map<String ,Object>context= ContextUtil.createContext(getSession());
			ProcessConfigurationModel result=new ProcessConfigurationModel();
			HashMap<String, String>defaultValues=new HashMap<String, String>();
			InputItem []items= convertProcessParamToInputItem(model,defaultValues,context);
			result.setItems(items);
			result.setTitle(MessageHandler.getMessage(model.getProcessName(), model.getProcessBundelPath(), getLanguage()));
			result.setProcessId(usecaseActionId);
			result.setProcessSubmitPath(model.getProcessExecutionPath());
			result.setRefreshPageAfterProcess(model.isRefreshAfter());
			result.setAlertForSuccess(model.isAlertSuccess());
			result.setDefaultValues(defaultValues);
			return result;	//result.getItems()[0].getLookupInfo().setData(new String[][]{{"1","Debug1"},{"2","Debug2"}});
		}
		return null;
	}

//***********************************************************************************	
private InputItem[] convertProcessParamToInputItem( ProcessModel model,HashMap<String, String>defaultValues,Map<String,Object>context){
	ProcessParameter []params=model.getParameters();
	if(params!=null&&params.length>0){
		
		ArrayList<InputItem> result=new ArrayList<InputItem>();
		for(int i=0;i<params.length;i++){
			if(ActionParameterTypes.processParameter.equals(params[i].getType())){
				InputItem item=new InputItem();
				item.setColumnName(params[i].getName());
				item.setHeader(MessageHandler.getMessage(model.getProcessName()+"."+params[i].getName(),
						model.getProcessBundelPath(), getLanguage()) );
				item.setDisplayType(GWTUtil.convertDisplayType(params[i].getDisplayType()));
				item.setIndex(params[i].getDisplayIndex());
				item.setMinValue(params[i].getMinValue());
				item.setMaxValue(params[i].getMaxValue());
				item.setMandatory(params[i].isMandatory());
				item.setConfirmRequired(params[i].isConfirmRequired());
				item.setMandatory(params[i].isMandatory());
				if(DisplayTypes.lookup.equals(params[i].getDisplayType())){
					LookupConfiguration confModel=params[i].getLookupModel();
					String condition= confModel.condition();
					if(condition!=null){
						List<String>deps= LogicParser.getParametersInString(condition);
						if(deps!=null&&deps.size()>0){
							item.mapDependency(InputItem.DEPENDENCY_TYPE_DATA_FILTER, deps.toArray(new String[deps.size()]));
						}
					}
					
					item.setLookupInfo(AbstractUtopiaUIHandler.convertNamePairTolookupInfo(BeanUtil.loadLookup(confModel, context),
							(confModel.orderby()==null||confModel.orderby().length==0||confModel.orderby()[0].trim().length()==0),
							getLanguage()));
				}else if(DisplayTypes.list.equals(params[i].getDisplayType())){
					item.setLookupInfo(AbstractUtopiaUIHandler.convertNamePairTolookupInfo(
							EnumUtil.getEnumLookups(params[i].getLookupModel().enumuratedClass(),getLanguage()),getLanguage()));
				}else if(DisplayTypes.LOV.equals(params[i].getDisplayType())){
					item.setLOVConfiguration(getLOVConfiguration(params[i].getLovConfiguration()));
				}else if(DisplayTypes.Date.equals(params[i].getDisplayType())){
					DateInfo dInfo=new DateInfo();
					dInfo.setDateType(WebUtil.getDateFormat(getLanguage()));
					item.setDateInfo(dInfo);
				}
				item.setReadOnlyLogic(params[i].getReadOnlyLogic());
				item.setDisplayLogic(params[i].getDisplayLogic());
				Object defaultValue=LogicEvaluator.evaluateDefaultValueLogic(params[i].getDefaultValue(), context,
													GWTUtil.getPreferedTypeForDisplayType(item.getDisplayType()));
				defaultValues.put(params[i].getName(),getFormatedValueOf(defaultValue, item,  context) );
				result.add(item);
			}
		}
		for(InputItem item:result){
			String readOnlyLogic=mapDependencyAndGetLogic(result, item, item.getReadOnlyLogic(),  InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY, context);
			String displayLogic=mapDependencyAndGetLogic(result, item, item.getDisplayLogic(),  InputItem.DEPENDENCY_TYPE_DATA_DISPLAY, context);
			item.setDisplayLogic(displayLogic);
			item.setReadOnlyLogic(readOnlyLogic);
		}
		return result.toArray(new InputItem[result.size()]);
	}
	return null;
}
//*************************************************************************************
protected  UILOVConfiguration getLOVConfiguration(LOVConfiguration lovConfiguration){
	UILOVConfiguration UILOV=new  UILOVConfiguration();
	UsecaseUIInfo info;
		 info=UIServiceFactory.getUsecase( lovConfiguration.searchLOVForm());
		if(info==null){
			logger.log(Level.WARNING,"fail to find usecase configuration for lovMeta data for formClass:"+lovConfiguration.searchLOVForm()+" ,please check configuration in:"+UIServiceFactory.UI_CONFIG_FILE_NAME);
			return null;
			}
		
	UILOV.setUsecaseName(ServiceFactory.getSecurityProvider().encrypt(info.getUsecase().getFullName()));
	UILOV.setSearchFormClass(ServiceFactory.getSecurityProvider().encrypt( lovConfiguration.searchLOVForm().getName()));
	String conditions=lovConfiguration.conditions();
	String encodedConditions=null;
	if(conditions!=null&&conditions.trim().length()>0){
			encodedConditions=ServiceFactory.getSecurityProvider().encrypt(conditions);
	}
	UILOV.setCondiotions(encodedConditions);
	UILOV.setMultiSelect(lovConfiguration.isMultiSelect());
	UILOV.setDisplayBoxColumns(lovConfiguration.boxDisplayingColumns());
	UILOV.setDisplaItemSeperator(lovConfiguration.boxDisplaySeperator());
	
	String bundelPath=ServiceFactory.getSubsystemConfiguration(info.getUsecase().getSubSystemId()).getUsecaseBundelName();
	bundelPath=bundelPath==null? lovConfiguration.searchLOVForm().getName():bundelPath;
	String header= MessageHandler.getMessage(info.getUsecase().getFullName(),bundelPath,getLanguage() );
	String actionName=MessageHandler.getMessage(predefindedActions.search.toString(),"ir.utopia.core.basicinformation.action.action" ,getLanguage());
	header= WebUtil.isRigthToLeft(getLanguage())?(actionName+" "+header):(header+" "+actionName);
	UILOV.setLovSearchTitle(header);
	return UILOV;
} 
//*************************************************************************************
protected static String mapDependencyAndGetLogic(List<InputItem>allItems,InputItem cItem,String logic,int dependencyMapType,Map<String,Object>context){
	if(logic!=null&&logic.trim().length()>0){
		int evaluationType=Evaluator.getEvaluationType(logic);
		if(evaluationType==Evaluator.LOGIC_EVALUATION_TYPE_COMBINATIONAL){
			String []logics= LogicParser.splitCombinationalLogic(logic);
			for(String l:logics){
				 if(Evaluator.getEvaluationType(l)==Evaluator.LOGIC_EVALUATION_TYPE_GROOVY){
						logic=logic.replace("{"+l+"}", String.valueOf(Evaluator.evaluateLogic(l, context)));
					}else{
						logic=logic.replace("{"+l+"}", l);
					}
			}
			return mapDependencyAndGetLogic(allItems, cItem, logic, dependencyMapType, context);
		}
		else if(evaluationType==Evaluator.LOGIC_EVALUATION_TYPE_GROOVY){
			logic=String.valueOf(Evaluator.evaluateLogic(logic, context));
		}else{
			List<String>params=LogicParser.getParametersInString(logic);
			if(params!=null&&params.size()>0){
				initDependencyMap(allItems, cItem, params.toArray(new String[params.size()]), dependencyMapType);
			}
			List<String>javaParams= LogicParser.getJavaParameters(logic);
			for(String javaParam:javaParams){
				Object javaValue= LogicParser.getJavaParameterValue(javaParam, context);
				logic=LogicParser.replaceJavaVariable(logic, javaParam,Enum.class.isInstance(javaValue)?String.valueOf(((Enum<?>)javaValue).ordinal()):"-1" );
			}
		}
		
		
	}
	return logic;
}
//******************************************************************************************	
	private static void initDependencyMap(List<InputItem>allItems,
			InputItem cItem, String []params,int dependencyType) {
		
		if(params!=null)
			for(String param:params){
				for(InputItem parent:allItems){
					if(parent.getColumnName().equals(param)){
						cItem.mapDependency(dependencyType, parent.getColumnName());
					}
				}
				
			}
	}
//*************************************************************************************
	protected  String getFormatedValueOf(Object input,InputItem item,Map<String,Object>context){
		int displayType=item.getDisplayType();
		Object result=input;
			if(input !=null ){
				if(!(input instanceof String)){
					if(input instanceof Enum){
						result= String.valueOf(((Enum<?>) input).ordinal());
					}else{
						result= input;
					}	
				}
			}
			return (displayType!=InputItem.DISPLAY_TYPE_COMBOBOX&&displayType!=InputItem.DISPLAY_TYPE_LIST&&displayType!=InputItem.DISPLAY_TYPE_NUMERIC&&
					displayType!=InputItem.DISPLAY_TYPE_RADIO_BUTTON&&
					displayType!=InputItem.DISPLAY_TYPE_DATE)? WebUtil.write(input,ContextUtil.getLoginLanguage(context)):
				(result==null?"":result.toString());
	}
//*************************************************************************************
public UILookupInfo reloadLookup(Long usecaseActionId, String lookupParmeter,
		String[] dependentFields, String[] dependesValue) {
	UILookupInfo result=reloadProcessLookup(usecaseActionId, lookupParmeter, dependentFields, dependesValue);
	result=result==null?reloadBasicActionLookup(usecaseActionId, lookupParmeter, dependentFields, dependesValue):result;
	return result;
}	
//*************************************************************************************
private UILookupInfo reloadBasicActionLookup(Long usecaseActionId, String lookupParmeter,
		String[] dependentFields, String[] dependesValue){
	
	try {
		UseCase usecase= UsecaseUtil.getUsecaseFromUsecaseAtion(usecaseActionId);
		UsecaseUIInfo uiInfo= UIServiceFactory.getUsecase(usecase.getUsecaseId());
		if(uiInfo==null){
			return null;
		}
		UtopiaFormMethodMetaData reloadingMethodMeta= uiInfo.getMeta().getMethodMetaData(lookupParmeter);
		if(dependentFields!=null){
			 Map<String,Object>context= ContextUtil.createContext(getSession());
			 LookupConfigurationModel lookupModel=uiInfo.getMeta().getLookupConfigurationModel(reloadingMethodMeta, context);
			for(int i=0;i<dependentFields.length;i++){
				UtopiaFormMethodMetaData parentMethodMeta= uiInfo.getMeta().getMethodMetaData(dependentFields[i]);
				Object value=UtopiaTypeConverter.convertFromString(parentMethodMeta.getReturnType(), getLanguage(), dependesValue[i]) ;
				context.put(dependentFields[i], value);
				lookupModel.setParameterValue(dependentFields[i], value);
			}
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(BeanUtil.loadLookup(lookupModel),getLanguage());
		}
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
	return null;
}
//*************************************************************************************
public UILookupInfo reloadLookup(String formClass,String usecaseName,String actionName,String lookupParmeter,
		String[] dependentFields, String[] dependesValue){
	ThreadGroup parentThread =
			Thread.currentThread().getThreadGroup();
			while (parentThread.getParent() != null) {
			parentThread = parentThread.getParent();
			}
//			String language=WebUtil.getLanguage(session);
//			String usecaseDecryptedName=ServiceFactory.getSecurityProvider().decrypt(usecaseName);
//			String actionDecryptedName=ServiceFactory.getSecurityProvider().decrypt(actionName);
			UtopiaFormMetaData meta=getFormMetaData(formClass);
//			return	meta.getHandler().getDataInputFormModel( usecaseDecryptedName, actionDecryptedName, language,ContextUtil.createContext(session));
			return meta.getHandler().reloadLookup(lookupParmeter, dependentFields, dependesValue);
}
//*************************************************************************************
private UILookupInfo reloadProcessLookup(Long usecaseActionId, String lookupParmeter,
		String[] dependentFields, String[] dependesValue){
	try {
		ProcessModel model= ServiceFactory.getProcessHandler().getProcesses(usecaseActionId);
		if(model!=null){
			ProcessParameter []params= model.getParameters();
			if(params!=null){
				 Map<String,Object>context= ContextUtil.createContext(getSession());
				 if(dependentFields!=null){
					 for(int i=0;i<dependentFields.length;i++){
						 Object value;
						 if(dependesValue[i]==null||dependesValue[i].trim().length()==0){
							 if(context.containsKey(dependentFields[i])){
								 value=context.get(dependentFields[i]);
							 }else{
								 value="";
							 }
						 }
						 else{
							 value=formatParameterValue(findProcessParameter(params, dependentFields[i]),dependesValue[i]);
						 }
						 context.put(dependentFields[i], value);
					 }
				 }
				 ProcessParameter param=findProcessParameter(params, lookupParmeter);
					if(param.getName().equals(lookupParmeter)){
						if(DisplayTypes.lookup.equals(param.getDisplayType())){
							return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(BeanUtil.loadLookup(param.getLookupModel(), context),getLanguage());
						}
					}
					
			}
			
		}
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
	
	return null;
}
//*************************************************************************************
private  ProcessParameter findProcessParameter(ProcessParameter []params ,String paramName){
	for(ProcessParameter param:params){
		if(param.getName().equals(paramName)){
			return param;
		}
	}
	return null;
}
//*************************************************************************************
private Object formatParameterValue(ProcessParameter param,String value){
	try {
		if(param!=null){
			if(DisplayTypes.lookup.equals(param.getDisplayType())){
				if(value!=null){
					return Long.parseLong(value.toString());
				}return -1l;
			}else if(DisplayTypes.checkBox.equals(param.getDisplayType())){
				return Boolean.parseBoolean(value);
			}
			return false;
		}
	} catch (Exception e) {
		logger.log(Level.WARNING,"fail to parse value --->"+value+" for parameter ---->"+param.getName());
	}
	return value;
}
//*************************************************************************************
public TreeNode[] loadchildren(String usecaseName,String formClass, Long nodeId) {
	try {
		usecaseName=ServiceFactory.getSecurityProvider().decrypt(usecaseName);
		ContextHolder.getContext().getContextMap().put(ContextUtil.RECORD_ID_KEY_IN_CONTEXT, nodeId);
		ContextHolder.getContext().setActionName(predefindedActions.view.toString());
		UseCase usc=UsecaseUtil.getUsecaseWithName(usecaseName);
		ContextHolder.getContext().setUsecase(usc);
		return getFormMetaData(formClass).getHandler().loadUsecaseTreeNodes(usc, nodeId);
	} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
	}
	return null;
}
//*************************************************************************************

}
