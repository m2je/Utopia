package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailConfigurationServiceAsync {

	

	void loadMailAccounts(Long mailServerId, AsyncCallback<UILookupInfo> callback);

	void saveMailConfiguration(Long serverId, String username, String password,
			String description, AsyncCallback<ProcessExecutionResult> callback);

	void deleteMailConfiguration(Long mailId,
			AsyncCallback<ProcessExecutionResult> callback);

	void updateMailConfiguration(Long mailId, String username, String password,
			String description, AsyncCallback<ProcessExecutionResult> callback);

	void loadConfiguration(Long configurationId,
			AsyncCallback<MailConfiguration> callback);

}
