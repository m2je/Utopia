package ir.utopia.core.util.mailconfig;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.LookupInfo;
import ir.utopia.core.smtp.bean.CoSmtpAddressesFacadeRemote;
import ir.utopia.core.smtp.persistent.CoSmtpAddresses;
import ir.utopia.core.smtp.persistent.CoSmtpConfiguration;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailConfiguration;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailConfigurationService;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailConfigurationAction extends UtopiaBasicAction implements MailConfigurationService {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(MailConfigurationAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 9137935902229561694L;

	@Override
	public UILookupInfo loadMailAccounts(Long serverId) {
		try {
			CoSmtpAddressesFacadeRemote bean=(CoSmtpAddressesFacadeRemote)ServiceFactory.lookupFacade(CoSmtpAddressesFacadeRemote.class);
			Map<String,Object>context= createContext();
			LookupConfigurationModel lookupModel=new LookupConfigurationModel(CoSmtpAddresses.class);
			lookupModel.setOwnDisplayColumns("username");
			lookupModel.setPrimaryKeyColumn("coSmtpAddressesId");
			context.put("coSmtpConfigurationId", serverId);
			lookupModel.addCondition("CoSmtpAddresses.coSmtpConfiguration.coSmtpConfigurationId=@coSmtpConfigurationId@", context);
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(bean.loadLookup(lookupModel, null), ContextUtil.getLoginLanguage(context) ) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}

	

	@Override
	public ProcessExecutionResult deleteMailConfiguration(Long mailConfigurationId) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context= createContext();
		try {
			CoSmtpAddressesFacadeRemote bean=(CoSmtpAddressesFacadeRemote)ServiceFactory.lookupFacade(CoSmtpAddressesFacadeRemote.class);
			CoSmtpAddresses address=new CoSmtpAddresses();
			address.setCoSmtpAddressesId(mailConfigurationId);
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

	@Override
	public ProcessExecutionResult saveMailConfiguration(Long serverId,String username,String password,String description) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context= createContext();
		try {
			CoSmtpAddressesFacadeRemote bean=(CoSmtpAddressesFacadeRemote)ServiceFactory.lookupFacade(CoSmtpAddressesFacadeRemote.class);
			CoSmtpAddresses address=new CoSmtpAddresses();
			address.setUsername(username);
			address.setPassword(ServiceFactory.getSecurityProvider().encrypt(password));
			address.setDescription(description);
			LookupInfo info=new LookupInfo(CoSmtpConfiguration.class, serverId);
			ArrayList<LookupInfo>infos=new ArrayList<LookupInfo>();
			infos.add(info);
			address.setLookupInfos(infos);
			bean.save(address);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e, context);
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	}

	@Override
	public ProcessExecutionResult updateMailConfiguration(Long mailConfigId,
			String username,String password,String description) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		Map<String,Object>context= createContext();
		try {
			CoSmtpAddressesFacadeRemote bean=(CoSmtpAddressesFacadeRemote)ServiceFactory.lookupFacade(CoSmtpAddressesFacadeRemote.class);
			CoSmtpAddresses address=bean.findById(mailConfigId);
			address.setUsername(username);
			address.setPassword(ServiceFactory.getSecurityProvider().encrypt(password));
			address.setDescription(description);
			bean.update(address);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
			ExceptionResult exresult= getExceptionHandler().handel(e,context);
			initProcessResultMessges(result, exresult.getMessages());
		}
		return result;
	}



	@Override
	public MailConfiguration loadConfiguration(Long mailConfigId) {
		try {
			CoSmtpAddressesFacadeRemote bean=(CoSmtpAddressesFacadeRemote)ServiceFactory.lookupFacade(CoSmtpAddressesFacadeRemote.class);
			CoSmtpAddresses address=bean.findById(mailConfigId);
			MailConfiguration result=new MailConfiguration();
			result.setUser(address.getUsername());
			result.setPasssword(ServiceFactory.getSecurityProvider().decrypt(address.getPassword()));
			result.setDescription(address.getDescription());
			return result;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}

	
}
