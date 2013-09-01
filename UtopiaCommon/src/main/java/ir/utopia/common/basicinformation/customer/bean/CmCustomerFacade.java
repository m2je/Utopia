package ir.utopia.common.basicinformation.customer.bean;

import ir.utopia.common.basicinformation.customer.persistent.CmCustomer;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

/**
 * Facade for entity BpRequest.
 * 
 * @see REQUEST.BpRequest
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CmCustomerFacade extends AbstractBasicUsecaseBean<CmCustomer, CmCustomer> implements CmCustomerFacadeRemote {

}

