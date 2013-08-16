package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UtilityPanelConfigurationExchange extends RemoteService{
		/**
		 * 
		 * @param serverName
		 * @param serverAdrress
		 * @param smtpPort
		 * @param customPropertyNames
		 * @param customPropertyValues
		 * @return
		 */
	 public ProcessExecutionResult saveSMTPConfiguration(String serverName,String serverAdrress,Integer smtpPort ,String description,Map<String,String>customProperties);
	 /**
	  * 
	  * @param id
	  * @param serverName
	  * @param serverAdrress
	  * @param smtPort
	  * @param customPropertyNames
	  * @param customPropertyValues
	  * @return
	  */
	 public ProcessExecutionResult updateSMTPConfiguration(Long id,String serverName,String serverAdrress,Integer smtpPort ,String description,Map<String,String>customProperties);
	 /**
	  * 
	  * @param id
	  * @return
	  */
	 public ProcessExecutionResult deleteSMTPConfiguration(Long id);
	 
	 /**
	  * 
	  * @return
	  */
	 public UILookupInfo loadCurrentConfigurations();
	 
	 public SMTPConfiguration loadConfiguration(Long configurationId);
}
