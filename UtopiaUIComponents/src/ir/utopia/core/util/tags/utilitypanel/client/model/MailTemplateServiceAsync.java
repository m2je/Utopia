package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailTemplateServiceAsync {

	void deleteMailTemplate(Long templateId,
			AsyncCallback<ProcessExecutionResult> callback);

	void getCurrentTemplates(AsyncCallback<UILookupInfo> callback);

	void saveMailTemplate(String name,String subject, String description, String template,List<List<String>>customProperties,
			AsyncCallback<ProcessExecutionResult> callback);

	void updateMailTemplate(Long templateId, String name,String subject, String description,
			String template,List<List<String>>customProperties, AsyncCallback<ProcessExecutionResult> callback);

	void loadMailTemplate(Long templateId, AsyncCallback<MailTemplate> callback);

}
