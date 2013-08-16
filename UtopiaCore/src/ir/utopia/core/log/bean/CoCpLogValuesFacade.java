package ir.utopia.core.log.bean;

// default package

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.log.persistent.CoCpLogValues;

import javax.ejb.Stateless;

/**
 * Facade for entity CoCpLogValues.
 * 
 * @see .CoCpLogValues
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoCpLogValuesFacade extends AbstractBasicUsecaseBean<CoCpLogValues, CoCpLogValues> implements CoCpLogValuesFacadeRemote {


}