package ir.utopia.common.systems.system.bean;


import ir.utopia.common.systems.system.persistent.CmSystemParameter;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Remote interface for CmSystemParameterFacade.
 * 
 * @author 
 */
@Remote
public interface CmSystemParameterFacadeRemote extends UtopiaBasicUsecaseBean<CmSystemParameter, CmSystemParameter> {
	
	public List<CmSystemParameter> getCurrentUserSystemParamters(Map<String,Object> context);
	}