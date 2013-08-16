package ir.utopia.core.smtp.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.smtp.persistent.CoSmtpConfiguration;

import javax.ejb.Stateless;
@Stateless
public class CoSmtpConfigurationFacade extends AbstractBasicUsecaseBean<CoSmtpConfiguration, CoSmtpConfiguration>
		implements CoSmtpConfigurationFacadeRemote {

	
}
