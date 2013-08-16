package ir.utopia.core.smtp.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.smtp.persistent.CoSmtpConfiguration;

import javax.ejb.Remote;

@Remote
public interface CoSmtpConfigurationFacadeRemote extends
		UtopiaBasicUsecaseBean<CoSmtpConfiguration, CoSmtpConfiguration> {
	
	
}
