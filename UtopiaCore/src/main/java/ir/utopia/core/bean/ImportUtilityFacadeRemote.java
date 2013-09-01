package ir.utopia.core.bean;

import ir.utopia.core.importer.setting.persistent.CoImporterSetting;
import ir.utopia.core.process.ProcessListener;

import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface ImportUtilityFacadeRemote extends UtopiaBean {

	public void importData(Class<? extends UtopiaBasicUsecaseBean<?,?>> facadeClass, ImportDataProvider dataProvider, int userFrom,
			int userTo, Map<String,Object>context, ProcessListener listener) throws Exception;

	public void importData(CoImporterSetting setting, ImportDataProvider dataProvider, int userFrom,
			int userTo, Map<String,Object>context, ProcessListener listener) throws Exception;

	public void scheduleImport(CoImporterSetting setting,ImportDataProvider dataProvider,Map<String,Object>context);
}
