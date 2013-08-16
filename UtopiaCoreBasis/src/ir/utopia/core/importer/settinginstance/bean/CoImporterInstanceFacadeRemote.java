package ir.utopia.core.importer.settinginstance.bean;

import javax.ejb.Remote;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.importer.settinginstance.persistent.CoImporterInstance;

@Remote
public interface CoImporterInstanceFacadeRemote extends
		UtopiaBasicUsecaseBean<CoImporterInstance, CoImporterInstance> {

}
