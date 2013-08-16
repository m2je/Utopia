package ir.utopia.core.importer.setting.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CmImporterSettingFacade.
 * 
 * @author Golnari@gamil
 */
@Remote
public interface CoImporterSettingFacadeRemote extends UtopiaBasicUsecaseBean<CoImporterSetting,CoImporterSetting> {
	
	public CoImporterSetting findByName(Object name,int... rowStartIdxAndCount);

	
	/**
	 * loads the system and all subSystems
	 * @return
	 */
	public CoImporterSetting loadSetting(String settingName);
	
	/**
	 * 
	 * @param usecaseId
	 * @return
	 */
	public List<CoImporterSetting> findAllByUsecaseId(Long usecaseId);
	/**
	 * 
	 * @return
	 */
	public List<CoImporterSetting>getScheduledImports();
}