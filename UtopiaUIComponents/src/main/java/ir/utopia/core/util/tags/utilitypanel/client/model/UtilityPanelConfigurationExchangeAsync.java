package ir.utopia.core.util.tags.utilitypanel.client.model;

import java.util.Map;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface UtilityPanelConfigurationExchangeAsync {

	

	void deleteSMTPConfiguration(Long id,
			AsyncCallback<ProcessExecutionResult> callback);

	void loadCurrentConfigurations(AsyncCallback<UILookupInfo> callback);

	void loadConfiguration(Long configurationId,
			AsyncCallback<SMTPConfiguration> callback);

	void saveSMTPConfiguration(String serverName, String serverAdrress,
			Integer smtpPort, String description,
			Map<String, String> customProperties,
			AsyncCallback<ProcessExecutionResult> callback);

	void updateSMTPConfiguration(Long id, String serverName,
			String serverAdrress, Integer smtpPort, String description,
			Map<String, String> customProperties,
			AsyncCallback<ProcessExecutionResult> callback);

	

	

}
