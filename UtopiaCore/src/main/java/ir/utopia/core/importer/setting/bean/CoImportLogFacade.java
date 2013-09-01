/**
 * 
 */
package ir.utopia.core.importer.setting.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.constants.Constants.IsActive;
import ir.utopia.core.importer.setting.persistent.CoImportLog;
import ir.utopia.core.importer.setting.persistent.CoVImportLog;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author Salarkia
 *
 */
@Stateless
public class CoImportLogFacade extends AbstractBasicUsecaseBean<CoImportLog,CoVImportLog> implements
		CoImportLogFacadeRemote {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoImportLogFacade.class.getName());
	}
	@Override
	public Long getLastImportedPrimaryKey(Long settingId,Map<String,Object>context) {
		try {
			StringBuffer query=new StringBuffer("select max(model.lastImportedPk) from CoImportLog model where model.coImporterSetting.coImporterSettingId=:coImporterSettingId and model.isactive=:isactive");
			Query q= entityManager.createQuery(query.toString());
			q.setParameter("isactive", IsActive.active);
			q.setParameter("coImporterSettingId", settingId);
			return  (Long)q.getSingleResult();
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
		}
		return null;
	}

	

}
