package ir.utopia.common.basicinformation.customer.bean;

import ir.utopia.common.basicinformation.customer.persistent.CmCustomer;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

/**
 * Remote interface for BpBillFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CmCustomerFacadeRemote extends UtopiaBasicUsecaseBean<CmCustomer, CmCustomer>{
}
