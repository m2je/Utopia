package ir.utopia.core.util.mailtemplate;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.customproperty.bean.CoCustomPropertyFacadeRemote;
import ir.utopia.core.customproperty.persistent.CoCustomProperty;
import ir.utopia.core.mailtemplate.bean.CoMailTemplateFacadeRemote;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.smtp.bean.CoSmtpConfigurationFacadeRemote;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailTemplate;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailTemplateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailTemplateAction extends UtopiaBasicAction implements MailTemplateService {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(MailTemplateAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5990063897960258650L;

	@Override
	public ProcessExecutionResult deleteMailTemplate(Long mailTemplateId) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context= createContext();
		try {
			CoMailTemplateFacadeRemote bean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			CoMailTemplate address=new CoMailTemplate();
			address.setCoMailTemplateId(mailTemplateId);
			bean.delete(address);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e, context);
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	}
//******************************************************************************************************************
	@Override
	public UILookupInfo getCurrentTemplates() {
		Map<String,Object>context=createContext();
		try {
			CoMailTemplateFacadeRemote bean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			List<NamePair>pairs= bean.loadLookup( null);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(pairs, ContextUtil.getLoginLanguage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//******************************************************************************************************************
	@Override
	public MailTemplate loadMailTemplate(Long templateId) {
		Map<String,Object>context=createContext();
		try {
			CoMailTemplateFacadeRemote bean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			CoMailTemplate template= bean.loadById(templateId);
			if(template!=null){
				MailTemplate templateResult=new MailTemplate();
				templateResult.setName(template.getName());
				templateResult.setDescription(template.getDescription());
				templateResult.setTemplate(template.getContent());
				templateResult.setTemplateId(templateId);
				templateResult.setSubject(template.getSubject());
				List<String>propName=new ArrayList<String>();
				List<String>propValues=new ArrayList<String>();
				List<String>resourceNames=new ArrayList<String>();
				CoCustomPropertyFacadeRemote customFacade=(CoCustomPropertyFacadeRemote)ServiceFactory.lookupFacade(CoCustomPropertyFacadeRemote.class);
				UseCase usecase= UsecaseUtil.getUseCase(CoMailTemplateFacadeRemote.class.getName());
				List<CoCustomProperty>props= customFacade.findByProperties(new String[]{"coUsecase.coUsecaseId","recordId"}, new Object[]{usecase.getUsecaseId(),templateId},  null);
				
				if(props!=null){
					for(CoCustomProperty row: props){
							propName.add(row.getPropertyName());
							propValues.add(row.getPropertyValue());
							resourceNames.add(row.getExtendedProps1());
					}
				}
				
				templateResult.setAttachmentNames(propName.toArray(new String[propName.size()]));
				templateResult.setAttachmetPaths(propValues.toArray(new String[propValues.size()]));
				templateResult.setResourceNames(resourceNames.toArray(new String[resourceNames.size()]));
				return templateResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		}
		return null;	}
//******************************************************************************************************************
	@Override
	public ProcessExecutionResult saveMailTemplate(String name,String description,String subject,String template,List<List<String>>customProperties) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context= createContext();
		try {
			CoMailTemplateFacadeRemote bean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			CoMailTemplate mailtemplate=new CoMailTemplate();
			mailtemplate.setName(name);
			mailtemplate.setDescription(description);
			mailtemplate.setSubject(subject);
			mailtemplate.setContent(template);
			mailtemplate.setCustomPropertyList(customProperties);
			bean.save(mailtemplate);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e, context);
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	}
//******************************************************************************************************************
	@Override
	public ProcessExecutionResult updateMailTemplate(Long templateId,String name,String subject,String description,String template,List<List<String>>customProperties) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context= createContext();
		try {
			CoMailTemplateFacadeRemote bean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			CoMailTemplate mailtemplate=bean.findById(templateId);
			mailtemplate.setName(name);
			mailtemplate.setDescription(description);
			mailtemplate.setContent(template);
			mailtemplate.setSubject(subject);
			mailtemplate.setCustomPropertyList(customProperties);
			bean.update(mailtemplate);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e, context);
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	}

}
