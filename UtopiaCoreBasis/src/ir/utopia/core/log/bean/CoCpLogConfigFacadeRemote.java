package ir.utopia.core.log.bean;

// default package

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.log.persistent.CoCpLogConfig;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Remote interface for CoCpLogConfigFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoCpLogConfigFacadeRemote extends UtopiaBasicUsecaseBean<CoCpLogConfig, CoCpLogConfig> {
	
public void save(List<CoCpLogConfig>configs,Map<String,Object>context)throws SaveRecordException;
	
public void delete(List<CoCpLogConfig>configs,Map<String,Object>context)throws SaveRecordException;
}