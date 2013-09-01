package ir.utopia.core.importer.setting.action;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.bean.ImportDataProvider;
import ir.utopia.core.bean.ImportUtilityFacadeRemote;
import ir.utopia.core.bean.UtopiaProcessBean;
import ir.utopia.core.constants.Constants.ImportFormat;
import ir.utopia.core.exception.DeleteRecordExeption;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.importer.FieldSetup;
import ir.utopia.core.importer.ImportSetup;
import ir.utopia.core.importer.model.DefaultImportDataHandler;
import ir.utopia.core.importer.model.DefaultImportDataProvider;
import ir.utopia.core.importer.model.ImportDataHandler;
import ir.utopia.core.importer.setting.bean.CoImporterSettingFacadeRemote;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;
import ir.utopia.core.struts.FormUtil;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.util.ImportSettingUtils;
import ir.utopia.core.util.tags.process.action.AbstractUtopiaProcessAction;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.ModelDriven;

public class ImporterSettingAction extends AbstractUtopiaProcessAction<UtopiaProcessBean> implements ModelDriven<ImporterSettingForm>,UtopiaProcessAction  {

	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(ImporterSettingAction.class.getName());
	}
	private static final long serialVersionUID = -4099768103662463989L;
	private static final Long MAX_ID_RESERVED = 1000L;
	ImporterSettingForm form;
	@Override
	public ImporterSettingForm getModel() {
		if(form==null){
			form=new ImporterSettingForm();
		}
		return form;
	}
//****************************************************************************************************	
	private void delete(CoImporterSettingFacadeRemote importerSettingRemote, Map<String,Object>context) {
		CoImporterSetting coImporterSetting =importerSettingRemote.findById(form.getImportSetting());
		try {
			if(coImporterSetting.getCoImporterSettingId() > MAX_ID_RESERVED)
				importerSettingRemote.delete(coImporterSetting);
		} catch (DeleteRecordExeption e) {
			logger.log(Level.SEVERE,"CoImportSettingManage delete failed", e);
			e.printStackTrace();
		}
	}
//****************************************************************************************************	
	@Override
	protected String getProcessName() {
		
		return "ImportConfigurationManagement";
	}
//****************************************************************************************************
	@Override
	public ProcessExecutionResult confirm(String[] params, String[] values) {
		return super.confirm(params, values);
	}
//****************************************************************************************************
	@Override
	public ProcessExecutionResult execute(String[] params, String[] values) {
		ProcessExecutionResult result=new ProcessExecutionResult(true, ProcessExecutionResult.PROCESS_STATUS_FINISHED);
		Map<String,Object>context=createContext();
		try {
			initParameters(params, values);
			CoImporterSetting setting=null;
			CoImporterSettingFacadeRemote importerSettingRemote=(CoImporterSettingFacadeRemote) ServiceFactory.lookupFacade(CoImporterSettingFacadeRemote.class.getName());
			if("save".equals(form.getActionName())){
				CoImporterSetting coImporterSetting =(CoImporterSetting) form.convertToPersistent();
				importerSettingRemote.save(coImporterSetting);
				setting=coImporterSetting;
			}
			else if("delete".equals(form.getActionName())){
				delete(importerSettingRemote,context );
				setting=null;
			}else if("update".equals(form.getActionName())){
				CoImporterSetting dbSource =importerSettingRemote.findById(form.getImportSetting());
				form.setImportSetting(dbSource.getCoImporterSettingId());
				CoImporterSetting p=(CoImporterSetting)form.convertToPersistent();
				importerSettingRemote.update(p);
				setting=p;
			}
			
//			if(setting!=null&&setting.getScdStartDate()!=null){
				ImportDataProvider dataProvider= getDataProvider(context);
				ImportUtilityFacadeRemote bean=(ImportUtilityFacadeRemote)ServiceFactory.lookupFacade(ImportUtilityFacadeRemote.class);
				bean.scheduleImport(setting, dataProvider, context);
//			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			super.handelException(result,  e);
			result.setSuccess(false);
		}
		
		return result;
	}
//****************************************************************************************************
	public ImportDataProvider getDataProvider(Map<String,Object>context){
		DefaultImportDataHandler handler=new DefaultImportDataHandler(); 
		form= getModel();
		String fromClass=ServiceFactory.getSecurityProvider().decrypt(form.getFormClass());
		UtopiaFormMetaData meta=FormUtil.getMetaData(fromClass);
		ImportSetup setup=new ImportSetup(meta,getUserLocale().getLanguage());
		UtopiaFormMethodMetaData[] methodsMD = meta.getMethodMetaData();
		List<FieldSetup> fieldsSetup=new ArrayList<FieldSetup>(); 
		for(UtopiaFormMethodMetaData methodMD:methodsMD){
			InputItem inputItem = (InputItem)methodMD.getAnnotation(InputItem.class);
			if(inputItem != null){
				FieldSetup fieldSetup = new FieldSetup(methodMD);
				fieldSetup.setReturnType(ImportSettingUtils.togglePrimitive(fieldSetup.getReturnType()));
				String fieldName=methodMD.getFieldName();
//				fieldSetup.setImportFormula(ImportSettingUtils.getFieldFormula(form.getSetting(),fieldName)); TODO delete
				fieldSetup.setType(ImportSettingUtils.getType(form.getSetting(),fieldName));
				fieldsSetup.add(fieldSetup);
			}
		}
		ImportFormat type=form.getFileType();
		Properties props=new Properties();
		props.put(ImportDataHandler.IMPORT_PROPERTIES_EXCEL_SHEET_INDEX, setup.getSheetIndex());
		props.put(ImportDataHandler.EXCEL_FIRST_ROW_IS_HEADER, setup.isFirstLineTitle());
		props.put(ImportDataHandler.TEXT_FETCH_EXPRESSION, form.getRegexp());
		props.put(ImportDataHandler.SQL, form.getSql());
		props.put(ImportDataHandler.RESOURCE_NAME, form.getResourceName());
		handler.setFile(form.getFileName(), type);
		handler.setProperties(props);
		setup.setMethodMetaData(fieldsSetup.toArray(new FieldSetup[fieldsSetup.size()]));
		setup.setResourceName(form.getResourceName());
		setup.setRegExp(form.getRegexp());
		setup.setLocale(getUserLocale().getLanguage());
		ImportDataProvider result=new DefaultImportDataProvider(handler,setup);
		return result;
		}
	


//************************************************************************************************
	
}
