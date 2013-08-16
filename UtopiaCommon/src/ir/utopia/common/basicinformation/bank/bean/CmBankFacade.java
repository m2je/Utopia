package ir.utopia.common.basicinformation.bank.bean;

import ir.utopia.common.basicinformation.bank.persistent.CmBank;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

@Stateless
public class CmBankFacade extends AbstractBasicUsecaseBean<CmBank,CmBank> implements CmBankFacadeRemote {

}