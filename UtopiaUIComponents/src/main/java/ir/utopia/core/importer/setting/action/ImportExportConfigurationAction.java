package ir.utopia.core.importer.setting.action;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.importer.FieldSetup;
import ir.utopia.core.importer.setting.bean.CoImporterSettingFacadeRemote;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.struts.ActionUtil;
import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.struts.UtopiaTypeConverter;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.util.GWTUtil;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageImExServices;
import ir.utopia.core.util.tags.importpage.client.ImExPageDataModel;
import ir.utopia.core.util.tags.importpage.client.ImExService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class ImportExportConfigurationAction extends UtopiaBasicAction implements ImExService,SearchPageImExServices{
	Logger logger=Logger.getLogger(ImportExportConfigurationAction.class.getName());
	private static final String MAP_TYPE_POST_FIX="_mapType";
	private static final String MAP_INDEX_POST_FIX="_index";
	String __usecaseFormclass;
	String __usecaseName;
	String __configurationName,__ConfigurationDesc;
	Long __settingId;
	ExecutionResult result;
	Map<String,String[]>requestParams;
	private String currentUri;
	

	public String get__usecaseFormclass() {
		return __usecaseFormclass;
	}
	public void set__usecaseFormclass(String __usecaseFormclass) {
		this.__usecaseFormclass = __usecaseFormclass;
	}
	public String get__usecaseName() {
		return __usecaseName;
	}
	public void set__usecaseName(String __usecaseName) {
		this.__usecaseName = __usecaseName;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7430097154128214639L;

	public String execute() throws Exception {
		Map<String,Object>context= ContextHolder.getContextMap();
		try {
			Map<Integer, String>map= ActionUtil.parseClassAndMethod(currentUri);
			String actionName=map.get(ActionUtil.METHOD);
			if(Constants.predefindedActions.save.name().equals(actionName)||Constants.predefindedActions.update.name().equals(actionName)){
				createOrUpdateSetting(context);
			}
			return "SUCCESS";
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result= getExceptionHandler().handel(e, context);
			return "ERROR";
		}
	}
//***************************************************************************************************************************************	
	public ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult deleteSetting(Long settingId){
		ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult result;
		Map<String,Object>context= ContextUtil.getContext();
		try {
			CoImporterSettingFacadeRemote settingFacade=(CoImporterSettingFacadeRemote)ServiceFactory.lookupFacade(CoImporterSettingFacadeRemote.class);
			CoImporterSetting setting=new CoImporterSetting();
			setting.setCoImporterSettingId(settingId);
			settingFacade.delete(setting);
			result=new ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result=GWTUtil.convertExectionResult(getExceptionHandler().handel(e, context));
		}
		return result;
	}
//***************************************************************************************************************************************	
	protected void createOrUpdateSetting(Map<String,Object>context)throws Exception{
		__usecaseFormclass=ServiceFactory.getSecurityProvider().decrypt(__usecaseFormclass);
			UtopiaFormMetaData meta= FormUtil.getMetaData(__usecaseFormclass);
			__usecaseName=ServiceFactory.getSecurityProvider().decrypt(__usecaseName);
			List<UtopiaFormMethodMetaData>methodMetas= meta.getInputItems();
			List<FieldSetup>fields=new ArrayList<FieldSetup>();
			Locale locale=getUserLocale();
			
			
			for(UtopiaFormMethodMetaData mMeta:methodMetas){
				if(requestParams.containsKey(mMeta.getFieldName()) || requestParams.containsKey(mMeta.getFieldName()+MAP_INDEX_POST_FIX)){
					String indexStr=requestParams.get(mMeta.getFieldName()+MAP_INDEX_POST_FIX)[0];
					int index=-1;
					if(indexStr!=null&&indexStr.trim().length()>0&&indexStr.matches("[0-9]+")){
						 index= Integer.parseInt(indexStr);
					}
					String[] defaultValues= requestParams.get(mMeta.getFieldName());
					Object defaultValue= UtopiaTypeConverter.convertFromString(mMeta.getReturnType(), locale.toString(), defaultValues);
					if((defaultValue!=null&&(defaultValue.toString().trim().length()>0))||index>0){
						FieldSetup field=new FieldSetup(mMeta);
						int mapType=Integer.parseInt(requestParams.get(mMeta.getFieldName()+MAP_TYPE_POST_FIX)[0]);
						field.setDefaultValue(defaultValue);
						field.setType(FieldSetup.Types.getType(mapType));
						field.setIndex(index);
						fields.add(field);
					}else{
						logger.log(Level.FINEST,"field {0} will be ignored when importing,it has no mapping preferences ",mMeta.getFieldName());
					}
				}else{
					logger.log(Level.WARNING,"fail to find configuration for importing field {0}",mMeta.getFieldName());
				}
			}
			CoImporterSetting setting=new CoImporterSetting();
			setting.setCoImporterSettingId(get__settingId());
			ObjectMapper mapper = new ObjectMapper();
			AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		    mapper.setAnnotationIntrospector(introspector);
			setting.setSetting(mapper.writeValueAsString(fields));
			setting.setName(get__configurationName());
			setting.setComment(get__ConfigurationDesc());
			LookupInfo usecaseInfo=new LookupInfo(CoUsecase.class, UsecaseUtil.getUsecaseWithName(__usecaseName).getUsecaseId());
			ArrayList<LookupInfo>lookupInfos=new ArrayList<LookupInfo>();
			lookupInfos.add(usecaseInfo);
			setting.setLookupInfos(lookupInfos);
			CoImporterSettingFacadeRemote settingFacade=(CoImporterSettingFacadeRemote)ServiceFactory.lookupFacade(CoImporterSettingFacadeRemote.class);
			if(setting.getCoImporterSettingId()!=null){
				setting=settingFacade.update(setting);
			}else{
				setting=settingFacade.save(setting);
			}
			__settingId=setting.getRecordId();
			result=new ExecutionResult(true);
	}
	
	

	public Long get__settingId() {
		return __settingId;
	}

	public void set__settingId(Long __settingId) {
		this.__settingId = __settingId;
	}

	public String get__configurationName() {
		return __configurationName;
	}

	public void set__configurationName(String __configurationName) {
		this.__configurationName = __configurationName;
	}

	public String get__ConfigurationDesc() {
		return __ConfigurationDesc;
	}

	public void set__ConfigurationDesc(String __ConfigurationDesc) {
		this.__ConfigurationDesc = __ConfigurationDesc;
	}

	public ExecutionResult getResult() {
		return result;
	}

	public void setResult(ExecutionResult result) {
		this.result = result;
	}

	

	@Override
	public ImExPageDataModel getSettingData(Long settingId) {
		try {
			CoImporterSettingFacadeRemote settingFacade=(CoImporterSettingFacadeRemote)ServiceFactory.lookupFacade(CoImporterSettingFacadeRemote.class);
			CoImporterSetting setting= settingFacade.findById(settingId);
			if(setting!=null){
				ImExPageDataModel result=new ImExPageDataModel();
				result.setName(setting.getName());
				result.setDescription(setting.getComment());
				result.setSettingId(settingId);
				result.setSetting(setting.getSetting());
				return result;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}

	
}
