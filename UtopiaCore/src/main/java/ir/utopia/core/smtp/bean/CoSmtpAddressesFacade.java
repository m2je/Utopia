package ir.utopia.core.smtp.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.smtp.persistent.CoSmtpAddresses;

import javax.ejb.Stateless;

/**
 * Facade for entity CoSmtpAddresses.
 * 
 * @see ir.utopia.core.smtp.persistent.CoSmtpAddresses
 * @author M2je
 */
@Stateless
public class CoSmtpAddressesFacade extends AbstractBasicUsecaseBean<CoSmtpAddresses, CoSmtpAddresses> implements CoSmtpAddressesFacadeRemote {
	

}