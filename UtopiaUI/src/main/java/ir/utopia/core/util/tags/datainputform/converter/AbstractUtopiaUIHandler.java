package ir.utopia.core.util.tags.datainputform.converter;

import static ir.utopia.core.struts.FormAndPersistentConverter.invokeObjectMethod;
import ir.utopia.common.basicinformation.fiscalyear.FiscalYearInfo;
import ir.utopia.common.basicinformation.fiscalyear.FiscalYearUtil;
import ir.utopia.common.dashboard.bean.CmTransitionFacadeRemote;
import ir.utopia.common.dashboard.persistent.TransitionRecord;
import ir.utopia.common.doctype.util.DocTypeUtil;
import ir.utopia.common.doctype.util.UsecaseDocInfo;
import ir.utopia.common.doctype.util.UsecaseDocStatusInfo;
import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UIServiceFactory;
import ir.utopia.core.attachment.bean.CoAttachmentFacadeRemote;
import ir.utopia.core.attachment.persistent.CoAttachment;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.bean.DefaultBeanReportModel;
import ir.utopia.core.bean.EntityPair;
import ir.utopia.core.bean.LookUpLoaderBean;
import ir.utopia.core.bean.OrderBy;
import ir.utopia.core.bean.SearchConditionItem;
import ir.utopia.core.bean.TreeLoaderBean;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.BooleanType;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.constants.QueryComparsionType;
import ir.utopia.core.form.annotation.DataInputForm.InputPageLayout;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityDataTypeMap;
import ir.utopia.core.form.annotation.FormPersistentAttribute.FormToEntityMapType;
import ir.utopia.core.form.annotation.IncludedForm.IncludedFormType;
import ir.utopia.core.logic.util.AnnotationUtil;
import ir.utopia.core.logic.util.Evaluator;
import ir.utopia.core.logic.util.LogicParser;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.model.UsecaseUIInfo;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.persistent.parentchild.ParentChildPair;
import ir.utopia.core.struts.CustomButtonConfiguration;
import ir.utopia.core.struts.FormAndPersistentConverter;
import ir.utopia.core.struts.FormColorLogic;
import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.struts.IncludedFormMetaData;
import ir.utopia.core.struts.ItemsDisplayConfiguration;
import ir.utopia.core.struts.UtopiaBasicForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData.LOVMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData.NativeConfiguration;
import ir.utopia.core.struts.UtopiaPageLinkMetaData;
import ir.utopia.core.struts.UtopiaTypeConverter;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.util.Cache;
import ir.utopia.core.util.DateUtil;
import ir.utopia.core.util.EnumUtil;
import ir.utopia.core.util.GWTUtil;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.logic.LogicEvaluator;
import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.ColorLogic;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.AbstractPageDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.AttachmentInfo;
import ir.utopia.core.util.tags.datainputform.client.model.CustomButton;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.LinkDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILOVConfiguration;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;
import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNodeComparator;
import ir.utopia.core.util.tags.datainputform.server.ReportModel;
import ir.utopia.core.util.tags.model.ViewPageModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;


public abstract class AbstractUtopiaUIHandler implements UtopiaUIHandler {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractUtopiaUIHandler.class.getName());
	}
	protected UtopiaFormMetaData meta;
	private static final Cache<String,Method>FORM_METHOD_CACHE=new Cache<String, Method>();
	private static final Cache<String,Class<?>>SEARCH_LOOKUP_ENITY_CACHE=new Cache<String, Class<?>>();
	private static final Class<?> []c=new Class<?>[]{};
	private static final Object []o=new Object[]{};
	
	
//***********************************************************************	
	public void setMetaData(UtopiaFormMetaData meta){
		this.meta=meta;
	}
//***********************************************************************	
	public static InputItem[] convertMethodMetaToInputItem(UtopiaFormMetaData formMeta,UtopiaFormMetaData parentFormMeta,Map<String,Object> context,UseCase usecase,predefindedActions action, String language,boolean loadLookups){
		ArrayList<InputItem>result=new ArrayList<InputItem>();
		List<UtopiaFormMethodMetaData> metaDatas;
		String parentFieldname= BeanUtil.getSelfParentLinkFieldName(formMeta.getPersistentClass());
		String parentPersistentMethodLinkName=null;
		if(parentFieldname!=null){
			parentPersistentMethodLinkName= AnnotationUtil.getGetterMethodName(parentFieldname);
		}
		if(predefindedActions.report.equals(action)){
			metaDatas=formMeta.getReportItems();
		}else{
		  metaDatas= formMeta.getInputItems();
		  }
		Map<InputItem,String>enddateMap=new HashMap<InputItem, String>();
		for(UtopiaFormMethodMetaData meta:metaDatas){
				InputItem cItem=convertFromMetaToInpuItem(formMeta,meta,parentFormMeta, context, language,enddateMap,action,loadLookups);
				if(parentPersistentMethodLinkName!=null&&parentPersistentMethodLinkName.equals(meta.getPersistentMethodName())){
					cItem.setParentLinkItem(true);
				}
				result.add(cItem);
		}
		IncludedFormMetaData []grids= formMeta.getIncludedForms();
		if(grids!=null&&grids.length>0){
			for(IncludedFormMetaData grid:grids){
				InputItem cItem=new InputItem();
				cItem.setColumnName(grid.getName());
				if(IncludedFormType.Interactive.equals(grid.getType())){
					cItem.setGridMetaData(getGridMetaData(cItem, formMeta, context, language,  grid,action));
					cItem.setDisplayType(InputItem.DISPLAY_TYPE_GRID);
				}else{
					UtopiaFormMetaData gridFormMetaData= FormUtil.getMetaData(grid.getFormClass()); 
					SearchPageModel searchModel=gridFormMetaData.getHandler().getSearchPageModel(language, usecase, ContextUtil.getUser(context));
					cItem.setSearchModel(searchModel);
					cItem.setDisplayType(InputItem.DISPLAY_TYPE_SEARCH_GRID);
				}
				result.add(cItem);
			}
		}
		
			for(InputItem it:enddateMap.keySet()){
				String endItem= enddateMap.get(it);
				for(InputItem cur: result){
					if(cur.getColumnName().equals(endItem)&&it.getDateInfo()!=null){
						it.getDateInfo().setEndDateItem(cur);
					}
				}
			}
		
		return result.toArray(new InputItem[result.size()]);
	}
	

//***********************************************************************************
	protected static InputItem convertFromMetaToInpuItem(UtopiaFormMetaData formMeta,UtopiaFormMethodMetaData mMeta,UtopiaFormMetaData parentFormMeta,Map<String,Object>context, String language,Map<InputItem,String>enddateMap,boolean loadLookups){
		return convertFromMetaToInpuItem(formMeta, mMeta,parentFormMeta, context, language, enddateMap,predefindedActions.save,loadLookups);
	}
//***********************************************************************************
	protected static InputItem convertFromMetaToInpuItem(UtopiaFormMetaData formMeta,UtopiaFormMethodMetaData mMeta,UtopiaFormMetaData parentFormMeta,Map<String,Object>context, String language,Map<InputItem,String>enddateMap,predefindedActions action,boolean loadLookups){
		int displayType=GWTUtil.convertDisplayType(mMeta.getInputTypeDisplayType());
		InputItem cItem= new InputItem();
		if(displayType==InputItem.DISPLAY_TYPE_DATE){
			DateInfo info=new DateInfo();
			cItem.setDateInfo(info);
			info.setDateType(WebUtil.getDateFormat(language));
			if(mMeta.getEndDateItem()!=null&&enddateMap!=null){
				enddateMap.put(cItem, mMeta.getEndDateItem());
			}
		}
		cItem.setHeader(WebUtil.write(formMeta.getHeader(mMeta, language),language));
		cItem.setDisplayType(displayType);
		cItem.setMinValue(mMeta.getMinValue());
		cItem.setMaxValue(mMeta.getMaxValue());
		cItem.setColumnName(mMeta.getFieldName());
		cItem.setReadOnly(mMeta.readOnlyOn(action));
		cItem.setIndex(mMeta.getInputFormIndex());
		if(!cItem.isReadOnly()){
			cItem.setReadOnly(LogicEvaluator.evaluateReadonlyLogic(mMeta.getReadonlyLogic(action), context));
		}
		if(mMeta.displayOn(action)){
			cItem.setVisible( LogicEvaluator.evaluateDisplayLogic(mMeta.getDisplayLogic(action), context));
		}else{
			cItem.setDisplayType(InputItem.DISPLAY_TYPE_HIDDEN);
		}
		
		cItem.setBreakLineAfer(mMeta.isBreakLineAfter());
		if(cItem.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX||cItem.getDisplayType()==InputItem.DISPLAY_TYPE_LIST||
				cItem.getDisplayType()==InputItem.DISPLAY_TYPE_RADIO_BUTTON){
			if(loadLookups){
				List<NamePair>pairs= formMeta.getHandler(). LoadLookupNamePairs(mMeta,context,language);
				LookupConfigurationModel model= formMeta.getLookupConfigurationModel(mMeta, context);
				cItem.setLookupInfo(convertNamePairTolookupInfo(pairs,model==null?true:model.isOrdered(),language));
			}
			LookupConfigurationModel model= formMeta.getLookupConfigurationModel(mMeta, context);
			if(model!=null){
				List<Condition>conditions= model.getConditions();
				if(conditions!=null){
					for(Condition cond:conditions){
						String [] params=cond.getParameters();
						initDependencyMap(formMeta,parentFormMeta, cItem, params,InputItem.DEPENDENCY_TYPE_DATA_FILTER);
					}
				}
			}
			String dynamicValidationLogic= mMeta.getDynamicValidation();
			if(dynamicValidationLogic!=null&&dynamicValidationLogic.trim().length()>0){
				List<String> params =LogicParser.getParametersInString(dynamicValidationLogic);
				if(params!=null){
					initDependencyMap(formMeta,parentFormMeta, cItem, params.toArray(new String[params.size()]), InputItem.DEPENDENCY_TYPE_DATA_FILTER);
				}
			}
		}
		 
		else if(cItem.getDisplayType()==InputItem.DISPLAY_TYPE_LOV){
			LOVMetaData lovConfig= mMeta.getLOVConfiguration(action);
			if(lovConfig!=null&&lovConfig.getConditions()!=null&&lovConfig.getConditions().trim().length()>0){
				List<String> params =LogicParser.getParametersInString(lovConfig.getConditions());
				if(params!=null){
					initDependencyMap(formMeta,parentFormMeta, cItem, params.toArray(new String[params.size()]), InputItem.DEPENDENCY_TYPE_DATA_FILTER);
				}
				
			}
		}
		
			String displayLogic=mMeta.getDisplayLogic();
			displayLogic= mapDependency(formMeta,cItem,parentFormMeta,displayLogic, InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,context);
			cItem.setDisplayLogic(displayLogic);
			String readOnlyLogic=mMeta.getReadonlyLogic();
			readOnlyLogic=mapDependency(formMeta,cItem,parentFormMeta,readOnlyLogic, InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,context);
			cItem.setReadOnlyLogic(readOnlyLogic);
		
		if(cItem.getDisplayType()==InputItem.DISPLAY_TYPE_NUMERIC){
			cItem.setDecimalPrecision(mMeta.getDecimalPercision());
			cItem.setDecimalSeperator(mMeta.getDecimalSeperator());
			cItem.setUseDecimalSeperator(mMeta.isUseDecimalSeperator());
		}
		
		cItem.setConfirmRequired(mMeta.shouldConfirm());
		cItem.setMandatory(mMeta.isMandatory());
		
		initNativeEvents(cItem, mMeta, action);
		initLOVConfiguration(cItem, mMeta, action,context);
		cItem.setPreferedWidth(mMeta.getPreferedWidth());
		return cItem;
	}
