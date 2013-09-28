package ir.utopia.core.importer.setting.action;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.importer.settinginstance.bean.CoImporterInstanceFacadeRemote;
import ir.utopia.core.importer.settinginstance.persistent.CoImporterInstance;
import ir.utopia.core.persistent.lookup.model.Condition;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.util.GWTUtil;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;
import ir.utopia.core.util.tags.importpage.client.ImportInstanceService;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportSettingInstanceAction extends UtopiaBasicAction implements ImportInstanceService {
	Logger logger=Logger.getLogger(ImportExportConfigurationAction.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 8087551153280023036L;

	public String execute() throws Exception {
		
		return "SUCCESS";
	}

	public ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult deleteSettingInstance(Long settingInstanceId){
		ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult result;
		Map<String,Object>context=createContext();
		try {
			CoImporterInstanceFacadeRemote bean=(CoImporterInstanceFacadeRemote)ServiceFactory.lookupFacade(CoImporterInstanceFacadeRemote.class);
			CoImporterInstance instance=new CoImporterInstance();
			instance.setCoImporterInstanceId(settingInstanceId);
			bean.delete(instance);
			result=new ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result=GWTUtil.convertExectionResult( getExceptionHandler().handel(e, context));
		}
		return result;
	}

	@Override
	public UILookupInfo loadImportInstances(Long settingId, int fileType) {
		try {
			Map<String,Object>context=createContext();
			CoImporterInstanceFacadeRemote bean=(CoImporterInstanceFacadeRemote)ServiceFactory.lookupFacade(CoImporterInstanceFacadeRemote.class);
			LookupConfigurationModel lookupModel=new LookupConfigurationModel(CoImporterInstance.class);
			lookupModel.setOwnDisplayColumns("name");
			lookupModel.setPrimaryKeyColumn("coImporterInstanceId");
			context.put("coImporterSettingId", settingId);
			context.put("importType", Constants.ImportFormat.convert(fileType));
			lookupModel.addCondition(new Condition("CoImporterInstance.coImporterSetting.coImporterSettingId=@coImporterSettingId@ AND CoImporterInstance.importType=:@importType@", context));
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo( bean.loadLookup( null),ContextUtil.getLoginLanguage(context) );
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
	
	
}
