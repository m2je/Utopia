package ir.utopia.common.basicinformation.jobtitle.bean;

import ir.utopia.common.basicinformation.jobtitle.persistent.CmJobTitle;
import ir.utopia.common.basicinformation.jobtitle.persistent.CmVJobTitle;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;


/**
 * Remote interface for CmJobTitleFacade.
 * 
 * @author Arsalani
 */
@Remote
public interface CmJobTitleFacadeRemote  extends UtopiaBasicUsecaseBean<CmJobTitle,CmVJobTitle> {

}
