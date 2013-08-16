package ir.utopia.core.mailtemplate.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;

import javax.ejb.Stateless;

/**
 * Facade for entity CoMailTemplate.
 * 
 * @see test.CoMailTemplate
 * @author M2je
 */
@Stateless
public class CoMailTemplateFacade extends AbstractBasicUsecaseBean<CoMailTemplate, CoMailTemplate> implements CoMailTemplateFacadeRemote {
	
}