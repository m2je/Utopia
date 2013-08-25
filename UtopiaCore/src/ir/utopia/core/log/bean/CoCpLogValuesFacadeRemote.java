package ir.utopia.core.log.bean;

// default package

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.log.persistent.CoCpLogValues;

import javax.ejb.Remote;

/**
 * Remote interface for CoCpLogValuesFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoCpLogValuesFacadeRemote extends UtopiaBasicUsecaseBean<CoCpLogValues, CoCpLogValues>{
	
}