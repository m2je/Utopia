package ir.utopia.common.basicinformation.jobtitle.bean;


import ir.utopia.common.basicinformation.jobtitle.persistent.CmJobTitle;
import ir.utopia.common.basicinformation.jobtitle.persistent.CmVJobTitle;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;


/**
 * Facade for entity CmJobTitle.
 * 
 * @author Arsalani 
 */
@Stateless
public class CmJobTitleFacade  extends AbstractBasicUsecaseBean<CmJobTitle,CmVJobTitle> implements CmJobTitleFacadeRemote {

}