//******************************************************************************************
	protected static String mapDependency(UtopiaFormMetaData formMeta,InputItem cItem,UtopiaFormMetaData parentFormMeta,String logic,int dependencyMapType,Map<String,Object>context){
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
				return mapDependency(formMeta, cItem, parentFormMeta, logic, dependencyMapType, context);
			}
			else if(evaluationType==Evaluator.LOGIC_EVALUATION_TYPE_GROOVY){
				logic=String.valueOf(Evaluator.evaluateLogic(logic, context));
			}else{
				List<String>params=LogicParser.getParametersInString(logic);
				if(params!=null&&params.size()>0){
					initDependencyMap(formMeta,parentFormMeta, cItem ,params.toArray(new String[params.size()]), dependencyMapType);
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
	private static void initDependencyMap(UtopiaFormMetaData formMeta,UtopiaFormMetaData parentFormMeta,
			InputItem cItem, String []params,int dependencyType) {
		
		if(params!=null)
			for(String param:params){
				UtopiaFormMethodMetaData parent=formMeta.getMethodMetaData(param);
				if(parent==null&&parentFormMeta!=null){
					parent=parentFormMeta.getMethodMetaData(param);
				}
				if(parent!=null){
					cItem.mapDependency(dependencyType, parent.getFieldName());
				}
			}
	}
//******************************************************************************************
	private static void initNativeEvents(InputItem item,UtopiaFormMethodMetaData meta,predefindedActions action){
		List<NativeConfiguration>nativeConfs= meta.getNativeConfigurations(action);
		if(nativeConfs!=null){
			List<String>eventHandleres=new ArrayList<String>();
			List<Integer>triggeringEvents=new ArrayList<Integer>();
			for(NativeConfiguration conf:nativeConfs){
				triggeringEvents.add( conf.getEventType().getDataInputKey());
				eventHandleres.add(conf.getFunctionName());
			}
			if(eventHandleres.size()>0){
				item.setTriggeringEvents(triggeringEvents.toArray(new Integer[triggeringEvents.size()]));
				item.setTriggerHandlers(eventHandleres.toArray(new String[eventHandleres.size()]));
			}
		}
		
	}
//******************************************************************************************
	private static void initLOVConfiguration(InputItem item,UtopiaFormMethodMetaData meta,predefindedActions action,Map<String,Object>context){
		LOVMetaData LOVmeta= meta.getLOVConfiguration(action);
		UsecaseUIInfo info;
		if(LOVmeta!=null){
			 info=UIServiceFactory.getUsecase( LOVmeta.getFromClass());
			if(info==null){
				logger.log(Level.WARNING,"fail to find usecase configuration for lovMeta data for formClass:"+LOVmeta.getFromClass().getName()+" ,please check configuration in:"+UIServiceFactory.UI_CONFIG_FILE_NAME);
				return;
			}
			UILOVConfiguration UILOV=new  UILOVConfiguration();
			UILOV.setUsecaseName(ServiceFactory.getSecurityProvider().encrypt(info.getUsecase().getFullName()));
			UILOV.setSearchFormClass(ServiceFactory.getSecurityProvider().encrypt( LOVmeta.getFromClass().getName()));
			String conditions=LOVmeta.getConditions();
			String encodedConditions=null;
			if(conditions!=null&&conditions.trim().length()>0){
					encodedConditions=ServiceFactory.getSecurityProvider().encrypt(conditions);
			}
			UILOV.setCondiotions(encodedConditions);
			UILOV.setMultiSelect(LOVmeta.isMultiSelect());
			UILOV.setDisplayBoxColumns(LOVmeta.getBoxSelectingColumns());
			UILOV.setDisplaItemSeperator(LOVmeta.getDisplayItemSeperator());
			String language=ContextUtil.getLoginLanguage(context);
			String bundelPath=ServiceFactory.getSubsystemConfiguration(info.getUsecase().getSubSystemId()).getUsecaseBundelName();
			bundelPath=bundelPath==null? LOVmeta.getFromClass().getName():bundelPath;
			String header= MessageHandler.getMessage(info.getUsecase().getFullName(),bundelPath,language );
			String actionName=MessageHandler.getMessage(predefindedActions.search.toString(),"ir.utopia.core.basicinformation.action.action" ,language);
			header= WebUtil.isRigthToLeft(language)?(actionName+" "+header):(header+" "+actionName);
			UILOV.setLovSearchTitle(header);
			item.setLOVConfiguration(UILOV);
		}
	}
//******************************************************************************************
	/**
	 * 
	 * @param item
	 * @param mMeta
	 * @param formMetaData
	 * @return
	 */
		public List<NamePair> LoadLookupNamePairs(UtopiaFormMethodMetaData mMeta,Map<String,Object> context,String language){
			if(DisplayTypes.lookup.ordinal()==mMeta.getInputTypeDisplayType()){
				try {
					LookupConfigurationModel model= meta.getLookupConfigurationModel(mMeta, context);
					Class<? extends UtopiaBasicPersistent>persistentClass= model.getOwnPersitentClass();
					Class<?>bean=BeanUtil.findRemoteClassFromPersistent(persistentClass);
					LookUpLoaderBean  remote =(LookUpLoaderBean)ServiceFactory.lookupFacade(bean.getName());
					return remote.loadLookup( );
				} catch (Exception e) {
					logger.log(Level.WARNING,"", e);
				}
			 	
			}else if(DisplayTypes.list.ordinal() == mMeta.getInputTypeDisplayType() || DisplayTypes.RadioButton.ordinal() == mMeta.getInputTypeDisplayType()){
				return EnumUtil.getEnumLookups( meta.getEnumClass(mMeta), language);
			}
			return null;
		}
//***********************************************************************************
		@Override
		public UILookupInfo reloadLookup(String fieldName,
				String[] dependentFields, String[] dependesValues) {
			try {
				UtopiaFormMetaData destinationMetaData=null;
				UtopiaFormMethodMetaData mMeta=null;
				if(fieldName.indexOf(".")>0){
					String detailName=fieldName.substring(0,fieldName.indexOf("."));
					fieldName=fieldName.substring(fieldName.indexOf(".")+1,fieldName.length());
					IncludedFormMetaData include=	meta.getIncludedForm(detailName);
					 UtopiaFormMetaData iMeta= FormUtil.getMetaData(include.getFormClass());
					 if(iMeta.getMethodMetaData(fieldName)!=null){
						 destinationMetaData=iMeta;
						 mMeta=iMeta.getMethodMetaData(fieldName);
					 }
					}else{
					destinationMetaData=meta;
					mMeta=meta.getMethodMetaData(fieldName);
				}
				if(destinationMetaData!=null&&mMeta!=null){
					Map<String,Object>context=ContextHolder.getContext().getContextMap();
					if(dependentFields!=null&&dependesValues!=null&&dependentFields.length>0){
						for(int i=0;i<dependentFields.length;i++){
							UtopiaFormMethodMetaData cMeta= destinationMetaData.getMethodMetaData(dependentFields[i]);
							if(cMeta!=null){
								context.put(dependentFields[i],formatMethodValue(cMeta, dependesValues[i],context));
							}else{
								context.put(dependentFields[i], dependesValues[i]);
							}
							
						}
					}
					LookupConfigurationModel configModel= destinationMetaData.getLookupConfigurationModel(mMeta, context);
					Class<? extends UtopiaBean> facadeClass= BeanUtil.findRemoteClassFromPersistent(configModel.getOwnPersitentClass()) ;
					if(facadeClass!=null){
						LookUpLoaderBean bean=(LookUpLoaderBean) ServiceFactory.lookupFacade(facadeClass);
						return convertNamePairTolookupInfo(bean.loadLookup(configModel,null), configModel!=null?configModel.isOrdered():true, ContextUtil.getLoginLanguage(context)) ;
					}
				}else{
					logger.log(Level.WARNING,"fail to find meta data for field name :"+fieldName+" , reload lookup failed");
				}
			} catch (Exception e) {
				logger.log(Level.WARNING,"fail to re load lookup for fieild :"+fieldName,e);
			}
			
			return null;
			
			
		}
//***********************************************************************************
	protected Object formatMethodValue(UtopiaFormMethodMetaData meta,String data,Map<String,Object>context){
				return UtopiaTypeConverter.convertFromString(meta.getReturnType(), ContextUtil.getLoginLanguage(context), data);
		} 
//***********************************************************************************
	protected static GridMetaData getGridMetaData(InputItem cItem,UtopiaFormMetaData formMeta, Map<String,Object> context,
			String language, 
			IncludedFormMetaData grid,predefindedActions action) {
		GridMetaData meta = new GridMetaData();
		String bundelPath=formMeta.getClazz().getName();
		meta.setAddRecordString(MessageHandler.getMessage("add", bundelPath, language));
		meta.setRemoveRecordString(MessageHandler.getMessage("remove", bundelPath, language));
		meta.setSelectAllStr(MessageHandler.getMessage("selectAll", bundelPath, language));
		meta.setSelectColumnName("");
		meta.setRemoveRecordMessage(MessageHandler.getMessage("DeleteRecordConfirm", "ir.utopia.core.util.tags.ComponentsUIGlossary", language));
		meta.setAllowRemoveRecord(grid.isUpdateable());
		meta.setAllowAddRecord(grid.isInsertRowAllowed()&&grid.isUpdateable());
		meta.setUpdateable(grid.isUpdateable());
		meta.setPrimaryKeyColumn(grid.getIdFieldName());
		meta.setGridTitle(MessageHandler.getMessage(grid.getName(), bundelPath, language));
		InputItem[] columns=getGridColumns(formMeta,grid,context,language,action);
		meta.setColumns(columns);
		ArrayList<String>defaultvalue=new ArrayList<String>();
		UtopiaFormMetaData gridFormMetaData= FormUtil.getMetaData(grid.getFormClass()); 
		String linkMethod= grid.getMyLinkMethod();
		linkMethod=linkMethod==null?"":linkMethod;
		for(UtopiaFormMethodMetaData column:gridFormMetaData.getInputItems()){
			if(linkMethod.equals(column.getMethodName()))continue;
			Object o=formMeta.getHandler().getDefaultValueOf(column, null,predefindedActions.save);
			if(Enum.class.isAssignableFrom(column.getReturnType())&&Integer.class.isInstance(o)){
				o=EnumUtil.getEnumString(column.getReturnType(),
						ContextUtil.getLoginLanguage(context), ((Integer)o));
			}
			if(!DisplayTypes.Hidden.equals(column.getDisplayType()))
				defaultvalue.add(o==null?"":o.toString());
		}
		meta.setPreferedWidth(grid.getPreferedWith());
		meta.setPreferedHeigth(grid.getPreferedHeigth());
		meta.setDefaultRowData(defaultvalue.toArray(new String[defaultvalue.size()]));
		meta.setAutofitColumns(grid.isAutoFitColumns());
		List<FormColorLogic>colorLogics= grid.getColorLogic();
		if(colorLogics!=null&&colorLogics.size()>0){
			List<ColorLogic>flatedColorLogics=new ArrayList<ColorLogic>();
			for(FormColorLogic colorLogic:colorLogics){
				 String colorLogicStr=colorLogic.getLogic();
				 if(colorLogicStr!=null&&colorLogicStr.length()>0){
					 String flatLogic= mapDependency(gridFormMetaData, cItem, formMeta, colorLogicStr, InputItem.DEPENDENCY_TYPE_COLOR_LOGIC, context);
					 flatedColorLogics.add(new ColorLogic(flatLogic, colorLogic.getCSSClass()));
				 }
			}
			meta.setColorLogic(flatedColorLogics);
		}
		return meta;
		
	}
	//***********************************************************************************
	protected static InputItem[] getGridColumns(UtopiaFormMetaData parentForm, IncludedFormMetaData  includedForm,Map<String,Object> context, String language,predefindedActions action){
		UtopiaFormMetaData includedFormMetaData= FormUtil.getMetaData(includedForm.getFormClass());
		ArrayList<InputItem>gridField=new ArrayList<InputItem>();
		Map<InputItem,String>enddateMap=new HashMap<InputItem, String>();
		for(UtopiaFormMethodMetaData  methodMeta:(predefindedActions.report.equals(action)||predefindedActions.view.equals(action) )?includedFormMetaData.getReportItems():includedFormMetaData.getInputItems()){
			if(methodMeta.getMethodName().equals(includedForm.getMyLinkMethod()))
				continue;

			InputItem item= convertFromMetaToInpuItem(FormUtil.getMetaData(includedForm.getFormClass()), methodMeta,parentForm, context, language, enddateMap,action,true);
			InputItem field=new InputItem(item);
			gridField.add(field);
		}
		InputItem []result= gridField.toArray(new InputItem[gridField.size()]);
		Arrays.sort(result,new Comparator<InputItem>(){
			public int compare(InputItem o1, InputItem o2) {
				return o1.getIndex()-o2.getIndex();
			}});
		return result;
	}
//***********************************************************************************
	public static UILookupInfo convertNamePairTolookupInfo(List<NamePair>pairs,String language){
		return convertNamePairTolookupInfo(pairs, true, language);
	}
//***********************************************************************************
	/**
	 * 	
	 * @param pairs
	 * @return
	 */
	public static UILookupInfo convertNamePairTolookupInfo(List<NamePair>pairs,boolean sort,String language){
			UILookupInfo  info=new UILookupInfo();
			info.setColumns("key","value");
			ArrayList<String[]>result=new ArrayList<String[]>();
			if(pairs!=null){
				for(NamePair pair:pairs){
					result.add( new String[]{String.valueOf(pair.getKey()),WebUtil.write(pair, language) });
				}
			}
			info.setData(result.toArray(new String[result.size()][2]),sort);
			return info;
		}
//***********************************************************************************	
	public Object getValueOf(InputItem item, UtopiaBasicForm<?> source) {
		if(item!=null){
			UtopiaFormMethodMetaData mitem=meta.getMethodMetaData(item.getColumnName());	
		try {
				if(item.getDisplayType()==InputItem.DISPLAY_TYPE_GRID){
					return getIncludedFormData(item.getColumnName(),source);
				}
				else{
					Class<?>clazz=source.getClass();
					String key=clazz+"|"+mitem.getMethodName();
					Method method;
					if(FORM_METHOD_CACHE.containsKey(key)){
						method=FORM_METHOD_CACHE.get(key);
					}else{
						method= clazz.getMethod(mitem.getMethodName(), c);
						FORM_METHOD_CACHE.put(key, method);
					}
					return method.invoke(source, o);
				}
			} catch (Exception e) {
				logger.log(Level.ALL,"fial to load value from "+item.getColumnName(),  e);
			}
		}
			return null;
	}
	
//***********************************************************************************	
@Override
	public Object getDefaultValueOf(InputItem item, UtopiaBasicForm<?> source,
			predefindedActions action) {
		if(item!=null){
			UtopiaFormMethodMetaData mitem=meta.getMethodMetaData(item.getColumnName());
			return getDefaultValueOf(mitem, source,action);
		}
		return null;
	}
//*****************************************************************************************
@Override
public Object getDefaultValueOf(UtopiaFormMethodMetaData meta, UtopiaBasicForm<?> source,
		predefindedActions action) {
	String logic= meta.getDefaultValueLogic(action);
	if(logic!=null&&logic.trim().length()>0){
		return LogicEvaluator.evaluateDefaultValueLogic(logic, ContextHolder.getContext().getContextMap(),meta.getReturnType());
	}else{
		if(boolean.class.isAssignableFrom(meta.getReturnType())||Boolean.class.isAssignableFrom(meta.getReturnType())){
			return true;
		}
	}
	return null;
}
//***********************************************************************************
	protected Collection<UtopiaBasicForm<?>> getIncludedFormData(String formKey,Object source) {
		Vector<UtopiaBasicForm<?>>result=new Vector<UtopiaBasicForm<?>>();
		IncludedFormMetaData included=meta.getIncludedForm(formKey);
		if(included==null){
			return result;
		}
		UtopiaFormMethodMetaData methodMeta= included.getParentLoadMethod();
		Object res=null;
		try {
			res=MethodUtils.invokeMethod(source, methodMeta.getMethodName(),  null);
		} catch (Exception e) {
			logger.log(Level.WARNING,"fail to invoke method :"+methodMeta.getMethodName());
		}
		if(Collection.class.isInstance(res)){
			for(Object record: (Collection<?>)res){
					result.add((UtopiaBasicForm<?>)record);
			}
		}else{
			logger.log(Level.WARNING,"No data found for form included form with key--> "+formKey);
		}
		return result;
	}
//*****************************************************************************************
	public UtopiaBasicForm<?> LoadForm(Long recordId,predefindedActions action) {
		 try {
			 UtopiaBean bean=(UtopiaBean) ServiceFactory.lookupFacade(meta.getRemoteUsecaseClass().getName());
			 Object []valuesToLoad=new Object[]{recordId,meta.getPersitentDetailFieldsToLoad()};
			 UtopiaPersistent persistent= (UtopiaPersistent)MethodUtils.invokeMethod(bean, meta.getUsecaseLoadMethod(),valuesToLoad);
			 UtopiaBasicForm<?> f = getForm(meta.getClazz(),persistent);
			return f;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
//*****************************************************************************************
protected UtopiaBasicForm<?> getForm(Class<?> formClass,UtopiaBasicPersistent persistent)throws Exception{
			UtopiaBasicForm<?> form=(UtopiaBasicForm<?>)formClass.getConstructor(c).newInstance(o);
			Map<String,Object>context=ContextHolder.getContextMap();
			Subject user=ContextUtil.getUser(context);
			form.setLocale(ServiceFactory.getSecurityProvider().getLocaleOf(user).getLanguage());
			form.importPersistent(persistent);
			return form;
	}
//*****************************************************************************************
public InputItem[] getSearchResultColumns(Map<String, Object> context, String language) {
	  List<UtopiaFormMethodMetaData>methods= meta.getSearchResultItems();
	  if(methods==null||methods.size()==0){
		  return new InputItem[0];
	  }
	  InputItem []result=new InputItem[methods.size()];
	  int index=0;
	  
	  
	  for(UtopiaFormMethodMetaData method:methods){
		result[index++]=convertFromMetaToInpuItem(meta,method,null, context, language,null,false);
	  }
	  
	return result;
}
//******************************************************************************************************
@Override
public SearchPageModel getSearchPageModel(String language,UseCase usecase,Subject user) {
	try {
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		SearchPageModel model=new SearchPageModel();
		InputItem []searchItems= getSearchItems(context,language );
		model.setSearchItems(searchItems);
		InputItem[] searchResultItems  =getSearchResultColumns(context, language);
		model.setSearchResultItems(searchResultItems);
		List<UtopiaFormMethodMetaData>orderbyCols= meta.getSearchResultItems();
		if(orderbyCols!=null){
			InputItem []orderbyItems=new InputItem[orderbyCols.size()];
			for(int i=0;i<orderbyCols.size();i++)
				orderbyItems[i]=convertFromMetaToInpuItem(meta,orderbyCols.get(i),null, context, language,null,predefindedActions.search,false);
			model.setOrderbyItems(orderbyItems);
		}
		String deleteUrl=getActionUrl(predefindedActions.delete,usecase.getFullName());
		String editUrl=getActionUrl(predefindedActions.update,usecase.getFullName());
		String reportUrl=getActionUrl(predefindedActions.view,usecase.getFullName());
		String saveUrl=getActionUrl(predefindedActions.save, usecase.getFullName());
		model.setDeleteUrl(deleteUrl);
		model.setEditUrl(editUrl);
		model.setReportUrl(reportUrl);
		model.setSaveUrl(saveUrl);
		model.setImportExportSetupPage("ImportExportDefinition"+Constants.STRUTS_EXTENSION_NAME);
		model.setImportPage("ImportPage"+Constants.STRUTS_EXTENSION_NAME);
		model.setPrimaryKeyFieldName(meta.getIdFieldName());
		
		HashMap<String, String>valueMap=new HashMap<String, String>();
		for(InputItem searchItem: searchItems){
			UtopiaFormMethodMetaData methodMeta=meta.getMethodMetaData(searchItem.getColumnName());
			Object defaultValue= getDefaultValueOf(methodMeta, null,predefindedActions.search);
			valueMap.put(searchItem.getColumnName(),getFormatedValueOf(defaultValue ,searchItem,null,false,context));
		}
		model.setValueModel(valueMap);
		model.setUsecaseActionId(getUsecaseActionId(usecase, predefindedActions.search));
		
		setActionsEnabledOnModel(user, model, usecase,null);
		model.setLinks(getLinks(user, usecase));
		return model;
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
	return null;
}
//****************************************************************************************************
private void setActionsEnabledOnModel(Subject user, AbstractPageDataModel model,
		UseCase uscase,UtopiaBasicForm<?> form) {
	Long []availableActions=ServiceFactory.getSecurityProvider().getUsecaseAvailableActions(user, 
			uscase.getUsecaseId());
	
	model.setDeleteEnable(false);
	model.setEditEnable(false);
	model.setReportEnable(false);
	model.setSaveEnable(false);
	model.setImportEnable(false);
	model.setExportEnable(false);
	if(availableActions!=null){
		for(Long action:availableActions){
			if(predefindedActions.delete.ordinal()==action.intValue()){
				model.setDeleteEnable(form==null||form.isDeleteable());
			}else if(predefindedActions.update.ordinal()==action.intValue()){
				model.setEditEnable(form==null||form.isUpdateable());
			}else if(predefindedActions.view.ordinal()==action.intValue()){
				model.setReportEnable(true);
			}else if(predefindedActions.save.ordinal()==action.intValue()){
				model.setSaveEnable(true);
			}else if(predefindedActions.importData.ordinal()==action.intValue()){
				model.setImportEnable(true);
			}else if(predefindedActions.exportData.ordinal()==action.intValue()){
				model.setExportEnable(true);
			}
		}
		}
}
//*****************************************************************************************
protected Long getUsecaseActionId(UseCase uscase ,predefindedActions action){
	List<UsecaseAction>actions= uscase.getUseCaseActions();
	for(UsecaseAction act:actions){
		if(action.ordinal()==(int)act.getActionId()){
			return act.getUsecaseActionId();
		}
	}
	return -1l;
}
//*****************************************************************************************
protected Long getUseCaseActionId(UseCase usecase,String actionName){
	if(actionName!=null&&actionName.trim().length()>0){
		List<UsecaseAction>actions= usecase.getUseCaseActions();
		for(UsecaseAction act:actions){
			if(actionName.equals(act.getMethodName())){
				return act.getUsecaseActionId();
			}
		}
	}
	
	return -1l;
}
//*****************************************************************************************
public String getActionUrl(Constants.predefindedActions action, String usecaseName){
	return getActionUrl(action, action.toString(), usecaseName);
}
//*****************************************************************************************
public static String getActionUrl(Constants.predefindedActions action,String actionName, String usecaseName){
	StringBuffer result;
	String parameterSign;
	if(predefindedActions.delete.equals(action)||predefindedActions.search.equals(action)){
		result=new StringBuffer(action.toString()).
		append(Constants.USECASE_SEPERATOR).append(usecaseName).append(Constants.STRUTS_EXTENSION_NAME);
		parameterSign="?";
	}else{
	result=new StringBuffer(Constants.REDIREDT_TO_PAGE_PREFIX).append(Constants.RIDIRECT_TO_PAGE_SEPERATOR).
	append(actionName).append(Constants.USECASE_SEPERATOR).append(usecaseName).append(Constants.STRUTS_EXTENSION_NAME);
	parameterSign="?";
	}
	Map<String,Object>context=ContextHolder.getContextMap();
	String language=ContextUtil.getLoginLanguage(context);
	result.append(parameterSign).append("locale=").append(language);
	return result.toString();
}
//*****************************************************************************************
public InputItem[] getSearchItems(Map<String, Object> context, String language) {
	List<UtopiaFormMethodMetaData>methods= meta.getSearchOnItems();
	  if(methods==null||methods.size()==0){
		  return new InputItem[0];
	  }
	  InputItem []result=new InputItem[methods.size()];
	  int index=0;
	  for(UtopiaFormMethodMetaData method:methods){
		result[index++]=convertFromMetaToInpuItem(meta,method, null,context, language,null,predefindedActions.search,true);
	  }
	
	return result;
}
//*****************************************************************************************
public SearchPageData getSearchResult(String []searchConditions,String []conditionValues,String defaultCondition,String []orderby,int fromIndex,int fetchSize,String language) {
	return getSearchResult(searchConditions, conditionValues, null,defaultCondition, orderby, fromIndex, fetchSize,  language);
}
//*****************************************************************************************
@Override
public SearchPageData getSearchResult(String []searchConditions,String []conditionValues,boolean []andOr, String defaultCondition,String []orderby,int fromIndex,int fetchSize, String language) {
	try {
			if(defaultCondition!=null&&defaultCondition.trim().length()>0){
				defaultCondition=ServiceFactory.getSecurityProvider().decrypt( defaultCondition);
			}else{
				defaultCondition=null;
			}
			
			if(WebUtil.isRigthToLeft(language)&& conditionValues!=null&&conditionValues.length>0){
				for(int i=0;i<conditionValues.length;i++){
					conditionValues[i]=WebUtil.revertRightToLeft(conditionValues[i]);
				}
			}
			Map<String,Object>context=ContextHolder.getContext().getContextMap();
		    List<? extends UtopiaBasicPersistent>persistentList=doSearch(searchConditions, conditionValues,andOr,defaultCondition, orderby, fromIndex, fetchSize, context, language);
			SearchPageData resultData=new SearchPageData();
			if(persistentList!=null){
				int index=0;
				 List<FormColorLogic>colorLogics= meta.getColorLogic();
				 List<UtopiaFormMethodMetaData> methodMetas=meta.getSearchResultItems();
				 Set<UtopiaFormMethodMetaData> colorLogicMetas = findColorLogicMethodMetas(colorLogics);
				 List<UtopiaBasicForm<?>>forms=convertPersitentToForm(persistentList, language,context);
				 List<GridRowData> rows=new ArrayList<GridRowData>(forms.size());
				for(UtopiaBasicForm<?>form:forms){
					int column=0;
					String []rowDisplay=new String[methodMetas.size()];
					String cssClass=null;
					Map<String,Object>rowContext=null;
					if(colorLogics!=null&&colorLogics.size()>0){
						rowContext=new HashMap<String, Object>(context); 
						cssClass=getRowCssClass(form, colorLogicMetas, colorLogics, rowContext);
					}
					for(UtopiaFormMethodMetaData methodMeta:methodMetas){
						rowDisplay[column++]=formatDisplayValue(methodMeta,
							MethodUtils.invokeMethod(form, methodMeta.getMethodName(), o),language) ;
					}
					GridRowData row=new GridRowData(form.getRecordId(),index,rowDisplay);
					row.setDisplayData(rowDisplay);
					row.setDeleteable(form.isDeleteable());
					row.setUpdateable(form.isUpdateable());
					row.setCssClass(cssClass);
					rows.add(row);
					index++;
				}
				resultData.setRows(rows);
				resultData.setTotalResultCount(getResultCount(searchConditions, conditionValues,defaultCondition,  language));
			}
			return resultData;
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
		return null;
	}
}
//***********************************************************************************************
protected String getRowCssClass(UtopiaBasicForm<?> form, Set<UtopiaFormMethodMetaData> colorLogicMetas,List<FormColorLogic>colorLogics,Map<String, Object> rowContext){
		String cssClass=null;
		for(UtopiaFormMethodMetaData methodMeta:colorLogicMetas){
			Object value=null;
			try {
				value = MethodUtils.invokeMethod(form, methodMeta.getMethodName(), o);
			} catch (Exception e) {
				if(logger.isLoggable(Level.FINE))
					logger.log(Level.FINE,"",e);
			}
			String propertyName=AnnotationUtil.getPropertyName(methodMeta.getMethodName());
			rowContext.put(propertyName, Enum.class.isInstance(value)?String.valueOf(((Enum<?>)value).ordinal()):value);
		}
		for(FormColorLogic logic:colorLogics){
			String logicText=logic.getLogic();
			List<String>javaParams= LogicParser.getJavaParameters(logicText);
			for(String javaParam:javaParams){
				Object javaValue= LogicParser.getJavaParameterValue(javaParam, rowContext);
				logicText=LogicParser.replaceJavaVariable(logicText, javaParam,Enum.class.isInstance(javaValue)?String.valueOf(((Enum<?>)javaValue).ordinal()):"-1" );
			}
			boolean evaluationResult=	LogicEvaluator.evaluateLogic(logicText, rowContext);
			if(evaluationResult){
				cssClass=logic.getCSSClass();
				break;
			}
		}
	return cssClass;
}
//***********************************************************************************************
protected Set<UtopiaFormMethodMetaData> findColorLogicMethodMetas(
		List<FormColorLogic> colorLogics) {
	Set<UtopiaFormMethodMetaData> colorLogicMetas= new HashSet<UtopiaFormMethodMetaData>();
	 if(colorLogics!=null&&colorLogics.size()>0){
		 if(colorLogics!=null&&colorLogics.size()>0){
			 for(FormColorLogic cl:colorLogics){
				List<String>params= LogicParser.getParametersInString( cl.getLogic());
				if(params!=null){
					for(String param:params){
						UtopiaFormMethodMetaData methodMeta= meta.getMethodMetaData(param);
						colorLogicMetas.add(methodMeta);
					}
				}
			 }
		 }
	 }
	return colorLogicMetas;
}
//***********************************************************************************************
protected  List<UtopiaBasicForm<?>> convertPersitentToForm(List<? extends UtopiaBasicPersistent>persistentList,String language,Map<String,Object> context)throws Exception{
		List<UtopiaBasicForm<?>> result=new ArrayList<UtopiaBasicForm<?>>();
		for(UtopiaBasicPersistent persistent:persistentList){
			UtopiaBasicForm<?>form=(UtopiaBasicForm<?>) ConstructorUtils.invokeConstructor(meta.getClazz(), o) ;
				form.setLocale(language);
				form.importPersistent(persistent);
				result.add(form);
		}
		return result;
}
//***********************************************************************************************
protected List<? extends UtopiaBasicPersistent> doSearch(String []searchConditions,String []conditionValues,boolean []andOr,String defaultConditionStr,
		String []orderby,int fromIndex,int fetchSize,Map<String,Object> context,String language)throws Exception{
	UtopiaBasicUsecaseBean<?, ?> bean=(UtopiaBasicUsecaseBean<?, ?>)ServiceFactory.lookupFacade(meta.getRemoteUsecaseClass().getName());
	Vector<OrderBy>pairs=getOrderBy(orderby);
	SearchConditionItem []items=getPropertyValue(searchConditions, conditionValues,andOr, context, language);
	Condition defaultCondition=null;
	if(defaultConditionStr!=null&&defaultConditionStr.trim().length()>0){
		defaultCondition=new Condition(defaultConditionStr);
	}
	return  bean.search(items,pairs.toArray(new OrderBy[pairs.size()]), 
			defaultCondition, fromIndex,fetchSize);
}
//***********************************************************************************************
protected String formatDisplayValue(UtopiaFormMethodMetaData col,Object methodResult,String language){
	if(Enum.class.isInstance(methodResult)){
		return EnumUtil.getEnumString(col.getReturnType(),language ,((Enum<?>)methodResult).ordinal());
	}else if(boolean.class.isInstance(methodResult)||Boolean.class.isInstance(methodResult)){
		boolean val=Boolean.valueOf(methodResult.toString());
		return EnumUtil.getEnumString(BooleanType.class, language, val?BooleanType.True.ordinal():BooleanType.False.ordinal());
	}else if(Constants.DisplayTypes.password.equals( col.getDisplayType())){
		return methodResult==null?"":methodResult.toString();
	} else{
		return WebUtil.write(methodResult,language);
		} 
}
//***********************************************************************************************
protected Vector<OrderBy> getOrderBy(String []orderby){
	Vector<OrderBy>pairs=new Vector<OrderBy>();
	if(orderby!=null){
		for(String cOrderBy:orderby){
			UtopiaFormMethodMetaData metacol=meta.getMethodMetaData(cOrderBy);
			if(metacol!=null){
				FormPersistentAttribute attribute=(FormPersistentAttribute)metacol.getAnnotation(FormPersistentAttribute.class);
				Class<?>entityClass=null;
				if(FormToEntityMapType.real.equals(attribute.formToEntityMapType())){
					if(DisplayTypes.lookup.ordinal()==metacol.getInputTypeDisplayType()||DisplayTypes.LOV.ordinal()==metacol.getInputTypeDisplayType()||
							FormToEntityDataTypeMap.LONG_TO_LOOKUP.equals(attribute.formToEntityMap())){
						entityClass=meta.getPersistentClass();
					}
					
				}else{
					entityClass= BeanUtil.getSearchEntityClass(meta.getPersistentClass()) ;
					entityClass=entityClass.equals(meta.getPersistentClass())?null:entityClass;
				}
				
				if(entityClass!=null){
					OrderBy pair=new OrderBy(entityClass,AnnotationUtil.getPropertyName(metacol.getPersistentMethodName()));
					pairs.add(pair);
					}
			
		}
	}
		}
	return pairs;
}
//***********************************************************************************************
public int getResultCount(String []searchConditions,String []conditionValues,String defaultConditionStr,
		 String language) {
	return getResultCount(searchConditions, conditionValues, null, defaultConditionStr,  language);
}
//***********************************************************************************************
public int getResultCount(String []searchConditions,String []conditionValues,boolean []andOr,String defaultConditionStr,
		 String language) {
	try {
		UtopiaBasicUsecaseBean<?, ?> bean=(UtopiaBasicUsecaseBean<?, ?>) ServiceFactory.lookupFacade(meta.getRemoteUsecaseClass().getName());
		Map<String,Object>context=ContextHolder.getContextMap();
		SearchConditionItem []items=getPropertyValue(searchConditions, conditionValues,andOr, context, language);
		Condition defaultCondition=null; 
		if(defaultConditionStr!=null&&defaultConditionStr.trim().length()>0){
			defaultCondition=new Condition(defaultConditionStr);
		}
		
		return bean.getResultCount(items,defaultCondition);
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
	return 0;
}
//***********************************************************************************************
@Override
public int getResultCount(String []searchConditions,String []conditionValues,
		String language) {
	return getResultCount(searchConditions, conditionValues,null,  language);
}
//***********************************************************************************************
protected SearchConditionItem[] getPropertyValue(String []searchConditions,String []conditionValues,boolean []andOr,Map<String, Object> context,String language){
	if(searchConditions==null||searchConditions.length==0)return new SearchConditionItem[0];
	if(conditionValues==null||conditionValues.length!=searchConditions.length)throw new IllegalArgumentException("condion siaze-->"+searchConditions.length+"!= values -->"+conditionValues.length);
	ArrayList<SearchConditionItem>result=new ArrayList<SearchConditionItem>(); 
	for(int i=0;i<searchConditions.length;i++){
		String condtionKey= searchConditions[i];
		String value=conditionValues[i];
		if(value==null||value.trim().length()==0)continue;
		UtopiaFormMethodMetaData methodMeta= meta.getMethodMetaData(condtionKey);
		if(methodMeta!=null&&methodMeta.isPersistentMethod()){
			FormToEntityDataTypeMap formToEntityMap= methodMeta.getFormToEntityMapType();
			FormToEntityMapType mapType= methodMeta.getMapType();
			if(value!=null&&value.trim().length()>0){
				Object res= UtopiaTypeConverter.convertFromString( methodMeta.getReturnType(),language, value);
				Class<?>entityClass=null;
				QueryComparsionType type=QueryComparsionType.eqaul;
				if(FormToEntityMapType.real.equals(mapType)){
					if(FormToEntityDataTypeMap.LONG_TO_LOOKUP.equals(formToEntityMap)){
						entityClass=findSearchLookupEntityClass(methodMeta);
					}else if(methodMeta.getInputTypeDisplayType()== 
						Constants.DisplayTypes.LargeString.ordinal()||
						methodMeta.getInputTypeDisplayType()== 
							Constants.DisplayTypes.String.ordinal()){
						type=QueryComparsionType.like;
					}else if(FormToEntityDataTypeMap.STRING_TO_DATE.equals(formToEntityMap)||
							FormToEntityDataTypeMap.STRING_TO_DATE_TIME.equals(formToEntityMap)){
							if(DateUtil.useSolarDate(language) ){
								res=DateUtil.solarToDate(value);
							}else{
								res=DateUtil.gerToDate(value);
							}
					} 
				} 
				SearchConditionItem condition=new SearchConditionItem(entityClass,condtionKey,res,type);
				condition.setAndWithPreviousCondition(andOr!=null&&andOr.length>i&&andOr[i]);
				result.add(condition);
			}
		}
	}
	
	return result.toArray(new SearchConditionItem[result.size()]);
}
//***********************************************************************************************
protected Class<?> findSearchLookupEntityClass(UtopiaFormMethodMetaData methodMeta){
	String key=meta.getClazz().getName()+"|"+methodMeta.getMethodName();
	if(!SEARCH_LOOKUP_ENITY_CACHE.containsKey(key)){
		Class<?>entityClass=null;
		Class<? extends UtopiaBasicPersistent>searchClass= BeanUtil.getSearchEntityClass(meta.getPersistentClass());
		if(!meta.getPersistentClass().equals(searchClass)){
			try {
				if(searchClass.getMethod(methodMeta.getPersistentMethodName(), c)==null){
					entityClass=methodMeta.getPersistentMethodClass();
					//in case of search entity does not contains search key ,although it's not a good idea to join
				}
			} catch (Exception e) {
				logger.log(Level.WARNING,"fail to locate method "+methodMeta.getPersistentMethodName()+" in class: "+searchClass.getName());
				entityClass=methodMeta.getPersistentMethodClass();
				//in case of search entity does not contains search key ,although it's not a good idea to join
			} 
			SEARCH_LOOKUP_ENITY_CACHE.put(key, entityClass);
		}
	}
	
	return SEARCH_LOOKUP_ENITY_CACHE.get(key);
}
//***********************************************************************************************
 @Override
public DataInputFormModel getDataInputFormModel(UseCase usecase,predefindedActions action,String language,boolean loadLookups){
	 	Map<String,Object>context=ContextHolder.getContext().getContextMap();
	 	context.put(ContextUtil.CURRENT_ACTION_PARAMETER, action.name());
		DataInputFormModel model=convertMetaToDataInputform(meta,context, usecase,action ,language,loadLookups);
		model.setBundel("save", MessageHandler.getMessage("save", "ir.utopia.core.constants.Glossory", language));
		model.setBundel("cancel", MessageHandler.getMessage("cancel", "ir.utopia.core.constants.Glossory", language));
		model.setBundel("reset",MessageHandler.getMessage("reset", "ir.utopia.core.constants.Glossory", language));
		model.setDirection(WebUtil.getDirection(language));
	return model;
}
//***********************************************************************************
protected DataInputFormModel convertMetaToDataInputform(UtopiaFormMetaData metaData, Map<String,Object> context,UseCase usecase,predefindedActions action, String language,boolean loadLookups){
	DataInputFormModel result=new DataInputFormModel();
	if(metaData==null){
		result.setServerMessage("Error loading form!!!!");
	}else{
		try {
			
			result.setUsecaseActionId(getUsecaseActionId(usecase, predefindedActions.save.equals(action)?
									predefindedActions.save:predefindedActions.update));
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		result.setActionUrl(UsecaseUtil.getActionUrl(usecase.getFullName(), predefindedActions.updateFromDashboard.equals(action)?"update":action.name()));
		result.setSaveUrl(UsecaseUtil.getActionUrl(usecase.getFullName(), predefindedActions.save.toString()));
		result.setUpdateUrl(UsecaseUtil.getActionUrl(usecase.getFullName(), predefindedActions.update.toString()));
		result.setDeleteUrl(UsecaseUtil.getActionUrl(usecase.getFullName(), predefindedActions.delete.toString()));
		result.setSearchURL(UsecaseUtil.getActionUrl(usecase.getFullName(), predefindedActions.search.toString()));
		result.setItems(getInputItems(usecase,action,language,loadLookups));
	}
	result.setPrimaryKeyName(metaData.getIdFieldName());
	result.setCurrencySign(getCurrencySign());	
	result.setColumnCount(metaData.getColumnCount());
	result.setValidationFunctions(metaData.getValidationFunctions());
	result.setOnLoadMethods(metaData.getOnLoadFunctions());
	result.setAfterLoadCallbacks(metaData.getAfterLoadCallBacks());
	result.setCustomButtons(convertCustomButtons(metaData,metaData.getCustomButtons(predefindedActions.save.equals(action)?
			predefindedActions.save:predefindedActions.update),context));
	if(InputPageLayout.DetailMaster.equals(metaData.getPageLayout())){
		result.setLayOut(DataInputFormModel.LAY_OUT_DETAIL_MASTER);
	}else{
		result.setLayOut(DataInputFormModel.LAY_OUT_SIMPLE);
	}
	result.setSupportAttachment(metaData.isSupportAttachment());
	result.setConfirmRevisionDecription(metaData.isRevisionSupport());
	return result;
}
//***********************************************************************************
protected String getCurrencySign(){
	return "$";
}
//***********************************************************************************
@SuppressWarnings("unchecked")
public DataInputDataModel getFormData(UseCase usecase,predefindedActions action,UtopiaBasicForm<?> currentData,Long recordId,String language,boolean reload) {
	DataInputDataModel result= new DataInputDataModel();
	Map<String,Object>context=ContextHolder.getContextMap();
	try {
		if(reload||currentData==null){
			if(recordId!=null&&recordId>0){
				currentData=LoadForm(recordId,predefindedActions.save);
			}
		}else{
			if(currentData!=null)
				refreshIncludedForms(currentData);
		}
	} catch (Exception e) {
		logger.log(Level.ALL,"fail to load usecase data ", e);
		return null;
	}
		try {
			HashMap<String, String>valueMap=new HashMap<String, String>();
			HashMap<String, GridData>gridData=new HashMap<String, GridData>();
			DataInputFormModel form=getDataInputFormModel(usecase, action, language, false);
			Map<String,Object>dynamicItemsMap=new HashMap<String, Object>(context);
			ArrayList<InputItem>dependentItems=new ArrayList<InputItem>();
			for(InputItem item: form.getItems()){
				Object value=null;
				if(currentData!=null){
					value=getValueOf(item, currentData);
				}else if(Constants.predefindedActions.save.equals(action)){
					value=getDefaultValueOf(item, currentData,predefindedActions.save);
				}
				String []dependencies=item.getFilterDepenedents();
				if(dependencies!=null&&dependencies.length>0){
					dependentItems.add(item);
				}
				dynamicItemsMap.put(item.getColumnName(), value);
				if(value!=null){
					if(item.getDisplayType()==InputItem.DISPLAY_TYPE_GRID){
						GridValueModel valueModel= new GridValueModel();  
						initFormGridData(valueModel,item,
								(Collection<UtopiaBasicForm<?>>)value,language,context);
						
						gridData.put(item.getColumnName(),valueModel) ;
					}else{
						valueMap.put(item.getColumnName(),getFormatedValueOf(value,item,null,true,context));
					 }
				}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_LARGE_STRING||item.getDisplayType()==InputItem.DISPLAY_TYPE_STRING){
					valueMap.put(item.getColumnName(),"");
				}
				
			}
			result.setIncludedGridsData(gridData);
			result.setValueModel(valueMap);
			if(recordId!=null&&recordId>0)
				result.setAttachmentInfo(getAttachments(recordId,usecase));
			HashMap<String, UILookupInfo> dynamicDataMap=new HashMap<String, UILookupInfo>();
			for(InputItem item:dependentItems){
				dynamicDataMap.put(item.getColumnName(),convertNamePairTolookupInfo(LoadLookupNamePairs(meta.getMethodMetaData(item.getColumnName()), dynamicItemsMap, language), language) );
			}
			result.setDynamicLookupsInfo(dynamicDataMap);
			
			result.setContext(getContext(context));
			
			Subject user=ContextUtil.getUser(context);
			setActionsEnabledOnModel(user, result, usecase,currentData);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
	return result;
}
//***********************************************************************************
@Override
public List<AttachmentInfo> getAttachments(Long recordId,UseCase usc){
	List<AttachmentInfo> result=null;
	try {
		CoAttachmentFacadeRemote bean=(CoAttachmentFacadeRemote)ServiceFactory.lookupFacade(CoAttachmentFacadeRemote.class);
		List<CoAttachment>attachments= bean.getAttachmentInfos(usc.getUsecaseId(), recordId);
		if(attachments!=null){
			 result=new ArrayList<AttachmentInfo>();
			for(CoAttachment attach:attachments){
				AttachmentInfo attachInfo=new AttachmentInfo();
				attachInfo.setFileName(attach.getFileName());
				attachInfo.setAttachmentId(ServiceFactory.getSecurityProvider().encrypt(String.valueOf(attach.getCoAttachmentId())));
				result.add(attachInfo);
			}
		}
	} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
	}
	return result;
}
//***********************************************************************************
@SuppressWarnings("unchecked")
protected void refreshIncludedForms(UtopiaBasicForm<?> form){
	UtopiaFormMetaData meta=FormUtil.getMetaData((Class<? extends UtopiaBasicForm<?>>)form.getClass());
	IncludedFormMetaData []includedForms= meta.getIncludedForms();
	Long recordId=form.getRecordId();
	if(includedForms!=null&&includedForms.length>0&&recordId!=null&&recordId>0){
		//in case of parent record changed and details was not changed the method details will be null
		//so handle details reload in case of edit action failed
		
		
		for(IncludedFormMetaData cIncluded:includedForms){
			UtopiaBasicForm<?> freshForm= meta.getHandler().LoadForm(recordId,predefindedActions.save);
			try {
			 	Object freshResult= MethodUtils.invokeMethod(freshForm, cIncluded.getParentLoadMethod().getMethodName(), o);
			 	Collection<UtopiaBasicForm<?>> currentResult=(Collection<UtopiaBasicForm<?>>) MethodUtils.invokeMethod(form, cIncluded.getParentLoadMethod().getMethodName(), o);
			 	currentResult=currentResult==null?new ArrayList<UtopiaBasicForm<?>>():currentResult;
			 	Method method=form.getClass().getMethod(cIncluded.getParentLoadMethod().getMethodName(), c);
		 		Collection<UtopiaBasicForm<?>>detailsCol=Set.class.isAssignableFrom(method.getReturnType())?
		 				new HashSet<UtopiaBasicForm<?>>():new ArrayList<UtopiaBasicForm<?>>();
		 				for(UtopiaBasicForm<?> f:currentResult){
		 					if(f!=null){
		 						detailsCol.add(f);
		 					}
		 				}
			 	if(Collection.class.isInstance(freshResult)){
		 			L1:	for(UtopiaBasicForm<?> cform: (Collection<UtopiaBasicForm<?>>)freshResult){
		 				if(detailsCol.contains(cform))continue L1;
		 					for(UtopiaBasicForm<?> cChangedForm: (Collection<UtopiaBasicForm<?>>)detailsCol){
				 				Long cId=cChangedForm.getRecordId();
					 			cId=cId==null?0l:Math.abs(cId);
			 					if(cform.getRecordId().equals(cId)){
			 						continue L1;
				 				}	
		 					}
		 					detailsCol.add(cform);
		 			}
			 	}
			 	MethodUtils.invokeMethod(form, AnnotationUtil.getSetterMethodName(cIncluded.getParentLoadMethod().getMethodName()), detailsCol);
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
			}
			 
		}
	}
}
//***********************************************************************************
protected void initFormGridData(GridValueModel valueModel,InputItem item, Collection<UtopiaBasicForm<?>>gridData,String language,Map<String,Object>context){
	if(gridData==null){
		return ;
	}
	GridMetaData meta=item.getGridMetaData();
	ArrayList<GridRowData>result=new ArrayList<GridRowData>();
	Map<InputItem,Set<Object>>loadingMap=new HashMap<InputItem, Set<Object>>();
	for(UtopiaBasicForm<?> form:gridData){
		GridRowData row=new GridRowData();
		ArrayList<Object> rowData=new ArrayList<Object>();
		ArrayList<String> rowDisplayData=new ArrayList<String>();
		ArrayList<String[]> rowHiddenData=new ArrayList<String[]>();
		for(InputItem colMeta:meta.getColumns()){
			 Object value=form.getMetaData().getHandler().getValueOf(colMeta,form);
			 String strValue;
			 strValue=getFormatedValueOf(value,colMeta,item.getColumnName(),false,context);
			 if(InputItem.DISPLAY_TYPE_LOV==colMeta.getDisplayType()||
					 InputItem.DISPLAY_TYPE_COMBOBOX==colMeta.getDisplayType()&&colMeta.getDisplayDepenedents()!=null&&colMeta.getDisplayDepenedents().length>0)
					 {
				 if(loadingMap.containsKey(colMeta)){
					 loadingMap.get(colMeta).add(value);
				 }else{
					 HashSet<Object>loadingData=new HashSet<Object>();
					 loadingData.add(value);
					 loadingMap.put(colMeta, loadingData);
				 }
			 }			 
			 if(InputItem.DISPLAY_TYPE_HIDDEN==colMeta.getDisplayType()){
				 rowHiddenData.add(new String []{colMeta.getColumnName(),strValue});
			 }else{
				 rowData.add(strValue);
				 if(strValue!=null&&strValue.trim().length()>0&&
						 (InputItem.DISPLAY_TYPE_COMBOBOX==colMeta.getDisplayType()||
						 InputItem.DISPLAY_TYPE_LIST==colMeta.getDisplayType()||
						 InputItem.DISPLAY_TYPE_RADIO_BUTTON==colMeta.getDisplayType())){
					 boolean found=false;
					 for(String[]cur :colMeta.getLookupInfo().getData()){
							if(strValue.equals(cur[0])){
								 rowDisplayData.add(cur[1]);
								 found=true;
								 break;
							}
						}
					 if(!found){
						 rowDisplayData.add(strValue);
						 if(loadingMap.containsKey(colMeta)){
							 loadingMap.get(colMeta).add(value);
						 }else{
							 HashSet<Object>loadingData=new HashSet<Object>();
							 loadingData.add(value);
							 loadingMap.put(colMeta, loadingData);
						 }
					 }
				 }else{
					 rowDisplayData.add(strValue);
				 }
			 }
			 
		}
		row.setDisplayData(rowDisplayData.toArray(new String[rowDisplayData.size()]));
		row.setData(rowData.toArray(new String[rowData.size()]));
		row.setHiddenParameters(rowHiddenData.toArray(new String[rowHiddenData.size()][]));
		row.setRowId(form.getRecordId());
		result.add(row);
	}
	if(loadingMap.size()>0){
		for(InputItem lookupMeta: loadingMap.keySet()){
			Set<Object>keys=loadingMap.get(lookupMeta);
			if(keys!=null&&keys.size()>0)
				try {
					initGridRowsDisplay(item,keys, context, meta, result, loadingMap,
						lookupMeta,language);
				} catch (Exception e) {
					logger.log(Level.WARNING,"",e);
				}
		}
	}
	GridRowData[] resultArray=result.toArray(new GridRowData[result.size()]);
	Arrays.sort(resultArray, new Comparator<GridRowData>() {

		@Override
		public int compare(GridRowData o1, GridRowData o2) {
			long res=o2.getRowId()-o1.getRowId();
			return res>0l?1:(res<01?-1:0);
		}
	});
	valueModel.setRows(resultArray);
}
//***********************************************************************************
@SuppressWarnings("unchecked")
private void initGridRowsDisplay(InputItem GridItem,Set<Object>keys, Map<String, Object> context,
		GridMetaData meta, ArrayList<GridRowData> result,
		Map<InputItem, Set<Object>> loadingMap, InputItem lookupMeta,String language) {
	UtopiaFormMetaData gridFormMetaData=FormUtil.getMetaData(this.meta.getIncludedForm(GridItem.getColumnName()).getFormClass());
	UtopiaFormMethodMetaData methodMeta= gridFormMetaData.getMethodMetaData(lookupMeta.getColumnName());
	int index= meta.getDisplayIndex(lookupMeta);
	if(lookupMeta.getDisplayType()==InputItem.DISPLAY_TYPE_LOV){
		LOVMetaData lovMeta= methodMeta.getLOVConfiguration(null);
		UtopiaFormMetaData lovMetaData= FormUtil.getMetaData((Class<? extends UtopiaBasicForm<?>>) lovMeta.getFromClass());
		Map<Long,String[]>values= loadLOVValues(keys.toArray(new Long[keys.size()]), lovMetaData);
		
		for(GridRowData row:result){
			Long key=(Long.parseLong(row.getData()[index]));
			String []displayValues=values.containsKey(key)?values.get(key):null;
			row.getDisplayData()[index]=displayValues!=null?getLOVLookupDisplay(lovMeta, lovMetaData.getSearchResultItems(), displayValues):row.getDisplayData()[index];
			}
	}else if(lookupMeta.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX){
		LookupConfigurationModel lookupModel=gridFormMetaData.getLookupConfigurationModel(methodMeta, context);
		if(lookupModel!=null){
			UILookupInfo info=loadLookupValues(keys, lookupModel,context);
			if(info!=null){
				lookupMeta.setLookupInfo(info);
				for(String []data:info.getData()){
					for(GridRowData row:result){
						if(row.getData()[index].equals(data[0]))
							row.getDisplayData()[index]=data[1];
						}
					}
				}
			}
		
	}
}
//***********************************************************************************
private UILookupInfo loadLookupValues(Set<Object> keys, LookupConfigurationModel inLookupModel,Map<String, Object> context) {
	String language=ContextUtil.getLoginLanguage(context);
	LookupConfigurationModel lookupModel=new LookupConfigurationModel(inLookupModel.getOwnPersitentClass());
	lookupModel.setOrderby(inLookupModel.getOrderby());
	lookupModel.setDescriptionColumn(inLookupModel.getDescriptionColumn());
	lookupModel.setSeperator(inLookupModel.getSeperator());
	lookupModel.setPrimaryKeyColumn(inLookupModel.getPrimaryKeyColumn());
	lookupModel.setOwnDisplayColumns(inLookupModel.getOwnDisplayColumns());
	StringBuffer conditionStr=new StringBuffer(inLookupModel.getOwnPersitentClass().getSimpleName()).append(".").append(inLookupModel.getPrimaryKeyColumn() ).
			append(" IN ( ");
	int i=0;
	Map<String,Object>paramValues=new HashMap<String, Object>();
	for(Object o :keys){
		if(i>0){
			conditionStr.append(",");
		}
		paramValues.put("key"+i, o);
		conditionStr.append("@key"+i+"@");
		i++;
	}
	conditionStr.append(")");
	Condition condition=new Condition(conditionStr.toString(),paramValues);
	lookupModel.addCondition(condition);
	try {
		Class<? extends UtopiaBasicPersistent>persistentClass= inLookupModel.getOwnPersitentClass();
		Class<?>bean=BeanUtil.findRemoteClassFromPersistent(persistentClass);
		LookUpLoaderBean  remote =(LookUpLoaderBean)ServiceFactory.lookupFacade(bean.getName());
		List<NamePair>pairs= remote.loadLookup(lookupModel );
		UILookupInfo info=convertNamePairTolookupInfo(pairs,inLookupModel.isOrdered(),language);
		return info;
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
	return null;
}
//***********************************************************************************
protected  String getFormatedValueOf(Object input,InputItem item,String gridColName,boolean loadLookup,Map<String,Object>context){
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
		if(displayType==InputItem.DISPLAY_TYPE_LOV&&loadLookup){
				return loadLOVValue(input, item.getColumnName(), gridColName);
		}else{
				result= result==null?"":result.toString();
		}
		
		
		return (displayType!=InputItem.DISPLAY_TYPE_COMBOBOX&&displayType!=InputItem.DISPLAY_TYPE_LIST&&
				(gridColName==null||displayType!=InputItem.DISPLAY_TYPE_NUMERIC)&&
				displayType!=InputItem.DISPLAY_TYPE_RADIO_BUTTON&&displayType!=InputItem.DISPLAY_TYPE_LOV&&displayType!=InputItem.DISPLAY_TYPE_PASSWORD&&
				displayType!=InputItem.DISPLAY_TYPE_DATE&&displayType!=InputItem.DISPLAY_TYPE_HIDDEN)? WebUtil.write(input,ContextUtil.getLoginLanguage(context)):
			(result==null?"":result.toString());
}
//***********************************************************************************
@SuppressWarnings("unchecked")
public String loadLOVValue(Object input,String item,String gridColName){
	if(Long.class.isInstance(input)){
	UtopiaFormMetaData myMeta=meta;
	if(gridColName!=null){
		  myMeta=FormUtil.getMetaData( meta.getIncludedForm(gridColName).getFormClass());
	}
	UtopiaFormMethodMetaData methodMeta= myMeta.getMethodMetaData(item);
	LOVMetaData lovMeta= methodMeta.getLOVConfiguration(null);
	UtopiaFormMetaData lovMetaData= FormUtil.getMetaData((Class<? extends UtopiaBasicForm<?>>) lovMeta.getFromClass());
	 List<UtopiaFormMethodMetaData> methodMetas=lovMetaData.getSearchResultItems();
	Map<Long,String[]>values= loadLOVValues(new Long[]{(Long)input}, lovMetaData);
	if(values!=null&&values.size()!=0){
		StringBuffer lovDisplay=new StringBuffer(input.toString());
		String []data=values.values().iterator().next();
		if(data.length>0){
			lovDisplay.append("|");
		lovDisplay.append(getLOVLookupDisplay( lovMeta, methodMetas, data));
		}
		return lovDisplay.toString();
	}
	}
		return input==null?"":input.toString();
}
//***********************************************************************************
private String getLOVLookupDisplay( LOVMetaData lovMeta,List<UtopiaFormMethodMetaData> methodMetas,String []data) {
		String []displayCols=lovMeta.getBoxSelectingColumns();
		StringBuffer lovDisplay=new StringBuffer();
		for(int i=0;i<displayCols.length;i++){
			for(int j=0;j<methodMetas.size();j++){
				UtopiaFormMethodMetaData cur=methodMetas.get(j);
				if(displayCols[i].equals(cur.getFieldName())){
					if(i>0){
						lovDisplay.append(lovMeta.getDisplayItemSeperator());
					}
					lovDisplay.append(data[j]);
				}
			}
		}
	
	return lovDisplay.toString();
}
//***********************************************************************************
public Map<Long,String[]> loadLOVValues(Long []keys,UtopiaFormMetaData lovMetaData){
		Map<Long,String[]>result=new HashMap<Long, String[]>();
		try {
			String idname=lovMetaData.getIdFieldName();
			String []searchColumnNames=new String[keys.length];
			String []values=new String[keys.length];
			boolean []andOr=new boolean[keys.length];
			for(int i=0;i<keys.length;i++){
				searchColumnNames[i]=idname;
				values[i]=String.valueOf(keys[i]);
				andOr[i]=false;
			}
			Map<String,Object>context=ContextHolder.getContextMap();
			SearchPageData searchResult=lovMetaData.getHandler().getSearchResult(searchColumnNames, values,andOr,null, null, 0, keys.length,  ContextUtil.getLoginLanguage(context));
			if(searchResult!=null){
				List<? extends GridRowData> rows=searchResult.getRows();
				for(GridRowData row:rows){
					result.put(row.getRowId(), row.getData());
				}
			} 
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		
	
	return result;
}
//***********************************************************************************
@Override
public int getReportRecordCount(ReportModel model){
if(model!=null){
	try {
		UtopiaBasicUsecaseBean<?, ?> bean=(UtopiaBasicUsecaseBean<?, ?>) ServiceFactory.lookupFacade(model.getUsecase().getRemoteClassName());
		LoadingParameterMapAndReportModel res=getBeanReportModel(model,bean.getSearchModelClass(), true);
		return  bean.getReportResultCount(res.reportModel);
		
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
}
return -1;
}
//***********************************************************************************
@Override
public Vector<Map<String, Object>> getReportData(ReportModel model,int from,int to) {
	Vector<Map<String, Object>> result=new Vector<Map<String,Object>>();
	try {
		UtopiaBasicUsecaseBean<?, ?> bean=(UtopiaBasicUsecaseBean<?, ?>) ServiceFactory.lookupFacade(model.getUsecase().getRemoteClassName());
		Class<? extends UtopiaBasicPersistent> searchObjectClass= bean.getSearchModelClass();
		LoadingParameterMapAndReportModel beanReportModel= getBeanReportModel(model,searchObjectClass, false);
		Long t4=System.currentTimeMillis();
		List<Object[]>res= bean.getReportResult(beanReportModel.reportModel, from, to);
		logger.log(Level.WARNING,"loading report datat took "+(System.currentTimeMillis()-t4)+" ms");
		Long t1=System.currentTimeMillis();
		if(res!=null){
			try {
				Map<Class<?>,UtopiaBasicForm<?>>attributeFormMap=new HashMap<Class<?>, UtopiaBasicForm<?>>();
				for(FieldAndMeta field:beanReportModel.fieldAndMeta){
					UtopiaBasicForm<?> cForm=attributeFormMap.get(field.metaClass);
						if(cForm==null){
							cForm=(UtopiaBasicForm<?>)ConstructorUtils.invokeConstructor(field.metaClass,o ) ;	
							attributeFormMap.put(field.metaClass, cForm);
						}
						
						
				}
				Map<String,Object>context= ContextHolder.getContext().getContextMap();
				String language=ContextUtil.getLoginLanguage(context);
				for(Object []rowData:res){
					HashMap<String, Object>objMap=new HashMap<String, Object>();
					for(FieldAndMeta field:beanReportModel.fieldAndMeta){
						int colIndex=beanReportModel.reportModel.getColumnIndex(field.pair);
						UtopiaFormMetaData cmeta=FormUtil.getMetaData(field.metaClass);
						if(cmeta==null){
							logger.log(Level.WARNING,"invalid form class "+field.metaClass+" skipped");
						}
						UtopiaFormMethodMetaData methodMeta=cmeta.getMethodMetaData(field.name);
						UtopiaBasicForm<?>form=attributeFormMap.get(field.metaClass);
						if(colIndex>=0){//if it's not a virtual field load from persistent value
							String settterMethodName=AnnotationUtil.getSetterMethodOfField(field.name);
							Object []formTopersResult=FormAndPersistentConverter.getFormToPersistentMethodParamClass( methodMeta, rowData[colIndex],language);
							if(formTopersResult!=null&&formTopersResult[0]!=null)
								invokeObjectMethod(form,settterMethodName,formTopersResult[0] ,(Class<?>)formTopersResult[1],language);	
						}
						
							Object formValue=MethodUtils.invokeMethod(form,methodMeta.getMethodName(), o) ;
							String mapKey=meta.getClazz().equals(field.metaClass)?field.name:meta.getIncludedForm(field.metaClass).getName()+"."+field.name;
							
							objMap.put(mapKey,formatDisplayValue(methodMeta, formValue, language)  );
					}
					
					
					result.add(objMap);
					
				}
			} catch (Exception e) {
				logger.log(Level.WARNING,"", e);
			}
		}
		logger.log(Level.INFO,"loading report data took :"+(System.currentTimeMillis()-t1)+" ms ");
		return result;
	} catch (Exception e) {
		logger.log(Level.WARNING,"", e);
	}
	return null;
}
//***********************************************************************************
@SuppressWarnings("unchecked")
protected LoadingParameterMapAndReportModel getBeanReportModel(ReportModel model,Class<? extends UtopiaBasicPersistent>searchObjectClass,boolean count){
	SearchConditionItem condition=null;
	Class<? extends UtopiaBasicPersistent>p=searchObjectClass;
	if(!meta.useSearchPersistentForReport()){
		p=  meta.getPersistentClass();
	}
	
	Long[] recordIds=model.getRecordIds();
	if(recordIds!=null&&recordIds.length>0){
 		 condition=new SearchConditionItem(p,BeanUtil.getPrimaryKeyColumn(p),recordIds,QueryComparsionType.in);
	}
	
	
	ArrayList<LookupConfigurationModel>loadingLookups=new ArrayList<LookupConfigurationModel>();
	List<FieldAndMeta>loadingProperties=new ArrayList<FieldAndMeta>();
	Map<String,Object>context=ContextHolder.getContext().getContextMap();
	List<UtopiaFormMethodMetaData>methodMeta= meta.getReportItems();
	for(UtopiaFormMethodMetaData mMeta:methodMeta){
		initByMethodMetadata(p,meta, mMeta,  loadingProperties,loadingLookups, context);
		}
	IncludedFormMetaData []includeds= meta.getIncludedForms();
	if(includeds!=null){
		for(IncludedFormMetaData include:includeds){
			String linkMethod=include.getMyLinkMethod();
			if(include.isShowInReport()){
				UtopiaFormMetaData cIncluded= FormUtil.getMetaData(include.getFormClass());
				Class<? extends UtopiaBasicPersistent> ip= cIncluded.getPersistentClass();
				if(meta.useSearchPersistentForReport()){
					ip=BeanUtil.getSearchEntityClass(ip);
				}
				for(UtopiaFormMethodMetaData mMeta:  cIncluded.getReportItems()){
					if(mMeta.getMethodName().equals(linkMethod))continue;
				 initByMethodMetadata(ip,cIncluded, mMeta,  loadingProperties,loadingLookups, context);
			}
		}
	}
		}
	String pkName=BeanUtil.getPrimaryKeyColumn(p);
	int pkColIndex=loadingProperties.size()==0?0:loadingProperties.size()-1;
	loadingProperties.add(new FieldAndMeta(meta.getIdFieldName(),(Class<? extends UtopiaBasicForm<?>>)meta.getClazz(),
			new EntityPair(p,pkName)));
	EntityPair []entityPairs=new EntityPair[loadingProperties.size()];
	int counter=0;
	for(FieldAndMeta fmeta:loadingProperties){
		entityPairs[counter++]=fmeta.pair;
	}
	DefaultBeanReportModel reportModel=new DefaultBeanReportModel(p,entityPairs);
	reportModel.setConditions(condition!=null?new SearchConditionItem[]{condition}:null);
	reportModel.setLookupsToLoad(loadingLookups);
	if(!count){//for performance 
		EntityPair orderBy=null;
		String fieldName;
		if(model.getOrderbyCol()!=null){
			fieldName= meta.getMethodMetaData(model.getOrderbyCol()).getPersistentFieldName();	
		}else{
			fieldName=getDefaultReportOrderBycolumn();
		}
		orderBy=new EntityPair(p,fieldName);
		
		reportModel.setOrderby(new EntityPair[]{new EntityPair(p,pkName),orderBy});
	}
	
	return new LoadingParameterMapAndReportModel(loadingProperties,reportModel,pkColIndex);

}
//***********************************************************************************
@SuppressWarnings("unchecked")
protected void initByMethodMetadata(Class<?>persistentClass,UtopiaFormMetaData formMeta, UtopiaFormMethodMetaData mMeta,List<FieldAndMeta>loadingProperties,ArrayList<LookupConfigurationModel>loadingLookups,Map<String, Object> context){
	EntityPair pair=null;
	if(FormPersistentAttribute.FormToEntityDataTypeMap.LONG_TO_LOOKUP.equals(mMeta.getFormToEntityMapType()) ){
		LookupConfigurationModel lookupModel=getLookupModel(persistentClass,formMeta,mMeta,context);
		if(lookupModel!=null){
			loadingLookups.add(lookupModel);
			return;
			}
	}else{
			 pair=getLoadingProperty(persistentClass,mMeta,context);
				
		}
	if(pair!=null)
		loadingProperties.add(new FieldAndMeta( mMeta.getFieldName(),(Class<? extends UtopiaBasicForm<?>>)formMeta.getClazz(),pair));
}
//***********************************************************************************
protected EntityPair getLoadingProperty(Class<?>persistentClass, UtopiaFormMethodMetaData mMeta,Map<String, Object> context){
	String persistentName=mMeta.getPersistentFieldName();
	if(persistentName!=null){
			return new EntityPair(persistentClass,persistentName);	
	}
	return null;
}
//***********************************************************************************
@SuppressWarnings("unchecked")
protected LookupConfigurationModel getLookupModel(Class<?>persistentClass,UtopiaFormMetaData meta,UtopiaFormMethodMetaData mMeta,Map<String, Object> context){
	LookupConfigurationModel model= BeanUtil.getLookupConfigurationModel(context, (Class<? extends UtopiaPersistent>)persistentClass) ;
	return model;
}
//***********************************************************************************
protected class LoadingParameterMapAndReportModel{
	List<FieldAndMeta> fieldAndMeta;
	DefaultBeanReportModel reportModel;
	int pkColIndex;
	public LoadingParameterMapAndReportModel(List<FieldAndMeta> fieldAndMeta,
			DefaultBeanReportModel reportModel,int pkColIndex) {
		super();
		this.fieldAndMeta= fieldAndMeta;
		this.reportModel = reportModel;
		this.pkColIndex=pkColIndex;
		}
	
}
//***********************************************************************************
protected class FieldAndMeta{
	String name;
	Class<? extends UtopiaBasicForm<?>> metaClass;
	EntityPair pair;
	public FieldAndMeta(String name,Class<? extends UtopiaBasicForm<?>> metaClass,EntityPair pair){
		this.name=name;
		this.metaClass=metaClass;
		this.pair=pair;
	}
	
}
//***********************************************************************************
protected String getDefaultReportOrderBycolumn(){
	UtopiaFormMethodMetaData methodMeta=meta.getMethodMetaData("name");
	methodMeta=(methodMeta==null||!methodMeta.isPersistentMethod())?meta.getMethodMetaData("code"):methodMeta;
	methodMeta=(methodMeta==null||!methodMeta.isPersistentMethod())?meta.getMethodMetaData("created"):methodMeta;
	methodMeta=(methodMeta==null||!methodMeta.isPersistentMethod())?meta.getMethodMetaData(meta.getIdFieldName()):methodMeta;
	return methodMeta.getPersistentFieldName();
}
//***********************************************************************************
protected List<LinkDataModel> getLinks(Subject user,UseCase usecase){
	List<LinkDataModel>result=null;
	List<UtopiaPageLinkMetaData> linkTopageLinks=meta.getLinkPages();
	Map<String,Object>context=ContextHolder.getContextMap();
	if(linkTopageLinks!=null&&linkTopageLinks.size()>0){
		result=new ArrayList<LinkDataModel>();
		Long []availableActions=ServiceFactory.getSecurityProvider().getUsecaseAvailableActions(user, 
				usecase.getUsecaseId());
		for(UtopiaPageLinkMetaData link:linkTopageLinks){
			LinkDataModel cur=new LinkDataModel();
			Long usActionId=getUseCaseActionId(usecase, link.getActionName());
			cur.setEnable(false);
			for(Long actId:availableActions){
				if(usActionId.longValue()==actId.longValue()){
					cur.setEnable(true);
				}
			}
			String linkUrl=link.getLinkedURL();
			linkUrl=(linkUrl!=null&&linkUrl.trim().length()>0)?linkUrl.trim():
				getActionUrl(null,link.getActionName(), usecase.getFullName());
			cur.setUrl(linkUrl) ;
			cur.setIcon(link.getIcon());
			cur.setPerRecord(link.isPerRecord());
			 
			cur.setTitle(MessageHandler.getMessage(link.getActionName(),meta.getClazz().getName(), WebUtil.getLanguage(context)));
			result.add(cur);
		}
		
	}
	
	return result;
	
}
//***********************************************************************************
@Override
public ViewPageModel getViewPageModel(
		String language, UseCase usecase, Subject user) {
	ViewPageModel result=new ViewPageModel();
	Map<String,Object>context=ContextHolder.getContext().getContextMap();
	result.setReportItems(convertMethodMetaToInputItem(meta,null, context, usecase,predefindedActions.report, language,true));
	return result;
}
//***********************************************************************************
@Override
public UtopiaBasicForm<? extends UtopiaBasicPersistent> getViewPageData(Long recordId) {
	try {
			UtopiaBasicPersistent p;
			UtopiaBasicUsecaseBean<?, ?>bean=(UtopiaBasicUsecaseBean<?, ?>) BeanUtil.lookupEntityFacade(meta.getPersistentClass());
			p=bean.loadByIdComplete(recordId);
			UtopiaBasicForm<? extends UtopiaBasicPersistent>baseForm= getForm(meta.getClazz(), p);
			if(meta.useSearchPersistentForView()){
				UtopiaBasicPersistent searchObject=bean.loadSearchObjectById(recordId);
				baseForm.importPersistent(searchObject);
				return baseForm;
				//return loadViewBaseOnSearchModel(recordId, context, bean);
			}else{
				return baseForm;
			}
		
	} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
	}
	
	return null;
}
//***********************************************************************************
@Override
public TreeNode[] loadUsecaseTreeNodes(UseCase usecase,Long parentId){
	ArrayList<TreeNode>result=new ArrayList<TreeNode>();
	try {
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		String language=ContextUtil.getLoginLanguage(context);
		UtopiaBasicUsecaseBean< ?, ?> bean=(UtopiaBasicUsecaseBean< ?, ?>)ServiceFactory.lookupFacade(usecase.getRemoteClassName());
		List<ParentChildPair> pairs=((TreeLoaderBean)bean).loadChildren(parentId,   null);
		if(pairs!=null){
			for(ParentChildPair pair:pairs){
				WebUtil.initNamePairName(pair,language);
				TreeNode node=new TreeNode(pair.getKey(),pair.getName() , pair.getDescription(), null, parentId);
				node.setChildrenCount(pair.getChildrenCount()==null?0:pair.getChildrenCount().intValue());
				result.add(node);
			}
			Collections.sort(result,new TreeNodeComparator());
		}
		} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
	}
	return result.toArray(new TreeNode[result.size()]);
}

//***********************************************************************************
protected CustomButton [] convertCustomButtons(UtopiaFormMetaData formMetaData,List<CustomButtonConfiguration> customButtons,Map<String,Object>context){
	if(customButtons!=null){
		ArrayList<CustomButton>result=new ArrayList<CustomButton>();
		for(CustomButtonConfiguration config:customButtons){
			CustomButton button=new CustomButton();
			button.setColumnName(config.getId());
			button.setCssClass(config.getCssClass());
			button.setOnclick(config.getOnClick());
			String displayLogic=config.getDisplayLogic();
			displayLogic= mapDependency(formMetaData,button,null,displayLogic, InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,context);
			button.setDisplayLogic(displayLogic);
			String readOnlyLogic=config.getReadOnlyLogic();
			readOnlyLogic=mapDependency(formMetaData,button,null,readOnlyLogic, InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,context);
			button.setReadOnlyLogic(readOnlyLogic);
			String language=ContextUtil.getLoginLanguage(context);
			button.setHeader(WebUtil.write(formMetaData.getHeader(config, language),language));
			result.add(button);
		}
		return result.toArray(new CustomButton[result.size()]);
	}
	return null;
}
//***********************************************************************************
@SuppressWarnings("unchecked")
@Override
public SearchPageData getDashBoardRecords(
		UsecaseSearchCriteria criteria) {
	SearchPageData searchResult=new SearchPageData();
	List<DashboardRecord>result=new ArrayList<DashboardRecord>();
	try {
		Long usecaseId= criteria.getUsecaseId();
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
		Long userId= ContextUtil.getUserId(context);
		FiscalYearInfo fiscalYear= FiscalYearUtil.getFiscalYear(context);
		CmTransitionFacadeRemote facade=
				(CmTransitionFacadeRemote)ServiceFactory.lookupFacade( CmTransitionFacadeRemote.class);
		int recordCount=facade.getTranistionalRecordCount(userId, usecaseId, fiscalYear.getId(),criteria.getFromDocTypeId());
		searchResult.setTotalResultCount(recordCount);
		if(recordCount>0){
					Set<UtopiaFormMethodMetaData> loadMethods=new HashSet<UtopiaFormMethodMetaData>();
					
					List<UtopiaFormMethodMetaData>titleMethods= new ArrayList<UtopiaFormMethodMetaData>() ;
					if(meta.getTitleMethods()==null||meta.getTitleMethods().getMethods()==null||meta.getTitleMethods().getMethods().size()==0){
						titleMethods.add(meta.getMethodMetaData( meta.getIdFieldName()));
					}else{
						titleMethods.addAll(meta.getTitleMethods().getMethods());
					}
					loadMethods.addAll(titleMethods);
					ItemsDisplayConfiguration descriptionMethods= meta.getDescriptionMethods();
					if(descriptionMethods!=null&&descriptionMethods.getMethods()!=null){
						loadMethods.addAll(descriptionMethods.getMethods());
					}
					ItemsDisplayConfiguration indexMethods=meta.getIndexMethods();
					if(indexMethods!=null&&indexMethods.getMethods()!=null){
						loadMethods.addAll(indexMethods.getMethods());
					}
					ItemsDisplayConfiguration dateMethods=meta.getLastModifiedDateMethods();
					if(dateMethods!=null&&dateMethods.getMethods()!=null){
						loadMethods.addAll(dateMethods.getMethods());
					}
			        OrderBy[] orderby= convertOrderBy(meta.getPersistentClass(),titleMethods,descriptionMethods.getMethods(),indexMethods.getMethods(), dateMethods.getMethods(),criteria.getOrderBy());
					List<TransitionRecord>transitionalRecord= facade.getTransitionRecords(userId, usecaseId,fiscalYear.getId(),criteria.getFromDocTypeId(),
							orderby==null?null:orderby,  criteria.getFrom(),criteria.getTo());
					if(transitionalRecord!=null){
							String language=ContextUtil.getLoginLanguage(context);
							
							List<FormColorLogic>colorLogics= meta.getColorLogic();
							UsecaseDocInfo docInfo= DocTypeUtil.getUsecaseDocStatusInfo(usecaseId,fiscalYear.getId());
							List<UsecaseDocStatusInfo>docStatuses= docInfo.getStatuses();
							
							Class<? extends UtopiaBasicForm<?>>formClass=(Class<? extends UtopiaBasicForm<?>>) meta.getClazz();
							List<UtopiaBasicForm<?>>forms=new ArrayList<UtopiaBasicForm<?>>();
							for(TransitionRecord persistent:transitionalRecord){
								UtopiaBasicForm<?> form=(UtopiaBasicForm<?>) ConstructorUtils.invokeConstructor(formClass, o);
								UtopiaPersistent p=(UtopiaPersistent)persistent.getRecord();
								form.setLocale(language);
								form.importPersistent(p);
								forms.add(form);
								
							}
							List<Map<String,Object>> cachedData=cacheFormData( forms);
							Map<UtopiaFormMethodMetaData,Map<?,String>> dataMap=getFormatedDataSet(cachedData, loadMethods, context);
							for(int i=0;i<transitionalRecord.size();i++){
								TransitionRecord persistent=transitionalRecord.get(i);
								UtopiaPersistent p=(UtopiaPersistent)persistent.getRecord();
								DashboardRecord record=new DashboardRecord();
								Map<String,Object>recordDataMap= cachedData.get(i);
								record.setSubject(getDisplayText(titleMethods, meta.getTitleMethods().getSeperator(),dataMap,recordDataMap) );
								record.setRowId((Long)recordDataMap.get(meta.getIdFieldName()) );
								record.setDescription(getDisplayText(descriptionMethods.getMethods(),descriptionMethods.getSeperator(), dataMap,recordDataMap));
								record.setIndex(getDisplayText(indexMethods.getMethods(),indexMethods.getSeperator(), dataMap, recordDataMap));
								record.setDate(getDisplayText(dateMethods.getMethods(),dateMethods.getSeperator(), dataMap, recordDataMap));
								record.setRead(persistent.isViewed());
								if(!record.isRead()){
									record.setCssClass(i%2==0?DashboardRecord.EVEN_UNREAD_ROW_CSS:DashboardRecord.ODD_UNREAD_ROW_CSS);
								}
								record.setLastModified(p.getUpdated());
								record.setUpdatedBy(persistent.getLastModifiedBy());
								Map<String,Object>rowContext=null;
								if(colorLogics!=null&&colorLogics.size()>0){
									rowContext=new HashMap<String, Object>(context); 
									rowContext.putAll(recordDataMap);
									for(FormColorLogic logic:colorLogics){
										boolean evaluationResult=	LogicEvaluator.evaluateLogic(logic.getLogic(), rowContext);
										if(evaluationResult){
											record.setCssClass(logic.getCSSClass());
											break;
										}
									}
									
								}
								try {
									Enum<?>status= (Enum<?>)MethodUtils.invokeExactMethod(p, docInfo.getMethodName(), o);
									for(UsecaseDocStatusInfo docStatusInfo:docStatuses){
										if(docStatusInfo.getStatus()==status.ordinal()){
											record.setDocStatusId(docStatusInfo.getStatusId());
											record.setStatus(docStatusInfo.getStatus());
										}
									}
								} catch (Exception e) {
									logger.log(Level.WARNING,"",e);
								}
								result.add(record);
							}
						}
		}
		
	} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
	}
	searchResult.setRows(result);
	return searchResult;
}
//*********************************************************************************************************************************************
protected String getDisplayText(List<UtopiaFormMethodMetaData> metaDatas,String seperator,Map<UtopiaFormMethodMetaData,Map<?,String>>dataMap,Map<String,Object> cachedData){
	StringBuffer result=new StringBuffer();
	int index=0;
	for(UtopiaFormMethodMetaData metaData:metaDatas){
		if(dataMap.containsKey(metaData)){
			Map<?,String>rowDisplay=dataMap.get(metaData);
			Object column;
				column =cachedData.get(metaData.getFieldName());
				if(rowDisplay.containsKey(column)){
					String cell=rowDisplay.get(column);
					if(cell!=null&&cell.trim().length()>0){
						if(index>0){
							result.append(seperator);
						}
						result.append(cell.trim());
						index++;
					}
				}
			
		}
		
	}
	return result.toString();
}
//*********************************************************************************************************************************************
protected List<Map<String,Object>> cacheFormData( List<UtopiaBasicForm<?>>forms){
	List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
	for(UtopiaBasicForm<?>form:forms){
		Map<String,Object> formDataMap=new HashMap<String, Object>();
		for(UtopiaFormMethodMetaData methodMeta:meta.getMethodMetaData()){
			Object temp =null;
			try {
				 temp = MethodUtils.invokeExactMethod(form, methodMeta.getMethodName(), o);
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
			formDataMap.put(methodMeta.getFieldName(), temp);
		}
		result.add(formDataMap);
	}
	return result;
}
//*********************************************************************************************************************************************
@SuppressWarnings("unchecked")
protected Map<UtopiaFormMethodMetaData,Map<?,String>> getFormatedDataSet(List<Map<String,Object>> cachedData,Set<UtopiaFormMethodMetaData> loadMethods, Map<String,Object>context) {
	
	String language=ContextUtil.getLoginLanguage(context);	
	Map<UtopiaFormMethodMetaData,Map<?,String>>result=new HashMap<UtopiaFormMethodMetaData, Map<?,String>>();
	try {
		for(UtopiaFormMethodMetaData methodMeta:loadMethods){
			HashSet<Object>cache=new HashSet<Object>();
			try {
			for(Map<String,Object>form:cachedData){
				Object temp = form.get(methodMeta.getFieldName());
				cache.add(temp);
			}
				Map<Object,String>formated=new HashMap<Object, String>();
				switch (methodMeta.getDisplayType()){
				case lookup:{
					UILookupInfo info= loadLookupValues(cache, meta.getLookupConfigurationModel(methodMeta, context), context);
					for(String data[]: info.getData()){
						formated.put(Long.parseLong(data[0]), data[1]);
					}
				}break;
				case LOV:{
					Map<Long,String[]>lovValues=loadLOVValues(cache.toArray(new Long[cache.size()]), meta);
					if(lovValues!=null&&lovValues.size()!=0){
						LOVMetaData lovMeta= methodMeta.getLOVConfiguration(null);
						UtopiaFormMetaData lovMetaData= FormUtil.getMetaData((Class<? extends UtopiaBasicForm<?>>) lovMeta.getFromClass());
						for(Long key:lovValues.keySet()){
							formated.put(key, getLOVLookupDisplay( lovMeta, lovMetaData.getSearchResultItems(), lovValues.get(key)));
						}
					}
				}break;
				case list:
				case RadioButton:{
					 List<NamePair>pairs= EnumUtil.getEnumLookups( meta.getEnumClass(methodMeta), language);
					 for(Object key:cache){
						 formated.put(key, WebUtil.write(pairs.get(((Enum<?>)key).ordinal()), language)) ;
					 }
				}
				default:{
					for(Object key:cache){
						formated.put(key, WebUtil.write(key, language));
					}
				}
			}
				result.put(methodMeta, formated);
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			 	
			}
	}
	} catch (Exception e) {
		logger.log(Level.WARNING,"",e);
	 	
	}
	return result;
}
//***********************************************************************************
protected HashMap<String, String>getContext(Map<String,Object>context){
	HashMap<String,String>result=new HashMap<String, String>();
	Object currentUserId=context.get(ContextUtil.CURRENT_USER_CONTEXT_PARAMETER);
	result.put(ContextUtil.CURRENT_USER_CONTEXT_PARAMETER,currentUserId==null?"":currentUserId.toString() );
	return result;
}
//***********************************************************************************
protected OrderBy[] convertOrderBy(Class<?> entityClass,List<UtopiaFormMethodMetaData>titleMethods,List<UtopiaFormMethodMetaData>descriptionMethods,
		List<UtopiaFormMethodMetaData>indexMethods,List<UtopiaFormMethodMetaData>dateMethods, 
		ir.utopia.core.util.tags.datainputform.client.grid.model.OrderBy orderBy){
	if(orderBy!=null){
		List<UtopiaFormMethodMetaData>collectionToLookup=null;
		String columnName=orderBy.getColumnName();
		if("index".equals(columnName)){
			collectionToLookup=indexMethods;
		}else if("date".equals(columnName)){
			collectionToLookup=dateMethods;
		}else if("subject".equals(columnName)){
			collectionToLookup=titleMethods;
		}else if("description".equals(columnName)){
			collectionToLookup=descriptionMethods;
		}
		if(collectionToLookup!=null){
			ArrayList<OrderBy>result=new ArrayList<OrderBy>();
			for(UtopiaFormMethodMetaData column:collectionToLookup){
				result.add( new OrderBy(entityClass,column.getPersistentFieldName(),
						orderBy.getDir()==ir.utopia.core.util.tags.datainputform.client.grid.model.OrderBy.SORT_ORDER_ASCENDING?OrderBy.OrderbyDirection.ASC:OrderBy.OrderbyDirection.DESC));
			}
			return result.toArray(new OrderBy[result.size()]);
		}
			
	}
	return null;
	
}
//***********************************************************************************
}
