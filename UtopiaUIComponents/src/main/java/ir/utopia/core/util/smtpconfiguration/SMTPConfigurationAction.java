package ir.utopia.core.util.smtpconfiguration;

import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.customproperty.bean.CoCustomPropertyFacadeRemote;
import ir.utopia.core.customproperty.persistent.CoCustomProperty;
import ir.utopia.core.smtp.bean.CoSmtpConfigurationFacadeRemote;
import ir.utopia.core.smtp.persistent.CoSmtpConfiguration;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.converter.AbstractUtopiaUIHandler;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.SMTPConfiguration;
import ir.utopia.core.util.tags.utilitypanel.client.model.UtilityPanelConfigurationExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMTPConfigurationAction extends UtopiaBasicAction implements UtilityPanelConfigurationExchange{
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(SMTPConfigurationAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5724650632362228440L;
	
	
//*********************************************************************************************************************	
	public String execute(){
		return "SUCCESS";
	}
//*********************************************************************************************************************
	@Override
	public ProcessExecutionResult deleteSMTPConfiguration(Long id) {
		ProcessExecutionResult result=new ProcessExecutionResult();
		try {
			CoSmtpConfigurationFacadeRemote facade=(CoSmtpConfigurationFacadeRemote)ServiceFactory.lookupFacade(CoSmtpConfigurationFacadeRemote.class);
			CoSmtpConfiguration config=new CoSmtpConfiguration();
			config.setCoSmtpConfigurationId(id);
			facade.delete(config);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
		}
		return result;
	}
//*********************************************************************************************************************
	@Override
	public SMTPConfiguration loadConfiguration(Long configurationId) {
		SMTPConfiguration  result=null;
		try {
			CoSmtpConfigurationFacadeRemote facade=(CoSmtpConfigurationFacadeRemote)ServiceFactory.lookupFacade(CoSmtpConfigurationFacadeRemote.class);
			result=new SMTPConfiguration();
			Map<String,Object>context= createContext();
			CoSmtpConfiguration config= facade.loadById(configurationId);
			if(config==null){
				throw new IllegalArgumentException("unable to load SMTPConfiguration with id:"+configurationId);
			}
			result.setId(config.getCoSmtpConfigurationId());
			result.setServerName(config.getName());
			result.setServerAdrress(config.getSmtpHost());
			result.setSmtpPort(config.getSmtpPort());
			result.setDescription(config.getDescription());
			CoCustomPropertyFacadeRemote customFacade=(CoCustomPropertyFacadeRemote)ServiceFactory.lookupFacade(CoCustomPropertyFacadeRemote.class);
			UseCase usecase= UsecaseUtil.getUseCase(CoSmtpConfigurationFacadeRemote.class.getName());
			List<CoCustomProperty>props= customFacade.findByProperties(new String[]{"coUsecase.coUsecaseId","recordId"}, new Object[]{usecase.getUsecaseId(),configurationId},  null);
			if(props!=null&&props.size()>0){
				ArrayList<String>names=new  ArrayList<String>();
				ArrayList<String>values=new  ArrayList<String>();
				for(CoCustomProperty cprop:props){
					names.add(cprop.getPropertyName());
					values.add(cprop.getPropertyValue());
				}
				result.setCustomPropertyNames(names.toArray(new String[names.size()]));
				result.setCustomPropertyValues(values.toArray(new String[values.size()]));
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return result;
	}
//*********************************************************************************************************************
	@Override
	public UILookupInfo loadCurrentConfigurations() {
		try {
			CoSmtpConfigurationFacadeRemote facade=(CoSmtpConfigurationFacadeRemote)ServiceFactory.lookupFacade(CoSmtpConfigurationFacadeRemote.class);
			Map<String,Object>context= createContext();
			return AbstractUtopiaUIHandler.convertNamePairTolookupInfo(facade.loadLookup( null),
					ContextUtil.getLoginLanguage(context)) ;
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//*********************************************************************************************************************	
	@Override
	public ProcessExecutionResult saveSMTPConfiguration(String serverName,String serverAddress,Integer smtpPort ,String description,Map<String,String>customProperties) {
		return doSaveSMTPCOnfiguration(-1l, serverName, serverAddress, smtpPort, description, customProperties);
	}
//*********************************************************************************************************************
	@Override
	public ProcessExecutionResult updateSMTPConfiguration(Long id,
			String serverName,String serverAdrress,Integer smtpPort ,String description,Map<String,String>customProperties) {
		return doSaveSMTPCOnfiguration(id, serverName, serverAdrress, smtpPort, description, customProperties);
	}
//*********************************************************************************************************************
	protected ProcessExecutionResult doSaveSMTPCOnfiguration(Long id,
			String serverName,String serverAdrress,Integer smtpPort ,String description,
			Map<String,String>customProperties){
		ProcessExecutionResult  result=new ProcessExecutionResult();
		try {
			boolean isupdate=id!=null&&id.longValue()>0;
			CoSmtpConfigurationFacadeRemote facade=(CoSmtpConfigurationFacadeRemote)ServiceFactory.lookupFacade(CoSmtpConfigurationFacadeRemote.class);
			Map<String,Object>context= createContext();
			CoSmtpConfiguration configuration=isupdate?facade.loadById(id):new CoSmtpConfiguration();
			configuration.setName(serverName);
			configuration.setSmtpHost(serverAdrress);
			configuration.setSmtpPort(smtpPort);
			configuration.setCustomProperties(customProperties);
			configuration.setDescription(description);
			if(isupdate){
				configuration= facade.update(configuration);
			}else{
				configuration= facade.save(configuration);
				
			}
			result.setSuccess(true);
			result.setProcessIdentifier(configuration.getCoSmtpConfigurationId());
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			result.setSuccess(false);
		}
		return result;
	}

}
