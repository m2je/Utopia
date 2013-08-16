package ir.utopia.common.basicinformation.bank.bean;

import ir.utopia.common.basicinformation.bank.persistent.CmBank;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;


@Remote
public interface CmBankFacadeRemote extends UtopiaBasicUsecaseBean<CmBank,CmBank> {

}