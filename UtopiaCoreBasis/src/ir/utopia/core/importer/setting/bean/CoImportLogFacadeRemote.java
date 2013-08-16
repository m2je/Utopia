/**
 * 
 */
package ir.utopia.core.importer.setting.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.importer.setting.persistent.CoImportLog;
import ir.utopia.core.importer.setting.persistent.CoVImportLog;

import java.util.Map;

import javax.ejb.Remote;

/**
 * @author Salarkia
 *
 */
@Remote
public interface CoImportLogFacadeRemote extends UtopiaBasicUsecaseBean<CoImportLog, CoVImportLog> {

	public Long getLastImportedPrimaryKey(Long settingId,Map<String,Object>context);
	
	
}
