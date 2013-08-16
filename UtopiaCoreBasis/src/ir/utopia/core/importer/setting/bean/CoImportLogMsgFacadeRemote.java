package ir.utopia.core.importer.setting.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.importer.setting.persistent.CoImportLogMsg;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
@Remote
public interface CoImportLogMsgFacadeRemote extends UtopiaBasicUsecaseBean<CoImportLogMsg,CoImportLogMsg> {

	public List<?> findInMessages(String query,Map<String,Object>queryValues);
}
