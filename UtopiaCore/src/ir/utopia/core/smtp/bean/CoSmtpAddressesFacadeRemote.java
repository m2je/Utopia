package ir.utopia.core.smtp.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.smtp.persistent.CoSmtpAddresses;

import javax.ejb.Remote;

/**
 * Remote interface for CoSmtpAddressesFacade.
 * 
 * @author M2je
 */
@Remote
public interface CoSmtpAddressesFacadeRemote extends UtopiaBasicUsecaseBean<CoSmtpAddresses, CoSmtpAddresses> {

}