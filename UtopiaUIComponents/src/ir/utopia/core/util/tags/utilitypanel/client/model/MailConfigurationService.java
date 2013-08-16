package ir.utopia.core.util.tags.utilitypanel.client.model;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MailConfigurationService extends RemoteService {
	
	/**
	 * 	
	 * @param mailServerId
	 * @return
	 */
	public UILookupInfo loadMailAccounts(Long mailServerId);
	
	
	/**
	 * 
	 * @param serverId
	 * @param username
	 * @param password
	 * @param description
	 */
	public ProcessExecutionResult saveMailConfiguration(Long serverId,String username,String password,String description);
	/**
	 * 
	 * @param mailId
	 * @param username
	 * @param password
	 * @param description
	 */
	public ProcessExecutionResult updateMailConfiguration(Long mailId,String username,String password,String description);
	/**
	 * 
	 * @param mailId
	 * @return
	 */
	public ProcessExecutionResult deleteMailConfiguration(Long mailId);
	
	/**
	 * 
	 * @param configurationId
	 * @return
	 */
	public MailConfiguration loadConfiguration(Long configurationId);
	
	
}
