package ir.utopia.core.importer.setting.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.importer.setting.persistent.CoImporterSetting;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Facade for entity CmImporterSetting.
 * 
 * @see ir.utopia.common.importer.setting.CmImporterSetting
 * @author Golnari@gmail
 */
@Stateless
public class CoImporterSettingFacade extends AbstractBasicUsecaseBean<CoImporterSetting,CoImporterSetting> implements CoImporterSettingFacadeRemote {
	// property constants
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoImporterSettingFacade.class.getName());
	}
	
	public static final String NAME = "name";
	public static final String PERSISTENT_NAME = "persistentName";
	
	public CoImporterSetting findByName(Object name, int... rowStartIdxAndCount) {
		List<CoImporterSetting> result=findByProperty(NAME, name, rowStartIdxAndCount);
		return (result!=null&&result.size()>0)?result.get(0):null;
	}

	public List<CoImporterSetting> findByPersistentName(Object persistentName, int... rowStartIdxAndCount) {
		return findByProperty(NAME, persistentName,rowStartIdxAndCount);
	}

	public CoImporterSetting loadSetting(String settingName) {
		CoImporterSetting system=findByName(settingName, null);
		return system;
	}

	
//***************************************************************************************************************
	@Override
	public List<CoImporterSetting> findAllByUsecaseId(Long usecaseId) {
		List<CoImporterSetting> result=findByProperty("coUsecase.coUsecaseId", usecaseId, null);
		return result;
	}
//***************************************************************************************************************
	public List<CoImporterSetting>getScheduledImports(){
		try {
			StringBuffer query=new StringBuffer("select model from ").append(CoImporterSetting.class.getSimpleName()).
			append(" model ").append(" where  model.isactive=:isactive and model.scdStartDate is not null " );
			Query q= entityManager.createQuery(query.toString());
			q.setParameter("isactive", IsActive.active);
			List<CoImporterSetting>res= q.getResultList();
			logger.log(Level.INFO,res.size() +" schedule import found ");
			 return res;
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}
}