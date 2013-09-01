package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MailTemplateService extends RemoteService {
	/**
	 * 
	 * @param name
	 * @param description
	 * @param emplate
	 * @return
	 */
	public ProcessExecutionResult saveMailTemplate(String name,String subject,String description,String template,List<List<String>>customProperties);
	/**
	 * 
	 * @param templateId
	 * @param name
	 * @param description
	 * @param emplate
	 * @return
	 */
	public ProcessExecutionResult updateMailTemplate(Long templateId,String name,String subject,String description,String template,List<List<String>>customProperties);
	/**
	 * 
	 * @param templateId
	 * @return
	 */
	public ProcessExecutionResult deleteMailTemplate(Long templateId);
	/**
	 * 
	 * @return
	 */
	public UILookupInfo getCurrentTemplates();
	/**
	 * 
	 * @param templateId
	 * @return
	 */
	public MailTemplate loadMailTemplate(Long templateId);
}
