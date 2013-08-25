package ir.utopia.core.mailtemplate.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;

import javax.ejb.Remote;

/**
 * Remote interface for CoMailTemplateFacade.
 * 
 * @author M2je
 */
@Remote
public interface CoMailTemplateFacadeRemote extends UtopiaBasicUsecaseBean<CoMailTemplate, CoMailTemplate>{
	
}